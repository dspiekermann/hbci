package de.rlm.hbci.gv;

import org.kapott.hbci.structures.Konto;

import de.rlm.hbci.GV;
import de.rlm.hbci.Session;
import de.rlm.hbci.gv.data.HbciData;

public abstract class GVAbstract<T extends HbciData> implements GV<T> {

	protected org.kapott.hbci.structures.Konto findKontoHbci(Session session, String kontoNr){
		if (kontoNr!=null){
			Konto[] kontoAll = session.getPassport().getAccounts();
			if (kontoAll!=null){
				for (Konto konto : kontoAll) {
					if (kontoNr.equals(konto.number)){
						return konto;
					}
				}
			}
		}
		return null;
	}
	
}
