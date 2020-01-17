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
 * {@code SOAPElementFactory} is a factory for XML fragments that
 * will eventually end up in the SOAP part. These fragments
 * can be inserted as children of the {@code SOAPHeader} or
 * {@code SOAPBody} or {@code SOAPEnvelope}.
 *
 * <p>Elements created using this factory do not have the properties
 * of an element that lives inside a SOAP header document. These
 * elements are copied into the XML document tree when they are
 * inserted.
 * @deprecated - Use {@code jakarta.xml.soap.SOAPFactory} for creating SOAPElements.
 * @see jakarta.xml.soap.SOAPFactory
 * @since 1.6
 */
public class SOAPElementFactory {

    private SOAPFactory soapFactory;

    private SOAPElementFactory(SOAPFactory soapFactory) {
        this.soapFactory = soapFactory;
    }

    /**
     * Create a {@code SOAPElement} object initialized with the
     * given {@code Name} object.
     *
     * @param name a {@code Name} object with the XML name for
     *             the new element
     *
     * @return the new {@code SOAPElement} object that was
     *         created
     *
     * @exception SOAPException if there is an error in creating the
     *            {@code SOAPElement} object
     *
     * @deprecated Use
     * jakarta.xml.soap.SOAPFactory.createElement(jakarta.xml.soap.Name)
     * instead
     *
     * @see jakarta.xml.soap.SOAPFactory#createElement(jakarta.xml.soap.Name)
     * @see jakarta.xml.soap.SOAPFactory#createElement(javax.xml.namespace.QName)
     */
    public SOAPElement create(Name name) throws SOAPException {
        return soapFactory.createElement(name);
    }

    /**
     * Create a {@code SOAPElement} object initialized with the
     * given local name.
     *
     * @param localName a {@code String} giving the local name for
     *             the new element
     *
     * @return the new {@code SOAPElement} object that was
     *         created
     *
     * @exception SOAPException if there is an error in creating the
     *            {@code SOAPElement} object
     *
     * @deprecated Use
     * jakarta.xml.soap.SOAPFactory.createElement(String localName) instead
     *
     * @see jakarta.xml.soap.SOAPFactory#createElement(java.lang.String)
     */
    public SOAPElement create(String localName) throws SOAPException {
        return soapFactory.createElement(localName);
    }

    /**
     * Create a new {@code SOAPElement} object with the given
     * local name, prefix and uri.
     *
     * @param localName a {@code String} giving the local name
     *                  for the new element
     * @param prefix the prefix for this {@code SOAPElement}
     * @param uri a {@code String} giving the URI of the
     *            namespace to which the new element belongs
     *
     * @return the new {@code SOAPElement} object that was
     *         created
     *
     * @exception SOAPException if there is an error in creating the
     *            {@code SOAPElement} object
     *
     * @deprecated Use
     * jakarta.xml.soap.SOAPFactory.createElement(String localName,
     *                      String prefix,
     *                      String uri)
     * instead
     *
     * @see jakarta.xml.soap.SOAPFactory#createElement(java.lang.String, java.lang.String, java.lang.String)
     */
    public SOAPElement create(String localName, String prefix, String uri)
        throws SOAPException {
        return soapFactory.createElement(localName, prefix, uri);
    }

    /**
     * Creates a new instance of {@code SOAPElementFactory}.
     *
     * @return a new instance of a {@code SOAPElementFactory}
     *
     * @exception SOAPException if there was an error creating the
     *            default {@code SOAPElementFactory}
     */
    public static SOAPElementFactory newInstance() throws SOAPException {
        try {
            return new SOAPElementFactory(SOAPFactory.newInstance());
        } catch (Exception ex) {
            throw new SOAPException(
                "Unable to create SOAP Element Factory: " + ex.getMessage());
        }
    }
}
