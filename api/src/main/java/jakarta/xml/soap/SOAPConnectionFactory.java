/*
 * Copyright (c) 2004, 2020 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License v. 1.0, which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

package jakarta.xml.soap;

/**
 * A factory for creating {@code SOAPConnection} objects. Implementation of this class
 * is optional. If {@code SOAPConnectionFactory.newInstance()} throws an
 * UnsupportedOperationException then the implementation does not support the
 * SAAJ communication infrastructure. Otherwise {@link SOAPConnection} objects
 * can be created by calling {@code createConnection()} on the newly
 * created {@code SOAPConnectionFactory} object.
 *
 * @since 1.6
 */
public abstract class SOAPConnectionFactory {

    /**
     * A constant representing the default value for a {@code SOAPConnection}
     * object. The default is the point-to-point SOAP connection.
     */
    private static final String DEFAULT_SOAP_CONNECTION_FACTORY
            = "com.sun.xml.internal.messaging.saaj.client.p2p.HttpSOAPConnectionFactory";

    /**
     * Creates an instance of the default
     * {@code SOAPConnectionFactory} object.
     *
     * This method uses the lookup procedure specified in {@link jakarta.xml.soap} to locate and load the
     * {@link jakarta.xml.soap.SOAPConnectionFactory} class.
     *
     * @return a new instance of a default
     *         {@code SOAPConnectionFactory} object
     *
     * @exception SOAPException if there was an error creating the
     *            {@code SOAPConnectionFactory}
     *
     * @exception UnsupportedOperationException if newInstance is not
     * supported.
     */
    public static SOAPConnectionFactory newInstance()
        throws SOAPException, UnsupportedOperationException
    {
        try {
            return FactoryFinder.find(
                    SOAPConnectionFactory.class,
                    DEFAULT_SOAP_CONNECTION_FACTORY,
                    true);
        } catch (Exception ex) {
            throw new SOAPException("Unable to create SOAP connection factory: "
                                    +ex.getMessage());
        }
    }

    /**
     * Create a new {@code SOAPConnection}.
     *
     * @return the new {@code SOAPConnection} object.
     *
     * @exception SOAPException if there was an exception creating the
     * {@code SOAPConnection} object.
     */
    public abstract SOAPConnection createConnection()
        throws SOAPException;
}
