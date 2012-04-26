
/*  $Id: GVCustomMsg.java 62 2008-10-22 17:03:26Z kleiner $

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

package org.kapott.hbci.GV;

import org.kapott.hbci.GV_Result.HBCIJobResultImpl;
import org.kapott.hbci.exceptions.InvalidUserDataException;
import org.kapott.hbci.manager.HBCIHandler;
import org.kapott.hbci.manager.HBCIUtilsInternal;
import org.kapott.hbci.manager.LogFilter;
import org.kapott.hbci.passport.HBCIPassport;

public final class GVCustomMsg
    extends HBCIJobImpl
{
    public static String getLowlevelName()
    {
        return "CustomMsg";
    }
    
    public GVCustomMsg(HBCIHandler handler)
    {
        super(handler,getLowlevelName(),new HBCIJobResultImpl());
        
        addConstraint("msg","msg",null, LogFilter.FILTER_NONE);

        HBCIPassport passport=handler.getPassport();
        addConstraint("my.number","KTV.number",passport.getUPD().getProperty("KInfo.KTV.number"), LogFilter.FILTER_IDS);
        addConstraint("my.subnumber","KTV.subnumber",passport.getUPD().getProperty("KInfo.KTV.subnumber",""), LogFilter.FILTER_MOST);
        addConstraint("my.country","KTV.KIK.country",passport.getUPD().getProperty("KInfo.KTV.KIK.country"), LogFilter.FILTER_NONE);
        addConstraint("my.blz","KTV.KIK.blz",passport.getUPD().getProperty("KInfo.KTV.KIK.blz"), LogFilter.FILTER_MOST);
        addConstraint("my.curr","curr",passport.getUPD().getProperty("KInfo.cur",""), LogFilter.FILTER_NONE);
        addConstraint("betreff","betreff","", LogFilter.FILTER_NONE);
        addConstraint("recpt","recpt","", LogFilter.FILTER_NONE);
    }
    
    public void setParam(String paramName,String value)
    {
        if (paramName.equals("msg")) {
            String st_maxlen=getJobRestrictions().getProperty("maxlen");
            
            if (st_maxlen!=null) {
                int maxlen=Integer.parseInt(st_maxlen);
                
                if (value.length()>maxlen) {
                    String msg=HBCIUtilsInternal.getLocMsg("EXCMSG_TOOLONG",new String[] {paramName,value,Integer.toString(maxlen)});
                    if (!HBCIUtilsInternal.ignoreError(getMainPassport(),"client.errors.ignoreWrongJobDataErrors",msg))
                        throw new InvalidUserDataException(msg);
                }
            }
        }
        super.setParam(paramName,value);
    }
    
    public void verifyConstraints()
    {
        super.verifyConstraints();
        checkAccountCRC("my");
    }
}
