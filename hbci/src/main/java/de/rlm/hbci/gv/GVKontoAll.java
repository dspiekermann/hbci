package de.rlm.hbci.gv;

import java.util.ArrayList;
import java.util.List;

import org.kapott.hbci.passport.HBCIPassport;
import org.kapott.hbci.structures.Konto;

import de.rlm.hbci.GVResult;
import de.rlm.hbci.GVResultImpl;
import de.rlm.hbci.HbciException;
import de.rlm.hbci.Session;

public class GVKontoAll extends GVAbstract<de.rlm.hbci.gv.data.Konto> {

	public GVResult<de.rlm.hbci.gv.data.Konto> execute(Session session) throws HbciException {
		HBCIPassport passport = session.getPassport();
		Konto[] konto = passport.getAccounts();
		
		List<de.rlm.hbci.gv.data.Konto> kontoList = new ArrayList<de.rlm.hbci.gv.data.Konto>();
		if (konto!=null){
			for (Konto k : konto) {
				kontoList.add(new de.rlm.hbci.gv.data.Konto(
						k.blz,
						k.number,
						k.iban,
						k.type,
						k.name,
						k.subnumber,
						k.country,
						k.curr));
			}
		}
		
		return new GVResultKonto(kontoList);	
	}
	
	private class GVResultKonto extends GVResultImpl<de.rlm.hbci.gv.data.Konto>{
		public GVResultKonto(List<de.rlm.hbci.gv.data.Konto> result) {
			super(result);
		}
	}

}
