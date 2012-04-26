
/*  $Id: INILetter.java,v 1.1 2011/05/04 22:37:42 willuhn Exp $

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

package org.kapott.hbci.passport;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;

import org.kapott.cryptalgs.SignatureParamSpec;
import org.kapott.hbci.exceptions.HBCI_Exception;
import org.kapott.hbci.manager.HBCIKey;
import org.kapott.hbci.manager.HBCIUtils;

/** Hilfsklasse f�r das Erzeugen von INI-Briefen (f�r RDH-Zug�nge). Diese Klasse
    erm�glicht das Erzeugen von INI-Briefen. Dazu werden Methoden bereitgestellt,
    mit deren Hilfe die f�r einen INI-Brief ben�tigten Daten ermittelt werden
    k�nnen. Au�erdem liefert die {@link #toString()}-Methode einen vorgefertigten
    INI-Brief (kann als Vorlage benutzt werden). */
public class INILetter
{
    /** INI-Brief f�r Institutsschl�ssel (wird f�r Vergleich mit tats�chlichem
        INI-Brief von der Bank ben�tigt) */
    public static final int TYPE_INST=1;
    /** INI-Brief f�r Nutzerschl�ssel erzeugen (muss nach dem Erstellen neuer
        Schl�ssel an die Bank versandt werden) */
    public static final int TYPE_USER=2;
    
    private HBCIPassportInternal passport;
    private HBCIKey              hbcikey;
    
    /** Anlegen eines neuen INI-Brief-Objektes.
        @param passport das Passport-Objekt (entspricht einem HBCI-Zugang), f�r
               den ein INI-Brief ben�tigt wird
        @param type gibt an, f�r welche Schl�ssel aus dem <code>passport</code> 
               der INI-Brief ben�tigt wird ({@link #TYPE_INST} f�r die Bankschl�ssel,
               {@link #TYPE_USER} f�r die Schl�ssel des Nutzers) */
    public INILetter(HBCIPassport passport,int type)
    {
        this.passport=(HBCIPassportInternal)passport;
        
        if (type==TYPE_INST) {
            hbcikey=passport.getInstSigKey();
            if (hbcikey==null)
                hbcikey=passport.getInstEncKey();
        } else {
            hbcikey=passport.getMyPublicSigKey();
        }
    }
    
    public static byte[] formatKeyData(BigInteger x, int minsize)
    {
        byte[] xArray=x.toByteArray();
        
        int realbits=x.bitLength();
        // System.out.println("bitlength: "+bits);
        int realbytes=realbits>>3;
        if ((realbits&0x07)!=0) {
            realbytes++;
        }
        // System.out.println("bytes: "+bytes);
        
        int finalsize=Math.max(minsize,realbytes);
        // System.out.println("size: "+size);
        
        byte[] retArray=new byte[finalsize];
        System.arraycopy(xArray, xArray.length-realbytes,
                         retArray, finalsize-realbytes,
                         realbytes);
        
        return retArray;
    }
    
    private BigInteger getModulus()
    {
        return ((RSAPublicKey)hbcikey.key).getModulus();
    }
    
    private BigInteger getExponent()
    {
        return ((RSAPublicKey)hbcikey.key).getPublicExponent();
    }
    
    /** Gibt den Modulus des �ffentlichen Schl�ssels zur�ck. 
        @return Modulus des �ffentlichen Schl�ssels */
    public byte[] getKeyModulusDisplay()
    {
        int minsize;
        if (passport.getProfileVersion().equals("1")) {
            minsize=96;
        } else {
            minsize=0;
        }
        return formatKeyData(getModulus(), minsize);
    }

    /** Gibt den Exponenten des �ffentlichen Schl�ssels zur�ck. 
        @return Exponent des �ffentlichen Schl�ssels */
    public byte[] getKeyExponentDisplay()
    {
        int minsize;
        if (passport.getProfileVersion().equals("1")) {
            minsize=96;
        } else {
            minsize=getKeyModulusDisplay().length;
        }
        return formatKeyData(getExponent(), minsize);
    }
    
    /** Gibt den Hashwert des �ffentlichen Schl�ssels zur�ck. 
        @return Hashwert des �ffentlichen Schl�ssels */
    public byte[] getKeyHashDisplay()
    {
        try {
            byte[] modulus=formatKeyData(getModulus(), 128);
            int    modSize=modulus.length;
            
            byte[] exponent=formatKeyData(getExponent(), Math.max(128, modSize));
            int    expSize=exponent.length;

            byte[] retArray=new byte[modSize+expSize];
            
            System.arraycopy(exponent,0, retArray,0,       expSize);
            System.arraycopy(modulus,0,  retArray,expSize, modSize);
            
            // System.out.println("hashdata: "+HBCIUtils.data2hex(retArray));
            
            // hash-verfahren h�ngt von rdh-profil ab
            MessageDigest      dig;
            SignatureParamSpec hashSpec=((AbstractRDHPassport)(passport)).getSignatureParamSpec();
            String             provider=hashSpec.getProvider();
            if (provider!=null) {
                dig=MessageDigest.getInstance(hashSpec.getHashAlg(), provider);
            } else {
                dig=MessageDigest.getInstance(hashSpec.getHashAlg());
            }
            return dig.digest(retArray);
        } catch (Exception e) {
            throw new HBCI_Exception("*** error while calculating hash value",e);
        }
    }
    
    /** Gibt einen "fertigen" INI-Brief zur�ck.
        @return INI-Brief */
    public String toString()
    {
        StringWriter ret=new StringWriter();
        PrintWriter out=new PrintWriter(ret);
        
        Date date=new Date();
        
        out.println();
        out.println("INI-Brief HBCI");
        out.println();
        out.println();
        out.println("Datum:                       "+HBCIUtils.date2StringLocal(date));
        out.println();
        out.println("Uhrzeit:                     "+HBCIUtils.time2StringLocal(date));
        out.println();
        out.println("Empf�nger BLZ:               "+passport.getBLZ());
        out.println();
        out.println("Benutzerkennung:             "+passport.getUserId());
        out.println();
        out.println("Schl�sselnummer:             "+hbcikey.num);
        out.println();
        out.println("Schl�sselversion:            "+hbcikey.version);
        out.println();
        out.println("HBCI-Version:                "+passport.getHBCIVersion());
        out.println();
        out.println("Sicherheitsprofil:           "+passport.getProfileMethod()+" "+passport.getProfileVersion());
        out.println();
        out.println();
        out.println("�ffentlicher Schl�ssel f�r die elektronische Signatur");
        out.println();
        out.println("  Exponent");
        out.println();
        String st=HBCIUtils.data2hex(getKeyExponentDisplay());
        for (int line=0;true;line++) {
            if (line*16*3>=st.length())
                break;
            out.println("    "+st.substring(line*(16*3), Math.min((line+1)*16*3, st.length())));
        }
        
        out.println();
        out.println("  Modulus");
        out.println();
        st=HBCIUtils.data2hex(getKeyModulusDisplay());
        for (int line=0;true;line++) {
            if (line*16*3>=st.length())
                break;
            out.println("    "+st.substring(line*(16*3), Math.min((line+1)*16*3, st.length())));
        }

        out.println();
        out.println("  Hashwert");
        out.println();
        st=HBCIUtils.data2hex(getKeyHashDisplay());
        for (int line=0;true;line++) {
            if (line*10*3>=st.length())
                break;
            out.println("    "+st.substring(line*(10*3), Math.min((line+1)*10*3, st.length())));
        }
        
        out.println();
        out.println("Ich best�tige hiermit den obigen �ffentlichen Schl�ssel");
        out.println("f�r meine elektronische Signatur");
        out.println();
        out.println();
        out.println();
        out.println();
        out.println();
        out.println();
        out.println();
        out.println("Ort/Datum                                       Unterschrift");
        
        out.close();
        return ret.toString();
    }
}
