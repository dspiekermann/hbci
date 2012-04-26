package de.rlm.hbci;

import java.util.List;

public interface GVResult<T> {
	
	public List<T> getResult();
	public boolean isOk();
	public String getErrorMessage();
	
}
