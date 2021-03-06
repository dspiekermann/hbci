
/*  $Id: PBKDF2.java 150 2009-08-12 08:54:26Z kleiner $

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

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class PBKDF2
{
    public static byte[] deriveKey(byte[] salt, long nof_iterations, byte[] pass, int dkLen, String algname)
    {
        try {
            // passphrase als input f�r key verwenden
            SecretKeySpec keyspec=new SecretKeySpec(pass,algname);
            Mac           mac=Mac.getInstance(algname);
            mac.init(keyspec);
            
            // siehe pkcs #5 v2.0 (RDH-2) bzw. pkcs #5 v2.1 (RDH-10)
            int    hLen=mac.getMacLength();
            byte[] dk=new byte[dkLen];
            
            int l=((dkLen-1)/hLen)+1;
            int r=dkLen - (l-1)*hLen;
            
            // System.out.println("deriveKey: hLen="+hLen+"; l="+l+"; r="+r);
            
            byte[] t=new byte[hLen];
            for (long i=0;i<l;i++) {
                Arrays.fill(t,(byte)0);
                
                byte[] u=new byte[salt.length+4];
                System.arraycopy(salt,0, u,0, salt.length);
                u[salt.length+0]=(byte)(((i+1)>>24)&0xFF);
                u[salt.length+1]=(byte)(((i+1)>>16)&0xFF);
                u[salt.length+2]=(byte)(((i+1)>>8)&0xFF);
                u[salt.length+3]=(byte)(((i+1)>>0)&0xFF);
                
                for (int j=0;j<nof_iterations;j++) {
                    u=mac.doFinal(u);
                    for (int k=0;k<t.length;k++) {
                        t[k]^=u[k];
                    }
                }
                
                System.arraycopy(t,0, dk,(int)(i*hLen), ((i!=(l-1))?hLen:r));
            }
            
            // System.out.print("derived key: ");
            // for (int i=0;i<dk.length;i++) {
            //    int x=dk[i]&0xFF;
            //    System.out.print(Integer.toString(x,16)+" ");
            // }
            // System.out.println();
            
            return dk;
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (IllegalStateException e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] paddingRFC1423(byte[] input)
    {
        int    len=input.length;
        
        // auf das n�chste vielfache von 8 vergr��ern
        int    newlen=((len>>3)+1)<<3;
        
        // anzahl der angef�gten padding-bytes ermitteln
        byte   diff=(byte)(newlen-len);
        
        // output-puffer erzeugen
        byte[] output=new byte[newlen];
        
        // originale daten in output-puffer kopieren
        System.arraycopy(input,0, output,0, len);
        
        // padding besteht aus N bytes mit dem inhalt N
        for (int i=len;i<newlen;i++) {
            output[i]=diff;
        }
    
        return output;
    }

}
