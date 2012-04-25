package de.rlm.bp.bean;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

public abstract class JsfBeanAbstract {

	public void addMessageError(String summary){
		addMessageError(summary, null);
	}
	
	public void addMessageError(String summary, String detail){
		addMessageError(null, summary, detail);
	}

	public void addMessageError(String clientId, String summary, String detail){
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, detail);
		addMessage(clientId, message);
	}

	public void addMessageInfo(String summary){
		addMessageInfo(summary, null);
	}
	
	public void addMessageInfo(String summary, String detail){
		addMessageError(null, summary, detail);
	}

	public void addMessageInfo(String clientId, String summary, String detail){
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail);
		addMessage(clientId, message);
	}

	public void addMessageWarn(String summary){
		addMessageWarn(summary, null);
	}
	
	public void addMessageWarn(String summary, String detail){
		addMessageWarn(null, summary, detail);
	}

	public void addMessageWarn(String clientId, String summary, String detail){
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, summary, detail);
		addMessage(clientId, message);
	}
	
	public void addMessage(FacesMessage message){
		addMessage(null, message);
	}

	public void addMessage(String clientId, FacesMessage message){
		FacesContext context = FacesContext.getCurrentInstance();
		if (context!=null){
			context.addMessage(clientId, message);
		}
	}

}
