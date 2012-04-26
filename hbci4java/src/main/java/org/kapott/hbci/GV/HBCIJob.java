
/*  $Id: HBCIJob.java,v 1.1 2011/05/04 22:37:53 willuhn Exp $

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

import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.kapott.hbci.GV_Result.HBCIJobResult;
import org.kapott.hbci.passport.HBCIPassport;
import org.kapott.hbci.structures.Konto;
import org.kapott.hbci.structures.Value;

/** <p>Schnittstelle f�r alle Auftr�ge, die via HBCI ausgef�hrt werden sollen. Ein
 HBCIJob-Objekt wird nur innerhalb von <em>HBCI4Java</em> verwaltet. Durch Aufruf einer der Methoden
 {@link org.kapott.hbci.manager.HBCIHandler#newJob(String)} oder
 {@link org.kapott.hbci.manager.HBCIHandler#newLowlevelJob(String)} wird
 eine neue Instanz eines HBCIJobs erzeugt. Die konkrete Klasse dieser Instanz ist
 f�r den Anwendungsentwickler nicht von Bedeutung.</p>
 <p>Die Anwendung muss nur die f�r diesen Job ben�tigten Parameter setzen (mit 
 {@link #setParam(String,String)}). Falls dieser Job mehrere digitale
 Signaturen ben�tigt, k�nnen mit der Methode {@link #addSignaturePassport(HBCIPassport,String)} 
 weitere Passport-Objekte zu diesem Job hinzugef�gt werden,
 die dann als Zweit-, Dritt-, ...-Signatur bei der Nachrichtenerzeugung verwendet
 werden. Anschlie�end kann der fertig spezifizierte Job zum aktuellen HBCI-Dialog 
 hinzugef�gt werden 
 ({@link #addToQueue()}).</p>
 <p>Nach Ausf�hrung des HBCI-Dialoges k�nnen die R�ckgabedaten und Statusinformationen f�r diesen
 Job ermittelt werden. Dazu wird die Methoode {@link #getJobResult()} ben�tigt, welche
 eine Instanz einer {@link org.kapott.hbci.GV_Result.HBCIJobResult}-Klasse zur�ckgibt.
 Die konkrete Klasse, um die es sich bei diesem Result-Objekt handelt, ist vom Typ des ausgef�hrten
 Jobs abh�ngig (z.B. gibt es eine Klasse, die Ergebnisdaten f�r Kontoausz�ge enth�lt, eine
 Klasse f�r Saldenabfragen usw.). Eine Beschreibung der einzelnen Klassen f�r Result-Objekte findet
 sich im Package <code>org.kapott.hbci.GV_Result</code>. Eine Beschreibung, welcher Job welche Klasse
 zur�ckgibt, befindet sich in der Package-Dokumentation zu diesem Package (<code>org.kapott.hbci.GV</code>).</p> */
public interface HBCIJob
{
    /** Gibt den internen Namen f�r diesen Job zur�ck.
     * @return Job-Name, wie er intern von <em>HBCI4Java</em> verwendet wird. */
    public String getName();
    
    /** Gibt die f�r diesen Job verwendete Segment-Versionsnummer zur�ck */
    public String getSegVersion();

    /** <p>Gibt zur�ck, wieviele Signaturen f�r diesen Job mindestens ben�tigt werden.
     *  Diese Information wird den BPD entnommen. In einigen F�llen gibt es
     *  in den UPD aktuellere Informationen zu einem bestimmten Gesch�ftsvorfall,
     *  zur Zeit werden die UPD von dieser Methode aber nicht ausgewertet.</p>
     *  <p>Wird f�r einen Job mehr als eine Signatur ben�tigt, so k�nnen mit der
     *  Methode {@link #addSignaturePassport(HBCIPassport, String)}
     *  Passports bestimmt werden, die f�r die Erzeugung der zus�tzlichen
     *  Signaturen verwendet werden sollen.</p>
     *  <p>Es wird au�erdem empfohlen, dass Auftr�ge, die mehrere Signaturen
     *  ben�tigen, jeweils in einer separaten HBCI-Nachricht versandt werden. Um das
     *  zu erzwingen, kann entweder ein HBCI-Dialog gef�hrt werden, der definitiv
     *  nur diesen einen Auftrag enth�lt (also nur ein 
     *  {@link #addToQueue()}
     *  f�r diesen Dialog), oder es wird beim Zusammenstellen der Jobs f�r einen
     *  Dialog sichergestellt, dass ein bestimmter Job in einer separaten Nachricht
     *  gesandt wird 
     *  ({@link org.kapott.hbci.manager.HBCIHandler#newMsg()}).</p>
     *  @return Mindest-Anzahl der ben�tigten Signaturen f�r diesen Job */
    public int getMinSigs();

    /** <p>Gibt zur�ck, welche Sicherheitsklasse f�r diesen Job mindestens ben�tigt wird.
     *  Diese Information wird den BPD entnommen. Sicherheitsklassen sind erst
     *  ab FinTS-3.0 definiert. Falls keine Sicherheitsklassen unterst�tzt werden
     *  (weil eine geringere HBCI-Version als FinTS-3.0 verwendet wird), wird 
     *  <code>1</code> zur�ckgegeben. Die Sicherheitsklasse ist nur die 
     *  Sicherheitsmechanismen DDV und RDH relevant - bei Verwendung von PIN/TAN
     *  hat die Sicherheitsklasse keine Bedeutung. </p>
     *  <p>Folgende Sicherheitsklassen sind definiert:</p>
     *  <ul>
     *  <li><code>0</code>: kein Sicherheitsdienst erforderlich</li>
     *  <li><code>1</code>: Authentication - es wird eine Signatur mit dem
     *  Signaturschl�ssel ben�tigt.</li>
     *  <li><code>2</code>: Authentication mit fortgeschrittener elektronischer
     *  Signatur unter Verwendung des Signaturschl�ssels.</li>
     *  <li><code>3</code>: Non-Repudiation mit fortgeschrittener elektronischer
     *  Signatur und optionaler Zertifikatspr�fung unter Verwendung des DigiSig-Schl�ssels</li>
     *  <li><code>4</code>: Non-Repudiation mit fortgeschrittener bzw. qualifizierter
     *  elektronischer Signatur und zwingender Zertifikats�berpr�fung mit dem
     *  DigiSig-Schl�ssel</li>
     *  </ul>
     *  @return Sicherheitsklasse, die f�r diese Job ben�tigt wird */
    public int getSecurityClass();

    /** Gibt alle m�glichen Job-Parameter f�r einen Lowlevel-Job zur�ck.
     * Die Anwendung dieser Methode ist nur sinnvoll, wenn es sich bei dem 
     * aktuellen Job um einen Lowlevel-Job handelt (erzeugt mit 
     * {@link org.kapott.hbci.manager.HBCIHandler#newLowlevelJob(String)}).
     * Die zur�ckgegebenen Parameternamen k�nnen als erstes Argument der
     * Methode {@link #setParam(String, String)} verwendet werden. 
     * @return Liste aller g�ltigen Parameternamen (nur f�r Lowlevel-Jobs) */
    public List getJobParameterNames();

    /** Gibt alle m�glichen Property-Namen f�r die Lowlevel-R�ckgabedaten dieses
     * Jobs zur�ck. Die Lowlevel-R�ckgabedaten k�nnen mit
     * {@link #getJobResult()} und {@link HBCIJobResult#getResultData()}
     * ermittelt werden. Diese Methode verwendet intern
     * {@link org.kapott.hbci.manager.HBCIHandler#getLowlevelJobResultNames(String)}.
     * @return Liste aller prinzipiell m�glichen Property-Keys f�r die 
     * Lowlevel-R�ckgabedaten dieses Jobs */
    public List getJobResultNames();

    /** <p>Gibt f�r einen Job alle bekannten Einschr�nkungen zur�ck, die bei
     der Ausf�hrung des jeweiligen Jobs zu beachten sind. Diese Daten werden aus den
     Bankparameterdaten des aktuellen Passports extrahiert. Sie k�nnen von einer HBCI-Anwendung
     benutzt werden, um gleich entsprechende Restriktionen bei der Eingabe von
     Gesch�ftsvorfalldaten zu erzwingen (z.B. die maximale Anzahl von Verwendungszweckzeilen,
     ob das �ndern von terminierten �berweisungen erlaubt ist usw.).</p>
     <p>Die einzelnen Eintr�ge des zur�ckgegebenen Properties-Objektes enthalten als Key die
     Bezeichnung einer Restriktion (z.B. "<code>maxusage</code>"), als Value wird der
     entsprechende Wert eingestellt. Die Bedeutung der einzelnen Restriktionen ist zur Zeit
     nur der HBCI-Spezifikation zu entnehmen. In sp�teren Programmversionen werden entsprechende
     Dokumentationen zur internen HBCI-Beschreibung hinzugef�gt, so dass daf�r eine Abfrageschnittstelle
     implementiert werden kann.</p>
     <p>Diese Methode verwendet intern 
     {@link org.kapott.hbci.manager.HBCIHandler#getLowlevelJobRestrictions(String)}</p>.
     @return Properties-Objekt mit den einzelnen Restriktionen */
    public Properties getJobRestrictions();

    /** Gibt alle f�r diesen Job gesetzten Parameter zur�ck. In dem
     zur�ckgegebenen <code>Properties</code>-Objekt sind werden die
     Parameter als <em>Lowlevel</em>-Parameter abgelegt. Au�erdem hat
     jeder Lowlevel-Parametername zus�tzlich ein Prefix, welches den
     Lowlevel-Job angibt, f�r den der Parameter gilt (also z.B.
     <code>Ueb3.BTG.value</code>
     @return aktuelle gesetzte Lowlevel-Parameter f�r diesen Job */
    public Properties getLowlevelParams();

    /** Setzen eines komplexen Job-Parameters (Kontodaten). Einige Jobs ben�tigten Kontodaten
     als Parameter. Diese m�ssten auf "normalem" Wege durch mehrere Aufrufe von 
     {@link #setParam(String,String)} erzeugt werden (L�nderkennung, Bankleitzahl, 
     Kontonummer, Unterkontomerkmal, W�hrung, IBAN, BIC).
     Durch Verwendung dieser Methode wird dieser Weg abgek�rzt. Es wird ein Kontoobjekt 
     �bergeben, f�r welches die entsprechenden <code>setParam(String,String)</code>-Aufrufe 
     automatisch erzeugt werden. 
     @param paramname die Basis der Parameter f�r die Kontodaten (f�r <code>my.country</code>,
     <code>my.blz</code>, <code>my.number</code>, <code>my.subnumber</code>, <code>my.bic</code>, 
     <code>my.iban</code>, <code>my.curr</code> w�re das also "<code>my</code>")
     @param acc ein Konto-Objekt, aus welchem die zu setzenden Parameterdaten entnommen werden */
    public void setParam(String paramname,Konto acc);

    /** Setzen eines komplexen Job-Parameters (Geldbetrag). Einige Jobs ben�tigten Geldbetr�ge
     als Parameter. Diese m�ssten auf "normalem" Wege durch zwei Aufrufe von 
     {@link #setParam(String,String)} erzeugt werden (je einer f�r
     den Wert und die W�hrung). Durch Verwendung dieser
     Methode wird dieser Weg abgek�rzt. Es wird ein Value-Objekt �bergeben, f�r welches
     die entsprechenden zwei <code>setParam(String,String)</code>-Aufrufe automatisch
     erzeugt werden.
     @param paramname die Basis der Parameter f�r die Geldbetragsdaten (f�r "<code>btg.value</code>" und
     "<code>btg.curr</code>" w�re das also "<code>btg</code>")
     @param v ein Value-Objekt, aus welchem die zu setzenden Parameterdaten entnommen werden */
    public void setParam(String paramname,Value v);

    /** Setzen eines Job-Parameters, bei dem ein Datums als Wert erwartet wird. Diese Methode
     dient als Wrapper f�r {@link #setParam(String,String)}, um das Datum in einen korrekt
     formatierten String umzuwandeln. Das "richtige" Datumsformat ist dabei abh�ngig vom
     aktuellen Locale.
     @param paramName Name des zu setzenden Job-Parameters
     @param date Datum, welches als Wert f�r den Job-Parameter benutzt werden soll */
    public void setParam(String paramName,Date date);

    /** Setzen eines Job-Parameters, bei dem ein Integer-Wert Da als Wert erwartet wird. Diese Methode
     dient nur als Wrapper f�r {@link #setParam(String,String)}.
     @param paramName Name des zu setzenden Job-Parameters
     @param i Integer-Wert, der als Wert gesetzt werden soll */
    public void setParam(String paramName,int i);

    /** <p>Setzen eines Job-Parameters. F�r alle Highlevel-Jobs ist in der Package-Beschreibung zum
     Package <code>org.kapott.hbci.GV</code> eine Auflistung aller Jobs und deren Parameter zu finden.
     F�r alle Lowlevel-Jobs kann eine Liste aller Parameter entweder mit dem Tool
     {@link org.kapott.hbci.tools.ShowLowlevelGVs} oder zur Laufzeit durch Aufruf
     der Methode {@link org.kapott.hbci.manager.HBCIHandler#getLowlevelJobParameterNames(String)} 
     ermittelt werden.</p>
     <p>Bei Verwendung dieser oder einer der anderen <code>setParam()</code>-Methoden werden zus�tzlich
     einige der Job-Restriktionen (siehe {@link #getJobRestrictions()}) analysiert. Beim Verletzen einer
     der �berpr�ften Einschr�nkungen wird eine Exception mit einer entsprechenden Meldung erzeugt.
     Diese �berpr�fung findet allerdings nur bei Highlevel-Jobs statt.</p>
     @param paramName der Name des zu setzenden Parameters.
     @param value Wert, auf den der Parameter gesetzt werden soll */
    public void setParam(String paramName,String value);
    
    /** <p>Hinzuf�gen dieses Jobs zu einem HBCI-Dialog. Diese Methode arbeitet analog zu 
        {@link #addToQueue(String)}, nur dass hier
        die <code>customerid</code> mit der Kunden-ID vorbelegt ist, wie sie
        im aktuellen Passport gespeichert ist.</p> */
    public void addToQueue();
    
    /** <p>Hinzuf�gen dieses Jobs zu einem HBCI-Dialog. Nachdem alle
        Jobparameter mit 
        {@link #setParam(String,String)}
        gesetzt wurden, kann der komplett spezifizierte Job mit dieser Methode
        zur Auftragsliste eines Dialoges hinzugef�gt werden.</p>
        <p>Die <code>customerId</code> gibt an, unter welcher Kunden-ID dieser Job
        ausgef�hrt werden soll. Existiert f�r eine HBCI-Nutzerkennung (ein Passport)
        nur genau eine Kunden-ID (wie das i.d.R. der Fall ist), so kann der
        <code>customerId</code>-Parameter weggelassen werden - <em>HBCI4Java</em>
        verwendet dann automatisch die richtige Kunden-ID (als Kunden-ID wird in diesem
        Fall der Wert von {@link HBCIPassport#getCustomerId()} verwendet). Gibt es aber mehrere
        g�ltige Kunden-IDs f�r einen HBCI-Zugang, so muss die Kunden-ID,
        die f�r diesen Job verwendet werden soll, mit angegeben werden.</p>
        <p>Jeder Auftrag (=Job) ist i.d.R. an ein bestimmtes Konto des Auftraggebers
        gebunden (�berweisung: das Belastungskonto; Saldenabfrage: das abzufragende
        Konto usw.). Als Kunden-ID f�r einen Auftrag muss <em>die</em> Kunden-ID
        angegeben werden, die f�r dieses Konto verf�gungsberechtigt ist.</p>
        <p>I.d.R. liefert eine Bank Informationen �ber alle Konten, auf die
        via HBCI zugegriffen werden kann. Ist das der Fall, so kann die Menge
        dieser Konten mit {@link HBCIPassport#getAccounts()} ermittelt werden.
        In jedem zur�ckgemeldeten {@link org.kapott.hbci.structures.Konto}-Objekt 
        ist im Feld <code>customerid</code> vermerkt, welche Kunden-ID f�r 
        dieses Konto verf�gungsberechtigt ist. Diese Kunden-ID m�sste dann also 
        beim Hinzuf�gen eines Auftrages angegeben werden, welcher das jeweilige 
        Konto betrifft.</p>
        <p>Liefert eine Bank diese Informationen nicht, so hat die Anwendung selbst
        eine Kontenverwaltung zu implementieren, bei der jedem Nutzerkonto eine
        zu verwendende Kunden-ID zugeordnet ist.</p>
        <p>Ein HBCI-Dialog kann aus beliebig vielen HBCI-Nachrichten bestehen. <em>HBCI4Java</em> versucht zun�chst,
        alle Jobs in einer einzigen Nachricht unterzubringen. Kann ein Job nicht mehr zur aktuellen
        Nachricht hinzugef�gt werden (weil sonst bestimmte vorgegebene Bedingungen nicht eingehalten
        werden), so legt <em>HBCI4Java</em> automatisch eine neue Nachricht an, zu der der Job schlie�lich
        hinzugef�gt wird. Beim Ausf�hren des HBCI-Dialoges (siehe {@link org.kapott.hbci.manager.HBCIHandler#execute()}) werden dann
        nat�rlich <em>alle</em> erzeugten Nachrichten zum HBCI-Server gesandt.</p> 
        <p>Der HBCI-Kernel bestimmt also automatisch, ob ein Auftrag noch mit in die aktuelle Nachricht
        aufgenommen werden kann, oder ob eine separate Nachricht erzeugt werden muss. Der manuelle
        Aufruf von {@link org.kapott.hbci.manager.HBCIHandler#newMsg() HBCIHandler.newMsg()} ist 
        deshalb im Prinzip niemals notwendig, es sei denn,
        es soll aus anderen Gr�nden eine neue Nachricht begonnen werden.</p>
        @param customerId die Kunden-ID, zu deren Dialog der Auftrag hinzugef�gt werden soll */
    public void addToQueue(String customerId);

    /** Gibt ein Objekt mit den R�ckgabedaten f�r diesen Job zur�ck. Das zur�ckgegebene Objekt enth�lt
     erst <em>nach</em> der Ausf�hrung des Jobs g�ltige Daten.
     @return ein Objekt mit den R�ckgabedaten und Statusinformationen zu diesem Job */
    public HBCIJobResult getJobResult();

    /** Hinzuf�gen eines Passports, welches f�r eine zus�tzliche Signatur f�r
     *  diesen Auftrag benutzt wird. <code>role</code> gibt dabei die Rolle an,
     *  die der Eigent�mer des zus�tzlichen Passports in Bezug auf diesen
     *  Job (bzw. die aktuelle Nachricht) einnimmt. G�ltige Werte sind in
     *  {@link org.kapott.hbci.passport.HBCIPassport} beschrieben.
     *  Mit der Methode {@link #getMinSigs()} kann ermittelt werden, wieviele
     *  Signaturen f�r einen Job mindestens ben�tigt werden. 
     *  @param passport das hinzuzuf�gende Passport-Objekt, welches f�r eine 
     *         zus�tzliche Signatur benutzt werden soll
     *  @param role die Rolle, in der sich der Eigent�mer des zus�tzlichen 
     *         Passport-Objektes bez�glich dieses Jobs befindet */
    public void addSignaturePassport(HBCIPassport passport,String role);
}
