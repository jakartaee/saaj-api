/*
 * Copyright (c) 2004, 2021 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License v. 1.0, which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

package jakarta.xml.soap;


import java.net.URLConnection;

/**
 * A point-to-point connection that a client can use for sending messages
 * directly to a remote party (represented by a URL, for instance).
 * <p>
 * The SOAPConnection class is optional. Some implementations may
 * not implement this interface in which case the call to
 * {@code SOAPConnectionFactory.newInstance()} (see below) will
 * throw an {@code UnsupportedOperationException}.
 * <p>
 * A client can obtain a {@code SOAPConnection} object using a
 * {@link SOAPConnectionFactory} object as in the following example:
 * <pre>{@code
 *      SOAPConnectionFactory factory = SOAPConnectionFactory.newInstance();
 *      SOAPConnection con = factory.createConnection();
 * }</pre>
 * A {@code SOAPConnection} object can be used to send messages
 * directly to a URL following the request/response paradigm.  That is,
 * messages are sent using the method {@code call}, which sends the
 * message and then waits until it gets a reply.
 *
 * @since 1.6
 */
public abstract class SOAPConnection implements AutoCloseable {

    private int readTimeout;
    private int connectTimeout;

    /**
     * Sends the given message to the specified endpoint and blocks until
     * it has returned the response.
     *
     * @param request the {@code SOAPMessage} object to be sent
     * @param to an {@code Object} that identifies
     *         where the message should be sent. It is required to
     *         support Objects of type
     *         {@code java.lang.String} and {@code java.net.URL}
     *
     * @return the {@code SOAPMessage} object that is the response to the
     *         message that was sent
     * @throws SOAPException if there is a SOAP error
     */
    public abstract SOAPMessage call(SOAPMessage request,
                                     Object to) throws SOAPException;

    /**
     * Gets a message from a specific endpoint and blocks until it receives,
     *
     * @param to an {@code Object} that identifies where
     *                  the request should be sent. Objects of type
     *                 {@code java.lang.String} and
     *                 {@code java.net.URL} must be supported.
     *
     * @return the {@code SOAPMessage} object that is the response to the
     *                  get message request
     * @throws SOAPException if there is a SOAP error
     * @since 1.6, SAAJ 1.3
     */
    public SOAPMessage get(Object to)
                                throws SOAPException {
        throw new UnsupportedOperationException("All subclasses of SOAPConnection must override get()");
    }

    /**
     * Sets the read timeout to a specified timeout, in milliseconds.
     * A timeout of zero is interpreted as an infinite timeout.
     *
     * @param timeout an {@code int} that specifies the timeout value to be used in milliseconds
     * @throws IllegalArgumentException if the timeout parameter is negative
     * @since 3.0
     */
    public void setConnectTimeout(int timeout) {
        if (timeout < 0) {
            throw new IllegalArgumentException("timeout can not be negative");
        }
        connectTimeout = timeout;
    }

    /**
     * Returns setting for connect timeout.
     * {@code 0} implies infinite timeout
     *
     * @return an {@code int} that indicates the connect timeout value in milliseconds
     * @since 3.0
     */
    public int getConnectTimeout() {
        return connectTimeout;
    }

    /**
     * Sets the read timeout to a specified timeout, in milliseconds.
     * A timeout of zero is interpreted as an infinite timeout.
     *
     * @param timeout an {@code int} that specifies the timeout value to be used in milliseconds
     * @throws IllegalArgumentException if the timeout parameter is negative
     * @since 3.0
     */
    public void setReadTimeout(int timeout) {
        if (timeout < 0) {
            throw new IllegalArgumentException("timeout can not be negative");
        }
        readTimeout = timeout;

    }

    /**
     * Returns setting for read timeout.
     * {@code 0} implies infinite timeout
     *
     * @return an {@code int} that indicates the read timeout value in milliseconds
     * @since 3.0
     */
    public int getReadTimeout() {
        return readTimeout;
    }

    /**
     * Closes this {@code SOAPConnection} object.
     *
     * @throws SOAPException if there is a SOAP error
     */
    public abstract void close()
        throws SOAPException;
}
