package de.rlm.hbci.gv.data;

import java.util.ArrayList;
import java.util.List;

public class User extends HbciData {

	private String customerId = null;
	private String userid = null;
	private List<Konto> konto = new ArrayList<Konto>();
	
	public User(String customerId, String userid, List<Konto> konto) {
		super();
		this.customerId = customerId;
		this.userid = userid;
		this.konto = konto;
	}

	public String getCustomerId() {
		return customerId;
	}

	public String getUserid() {
		return userid;
	}

	public List<Konto> getKonto() {
		return konto;
	}
	
	
	
}
