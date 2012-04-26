package de.rlm.hbci;

import java.util.List;

import org.kapott.hbci.status.HBCIExecStatus;

public abstract class GVResultImpl<T> implements GVResult<T> {
	
	private List<T> result = null;
	private boolean isOk = true;
	private String errorMessage = null;

	public GVResultImpl(List<T> result){
		this.result = result;
	}
	public GVResultImpl(HBCIExecStatus status, List<T> result){
		this.result = result;
		this.isOk = status.isOK();
		this.errorMessage = status.getErrorString();
	}
	
	public List<T> getResult() {
		return result;
	}

	public boolean isOk() {
		return isOk;
	}

	public String getErrorMessage() {
		return errorMessage;
	}
	
}
