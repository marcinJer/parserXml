import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.sql.Date;

public class MainClass {
    public static void main(String[] args) throws IOException, JAXBException, ParserConfigurationException, SAXException {
//        System.out.println(GenerateUrlLink.generateFirstUrlToXmlFile(Date.valueOf("2015-02-12")));
//        System.out.println(GenerateUrlLink.generateSecondUrlToXmlFile(Date.valueOf("2015-03-12")));
//        GenerateUrlLink.getXmlFilename(Date.valueOf("2014-03-12"));
//        System.out.println(GenerateUrlLink.generateUrlToXmlFile(Date.valueOf("2014-03-12")));
//        GenerateUrlLink.unMarshalingExample(Date.valueOf("2014-03-12"), "USD");
        System.out.println(GenerateUrlLink.dirTxtList(Date.valueOf("2014-03-12"),Date.valueOf("2014-03-17"), "USD"));
    }
}
