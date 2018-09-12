/*
 * Copyright (c) 2016, 2018 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License v. 1.0, which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

package saaj.factory;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import java.io.IOException;
import java.io.InputStream;

/**
 * (Another) Valid implementation class for tests
 * - several implementations necessary to test different configuration approaches
 */
public class Valid2 extends MessageFactory {
    @Override
    public SOAPMessage createMessage() throws SOAPException {
        return null;
    }

    @Override
    public SOAPMessage createMessage(MimeHeaders headers, InputStream in) throws IOException, SOAPException {
        return null;
    }
}
