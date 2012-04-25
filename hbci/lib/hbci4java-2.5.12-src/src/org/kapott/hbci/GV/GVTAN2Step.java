
/*  $Id: GVTAN2Step.java 62 2008-10-22 17:03:26Z kleiner $

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


import java.util.Properties;

import org.kapott.hbci.GV_Result.GVRSaldoReq;
import org.kapott.hbci.manager.HBCIHandler;
import org.kapott.hbci.manager.HBCIUtils;
import org.kapott.hbci.manager.LogFilter;
import org.kapott.hbci.status.HBCIMsgStatus;

/**
 * @author stefan.palme
 */
public class GVTAN2Step 
    extends HBCIJobImpl
{
    private GVTAN2Step  otherTAN2StepTask;
    private HBCIJobImpl origTask;
    
    public static String getLowlevelName()
    {
        return "TAN2Step";
    }
    
    public GVTAN2Step(HBCIHandler handler)
    {
        super(handler,getLowlevelName(),new GVRSaldoReq());

        addConstraint("process","process",null, LogFilter.FILTER_NONE);
        addConstraint("orderhash","orderhash","", LogFilter.FILTER_NONE);
        addConstraint("orderref","orderref","", LogFilter.FILTER_NONE);
        addConstraint("listidx","listidx","", LogFilter.FILTER_NONE);
        addConstraint("notlasttan","notlasttan","N", LogFilter.FILTER_NONE);
        addConstraint("info","info","", LogFilter.FILTER_NONE);
        
        addConstraint("storno","storno","", LogFilter.FILTER_NONE);
        addConstraint("challengeklass","challengeklass","", LogFilter.FILTER_NONE);
        addConstraint("ChallengeKlassParam1", "ChallengeKlassParams.param","", LogFilter.FILTER_IDS);
        addConstraint("ChallengeKlassParam2", "ChallengeKlassParams.param_2","", LogFilter.FILTER_IDS);
        addConstraint("ChallengeKlassParam3", "ChallengeKlassParams.param_3","", LogFilter.FILTER_IDS);
        addConstraint("ChallengeKlassParam4", "ChallengeKlassParams.param_4","", LogFilter.FILTER_IDS);
        addConstraint("ChallengeKlassParam5", "ChallengeKlassParams.param_5","", LogFilter.FILTER_IDS);
        addConstraint("ChallengeKlassParam6", "ChallengeKlassParams.param_6","", LogFilter.FILTER_IDS);
        addConstraint("ChallengeKlassParam7", "ChallengeKlassParams.param_7","", LogFilter.FILTER_IDS);
        addConstraint("ChallengeKlassParam8", "ChallengeKlassParams.param_8","", LogFilter.FILTER_IDS);
        addConstraint("ChallengeKlassParam9", "ChallengeKlassParams.param_9","", LogFilter.FILTER_IDS);
        
        // TODO: tanmedia fehlt
    }
    
    public void setParam(String paramName, String value)
    {
        if (paramName.equals("orderhash")) {
            value="B"+value;
        }
        super.setParam(paramName,value);
    }

    public void storeOtherTAN2StepTask(GVTAN2Step other)
    {
        this.otherTAN2StepTask=other;
    }
    
    public void storeOriginalTask(HBCIJobImpl task)
    {
        this.origTask=task;
    }
    
    protected void saveReturnValues(HBCIMsgStatus status, int sref) {
        super.saveReturnValues(status, sref);
        
        if (origTask!=null) {
            int orig_segnum=Integer.parseInt(origTask.getJobResult().getSegNum());
            HBCIUtils.log("storing return values in orig task (segnum="+orig_segnum+")", HBCIUtils.LOG_DEBUG);
            origTask.saveReturnValues(status,orig_segnum);
        }
    }

    protected void extractResults(HBCIMsgStatus msgstatus,String header,int idx)
    {
        Properties result=msgstatus.getData();
        String segcode=result.getProperty(header+".SegHead.code");
        HBCIUtils.log("found HKTAN response with segcode "+segcode,HBCIUtils.LOG_DEBUG);
        
        if (origTask!=null && new StringBuffer(origTask.getHBCICode()).replace(1,2,"I").toString().equals(segcode)) {
            // das ist f�r PV#2, wenn nach dem nachtr�glichen versenden der TAN das
            // antwortsegment des jobs aus der vorherigen Nachricht zur�ckommt
            HBCIUtils.log("this is a response segment for the original task - storing results in the original job",HBCIUtils.LOG_DEBUG);
            origTask.extractResults(msgstatus,header,idx);
        } else {
            HBCIUtils.log("this is a \"real\" HKTAN response - analyzing HITAN data",HBCIUtils.LOG_DEBUG);
            
            String challenge=result.getProperty(header+".challenge");
            if (challenge!=null) {
                HBCIUtils.log("found challenge '"+challenge+"' in HITAN - saving it temporarily in passport",HBCIUtils.LOG_DEBUG);
                // das ist f�r PV#1 (die antwort auf das einreichen des auftrags-hashs) oder 
                // f�r PV#2 (die antwort auf das einreichen des auftrages)
                // in jedem fall muss mit der n�chsten nachricht die TAN �bertragen werden
                getMainPassport().setPersistentData("pintan_challenge",challenge);
                
                // TODO: es muss hier evtl. noch �berpr�ft werden, ob
                // der zur�ckgegebene auftragshashwert mit dem urspr�nglich versandten
                // �bereinstimmt
                // f�r pv#1 gilt: hitan_orderhash == sent_orderhash (from previous hktan)
                // f�r pv#2 gilt: hitan_orderhash == orderhash(gv from previous GV segment)
                
                // TODO: hier noch die optionale DEG ChallengeValidity bereitstellen
            }
            
            String orderref=result.getProperty(header+".orderref");
            if (orderref!=null) {
                // orderref ist nur f�r PV#2 relevant
                HBCIUtils.log("found orderref '"+orderref+"' in HITAN",HBCIUtils.LOG_DEBUG);
                if (otherTAN2StepTask!=null) {
                    // hier sind wir ganz sicher in PV#2. das hier ist die antwort auf das
                    // erste HKTAN (welches mit dem eigentlichen auftrag verschickt wird)
                    // die orderref muss im zweiten HKTAN-job gespeichert werden, weil in
                    // dieser zweiten nachricht dann die TAN mit �bertragen werden muss
                    HBCIUtils.log("storing it in following HKTAN task",HBCIUtils.LOG_DEBUG);
                    otherTAN2StepTask.setParam("orderref",orderref);
                } else {
                    HBCIUtils.log("no other HKTAN task known - ignoring orderref",HBCIUtils.LOG_DEBUG);
                }
            }
            
        }
    }
}
