package pl.parser;

public class Values {

    private Double average;
    private Double standardDeviation;

    public Values(Double average, Double standardDeviation) {
        this.average = average;
        this.standardDeviation = standardDeviation;
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
        return "Średnia kursów kupna wynosi: " + average +
                "\nOdchylenie standardowe kursów sprzedaży wynosi: " + standardDeviation;
    }
}
