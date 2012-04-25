package org.kapott.hbci.tests.apps;

import java.util.Properties;

import org.kapott.hbci.GV.HBCIJob;
import org.kapott.hbci.callback.HBCICallbackConsole;
import org.kapott.hbci.manager.HBCIHandler;
import org.kapott.hbci.manager.HBCIUtils;
import org.kapott.hbci.passport.AbstractHBCIPassport;
import org.kapott.hbci.passport.HBCIPassport;
import org.kapott.hbci.status.HBCIExecStatus;
import org.kapott.hbci.structures.Konto;
import org.kapott.hbci.structures.Value;

public class TestSEPAUeb
{
    public static void main(String[] args)
    {
        Properties props=new Properties();
        
        // zu verwendendes passport konfigurieren
        props.setProperty("client.passport.default", "RDHNew");
        props.setProperty("client.passport.RDHNew.filename", "/home/stefan.palme/projects/hbci4java-stable/private/passports/passport_pieper_rdh10");
        props.setProperty("client.passport.RDHNew.init", "1");

        // loglevel und -filter auf maximales logging setzen
        props.setProperty("log.loglevel.default", "4");
        props.setProperty("log.filter", "0");
        
        HBCIUtils.init(props, new HBCICallbackConsole());
        
        // hbcihandler instanziieren
        HBCIPassport passport=AbstractHBCIPassport.getInstance();
        HBCIHandler  handler=new HBCIHandler("300", passport);
        
        // eigenes konto
        Konto myAccount=passport.getAccounts()[0];
        myAccount.bic="GENODEM1MSC";
        
        // gegenkonto
        Konto targetAccount=new Konto("DE", "86055592", "1800214215");
        targetAccount.name="Stefan Palme";
        targetAccount.bic="WELADE8L";
        targetAccount.iban="DE47860555921800214215";

        
        // sepa-ueberweisung erzeugen
        HBCIJob job=handler.newJob("UebSEPA");
        // daten fuer eigenes konto setzen
        job.setParam("src", myAccount);
        // daten fuer gegenkonto setzen
        job.setParam("dst", targetAccount);
        // betrag + waehrung
        job.setParam("btg", new Value(2,"EUR"));
        // verwendungszweck (nur EINE Zeile, dafuer aber mehr als 27 zeichen erlaubt)
        job.setParam("usage", "Test Verwendungszweck");
        
        /*
        HBCIJob job=handler.newJob("SaldoReqSEPA");
        job.setParam("my.bic", myAccount.bic);
        job.setParam("my.iban", myAccount.iban);
        */
        
        // job hinzufuegen
        job.addToQueue();
        
        // ausfuehren
        HBCIExecStatus status=handler.execute();
        System.out.println("status:");
        System.out.println(status);
        
        System.out.println("result:");
        System.out.println(job.getJobResult());
        
        // aufraeumen
        handler.close();
    }
}
