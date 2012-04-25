
/*  $Id: HBCIKey.java 62 2008-10-22 17:03:26Z kleiner $

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

package org.kapott.hbci.manager;

import java.io.Serializable;
import java.security.Key;

/** Diese Klasse repr�sentiert einen von <em>HBCI4Java</em>verwendeten Schl�ssel.
    Ein solcher HBCI-Schl�ssel besteht aus
    administrativen Daten zu diesem Schl�ssel (Besitzer, Version) sowie den eigentlichen
    kryptographischen Daten. Bei Verwendung von asymmetrischen Sicherheitsverfahren
    (RDH) werden sowohl f�r den �ffentlichen als auch f�r den privaten Schl�sselteil
    intern je ein <code>HBCIKey</code>-Objekt verwendet!
    Bei einigen Sicherheitsverfahren (DDV, PinTan) werden
    die kryptografischen Daten nicht in diesem Objekt gespeichert, sondern nur die
    administrativen.*/
public final class HBCIKey
     implements Serializable
{
    /** L�ndercode des Schl�sselbesitzers */
    public String country;
    /** Bankleitzahl des Schl�sselbesitzers */
    public String blz;
    /** Nutzerkennung des Schl�sselbesitzers. Wenn der Schl�ssel
        einem "richtigen" Nutzer geh�rt, so wird hier seine HBCI-Userkennung eingestellt;
        geh�rt der Schl�ssel der Bank, so steht hier eine bankinterne
        ID (u.U. die Bankleitzahl o.�.) */
    public String userid;
    /** Schl�sselnummer */
    public String num;
    /** Schl�sselversion */
    public String version;
    /** kryptographische Schl�sseldaten (kann <code>null</code> sein)*/
    public Key key;

    /** Neues <code>HBCIKey</code>-Objekt erzeugen */
    public HBCIKey()
    {
    }

    public HBCIKey(String country, String blz, String userid, String num, String version, Key key)
    {
        this.country = country;
        this.blz = blz;
        this.userid = userid;
        this.num = num;
        this.version = version;
        this.key = key;
    }
    
    public String toString()
    {
        StringBuffer ret=new StringBuffer();
        
        ret.append("country="+country);
        ret.append(", blz="+blz);
        ret.append(", userid="+userid);
        ret.append(", num="+num);
        ret.append(", version="+version);
        ret.append(", key="+key);
        
        return ret.toString();
    }
}
