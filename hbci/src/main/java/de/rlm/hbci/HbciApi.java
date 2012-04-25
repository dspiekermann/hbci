package de.rlm.hbci;

import de.rlm.hbci.gv.GVKontoAll;
import de.rlm.hbci.gv.GVUmsatz;
import de.rlm.hbci.gv.data.Konto;
import de.rlm.hbci.gv.data.Umsatz;

public class HbciApi {
	
	private UserRequest userRequest = null;
	
	private HbciApi(UserRequest userRequest){
		this.userRequest = userRequest;
	}
	
	public static HbciApi getInstance(UserRequest userRequest){
		return new HbciApi(userRequest);
	}
	
	public GVResult<Konto> getKontoAll() throws HbciException{
		GVThread<Konto> thread = new GVThread<Konto>();
		GVResult<Konto> result = thread.execute(userRequest, new GVKontoAll());
		return result;
	}

	public GVResult<Umsatz> getUmsatzByKonto(String kontoNr) throws HbciException{
		GVThread<Umsatz> thread = new GVThread<Umsatz>();
		GVResult<Umsatz> result = thread.execute(userRequest, new GVUmsatz(kontoNr));
		return result;
	}

}
