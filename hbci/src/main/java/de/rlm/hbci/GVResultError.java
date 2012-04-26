package de.rlm.hbci;

import java.util.List;

public abstract class GVResultError<T> implements GVResult<T> {
	
	private String errorMessage = null;

	public GVResultError(String errorMessage){
		this.errorMessage = errorMessage;
	}
	
	public List<T> getResult() {
		return null;
	}

	public boolean isOk() {
		return false;
	}

	public String getErrorMessage() {
		return errorMessage;
	}
	
}
