package org.kapott.hbci.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.kapott.hbci.callback.HBCICallbackConsole;
import org.kapott.hbci.manager.HBCIUtils;
import org.kapott.hbci.passport.AbstractHBCIPassport;
import org.kapott.hbci.passport.AbstractRDHSWPassport;
import org.kapott.hbci.passport.HBCIPassportInternal;
import org.kapott.hbci.passport.HBCIPassportRDH2File;

public class ConvertRDHNewToRDH2File
{
    public static void main(String[] args) 
    throws IOException
    {
        HBCIUtils.init(null,new HBCICallbackConsole());

        String nameOld=readParam(args,0,"Filename of old RDHNew passport file");
        HBCIUtils.setParam("client.passport.RDHNew.filename",nameOld);
        HBCIUtils.setParam("client.passport.RDHNew.init","1");
        HBCIPassportInternal passportOld=(HBCIPassportInternal)AbstractHBCIPassport.getInstance("RDHNew");

        String nameNew=readParam(args,1,"Filename of new RDH2File passport file");
        HBCIUtils.setParam("client.passport.RDH2File.filename",nameNew);
        HBCIUtils.setParam("client.passport.RDH2File.init","0");
        HBCIPassportInternal passportNew=(HBCIPassportInternal)AbstractHBCIPassport.getInstance("RDH2File");

        passportNew.setCountry(passportOld.getCountry());
        passportNew.setBLZ(passportOld.getBLZ());
        passportNew.setHost(passportOld.getHost());
        passportNew.setPort(passportOld.getPort());
        passportNew.setUserId(passportOld.getUserId());
        passportNew.setCustomerId(passportOld.getCustomerId());
        passportNew.setSysId(passportOld.getSysId());
        passportNew.setSigId(passportOld.getSigId());
        passportNew.setProfileVersion(passportOld.getProfileVersion());
        passportNew.setHBCIVersion(passportOld.getHBCIVersion());
        passportNew.setBPD(passportOld.getBPD());
        passportNew.setUPD(passportOld.getUPD());

        ((HBCIPassportRDH2File)passportNew).setInstSigKey(((AbstractRDHSWPassport)passportOld).getInstSigKey());
        ((HBCIPassportRDH2File)passportNew).setInstEncKey(((AbstractRDHSWPassport)passportOld).getInstEncKey());
        ((HBCIPassportRDH2File)passportNew).setMyPublicSigKey(((AbstractRDHSWPassport)passportOld).getMyPublicSigKey());
        ((HBCIPassportRDH2File)passportNew).setMyPrivateSigKey(((AbstractRDHSWPassport)passportOld).getMyPrivateSigKey());
        ((HBCIPassportRDH2File)passportNew).setMyPublicEncKey(((AbstractRDHSWPassport)passportOld).getMyPublicEncKey());
        ((HBCIPassportRDH2File)passportNew).setMyPrivateEncKey(((AbstractRDHSWPassport)passportOld).getMyPrivateEncKey());
        ((HBCIPassportRDH2File)passportNew).setProfileVersion(((AbstractRDHSWPassport)passportOld).getProfileVersion());

        passportNew.saveChanges();

        passportOld.close();
        passportNew.close();            
    }

    private static String readParam(String[] args,int idx,String st)
    throws IOException
    {
        String ret;

        System.out.print(st+": ");
        System.out.flush();

        if (args.length<=idx) {
            ret=new BufferedReader(new InputStreamReader(System.in)).readLine();
        } else {
            System.out.println(args[idx]);
            ret=args[idx];
        }

        return ret;
    }
}
