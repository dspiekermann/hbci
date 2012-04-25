package de.rlm.hbci;

public class ExecutionContext {
	
	ExecutionContext(){
	}
	
	public void execute(Session session, GV gv)  throws HbciException{
		try {
			gv.execute(session);
		} catch (HbciException e) {
			if (session!=null){
				session.destroy();
			}
			throw e;
		}
	}

}
