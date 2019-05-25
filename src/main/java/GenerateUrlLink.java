import org.omg.CosNaming.NamingContextPackage.NotFound;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GenerateUrlLink {

    private static String generateDateFormat(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyMMdd");
        return formatter.format(date);
    }

    private static Integer generateDate(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyMMdd");
        return Integer.parseInt(formatter.format(date));
    }

    private static URL generateUrlToXmlFile(String string) {
        URL myURL = null;
        try {
            myURL = new URL("http://www.nbp.pl/kursy/xml/" + string + ".xml");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return myURL;
    }

    private static void unMarshalingExample(URL url, String currencyCode) {
        JAXBContext jaxbContext = null;
        try {
            jaxbContext = JAXBContext.newInstance(Currencies.class);
            Unmarshaller jaxbUnmarshaller = null;

            jaxbUnmarshaller = jaxbContext.createUnmarshaller();


            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            Currencies currencies = null;

            currencies = (Currencies) jaxbUnmarshaller.unmarshal(factory
                    .newDocumentBuilder()
                    .parse(String.valueOf(url)));

            List<String> buyingCourseWithDot = currencies.getCurrencies().stream()
                    .filter(currency -> currency.getKod_waluty().equals(currencyCode))
                    .map(currency -> currency.getKurs_kupna().replaceAll(",", "."))
                    .collect(Collectors.toList());

            List<String> sellingCourseWithDot = currencies.getCurrencies().stream()
                    .filter(currency -> currency.getKod_waluty().equals(currencyCode))
                    .map(currency -> currency.getKurs_sprzedazy().replaceAll(",", "."))
                    .collect(Collectors.toList());


            System.out.println(sellingCourseWithDot);
            System.out.println(buyingCourseWithDot);
        } catch (JAXBException | SAXException | IOException | ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    public static List dirTxtList(Date startDate, Date endDate, String currencyCode) throws MalformedURLException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
        final Integer getYearFromStartDate = Integer.parseInt(formatter.format(startDate));
        final Integer getYearFromEndDate = Integer.parseInt(formatter.format(endDate));

        List<URL> listOfDirTextFiles = new ArrayList<>();
        for (int i = getYearFromStartDate; i <= getYearFromEndDate; i++) {
            URL myURL = new URL("http://www.nbp.pl/kursy/xml/dir" + i + ".txt");
            listOfDirTextFiles.add(myURL);
        }
        return urlXmlLinks(listOfDirTextFiles, startDate, endDate, currencyCode);
    }

    private static List urlXmlLinks(List<URL> listOfDirFiles, Date startDate, Date endDate, String currencyCode) {
        listOfDirFiles.forEach(url -> {
            try {
                final List<String> listOfAllStrings = getStringsFromDirFile(url);

                List<String> listOfAllStringsForCTable = listOfAllStrings.stream()
                        .filter(string -> string.startsWith("c"))
                        .collect(Collectors.toList());


                List<Integer> listOfAllIntegersAsDate = listOfAllStringsForCTable.stream()
                        .map(string -> Integer.parseInt(string.substring(5, 11)))
                        .collect(Collectors.toList());

                List<Integer> listOfWantedDates = listOfAllIntegersAsDate.stream()
                        .filter(integer -> integer >= generateDate(startDate) && integer <= generateDate(endDate))
                        .collect(Collectors.toList());

                List<URL> listOfAllWantedXmlUrls = listOfAllStringsForCTable.stream()
                        .filter(s -> listOfWantedDates.stream()
                                .map(String::valueOf)
                                .anyMatch(s::contains))
                        .map(GenerateUrlLink::generateUrlToXmlFile)
                        .collect(Collectors.toList());


                System.out.println(listOfAllWantedXmlUrls);
                listOfAllWantedXmlUrls.forEach(url1 -> unMarshalingExample(url1, currencyCode));



            } catch (IOException e) {
                e.printStackTrace();
            }

        });
        return null;
    }

    private static List<String> getStringsFromDirFile(URL url) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
        String line;
        final List<String> listOfAllStrings = new ArrayList<>();
        while ((line = in.readLine()) != null) {
            listOfAllStrings.add(line);
        }
        in.close();

        return listOfAllStrings;
    }

}
