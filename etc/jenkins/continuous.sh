#!/bin/bash -e
#
# Copyright (c) 2019 Oracle and/or its affiliates. All rights reserved.
#
# This program and the accompanying materials are made available under the
# terms of the Eclipse Distribution License v. 1.0, which is available at
# http://www.eclipse.org/org/documents/edl-v10.php.
#
# SPDX-License-Identifier: BSD-3-Clause

# Arguments:
#  N/A

echo '-[ Jakarta SOAP with Attachments Specification Build ]--------------------------'
(cd spec && mvn -U -C -B -Dstatus='DRAFT' clean install)
echo '-[ Jakarta SOAP with Attachments API Build ]------------------------------------'
(cd api && mvn -U -C -B -V -Psnapshots,staging,oss-release,coverage clean deploy)
