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
 * The content for a {@code Detail} object, giving details for
 * a {@code SOAPFault} object.  A {@code DetailEntry} object,
 * which carries information about errors related to the {@code SOAPBody}
 * object that contains it, is application-specific.
 *
 * @since 1.6
 */
public interface DetailEntry extends SOAPElement {

}
