// Job input parameters:
//   SPEC_VERSION      - Specification version to release
//   NEXT_SPEC_VERSION - Next specification snapshot version to set (e.g. 1.2.4-SNAPSHOT)
//   API_VERSION       - API version to release
//   NEXT_API_VERSION  - Next API snapshot version to set (e.g. 1.2.4-SNAPSHOT)
//   BRANCH            - Branch to release
//   DRY_RUN           - Do not publish artifacts to OSSRH and code changes to GitHub
//   OVERWRITE         - Allows to overwrite existing version in git and OSSRH staging repositories

// Job internal argumets:
//   GIT_USER_NAME       - Git user name (for commits)
//   GIT_USER_EMAIL      - Git user e-mail (for commits)
//   SSH_CREDENTIALS_ID  - Jenkins ID of SSH credentials
//   GPG_CREDENTIALS_ID  - Jenkins ID of GPG credentials (stored as KEYRING variable)
//   SETTINGS_XML_ID     - Jenkins ID of settings.xml file
//   SETTINGS_SEC_XML_ID - Jenkins ID of settings-security.xml file

def notifyFailed() {
    emailext (
        subject: "FAILED: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]'",
        body: """
FAILED: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]':
Check console output at "${env.JOB_NAME} [${env.BUILD_NUMBER}]"
""",
        recipientProviders: [[$class: 'DevelopersRecipientProvider']]
    )
}

pipeline {
    
    agent any

    tools {
        jdk 'openjdk-jdk11-latest'
        maven 'apache-maven-latest'
    }

    environment {
        SPEC_DIR="${WORKSPACE}/spec"
        API_DIR="${WORKSPACE}/api"
    }

    stages {
        // Initialize build environment
        stage('Init') {
            steps {
                git branch: BRANCH, credentialsId: SSH_CREDENTIALS_ID, url: GIT_URL
                // GPG initialization
                withCredentials([file(credentialsId: GPG_CREDENTIALS_ID, variable: 'KEYRING')]) {
                    sh '''
                        gpg --batch --import ${KEYRING}
                        for fpr in $(gpg --list-keys --with-colons  | awk -F: '/fpr:/ {print $10}' | sort -u);
                        do
                          echo -e "5\ny\n" |  gpg --batch --command-fd 0 --expert --edit-key $fpr trust;
                        done

                    '''
                }
                // Git configuration
                sh '''
                    git config --global user.name "${GIT_USER_NAME}"
                    git config --global user.email "${GIT_USER_EMAIL}"
                '''
            }
        }
        // Perform release
        try {
            stage('Build') {
                steps {
                    configFileProvider([
                            configFile(
                                fileId: SETTINGS_XML_ID,
                                targetLocation: '/home/jenkins/.m2/settings.xml'
                            ), 
                            configFile(
                                fileId: SETTINGS_SEC_XML_ID, 
                                targetLocation: '/home/jenkins/.m2/'
                            )]) {
                        sshagent([SSH_CREDENTIALS_ID]) {
                            sh 'etc/jenkins/continuous.sh'
                        }
                    }
                    junit '**/target/surefire-reports/*.xml'
                    recordIssues(tools: [spotBugs(useRankAsPriority: true)])
                }
            }
        } catch(e) {
            currentBuild.result = "FAILED"
            notifyFailed()
            throw e
        }
        
    }

}
