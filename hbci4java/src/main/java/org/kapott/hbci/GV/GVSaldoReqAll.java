
/*  $Id: GVSaldoReqAll.java 62 2008-10-22 17:03:26Z kleiner $

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

import org.kapott.hbci.manager.HBCIHandler;
import org.kapott.hbci.manager.LogFilter;
import org.kapott.hbci.passport.HBCIPassport;

public final class GVSaldoReqAll
    extends GVSaldoReq
{
    public static String getLowlevelName()
    {
        return "Saldo";
    }
    
    public GVSaldoReqAll(HBCIHandler handler)
    {
        super(handler,getLowlevelName());

        HBCIPassport passport=handler.getPassport();
        addConstraint("maxentries","maxentries","", LogFilter.FILTER_NONE);
        addConstraint("my.country","KTV.KIK.country",passport.getUPD().getProperty("KInfo.KTV.KIK.country"), LogFilter.FILTER_NONE);
        addConstraint("my.blz","KTV.KIK.blz",passport.getUPD().getProperty("KInfo.KTV.KIK.blz"), LogFilter.FILTER_MOST);
        addConstraint("my.number","KTV.number",passport.getUPD().getProperty("KInfo.KTV.number"), LogFilter.FILTER_IDS);
        addConstraint("my.subnumber","KTV.subnumber",passport.getUPD().getProperty("KInfo.KTV.subnumber",""), LogFilter.FILTER_MOST);
        addConstraint("my.curr","curr",passport.getUPD().getProperty("KInfo.cur",""), LogFilter.FILTER_NONE);
        addConstraint("dummyall","allaccounts", "J", LogFilter.FILTER_NONE);
    }
    
    public void verifyConstraints()
    {
        super.verifyConstraints();
        checkAccountCRC("my");
    }
}
