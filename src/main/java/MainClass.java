import java.io.IOException;
import java.sql.Date;

public class MainClass {
    public static void main(String[] args) throws IOException {
        System.out.println(GenerateUrlLink.dirTxtList(Date.valueOf("2014-03-12"),Date.valueOf("2014-03-17"), "USD"));
    }
}
