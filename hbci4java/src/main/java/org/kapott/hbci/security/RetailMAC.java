
/*  $Id: RetailMAC.java 62 2008-10-22 17:03:26Z kleiner $

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

import java.security.Key;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;

import org.kapott.hbci.exceptions.HBCI_Exception;

public class RetailMAC
{
    private SecretKey       deskey;
    private SecretKey       desedekey;
    private IvParameterSpec ivspec;

    private byte[]          buffer=new byte[16];
    private int             offset;
    private byte[]          c=new byte[8];
    private Cipher          cipher;

    public RetailMAC(Key key,IvParameterSpec iv)
    {
        try {
            SecretKeyFactory fac=SecretKeyFactory.getInstance("DESede");
            DESedeKeySpec    spec=(DESedeKeySpec)fac.getKeySpec((SecretKey)key,
                                                                DESedeKeySpec.class);
            byte[]           desedekeydata=spec.getKey();

            DESKeySpec spec2=new DESKeySpec(desedekeydata);
            fac=SecretKeyFactory.getInstance("DES");
            this.deskey=fac.generateSecret(spec2);

            this.desedekey=(SecretKey)key;
            this.ivspec=iv;
            this.cipher=Cipher.getInstance("DES");
            reset();
        } catch (Exception e) {
            throw new HBCI_Exception(e);
        }
    }
    
    public byte[] doFinal(byte[] data)
    {
        update(data,0,data.length);
        return doFinal();
    }

    public byte[] doFinal()
    {
        for (int i=0;i<8;i++) {
            buffer[i]^=c[i];
        }

        try {
            Cipher cipher2=Cipher.getInstance("DESede");
            cipher2.init(Cipher.ENCRYPT_MODE,desedekey);
            c=cipher2.doFinal(buffer,0,8);
        } catch (Exception ex) {
            throw new HBCI_Exception(ex);
        }
        
        byte[] ret=new byte[8];
        System.arraycopy(c,0,ret,0,8);

        reset();
        return ret;
    }

    public void update(byte data)
    {
        buffer[offset++]=data;
        if (offset>8) {
            hashIt();
        }
    }

    public void reset()
    {
        Arrays.fill(buffer,(byte)0);
        System.arraycopy(ivspec.getIV(),0,c,0,8);
        offset=0;
    }

    public int getMacLength()
    {
        return 8;
    }

    public void update(byte[] ibuffer,int offset,int len)
    {
        for (int i=0;i<len;i++)
            update(ibuffer[offset+i]);
    }

    private void hashIt()
    {
        for (int i=0;i<8;i++) {
            buffer[i]^=c[i];
        }

        try {
            cipher.init(Cipher.ENCRYPT_MODE,deskey);
            c=cipher.doFinal(buffer,0,8);

            System.arraycopy(buffer,8,buffer,0,8);
            Arrays.fill(buffer,8,16,(byte)0);
            offset-=8;
        } catch (Exception ex) {
            throw new HBCI_Exception(ex);
        }
    }
}
