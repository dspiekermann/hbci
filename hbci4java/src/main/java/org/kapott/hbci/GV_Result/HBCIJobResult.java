
/*  $Id: HBCIJobResult.java,v 1.1 2011/05/04 22:37:47 willuhn Exp $

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

package org.kapott.hbci.GV_Result;

import java.util.Properties;

import org.kapott.hbci.passport.HBCIPassport;
import org.kapott.hbci.status.HBCIRetVal;
import org.kapott.hbci.status.HBCIStatus;

/** Basis-Interface f�r die R�ckgabedaten von 
 ausgef�hrten HBCI-Jobs. Alle Klassen in diesem Package implementieren dieses
 Interface. In ihm werden Methoden und Felder f�r die Auswertung 
 von Status-Informationen und f�r die R�ckgabe der Antwortdaten
 in ihrer urspr�nglichen Form (wie sie in der HBCI-Nachricht enthalten
 waren) bereitgestellt. */
public interface HBCIJobResult
{
    /** Gibt zur�ck, wieviele HBCI-Statuscode (siehe 
     {@link org.kapott.hbci.status.HBCIRetVal}) in den Statusdaten zu
     diesem Job gespeichert sind. Dabei werden die globalen Statusinformationen
     (die sich auf die gesamte Nachricht beziehen und nicht nur auf ein Segment
     dieses Jobs) nicht mitgez�hlt
     @return Anzahl der HBCI-Statuscodes in den Job-Statusinformationen */
    public int getRetNumber();

    /** Gibt einen bestimmten HBCI-Statuscode aus den Job-Statusinformationen
     zur�ck. Die Anzahl der hier zur Verf�gung stehenden R�ckgabewerte kann
     mit {@link #getRetNumber()} ermittelt werden.
     @param idx Indenummer des HBCI-Statuscodes (von 0 bis Anzahl-1)
     @return einen HBCI-Statuscode */
    public HBCIRetVal getRetVal(int idx);

    /** <p>Gibt an, ob der Job erfolgreich ausgef�hrt wurde oder nicht.</p>
     <p>Bei <code>true</code> ist der Job mit Sicherheit erfolgreich ausgef�hrt worden.
     Bei <code>false</code> kann es sein, dass der Job trotzdem ausgef�hrt wurde und nur
     die Antwortnachricht vom HBCI-Server nicht empfangen werden konnte oder fehlerhaft war.
     In diesem Fall sollte also die Fehlermeldung aus 
     {@link org.kapott.hbci.status.HBCIStatus#getErrorString() jobStatus.getErrorString()} bzw. 
     {@link org.kapott.hbci.status.HBCIStatus#getErrorString() globStatus.getErrorString()}
     genau ausgewertet werden.</p>
     @return <code>true</code>, wenn der Auftrag mit Sicherheit erfolgreich
     eingereicht/ausgef�hrt wurde; sonst <code>false</code>*/
    public boolean isOK();

    /** Gibt die Dialog-ID zur�ck, unter der der dazugeh�rige Job ausgef�hrt wurde.
     Wird haupts�chlich intern verwendet. Zur Bereitstellung einer eindeutigen ID
     f�r den Job siehe {@link #getJobId()}.
     @return Dialog-ID des Dialoges, in welchem der Job ausgef�hrt wurde */
    public String getDialogId();

    /** Gibt die Nachrichtennummer innerhalb des Dialoges zur�ck, in dem der dazugeh�rige Job 
     ausgef�hrt wurde. Wird haupts�chlich intern verwendet. Zur Bereitstellung einer eindeutigen ID
     f�r den Job siehe {@link #getJobId()}.
     @return Nachrichtennummer der Nachricht, in welcher der Job ausgef�hrt wurde */
    public String getMsgNum();

    /** Gibt die Segmentnummer des Segmentes innerhalb der Auftragsnachricht zur�ck,
     in welchem die Job-Daten �bertragen wurden.
     Wird haupts�chlich intern verwendet. Zur Bereitstellung einer eindeutigen ID
     f�r den Job siehe {@link #getJobId()}.
     @return Segmentnummer des Auftragssegmentes */
    public String getSegNum();

    /** Gibt einen Job-Identifikationsstring zur�ck, mit dessen Hilfe sich der Job
     f�r das {@link org.kapott.hbci.GV_Result.GVRStatus Statusprotokoll} identifizieren l�sst
     @return die Job-Identifikationsnummer f�r den dazugeh�rigen Auftrag */
    public String getJobId();

    /** Gibt die Job-Antwortdaten im Rohformat zur�ck. 
     F�r die Keys des Properties-Objektes gibt es zwei Auspr�gungen:
     <ul>
     <li><p>mit Prefix <code>content.</code> bzw. <code>content_NUM.</code>:<br/>
     Dieses Key-Value-Paar stellt ein Datenelement aus der Antwortnachricht dar.
     Der Rest des Keys (nach dem Prefix) gibt dabei den Lowlevel-Namen des
     Ergebnisdatenelementes an. Eine Liste aller m�glichen Lowlevel-Namen kann
     zur Laufzeit mit 
     {@link org.kapott.hbci.manager.HBCIHandler#getLowlevelJobResultNames(String)}
     oder mit {@link org.kapott.hbci.GV.HBCIJob#getJobResultNames()} 
     ermittelt werden.</p></li>
     <li><p>mit Prefix <code>basic</code>:<br/>
     Hier werden jobinterne Daten gespeichert:</p>
     <ul>
     <li><code>basic.dialogid</code> enth&auml;lt die Dialog-ID, mit der der Job ausgef&uuml;hrt wurde</li>
     <li><code>basic.msgnum</code> enth&auml;lt die Nachrichtennummer innerhalb des Dialoges, in dem der Job ausgef&uuml;hrt wurde</li>
     <li><code>basic.segnum</code> enth&auml;lt die Segmentnummer innerhalb der Nachricht, in der der Job ausgef&uuml;hrt wurde</li>
     </ul>
     <p>Diese Daten sollten niemals manuell ausgewertet werden, da es diese <code>basic</code>-Daten
     in Zukunft nicht mehr geben wird!</p></li>
     </ul>
     @return die Antwortdaten im Rohformat */
    public Properties getResultData();

    /** Gibt ein Status-Objekt zur�ck, welches Status-Informationen zur HBCI-Nachricht selbst
     enth�lt, in der die Job-Auftragsdaten �bermittelt wurden.
     @return Statusinformationen zur Auftragsnachricht */
    public HBCIStatus getGlobStatus();

    /** Gibt ein Status-Objekt zur�ck, welches Status-Informationen �ber das Auftragssegment
     enth�lt, in dem die Job-Auftragsdaten �bermittelt wurden.
     @return Status-Informationen, die genau diesen Job betreffen */
    public HBCIStatus getJobStatus();
    
    /** Gibt das Passport-Objekt zur�ck, f�r welches der Job erzeugt wurde.
     * @return Passport-Objekt */
    public HBCIPassport getPassport();
}