#!/bin/bash -e
#
# Arguments:
#  N/A

echo '-[ Jakarta SOAP with Attachments Specification Build ]--------------------------'
(cd spec && mvn -U -C -Psnapshots,oss-release clean install deploy)
echo '-[ Jakarta SOAP with Attachments API Build ]------------------------------------'
(cd api && mvn -U -C -Psnapshots,oss-release clean install spotbugs:spotbugs deploy)
