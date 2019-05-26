package pl.parser.nbp;

public final class Course {
    private Double buyingCourse;
    private Double sellingCourse;

    public Course(Double buyingCourse, Double sellingCourse) {
        this.buyingCourse = buyingCourse;
        this.sellingCourse = sellingCourse;
    }

    public Double getBuyingCourse() {
        return buyingCourse;
    }

    public void setBuyingCourse(Double buyingCourse) {
        this.buyingCourse = buyingCourse;
    }

    public Double getSellingCourse() {
        return sellingCourse;
    }

    public void setSellingCourse(Double sellingCourse) {
        this.sellingCourse = sellingCourse;
    }
}
