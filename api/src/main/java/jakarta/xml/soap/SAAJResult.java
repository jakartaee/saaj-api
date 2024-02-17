/*
 * Copyright (c) 2004, 2024 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License v. 1.0, which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

package jakarta.xml.soap;

import javax.xml.transform.dom.DOMResult;

/**
 * Acts as a holder for the results of a JAXP transformation or a JAXB
 * marshalling, in the form of a SAAJ tree. These results should be accessed
 * by using the {@link #getResult()} method. The {@link DOMResult#getNode()}
 * method should be avoided in almost all cases.
 *
 * @author XWS-Security Development Team
 *
 * @since 1.6, SAAJ 1.3
 */
public class SAAJResult extends DOMResult {

    /**
     * Creates a {@code SAAJResult} that will present results in the form
     * of a SAAJ tree that supports the default (SOAP 1.1) protocol.
     * <p>
     * This kind of {@code SAAJResult} is meant for use in situations where the
     * results will be used as a parameter to a method that takes a parameter
     * whose type, such as {@code SOAPElement}, is drawn from the SAAJ
     * API. When used in a transformation, the results are populated into the
     * {@code SOAPPart} of a {@code SOAPMessage} that is created internally.
     * The {@code SOAPPart} returned by {@link DOMResult#getNode()}
     * is not guaranteed to be well-formed.
     *
     * @throws SOAPException if there is a problem creating a {@code SOAPMessage}
     *
     * @since 1.6, SAAJ 1.3
     */
    public SAAJResult() throws SOAPException {
        this(MessageFactory.newInstance().createMessage());
    }

    /**
     * Creates a {@code SAAJResult} that will present results in the form
     * of a SAAJ tree that supports the specified protocol. The
     * {@code DYNAMIC_SOAP_PROTOCOL} is ambiguous in this context and will
     * cause this constructor to throw an {@code UnsupportedOperationException}.
     * <p>
     * This kind of {@code SAAJResult} is meant for use in situations where the
     * results will be used as a parameter to a method that takes a parameter
     * whose type, such as {@code SOAPElement}, is drawn from the SAAJ
     * API. When used in a transformation the results are populated into the
     * {@code SOAPPart} of a {@code SOAPMessage} that is created
     * internally. The {@code SOAPPart} returned by {@link DOMResult#getNode()}
     * is not guaranteed to be well-formed.
     *
     * @param protocol the name of the SOAP protocol that the resulting SAAJ
     *                      tree should support
     *
     * @throws SOAPException if a {@code SOAPMessage} supporting the
     *             specified protocol cannot be created
     *
     * @since 1.6, SAAJ 1.3
     */
    public SAAJResult(String protocol) throws SOAPException {
        this(MessageFactory.newInstance(protocol).createMessage());
    }

    /**
     * Creates a {@code SAAJResult} that will write the results into the
     * {@code SOAPPart} of the supplied {@code SOAPMessage}.
     * In the normal case these results will be written using DOM APIs and,
     * as a result, the finished {@code SOAPPart} will not be guaranteed
     * to be well-formed unless the data used to create it is also well-formed.
     * When used in a transformation the validity of the {@code SOAPMessage}
     * after the transformation can be guaranteed only by means outside SAAJ
     * specification.
     *
     * @param message the message whose {@code SOAPPart} will be
     *                  populated as a result of some transformation or
     *                  marshalling operation
     *
     * @since 1.6, SAAJ 1.3
     */
    public SAAJResult(SOAPMessage message) {
        super(message.getSOAPPart());
    }

    /**
     * Creates a {@code SAAJResult} that will write the results as a
     * child node of the {@code SOAPElement} specified. In the normal
     * case these results will be written using DOM APIs and as a result may
     * invalidate the structure of the SAAJ tree. This kind of
     * {@code SAAJResult} should only be used when the validity of the
     * incoming data can be guaranteed by means outside of the SAAJ
     * specification.
     *
     * @param rootNode the root to which the results will be appended
     *
     * @since 1.6, SAAJ 1.3
     */
    public SAAJResult(SOAPElement rootNode) {
        super(rootNode);
    }


    /**
     * Gets the resulting Tree that was created under the specified root Node.
     *
     * @return the resulting Tree that was created under the specified root Node.
     * @since 1.6, SAAJ 1.3
     */
    public jakarta.xml.soap.Node getResult() {
        return (jakarta.xml.soap.Node)super.getNode().getFirstChild();
     }
}
