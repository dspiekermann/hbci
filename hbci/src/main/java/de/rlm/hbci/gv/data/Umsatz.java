package de.rlm.hbci.gv.data;

import java.util.Date;
import java.util.List;

public class Umsatz extends HbciData {
	
	private Date buchungsDatum = null;
	private Date valuta = null;
	private List<String> verwendungszweck = null;
	private double betrag = 0;
	private String waehrung = null;
	
	public Umsatz(Date buchungsDatum, Date valuta, List<String> verwendungszweck, double betrag, String waehrung) {
		super();
		this.buchungsDatum = buchungsDatum;
		this.valuta = valuta;
		this.verwendungszweck = verwendungszweck;
		this.betrag = betrag;
		this.waehrung = waehrung;
	}
	public Date getBuchungsDatum() {
		return buchungsDatum;
	}
	public Date getValuta() {
		return valuta;
	}
	public List<String> getVerwendungszweck() {
		return verwendungszweck;
	}
	public double getBetrag() {
		return betrag;
	}
	public String getWaehrung() {
		return waehrung;
	}

}
