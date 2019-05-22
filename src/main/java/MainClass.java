import java.io.IOException;
import java.sql.Date;

public class MainClass {
    public static void main(String[] args) throws IOException {
//        System.out.println(GenerateUrlLink.generateFirstUrlToXmlFile(Date.valueOf("2015-02-12")));
//        System.out.println(GenerateUrlLink.generateSecondUrlToXmlFile(Date.valueOf("2015-03-12")));
//        GenerateUrlLink.getXmlFilename(Date.valueOf("2014-03-12"));
        System.out.println(GenerateUrlLink.generateUrlToXmlFile(Date.valueOf("2014-03-12")));
    }
}
