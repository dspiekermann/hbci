package de.rlm.hbci.gv;

import java.util.ArrayList;
import java.util.List;

import org.kapott.hbci.manager.HBCIDialog;
import org.kapott.hbci.passport.HBCIPassport;
import org.kapott.hbci.status.HBCIDialogStatus;
import org.kapott.hbci.status.HBCIExecStatus;
import org.kapott.hbci.structures.Konto;

import de.rlm.hbci.GVResult;
import de.rlm.hbci.GVResultImpl;
import de.rlm.hbci.HbciException;
import de.rlm.hbci.Session;
import de.rlm.hbci.gv.data.User;

public class GVUser extends GVAbstract<de.rlm.hbci.gv.data.User> {

	public GVResult<de.rlm.hbci.gv.data.User> execute(Session session) throws HbciException {
		
		final HBCIExecStatus status = session.getHbciHandle().verifyTAN(session.getUserRequest().getUserId());
		
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
		
		List<User> userList = new ArrayList<User>();
		userList.add(new User(passport.getCustomerId(), passport.getUserId(), kontoList));
		
		return new GVResultUser(status, userList);	
	}
	
	private class GVResultUser extends GVResultImpl<de.rlm.hbci.gv.data.User>{
		public GVResultUser(HBCIExecStatus status, List<de.rlm.hbci.gv.data.User> result) {
			super(status, result);
		}
	}

}
