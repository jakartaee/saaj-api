/*
 * Copyright (c) 2005, 2020 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Distribution License v. 1.0, which is available at
 * http://www.eclipse.org/org/documents/edl-v10.php.
 *
 * SPDX-License-Identifier: BSD-3-Clause
 */

/**
 * Provides the API for creating and building SOAP messages. This package
 * is defined in the <i>Jakarta SOAP with Attachments</i> specification.
 * 
 * <p> The API in the <code>jakarta.xml.soap</code> package allows you to do the following:
 *  
 * <ul>
 *     <li>create a point-to-point connection to a specified endpoint   
 *     <li>create a SOAP message   
 *     <li>create an XML fragment   
 *     <li>add content to the header of a SOAP message   
 *     <li>add content to the body of a SOAP message   
 *     <li>create attachment parts and add content to them   
 *     <li>access/add/modify parts of a SOAP message   
 *     <li>create/add/modify SOAP fault information   
 *     <li>extract content from a SOAP message   
 *     <li>send a SOAP request-response message   
 * </ul>
 *
 * <p>
 * References in this document to SAAJ refer to the Jakarta SOAP with Attachments API unless otherwise noted.<br>
 * References in this document to JAXB refer to the Jakarta XML Binding unless otherwise noted.
 *
 * <p>
 * In addition the APIs in the <code>jakarta.xml.soap</code> package extend
 * their  counterparts in the <code>org.w3c.dom</code> package. This means that
 * the  <code>SOAPPart</code> of a <code>SOAPMessage</code> is also a DOM Level
 * 2 <code>Document</code>, and can be manipulated as such by applications,
 * tools and libraries that use DOM (see http://www.w3.org/DOM/ for more information).
 * It is important to note that, while it is possible to use DOM APIs to add
 * ordinary DOM nodes to a SAAJ tree, the SAAJ APIs are still required to return
 * SAAJ types when examining or manipulating the tree. In order to accomplish
 * this the SAAJ APIs (specifically {@link jakarta.xml.soap.SOAPElement#getChildElements()})
 * are allowed to silently replace objects that are incorrectly typed relative
 * to SAAJ requirements with equivalent objects of the required type. These
 * replacements must never cause the logical structure of the tree to change,
 * so from the perspective of the DOM APIs the tree will remain unchanged. However,
 * the physical composition of the tree will have changed so that references
 * to the nodes that were replaced will refer to nodes that are no longer a
 * part of the tree. The SAAJ APIs are not allowed to make these replacements
 * if they are not required so the replacement objects will never subsequently
 * be silently replaced by future calls to the SAAJ API.
 * <p>
 * What this means in practical terms is that an application that starts to use 
 * SAAJ APIs on a tree after manipulating it using DOM APIs must assume that the 
 * tree has been translated into an all SAAJ tree and that any references to objects 
 * within the tree that were obtained using DOM APIs are no longer valid. Switching
 * from SAAJ APIs to DOM APIs is not allowed to cause invalid references and
 * neither is using SAAJ APIs exclusively. It is only switching from using DOM
 * APIs on a particular SAAJ tree to using SAAJ APIs that causes the risk of
 * invalid references.
 *
 * <h2>Discovery of SAAJ implementation</h2>
 * <p>
 * There are several factories defined in the SAAJ API to discover and load specific implementation:
 *
 * <ul>
 *     <li>{@link jakarta.xml.soap.SOAPFactory}
 *     <li>{@link jakarta.xml.soap.MessageFactory}
 *     <li>{@link jakarta.xml.soap.SOAPConnectionFactory}
 *     <li>{@link jakarta.xml.soap.SAAJMetaFactory}
 * </ul>
 *
 * First three define {@code newInstance()} method which uses a common lookup procedure to determine
 * the implementation class:
 *
 * <ul>
 *  <li>Checks if a system property with the same name as the factory class is set (e.g.
 *  {@code jakarta.xml.soap.SOAPFactory}). If such property exists then its value is assumed to be the fully qualified
 *  name of the implementation class. This phase of the look up enables per-JVM override of the SAAJ implementation.
 *  <li>Use the configuration file "jaxm.properties". The file is in standard
 *  {@link java.util.Properties} format and typically located in the
 *  {@code conf} directory of the Java installation. It contains the fully qualified
 *  name of the implementation class with the key being the system property
 *  defined above.
 *  <li> Use the service-provider loading facilities, defined by the {@link java.util.ServiceLoader} class,
 *  to attempt to locate and load an implementation of the service using the {@linkplain
 *  java.util.ServiceLoader#load(java.lang.Class) default loading mechanism}.
 *  <li> Finally, if all the steps above fail, {@link jakarta.xml.soap.SAAJMetaFactory} instance is used
 *  to locate specific implementation (for {@link jakarta.xml.soap.MessageFactory} and {@link jakarta.xml.soap.SOAPFactory})
 *  or platform default implementation is used ({@link jakarta.xml.soap.SOAPConnectionFactory}).
 *  Whenever {@link jakarta.xml.soap.SAAJMetaFactory} is used, its lookup procedure to get actual instance is performed.
 * </ul>
 */
package jakarta.xml.soap;
