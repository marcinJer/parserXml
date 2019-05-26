package pl.parser.nbp;

import java.util.Date;

public class Values {

    private String currencyCode;
    private Date startDate;
    private Date endDate;
    private Double average;
    private Double standardDeviation;

    public Values(Double average, Double standardDeviation) {
        this.average = average;
        this.standardDeviation = standardDeviation;
    }

    public Values(String currencyCode, Date startDate, Date endDate, Double average, Double standardDeviation) {
        this.currencyCode = currencyCode;
        this.startDate = startDate;
        this.endDate = endDate;
        this.average = average;
        this.standardDeviation = standardDeviation;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Double getAverage() {
        return average;
    }

    public void setAverage(Double average) {
        this.average = average;
    }

    public Double getStandardDeviation() {
        return standardDeviation;
    }

    public void setStandardDeviation(Double standardDeviation) {
        this.standardDeviation = standardDeviation;
    }

    @Override
    public String toString() {
        return "Kod waluty = " + currencyCode + '\n' +
                "Data poczatkowa = " + startDate + '\n'+
                "Data koncowa = " + endDate + '\n' +
                "Srednia kursow kupna = " + average + '\n' +
                "Odchylenie standardowe kursow sprzedazy = " + standardDeviation;
    }
}
