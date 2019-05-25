import java.math.BigDecimal;

public final class MyResult {
    private BigDecimal buyingCourse;
    private BigDecimal sellingCourse;

    public MyResult(BigDecimal buyingCourse, BigDecimal sellingCourse) {
        this.buyingCourse = buyingCourse;
        this.sellingCourse = sellingCourse;
    }

    public BigDecimal getBuyingCourse() {
        return buyingCourse;
    }

    public void setBuyingCourse(BigDecimal buyingCourse) {
        this.buyingCourse = buyingCourse;
    }

    public BigDecimal getSellingCourse() {
        return sellingCourse;
    }

    public void setSellingCourse(BigDecimal sellingCourse) {
        this.sellingCourse = sellingCourse;
    }
}
