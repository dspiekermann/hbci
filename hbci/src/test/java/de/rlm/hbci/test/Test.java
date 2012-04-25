package de.rlm.hbci.test;

import java.util.List;

import junit.framework.TestCase;

import de.rlm.hbci.GVResult;
import de.rlm.hbci.GVThread;
import de.rlm.hbci.HbciApi;
import de.rlm.hbci.HbciException;
import de.rlm.hbci.SessionFactory;
import de.rlm.hbci.UserRequest;
import de.rlm.hbci.gv.GVKontoAll;
import de.rlm.hbci.gv.GVUmsatz;
import de.rlm.hbci.gv.data.Konto;
import de.rlm.hbci.gv.data.Umsatz;

public class Test extends TestCase {
	
	private int activeThreadsStart = 0;
	private int activeGroupThreadsStart = 0;

	private UserRequest userRequestComso = new UserRequest() {
		public String getUserId() {
			return "CosmoSMS";
		}

		public String getPassword() {
			return "13795";
		}

		public String getBlz() {
			return "37050198";
		}
	};
	private UserRequest userRequest6123 = new UserRequest() {
		public String getUserId() {
			return "6123lbchip";
		}

		public String getPassword() {
			return "51067";
		}

		public String getBlz() {
			return "37050198";
		}
	};
	private UserRequest userRequestMe = new UserRequest() {
		public String getUserId() {
			return "1900104959";
		}

		public String getPassword() {
			return "07029";
		}

		public String getBlz() {
			return "37050198";
		}
	};

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		ThreadGroup threadGroupStart = Thread.currentThread().getThreadGroup();
		activeThreadsStart = threadGroupStart.activeCount();
		activeGroupThreadsStart = threadGroupStart.activeGroupCount();
		
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		SessionFactory.destroySession(userRequestComso);
		SessionFactory.destroySession(userRequest6123);
		SessionFactory.destroySession(userRequestMe);
		
		//same nr of active threads
		ThreadGroup threadGroupEnd = Thread.currentThread().getThreadGroup();
		int activeThreadsEnd = threadGroupEnd.activeCount();
		int activeGroupThreadsEnd = threadGroupEnd.activeGroupCount();
		//we dont want any bodies
		assertTrue(activeThreadsStart == activeThreadsEnd);
		assertTrue(activeGroupThreadsStart == activeGroupThreadsEnd);
	}

	public void test3() throws HbciException {
		HbciApi api = HbciApi.getInstance(userRequest6123);
		
		GVResult<Konto> result =  api.getKontoAll();
		assertNotNull(result);
		List<Konto> data = result.getResult();
		assertNotNull(data);
		assertTrue(data.size() > 0);

		GVResult<Konto> result2 = api.getKontoAll();
		assertNotNull(result2);
		List<Konto> data2 = result2.getResult();
		assertNotNull(data2);

		//same nr of account from both results
		assertTrue(data.size() == data2.size());
		
		GVResult<Umsatz> resultUmsatz = api.getUmsatzByKonto(data.get(3).getKontoNr());
		assertNotNull(resultUmsatz);
		List<Umsatz> dataUmsatz = resultUmsatz.getResult();
		assertNotNull(dataUmsatz);
		assertTrue(dataUmsatz.size() > 0);

	}

	public void test4() throws HbciException {
		GVThread<Konto> t = new GVThread<Konto>();
		GVResult<Konto> result = t.execute(userRequestComso, new GVKontoAll());
		assertNotNull(result);
		List<Konto> data = result.getResult();
		assertNotNull(data);
	}
}
