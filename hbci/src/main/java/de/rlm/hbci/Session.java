package de.rlm.hbci;

import org.kapott.hbci.manager.HBCIHandler;
import org.kapott.hbci.passport.AbstractHBCIPassport;
import org.kapott.hbci.passport.HBCIPassport;

public class Session {
	
	private UserRequest userRequest = null;
	private Environment environment = null;
	
	private static HBCIPassport passport;
	private static HBCIHandler hbciHandle;

	
	Session(Environment environment, UserRequest userRequest) {
		super();
		this.userRequest = userRequest;
		this.environment = environment;
	}
	
	public UserRequest getUserRequest() {
		return userRequest;
	}
	public Environment getEnvironment() {
		return environment;
	}
	
	void init(){
		// Nutzer-Passport initialisieren
		Object passportDescription = "Passport für User: " + userRequest.getUserId();
		passport = AbstractHBCIPassport.getInstance(passportDescription);
		
		// ein HBCI-Handle für einen Nutzer erzeugen
		String version = passport.getHBCIVersion();
		hbciHandle = new HBCIHandler((version.length() != 0) ? version : "plus", passport);
	}

	public HBCIPassport getPassport() {
		return passport;
	}

	public HBCIHandler getHbciHandle() {
		return hbciHandle;
	}
	
	public void destroy(){
		if (hbciHandle != null) {
			hbciHandle.close();
		} else if (passport != null) {
			passport.close();
		}
	}
	
	
}
