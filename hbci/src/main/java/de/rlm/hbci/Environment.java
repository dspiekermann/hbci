package de.rlm.hbci;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import org.kapott.hbci.callback.HBCICallback;
import org.kapott.hbci.manager.FileSystemClassLoader;
import org.kapott.hbci.manager.HBCIUtils;

import de.rlm.hbci.common.Constants;

public class Environment {
	
	private UserRequest userRequest = null;
	
	private Environment(UserRequest userRequest){
		this.userRequest = userRequest;
	}
	
	private void init() throws HbciException {
		if (userRequest==null){
			throw new HbciException("userRequest not provided, cannot setup environment");
		}
		String userid = userRequest.getUserId();
		String blz = userRequest.getBlz();
		if (userid==null || "".equals(userid)){
			throw new HbciException("userid not provided, cannot setup environment");
		}
		if (blz==null || "".equals(blz)){
			throw new HbciException("blz not provided, cannot setup environment");
		}
		// HBCI4Java initialisieren
		HBCICallback callback = new RlmHbciCallback(userRequest);
//		HBCICallback callback = new HBCICallbackConsole();
		Properties properties = HBCIUtils.loadPropertiesFile(this.getClass().getClassLoader(), Constants.HBCI_PROPERTYFILE);
		
		//TODO this is crap -> beautify
		String file = Constants.PATH_USER_PASSPORTS + userid + Constants.SUFFIX_PIN_TAN_USER_PASSPORTS;
		properties.put(Constants.KEY_PATH_PASSPORTS, file);
		
//		HBCIUtils.init(properties, callback);
		HBCIUtils.initThread(properties, callback);
	}
	
	public UserRequest getUserRequest(){
		return userRequest;
	}

	//TODO consider to create one instance per user (threadlocal or per session
	public static Environment create(UserRequest userRequest) throws HbciException{
		Environment instance = new Environment(userRequest);
		instance.init();
		return instance;
	}
}
