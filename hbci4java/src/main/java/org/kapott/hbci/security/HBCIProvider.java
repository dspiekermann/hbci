
/*  $Id: HBCIProvider.java 135 2009-07-24 21:50:39Z kleiner $

    This file is part of HBCI4Java
    Copyright (C) 2001-2008  Stefan Palme

    HBCI4Java is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    HBCI4Java is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
*/

package org.kapott.hbci.security;

import org.kapott.hbci.manager.HBCIUtils;

public final class HBCIProvider
     extends java.security.Provider
{
    private void updateProviders()
    {
        // System.setProperty("file.encoding","ISO-8859-1");
        HBCIUtils.log("adding HBCI security provider",HBCIUtils.LOG_DEBUG);
    }

    public HBCIProvider()
    {
        super("HBCIProvider",1.0,"Class for HBCI-needed services");

        put("MessageDigest.RIPEMD160", "org.kapott.hbci.security.RIPEMD160");
        put("MessageDigest.MDC2", "org.kapott.hbci.security.MDC2");

        put("Signature.ISO9796p1", "org.kapott.hbci.security.ISO9796p1");
        put("Signature.ISO9796p2", "org.kapott.hbci.security.ISO9796p2");
        put("Signature.PKCS1_PSS", "org.kapott.hbci.security.PKCS1_PSS");

        // put("Alg.Alias.MessageDigest.Dig_999", "RIPEMD160");
        // put("Alg.Alias.Signature.Sig_10", "ISO9796p1");

        updateProviders();
    }
}
