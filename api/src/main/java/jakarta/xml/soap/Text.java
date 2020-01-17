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
 * A representation of a node whose value is text.  A {@code Text} object
 * may represent text that is content or text that is a comment.
 *
 * @since 1.6
 */
public interface Text extends Node, org.w3c.dom.Text {

    /**
     * Retrieves whether this {@code Text} object represents a comment.
     *
     * @return {@code true} if this {@code Text} object is a
     *         comment; {@code false} otherwise
     */
    public boolean isComment();
}
