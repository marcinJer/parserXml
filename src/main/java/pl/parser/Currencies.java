package pl.parser;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "tabela_kursow")
@XmlAccessorType (XmlAccessType.FIELD)
public class Currencies
{
	@XmlElement(name = "pozycja")
	private List<Currency> currencies = null;

	public List<Currency> getCurrencies() {
		return currencies;
	}

	public void setCurrencies(List<Currency> currencies) {
		this.currencies = currencies;
	}
}
