/*
 * Copyright (c) 2017, 2019 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License v. 1.0, which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

/**
 * Jakarta SOAP with Attachments API.
 *
 * <p>
 * References in this document to SAAJ refer to the Jakarta SOAP with Attachments API unless otherwise noted.
 */
module java.xml.soap {
    requires transitive java.xml;
    requires transitive jakarta.activation;
    requires java.logging;

    exports javax.xml.soap;

    uses javax.xml.soap.MessageFactory;
    uses javax.xml.soap.SAAJMetaFactory;
    uses javax.xml.soap.SOAPConnectionFactory;
    uses javax.xml.soap.SOAPFactory;
}
