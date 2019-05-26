import java.io.IOException;
import java.sql.Date;

public class MainClass {
    public static void main(String[] args) throws IOException {
        System.out.println(GenerateUrlLink.dirTxtList(Date.valueOf("2014-12-30"),Date.valueOf("2015-01-02"), "USD"));
    }
}
