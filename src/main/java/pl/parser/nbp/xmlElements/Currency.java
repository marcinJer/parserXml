package pl.parser.nbp.xmlElements;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "pozycja")
@XmlAccessorType(XmlAccessType.FIELD)
public class Currency {

    private String nazwa_waluty;
    private int przelicznik;
    private String kod_waluty;
    private String kurs_kupna;
    private String kurs_sprzedazy;

    public String getNazwa_waluty() {
        return nazwa_waluty;
    }

    public void setNazwa_waluty(String nazwa_waluty) {
        this.nazwa_waluty = nazwa_waluty;
    }

    public int getPrzelicznik() {
        return przelicznik;
    }

    public void setPrzelicznik(int przelicznik) {
        this.przelicznik = przelicznik;
    }

    public String getKod_waluty() {
        return kod_waluty;
    }

    public void setKod_waluty(String kod_waluty) {
        this.kod_waluty = kod_waluty;
    }

    public String getKurs_kupna() {
        return kurs_kupna;
    }

    public void setKurs_kupna(String kurs_kupna) {
        this.kurs_kupna = kurs_kupna;
    }

    public String getKurs_sprzedazy() {
        return kurs_sprzedazy;
    }

    public void setKurs_sprzedazy(String kurs_sprzedazy) {
        this.kurs_sprzedazy = kurs_sprzedazy;
    }

    @Override
    public String toString() {
        return "nazwa_waluty='" + nazwa_waluty + '\'' +
                ", przelicznik=" + przelicznik +
                ", kod_waluty='" + kod_waluty + '\'' +
                ", kurs_kupna=" + kurs_kupna +
                ", kurs_sprzedazy=" + kurs_sprzedazy +
                '}';
    }
}
