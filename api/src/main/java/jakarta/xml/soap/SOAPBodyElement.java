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

/**
 * A {@code SOAPBodyElement} object represents the contents in
 * a {@code SOAPBody} object.  The {@code SOAPFault} interface
 * is a {@code SOAPBodyElement} object that has been defined.
 * <P>
 * A new {@code SOAPBodyElement} object can be created and added
 * to a {@code SOAPBody} object with the {@code SOAPBody}
 * method {@code addBodyElement}. In the following line of code,
 * {@code sb} is a {@code SOAPBody} object, and
 * {@code myName} is a {@code Name} object.
 * {@snippet :
 *  SOAPBodyElement sbe = sb.addBodyElement(myName);
 * }
 *
 * @since 1.6
 */
public interface SOAPBodyElement extends SOAPElement {
}
