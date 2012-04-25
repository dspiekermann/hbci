package org.kapott.hbci.GV;

import java.util.Properties;

import org.kapott.hbci.manager.HBCIHandler;
import org.kapott.hbci.manager.HBCIUtilsInternal;
import org.kapott.hbci.manager.LogFilter;
import org.kapott.hbci.passport.HBCIPassport;

public class GVUebEil
    extends GVUeb
{
    public static String getLowlevelName()
    {
        return "UebEil";
    }
    
    public GVUebEil(HBCIHandler handler)
    {
        super(handler,getLowlevelName());

        HBCIPassport passport=handler.getPassport();
        
        addConstraint("src.number","My.number",null, LogFilter.FILTER_IDS);
        addConstraint("src.subnumber","My.subnumber","", LogFilter.FILTER_MOST);
        addConstraint("dst.blz","Other.KIK.blz",null, LogFilter.FILTER_MOST);
        addConstraint("dst.number","Other.number",null, LogFilter.FILTER_IDS);
        addConstraint("dst.subnumber","Other.subnumber","", LogFilter.FILTER_MOST);
        addConstraint("btg.value","BTG.value",null, LogFilter.FILTER_MOST);
        addConstraint("btg.curr","BTG.curr",null, LogFilter.FILTER_NONE);
        addConstraint("name","name",null, LogFilter.FILTER_IDS);

        addConstraint("src.blz","My.KIK.blz",passport.getUPD().getProperty("KInfo.KTV.KIK.blz"), LogFilter.FILTER_MOST);
        addConstraint("src.country","My.KIK.country",passport.getUPD().getProperty("KInfo.KTV.KIK.country"), LogFilter.FILTER_NONE);
        addConstraint("dst.country","Other.KIK.country",passport.getCountry(), LogFilter.FILTER_NONE);
        addConstraint("name2","name2","", LogFilter.FILTER_IDS);
        addConstraint("key","key","51", LogFilter.FILTER_NONE);

        Properties parameters=getJobRestrictions();
        int        maxusage=Integer.parseInt(parameters.getProperty("maxusage"));

        for (int i=0;i<maxusage;i++) {
            String name=HBCIUtilsInternal.withCounter("usage",i);
            addConstraint(name,"usage."+name,"", LogFilter.FILTER_MOST);
        }
    }
}
