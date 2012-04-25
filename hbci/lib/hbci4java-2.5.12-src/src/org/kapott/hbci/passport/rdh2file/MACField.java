
/*  $Id: MACField.java 62 2008-10-22 17:03:26Z kleiner $

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

package org.kapott.hbci.passport.rdh2file;


public class MACField
    extends TLV
{
    public MACField()
    {
        super(0x4d44);
    }
    
    public MACField(TLV tlv)
    {
        super(tlv);
    }
    
    public byte[] getMac()
    {
        return this.getData();
    }
    
    public void setMac(byte[] mac)
    {
        this.setData(mac);
    }
    
    public void updateData()
    {
    }
    
    public String toString()
    {
        StringBuffer ret=new StringBuffer();
        byte[]       mac=this.getData();
        ret.append("macfield: mac=");
        for (int i=0;i<mac.length;i++) {
            int x=mac[i]&0xFF;
            ret.append(Integer.toString(x,16)+" ");
        }
        
        return ret.toString();
    }
}