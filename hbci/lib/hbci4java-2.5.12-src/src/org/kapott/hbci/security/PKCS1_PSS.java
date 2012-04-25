
/*  $Id$

    This file is part of HBCI4Java
    Copyright (C) 2001-2009  Stefan Palme

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

import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.SignatureSpi;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.Arrays;

import org.kapott.hbci.exceptions.NoHashAlgSetException;

public class PKCS1_PSS 
    extends SignatureSpi 
{
    private RSAPublicKey       pubKey;
    private PrivateKey         privKey;
    private MessageDigest      dig;
    private SignatureParamSpec param;
    
    // ----- some interface stuff ---------------------------------------------

    protected Object engineGetParameter(String parameter)
    {
        return null;
    }

    protected void engineInitSign(PrivateKey privateKey)
    {
        try {
            String provider=param.getProvider();
            if (provider!=null) {
                dig=MessageDigest.getInstance(param.getHashAlg(),provider);
            } else {
                dig=MessageDigest.getInstance(param.getHashAlg());
            }
        } catch (Exception e) {
            throw new NoHashAlgSetException(e);
        }

        this.privKey=privateKey;
    }

    protected void engineInitVerify(PublicKey publicKey)
    {
        try {
            String provider=param.getProvider();
            if (provider!=null) {
                dig=MessageDigest.getInstance(param.getHashAlg(),provider);
            } else {
                dig=MessageDigest.getInstance(param.getHashAlg());
            }
        } catch (Exception e) {
            throw new NoHashAlgSetException(e);
        }

        this.pubKey=(RSAPublicKey)publicKey;
    }

    protected void engineSetParameter(String param, Object value)
    {
    }

    protected void engineSetParameter(AlgorithmParameterSpec param)
        throws InvalidAlgorithmParameterException
    {
        if (param instanceof SignatureParamSpec)
            this.param=(SignatureParamSpec)(param);
        else {
            throw new InvalidAlgorithmParameterException();
        }
    }

    protected void engineUpdate(byte b)
    {
        dig.update(b);
    }

    protected void engineUpdate(byte[] b,int offset,int length)
    {
        for (int i=0;i<length;i++) {
            engineUpdate(b[offset+i]);
        }
    }

    protected int engineSign(byte[] output,int offset,int len)
    throws SignatureException
    {
        byte[] sig=engineSign();

        if (offset+len>output.length)
            throw new SignatureException("output result too large for buffer");
        System.arraycopy(sig,0,output,offset,sig.length);
        return sig.length;
    }

    protected byte[] engineSign()
        throws SignatureException
    {
        byte[] msg = dig.digest();
        return pss_sign(privKey, msg);
    }


    protected boolean engineVerify(byte[] sig)
        throws SignatureException
    {
        byte[] msg = dig.digest();
        return pss_verify(pubKey, msg, sig);
    }

    // --- stuff from the PKCS#1-PSS specification ---------------------------
    
    private byte[] i2os(BigInteger x, int outLen)
    {
        byte[] bytes=x.toByteArray();
        
        if (bytes.length>outLen) {
            // created output len does not fit into outLen
            // maybe this are only leading zeroes, so we will check this
            for (int i=0; i<(bytes.length-outLen); i++) {
                if (bytes[i]!=0) {
                    throw new RuntimeException("value too large");
                }
            }
            
            // ok, now remove leading zeroes
            byte[] out=new byte[outLen];
            System.arraycopy(bytes, bytes.length-outLen, out, 0, outLen);
            bytes = out;
            
        } else if (bytes.length<outLen) {
            // created output is too small, so create leading zeroes
            byte[] out=new byte[outLen];
            System.arraycopy(bytes, 0, out, outLen-bytes.length, bytes.length);
            bytes = out;
        }
        
        return bytes;
    }
    
    private BigInteger os2i(byte[] bytes)
    {
        return new BigInteger(+1, bytes);
    }
    
    private BigInteger sp1(PrivateKey key, BigInteger m)
    {
        BigInteger result;
        
        if (key instanceof RSAPrivateKey) {
            BigInteger d=((RSAPrivateKey)key).getPrivateExponent();
            BigInteger n=((RSAPrivateKey)key).getModulus();
            result = m.modPow(d,n);
        } else {
            RSAPrivateCrtKey2 key2=(RSAPrivateCrtKey2)key;
            BigInteger p=key2.getP();
            BigInteger q=key2.getQ();
            BigInteger dP=key2.getdP();
            BigInteger dQ=key2.getdQ();
            BigInteger qInv=key2.getQInv();
            
            BigInteger s1 = m.modPow(dP,p);
            BigInteger s2 = m.modPow(dQ,q);
            BigInteger h = s1.subtract(s2).multiply(qInv).mod(p);
            result = s2.add(q.multiply(h));
        }
        
        return result;
    }
    
    private BigInteger vp1(RSAPublicKey key, BigInteger s)
    {
        BigInteger e=key.getPublicExponent();
        BigInteger n=key.getModulus();
        BigInteger m=s.modPow(e,n);
        return m;
    }
    
    private byte[] concat(byte[] x1, byte[] x2)
    {
        byte[] result=new byte[x1.length+x2.length];
        System.arraycopy(x1,0, result,0,         x1.length);
        System.arraycopy(x2,0, result,x1.length, x2.length);
        return result;
    }
    
    private byte[] hash(byte[] data) 
    {
        dig.reset();
        return dig.digest(data);
    }
    
    private byte[] mgf1(byte[] mgfSeed, int maskLen)
    {
        int    hLen=dig.getDigestLength();
        byte[] T=new byte[0];
        for (int i=0; i<Math.ceil(maskLen/(double)hLen); i++) {
            byte[] c=i2os(new BigInteger(Integer.toString(i)), 4);
            T = concat(T, hash(concat(mgfSeed,c)));
        }
        
        byte[] result=new byte[maskLen];
        System.arraycopy(T,0, result,0, maskLen);
        return result;
    }
    
    private byte[] random_os(int len)
    {
        byte[] result=new byte[len];
        for (int i=0; i<len; i++) {
            result[i]=(byte)(256*Math.random());
        }
        return result;
    }
    
    private byte[] xor_os(byte[] a1, byte[] a2)
    {
        if (a1.length!=a2.length) {
            throw new RuntimeException("a1.len != a2.len");
        }
        
        byte[] result=new byte[a1.length];
        for (int i=0; i<result.length; i++) {
            result[i] = (byte)(a1[i] ^ a2[i]);
        }
        
        return result;
    }
    
    private byte[] emsa_encode(byte[] msg, int emBits)
    {
        int emLen=emBits>>3;
        if ((emBits&7) != 0) {
            emLen++;
        }
        
        byte[] mHash = hash(msg);
        int    hLen = dig.getDigestLength();
        int    sLen = hLen;
        
        byte[] salt = random_os(sLen);
        // TODO: test salt byte[] salt = string2ba("e3 b5 d5 d0 02 c1 bc e5 0c 2b 65 ef 88 a1 88 d8 3b ce 7e 61");
        
        byte[] zeroes = new byte[8];
        byte[] m2 = concat(concat(zeroes,mHash),salt);
        byte[] H = hash(m2);
        byte[] PS = new byte[emLen-sLen-hLen-2];
        byte[] DB = concat(concat(PS, new byte[] {0x01}), salt);
        byte[] dbMask = mgf1(H, emLen-hLen-1);
        byte[] maskedDB = xor_os(DB, dbMask);
        
        // set leftmost X bits in maskedDB to zero
        int  tooMuchBits=(emLen<<3)-emBits;
        byte mask=(byte)(0xFF>>>tooMuchBits);
        maskedDB[0] &= mask;
        
        byte[] EM = concat(concat(maskedDB,H), new byte[] {(byte)0xBC});
        return EM;
    }
    
    private boolean emsa_verify(byte[] msg, byte[] EM, int emBits)
    {
        int emLen=emBits>>3;
        if ((emBits&7) != 0) {
            emLen++;
        }
        
        byte[] mHash = hash(msg);
        int    hLen = dig.getDigestLength();
        int    sLen = hLen;
        if (EM[EM.length-1]!=(byte)0xBC) {
            return false;
        }
        
        byte[] maskedDB = new byte[emLen-hLen-1];
        byte[] H = new byte[hLen];
        System.arraycopy(EM,0,            maskedDB,0, emLen-hLen-1);
        System.arraycopy(EM,emLen-hLen-1, H,0,        hLen);
        
        // TODO: verify if first X bits of maskedDB are zero
        
        byte[] dbMask = mgf1(H, emLen-hLen-1);
        byte[] DB = xor_os(maskedDB, dbMask);
        
        // set leftmost X bits of DB to zero
        int  tooMuchBits=(emLen<<3)-emBits;
        byte mask=(byte)(0xFF>>>tooMuchBits);
        DB[0] &= mask;
        
        // TODO: another consistency check
        
        byte[] salt = new byte[sLen];
        System.arraycopy(DB,DB.length-sLen, salt,0, sLen);
        
        byte[] zeroes = new byte[8];
        byte[] m2 = concat(concat(zeroes,mHash),salt);
        
        byte[] H2 = hash(m2);
        
        return Arrays.equals(H,H2);
    }
    
    private byte[] pss_sign(PrivateKey key, byte[] msg)
    {
        // Modulus holen, weil dessen Bitlänge benötigt wird
        BigInteger bModulus;
        if (privKey instanceof RSAPrivateKey) {
            bModulus=((RSAPrivateKey)key).getModulus();
        } else {
            bModulus=((RSAPrivateCrtKey2)key).getP().multiply(((RSAPrivateCrtKey2)key).getQ());
        }        
        
        int modBits = bModulus.bitLength();
        int k = modBits>>3;
        if ((modBits&7) != 0) {
            k++;
        }

        byte[] EM = emsa_encode(msg, modBits-1);
        BigInteger m = os2i(EM);
        BigInteger s = sp1(key, m);
        byte[] S = i2os(s, k);
        return S;
    }
    
    private boolean pss_verify(RSAPublicKey key, byte[] msg, byte[] S)
    {
        BigInteger s = os2i(S);
        BigInteger m = vp1(key, s);
        BigInteger n = key.getModulus();
        
        int emBits = n.bitLength()-1;
        int emLen  = emBits>>3;
        if ((emBits&7)!=0) {
            emLen++;
        }
        
        byte[] EM = i2os(m, emLen);
        return emsa_verify(msg, EM, emBits);
    }
    
    // ---- test programm with the "official" test vectors -------------------

    private void main2()
        throws Exception
    {
        try {
            SignatureParamSpec p=new SignatureParamSpec("SHA-1", null);
            
            String provider=p.getProvider();
            if (provider!=null) {
                dig=MessageDigest.getInstance(p.getHashAlg(),provider);
            } else {
                dig=MessageDigest.getInstance(p.getHashAlg());
            }
        } catch (Exception e) {
            throw new NoHashAlgSetException(e);
        }
        
        BigInteger p=new BigInteger(+1, string2ba("d1 7f 65 5b f2 7c 8b 16 d3 54 62 c9 05 cc 04 a2 6f 37 e2 a6 7f a9 c0 ce 0d ce d4 72 39 4a 0d f7 43 fe 7f 92 9e 37 8e fd b3 68 ed df f4 53 cf 00 7a f6 d9 48 e0 ad e7 57 37 1f 8a 71 1e 27 8f 6b"));
        BigInteger q=new BigInteger(+1, string2ba("c6 d9 2b 6f ee 74 14 d1 35 8c e1 54 6f b6 29 87 53 0b 90 bd 15 e0 f1 49 63 a5 e2 63 5a db 69 34 7e c0 c0 1b 2a b1 76 3f d8 ac 1a 59 2f b2 27 57 46 3a 98 24 25 bb 97 a3 a4 37 c5 bf 86 d0 3f 2f"));
        BigInteger dP=new BigInteger(+1, string2ba("9d 0d bf 83 e5 ce 9e 4b 17 54 dc d5 cd 05 bc b7 b5 5f 15 08 33 0e a4 9f 14 d4 e8 89 55 0f 82 56 cb 5f 80 6d ff 34 b1 7a da 44 20 88 53 57 7d 08 e4 26 28 90 ac f7 52 46 1c ea 05 54 76 01 bc 4f"));
        BigInteger dQ=new BigInteger(+1, string2ba("12 91 a5 24 c6 b7 c0 59 e9 0e 46 dc 83 b2 17 1e b3 fa 98 81 8f d1 79 b6 c8 bf 6c ec aa 47 63 03 ab f2 83 fe 05 76 9c fc 49 57 88 fe 5b 1d df de 9e 88 4a 3c d5 e9 36 b7 e9 55 eb f9 7e b5 63 b1"));
        BigInteger qInv=new BigInteger(+1, string2ba("a6 3f 1d a3 8b 95 0c 9a d1 c6 7c e0 d6 77 ec 29 14 cd 7d 40 06 2d f4 2a 67 eb 19 8a 17 6f 97 42 aa c7 c5 fe a1 4f 22 97 66 2b 84 81 2c 4d ef c4 9a 80 25 ab 43 82 28 6b e4 c0 37 88 dd 01 d6 9f"));
        PrivateKey key=new RSAPrivateCrtKey2(p,q,dP,dQ,qInv);
        
        BigInteger modulus=new BigInteger(+1, string2ba("a2 ba 40 ee 07 e3 b2 bd 2f 02 ce 22 7f 36 a1 95 02 44 86 e4 9c 19 cb 41 bb bd fb ba 98 b2 2b 0e 57 7c 2e ea ff a2 0d 88 3a 76 e6 5e 39 4c 69 d4 b3 c0 5a 1e 8f ad da 27 ed b2 a4 2b c0 00 fe 88 8b 9b 32 c2 2d 15 ad d0 cd 76 b3 e7 93 6e 19 95 5b 22 0d d1 7d 4e a9 04 b1 ec 10 2b 2e 4d e7 75 12 22 aa 99 15 10 24 c7 cb 41 cc 5e a2 1d 00 ee b4 1f 7c 80 08 34 d2 c6 e0 6b ce 3b ce 7e a9 a5"));
        BigInteger exponent=new BigInteger(+1, string2ba("01 00 01"));
        RSAPublicKeySpec spec=new RSAPublicKeySpec(modulus, exponent);
        KeyFactory fac=KeyFactory.getInstance("RSA");
        RSAPublicKey pubKey=(RSAPublicKey)fac.generatePublic(spec);
        
        byte[] msg=string2ba("85 9e ef 2f d7 8a ca 00 30 8b dc 47 11 93 bf 55 bf 9d 78 db 8f 8a 67 2b 48 46 34 f3 c9 c2 6e 64 78 ae 10 26 0f e0 dd 8c 08 2e 53 a5 29 3a f2 17 3c d5 0c 6d 5d 35 4f eb f7 8b 26 02 1c 25 c0 27 12 e7 8c d4 69 4c 9f 46 97 77 e4 51 e7 f8 e9 e0 4c d3 73 9c 6b bf ed ae 48 7f b5 56 44 e9 ca 74 ff 77 a5 3c b7 29 80 2f 6e d4 a5 ff a8 ba 15 98 90 fc");
        
        byte[]  sig = pss_sign(key, msg);
        boolean ok = pss_verify(pubKey, msg, sig);
    }
    
    public static void main(String[] args)
        throws Exception
    {
        PKCS1_PSS pkcs=new PKCS1_PSS();        
        pkcs.main2();
    }
    
    // ----- some helper methods ---------------------------------------------

    private String ba2st(byte[] ba) {
        StringBuffer result=new StringBuffer();
        
        for (int i=0;i<ba.length;i++) {
                int    x = ba[i];
                if (x<0) {
                        x+=256;
                }
                String st=Integer.toString(x,16);
                if (st.length()==1) {
                        st="0"+st;
                }
                result.append(st.substring(st.length()-2)+" ");
        }
        
        return result.toString();
    }
    
    private byte[] string2ba(String st)
    {
        byte[]   result=new byte[0];
        String[] bytes=st.split(" +");
        
        for (int i=0; i<bytes.length; i++) {
            int x=Integer.parseInt(bytes[i],16);
            result = concat(result, new byte[] {(byte)x});
        }
        
        return result;
    }
}
