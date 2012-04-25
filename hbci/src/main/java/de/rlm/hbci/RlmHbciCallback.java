package de.rlm.hbci;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.kapott.hbci.callback.AbstractHBCICallback;
import org.kapott.hbci.exceptions.HBCI_Exception;
import org.kapott.hbci.manager.HBCIUtilsInternal;
import org.kapott.hbci.passport.HBCIPassport;

import de.rlm.hbci.common.Constants;

public class RlmHbciCallback extends AbstractHBCICallback {
	
	private UserRequest userRequest = null;
	
	private static final Log LOG = LogFactory.getLog(RlmHbciCallback.class);
	
	//the initialization is before creating the user session so we have to put it in here
	RlmHbciCallback(UserRequest userRequest){
		this.userRequest = userRequest;
	}
	
	public void callback(HBCIPassport passport, int reason, String msg, int dataType, StringBuffer retData) {

		String userid = null;
		String blz = null;
		String password = null;
		if (userRequest!=null){
			userid = userRequest.getUserId();
			blz = userRequest.getBlz();
			password = userRequest.getPassword();
		}
		
        try {
            switch (reason) {
                case NEED_PASSPHRASE_LOAD:
                case NEED_PASSPHRASE_SAVE:
                case NEED_SOFTPIN:
                	retData.replace(0,retData.length(), Constants.PASSPORT_TAN);
            		break;
                case NEED_PT_PIN:
            		if (password==null){
            			throw new HBCI_Exception("no password for user " + userid + " provided");
            		}
            		retData.replace(0,retData.length(), password);
                    break;
                case NEED_PT_TAN:
                    break;
                case NEED_COUNTRY:
            		retData.replace(0,retData.length(), Constants.COUNTRY);
                    break;
                case NEED_BLZ:
            		if (blz==null){
            			throw new HBCI_Exception("no blz provided user " + userid + " provided");
            		}
            		retData.replace(0,retData.length(), blz);
                    break;
                case NEED_HOST:
            		retData.replace(0,retData.length(), Constants.PINTAN_HOST);
                    break;
                case NEED_PORT:
            		retData.replace(0,retData.length(), Constants.PINTAN_PORT);
                    break;
                case NEED_FILTER:
            		retData.replace(0,retData.length(), Constants.COMMUNICATION_FILTER);
                    break;
                case NEED_USERID:
            		retData.replace(0,retData.length(), userid);
                    break;
                case NEED_CUSTOMERID:
            		retData.replace(0,retData.length(), userid);
                    break;
                case NEED_CHIPCARD:
                    break;
                case NEED_HARDPIN:
                    break;
                case NEED_REMOVE_CHIPCARD:
                    break;
                case HAVE_CHIPCARD:
                    break;
                case HAVE_HARDPIN:
                    break;
                case NEED_NEW_INST_KEYS_ACK:
                    break;
                case HAVE_NEW_MY_KEYS:
                    break;
                case HAVE_INST_MSG:
                    break;
                case NEED_CONNECTION:
                	break;
                case CLOSE_CONNECTION:
                    break;
                case HAVE_CRC_ERROR:
                    break;
                case HAVE_IBAN_ERROR:
                    break;
                case HAVE_ERROR:
                    break;
                case NEED_SIZENTRY_SELECT:
                    break;
                case NEED_PT_SECMECH:
                	//TODO we need to figure out which method the user wants
            		retData.replace(0,retData.length(), Constants.CHIP_TAN_METHOD_MANUELL);
                    break;
                case NEED_PROXY_USER:
                    break;
                case NEED_PROXY_PASS:
                    break;
                case NEED_INFOPOINT_ACK:
                    break;

                default:
                    throw new HBCI_Exception(HBCIUtilsInternal.getLocMsg("EXCMSG_CALLB_UNKNOWN",Integer.toString(reason)));
            }
        } catch (Exception e) {
            throw new HBCI_Exception(HBCIUtilsInternal.getLocMsg("EXCMSG_CALLB_ERR"),e);
        } finally {
    		
    		LOG.info("callback - reason: " + reason + " - msg: " + msg + " - dataType: " + dataType + " retData: " + retData);
        }
		
	}

	public void log(String msg, int level, Date date, StackTraceElement trace) {
		LOG.info("log - level:" + level + " - " + msg);
	}

	public void status(HBCIPassport passport, int statusTag, Object[] o) {
		LOG.info("status - status:" + statusTag);
	}
	
}