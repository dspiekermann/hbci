package de.rlm.bp.bean;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import javax.faces.context.FacesContext;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

@Controller
@Scope("session")
public class LanguageBean {

	private Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();

	private static Map<String, Object> countries;
	static {
		countries = new LinkedHashMap<String, Object>();
		countries.put("English", Locale.ENGLISH); // label, value
		countries.put("German", Locale.GERMAN);
	}
	
	public Map<String, Object> getCountriesInMap() {
		return countries;
	}
	
	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public String getLocaleCode() {
		return locale.getLanguage();
	}

	public void setLocaleCode(String localeCode) {
		this.locale = new Locale(localeCode);
	}

	// value change event listener
	public void countryLocaleCodeChanged() {
		// loop country map to compare the locale code
		for (Map.Entry<String, Object> entry : countries.entrySet()) {

			if (entry.getValue().toString().equals(locale)) {

				FacesContext.getCurrentInstance().getViewRoot().setLocale((Locale) entry.getValue());

				break;
			}
		}
	}
	
}
