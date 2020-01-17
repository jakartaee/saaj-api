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
 * An object that stores a MIME header name and its value. One or more
 * {@code MimeHeader} objects may be contained in a {@code MimeHeaders}
 * object.
 *
 * @see MimeHeaders
 * @since 1.6
 */
public class MimeHeader {

   private String name;
   private String value;

   /**
    * Constructs a {@code MimeHeader} object initialized with the given
    * name and value.
    *
    * @param name a {@code String} giving the name of the header
    * @param value a {@code String} giving the value of the header
    */
    public MimeHeader(String name, String value) {
        this.name = name;
        this.value = value;
    }

    /**
     * Returns the name of this {@code MimeHeader} object.
     *
     * @return the name of the header as a {@code String}
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the value of this {@code MimeHeader} object.
     *
     * @return  the value of the header as a {@code String}
     */
    public String getValue() {
        return value;
    }
}
