package de.rlm.hbci;

public interface GV<T> {
	
	public GVResult<T> execute(Session session) throws HbciException;

}
