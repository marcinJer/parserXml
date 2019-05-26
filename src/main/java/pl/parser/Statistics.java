package pl.parser;

import java.util.List;

public class Statistics {
    List<Double> data;
    int size;

    public Statistics(List<Double> data) {
        this.data = data;
        size = data.size();
    }

    double getMean() {
        double sum = 0.0;
        for (double a : data)
            sum += a;
        return sum / size;
    }

    double getVariance() {
        if (!(data.size() == 1)) {
            double mean = getMean();
            double temp = 0;
            for (double a : data)
                temp += (a - mean) * (a - mean);
            return temp / (data.size() - 1);
        }else return 0.0;
    }

    double getStdDev() {
        return Math.sqrt(getVariance());
    }

}