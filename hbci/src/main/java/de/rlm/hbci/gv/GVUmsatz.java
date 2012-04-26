package de.rlm.hbci.gv;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.kapott.hbci.GV.HBCIJob;
import org.kapott.hbci.GV_Result.GVRKUms;
import org.kapott.hbci.manager.HBCIHandler;
import org.kapott.hbci.status.HBCIExecStatus;
import org.kapott.hbci.status.HBCIStatus;
import org.kapott.hbci.structures.Konto;
import org.kapott.hbci.structures.Value;

import de.rlm.hbci.GVResult;
import de.rlm.hbci.GVResultError;
import de.rlm.hbci.GVResultImpl;
import de.rlm.hbci.HbciException;
import de.rlm.hbci.Session;
import de.rlm.hbci.common.Constants;
import de.rlm.hbci.gv.data.Umsatz;

public class GVUmsatz extends GVAbstract<Umsatz> {

	private String kontoNr = null;

	public GVUmsatz(String kontoNr) {
		this.kontoNr = kontoNr;
	}

	public GVResult<Umsatz> execute(Session session) throws HbciException {

		List<Umsatz> umsatzList = new ArrayList<Umsatz>();

		Konto konto = findKontoHbci(session, kontoNr);
		if (konto != null) {
			HBCIHandler hbciHandle = session.getHbciHandle();

			// Job zur Abholung der Kontoauszüge erzeugen
			HBCIJob auszug = hbciHandle.newJob(Constants.GV_KUMSALL);

			// TODO set date
			auszug.setParam("my", konto);
			// evtl. Datum setzen, ab welchem die Auszüge geholt werden sollen
			// job.setParam("startdate","21.5.2003");
			auszug.addToQueue();

			// alle Jobs in der Job-Warteschlange ausführen
			final HBCIExecStatus ret = hbciHandle.execute();

			GVRKUms result = (GVRKUms) auszug.getJobResult();

			// wenn der Job "Kontoauszüge abholen" erfolgreich ausgeführt wurde
			if (ret.isOK()) {
				if (result.isOK()) {

					List lines = result.getFlatData();
					int numof_lines = lines.size();

					for (Iterator j = lines.iterator(); j.hasNext();) { // alle
																		// Umsatzeinträge
																		// durchlaufen
						GVRKUms.UmsLine entry = (GVRKUms.UmsLine) j.next();

						// für jeden Eintrag ein Feld mit allen
						// Verwendungszweckzeilen extrahieren
						List usages = entry.usage;
						int numof_usagelines = usages.size();

						List<String> verwendungszweck = new ArrayList<String>();
						for (Iterator k = usages.iterator(); k.hasNext();) { // alle
																				// Verwendungszweckzeilen
																				// durchlaufen
							String usageline = (String) k.next();
							verwendungszweck.add(usageline);
						}

						double betrag = 0;
						String waehrung = null;
						Value value = entry.value;
						if (value != null) {
							betrag = value.getDoubleValue();
							waehrung = value.getCurr();
						}
						Umsatz umsatz = new Umsatz(entry.bdate, entry.valuta, verwendungszweck, betrag, waehrung);
						umsatzList.add(umsatz);
					}

				}
			}

			return new GVResultGVKUms(ret, umsatzList);
		}
		
		return new GVResultError<Umsatz>("konto for kontoNr " + kontoNr + " does not exists") {};
	}

	private class GVResultGVKUms extends GVResultImpl<Umsatz> {
		public GVResultGVKUms(HBCIExecStatus status, List<Umsatz> result) {
			super(status, result);
		}
	}

}
