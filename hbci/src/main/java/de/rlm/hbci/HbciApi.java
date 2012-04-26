package de.rlm.hbci;

import de.rlm.hbci.gv.GVKontoAll;
import de.rlm.hbci.gv.GVUmsatz;
import de.rlm.hbci.gv.GVUser;
import de.rlm.hbci.gv.data.Konto;
import de.rlm.hbci.gv.data.Umsatz;
import de.rlm.hbci.gv.data.User;

public class HbciApi {
	
	private UserRequest userRequest = null;
	
	private HbciApi(UserRequest userRequest){
		this.userRequest = userRequest;
	}
	
	public static HbciApi getInstance(UserRequest userRequest){
		return new HbciApi(userRequest);
	}
	
	public GVResult<User> login() throws HbciException{
		GVThread<User> thread = new GVThread<User>();
		GVResult<User> result = thread.execute(userRequest, new GVUser());
		return result;
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
