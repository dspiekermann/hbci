package de.rlm.hbci;

import java.util.List;

public abstract class GVResultImpl<T> implements GVResult<T> {
	
	private List<T> result = null;

	public GVResultImpl(List<T> result){
		this.result = result;
	}
	
	public List<T> getResult() {
		return result;
	}
}
