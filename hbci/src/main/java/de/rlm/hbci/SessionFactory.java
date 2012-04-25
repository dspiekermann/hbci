package de.rlm.hbci;

import java.util.Hashtable;

import de.rlm.hbci.common.Tools;

public class SessionFactory {
	
	//TODO we need a unique key of blz and userid or smthg else
	private static final Hashtable<ThreadGroup, Session> SESSIONS = new Hashtable<ThreadGroup, Session>();
	
	public static Session getOrCreateSession(UserRequest userRequest) throws HbciException{
		synchronized (userRequest) {
			Session session = null;
			ThreadGroup tg = Tools.getOrCreateUserThreadGroup(userRequest);
			if (tg!=null){
				session = SESSIONS.get(tg);
			}
			if (session!=null){
				return session;
			}
			if (session==null){
				session = createSession(userRequest, tg);
			}
			return session;
		}
	}
	
	//TODO prevent synchronized access - we need a real session object to synchronize! and put the hbci session in websession, so that there is no need to organize it ourselves
	private static Session createSession(UserRequest userRequest, ThreadGroup tg) throws HbciException{
		synchronized (userRequest) {
			Session session = null;
			if (tg!=null){
				session = SESSIONS.remove(tg);
				if (session!=null){
					session.destroy();
					//we have a problem, should never happen
					throw new HbciException("session for thread-group " + tg.getName() + " already exists!");
				}
			}
			Environment environment = Environment.create(userRequest);
			session = new Session(environment, userRequest);
			//TODO is there a session timeout? does hbci quits all session automatically? if not, we have to clean up!
			//we have to put it in context before calling init, so that we can retrieve the password
			if (tg!=null){
				SESSIONS.put(tg, session);
			}
			session.init();
			
			return session;
		}
	}
	
	public static Session getSession(UserRequest userRequest){
		ThreadGroup tg = Tools.getUserThreadGroup(userRequest);
		if (tg!=null){
			return SESSIONS.get(tg);
		}
		return null;
	}

	public static void destroySession(UserRequest userRequest) throws HbciException{
		synchronized (userRequest) {
			ThreadGroup tg = Tools.getUserThreadGroup(userRequest);
			if (tg!=null){
				Session session = SESSIONS.remove(tg);
				if (session!=null){
					session.destroy();
					//only if we have a valid session that we remove, we want to destory the threadgroup -> no more need for it!
					tg.destroy();
				}
			}
		}
		
	}
}
