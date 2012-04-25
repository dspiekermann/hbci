package de.rlm.bp.bean;

import java.io.IOException;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletException;

import org.springframework.context.annotation.Scope;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;

@Controller
@Scope("request")
public class LoginBean {
	
	private int errorCode = 0;

    // This is the action method called when the user clicks the "login" button
    public String doLogin() throws IOException, ServletException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        externalContext.dispatch("/j_spring_security_check");
        facesContext.responseComplete();
        return null;
    }	

    public String doLogout() throws IOException, ServletException {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        externalContext.dispatch("/j_spring_security_logout");
        facesContext.responseComplete();
        return null;
    }

    //hierüber werden in der PreRender-Phase (nach ggf. erfolglosem Loginversuch, die Fehlermeldungen aus Spring nach jsf geschaufelt)
    public void checkErrors() {
        if (errorCode == 1) {
	    	Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
	    	Object loginExceptionObject = sessionMap.get("SPRING_SECURITY_LAST_EXCEPTION");
	    	if (loginExceptionObject!=null){
	    		if (loginExceptionObject instanceof AuthenticationException){
	    			AuthenticationException ae = (AuthenticationException) loginExceptionObject;
	                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, ae.getMessage(), ae.getMessage()));
	    		}
	    	} else {
	            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Invalid credentials", "Invalid credentials"));
	    	}
        }
    }

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
    
    
    
}

