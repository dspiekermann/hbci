package de.rlm.hbci.gv.data;

public class Konto extends HbciData {

	private String blz = null;
	private String kontoNr = null;
	private String iban = null;
	private String type = null;
	private String merkmal = null;
	private String land = null;
	private String waehrung = null;
	private String kontoinhaber = null;
	
	
	public Konto(String blz, String kontoNr, String iban, String type, String merkmal, String kontoinhaber, String land, String waehrung) {
		super();
		this.blz = blz;
		this.kontoNr = kontoNr;
		this.iban = iban;
		this.type = type;
		this.merkmal = merkmal;
		this.land = land;
		this.waehrung = waehrung;
		this.kontoinhaber = kontoinhaber;
	}
	
	public String getBlz() {
		return blz;
	}
	public String getKontoNr() {
		return kontoNr;
	}
	public String getIban() {
		return iban;
	}
	public String getType() {
		return type;
	}
	public String getMerkmal() {
		return merkmal;
	}
	public String getLand() {
		return land;
	}
	public String getWaehrung() {
		return waehrung;
	}
	public String getKontoinhaber() {
		return kontoinhaber;
	}

	@Override
	public String toString() {
		return "Konto [blz=" + blz + ", kontoNr=" + kontoNr + ", iban=" + iban + ", type=" + type + ", merkmal=" + merkmal + ", land=" + land + ", waehrung="
				+ waehrung + ", kontoinhaber=" + kontoinhaber + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((blz == null) ? 0 : blz.hashCode());
		result = prime * result + ((iban == null) ? 0 : iban.hashCode());
		result = prime * result + ((kontoNr == null) ? 0 : kontoNr.hashCode());
		result = prime * result + ((kontoinhaber == null) ? 0 : kontoinhaber.hashCode());
		result = prime * result + ((land == null) ? 0 : land.hashCode());
		result = prime * result + ((merkmal == null) ? 0 : merkmal.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + ((waehrung == null) ? 0 : waehrung.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Konto other = (Konto) obj;
		if (blz == null) {
			if (other.blz != null)
				return false;
		} else if (!blz.equals(other.blz))
			return false;
		if (iban == null) {
			if (other.iban != null)
				return false;
		} else if (!iban.equals(other.iban))
			return false;
		if (kontoNr == null) {
			if (other.kontoNr != null)
				return false;
		} else if (!kontoNr.equals(other.kontoNr))
			return false;
		if (kontoinhaber == null) {
			if (other.kontoinhaber != null)
				return false;
		} else if (!kontoinhaber.equals(other.kontoinhaber))
			return false;
		if (land == null) {
			if (other.land != null)
				return false;
		} else if (!land.equals(other.land))
			return false;
		if (merkmal == null) {
			if (other.merkmal != null)
				return false;
		} else if (!merkmal.equals(other.merkmal))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		if (waehrung == null) {
			if (other.waehrung != null)
				return false;
		} else if (!waehrung.equals(other.waehrung))
			return false;
		return true;
	}


}
