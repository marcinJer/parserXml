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

    /*private static String getXmlFilename(Date date) throws IOException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
        final String getYear = formatter.format(date);
        URL myURL = new URL("http://www.nbp.pl/kursy/xml/dir" + getYear + ".txt");

        BufferedReader in = new BufferedReader(new InputStreamReader(myURL.openStream()));
        String line;
        final List<String> list = new ArrayList<>();
        while ((line = in.readLine()) != null) {
            list.add(line);
        }
        in.close();

        Optional<String> c = list.stream()
                .filter(String -> String.startsWith("c") && String.endsWith(generateDateFormat(date)))
                .findFirst();
        if (c.isPresent()) {
            c.get();
            return c.get();
        } else return null;

    }*/

    /*public static URL generateUrlToXmlFile(Date date) throws IOException {
        String xmlFilename = getXmlFilename(date);
        URL myURL = new URL("http://www.nbp.pl/kursy/xml/" + xmlFilename + ".xml");
        return myURL;
    }*/

    /*public static void unMarshalingExample(Date date, String currencyCode) throws JAXBException, IOException, ParserConfigurationException, SAXException {
        JAXBContext jaxbContext = JAXBContext.newInstance(Currencies.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        Currencies currencies = (Currencies) jaxbUnmarshaller.unmarshal(factory
                .newDocumentBuilder()
                .parse(String.valueOf(generateUrlToXmlFile(date))));

        Optional<String> buyingCourseWithDot = currencies.getCurrencies().stream()
                .filter(currency -> currency.getKod_waluty().equals(currencyCode))
                .map(currency -> currency.getKurs_kupna().replaceAll(",", "."))
                .findFirst();

        List<String> sellingCourseWithDot = currencies.getCurrencies().stream()
                .filter(currency -> currency.getKod_waluty().equals(currencyCode))
                .map(currency -> currency.getKurs_sprzedazy().replaceAll(",", "."))
                .collect(Collectors.toList());


        System.out.println(sellingCourseWithDot);
    }*/

    public static List dirTxtList(Date startDate, Date endDate) throws MalformedURLException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
        final Integer getYearFromStartDate = Integer.parseInt(formatter.format(startDate));
        final Integer getYearFromEndDate = Integer.parseInt(formatter.format(endDate));

        List<URL> listOfDirTextFiles = new ArrayList<>();
        for (int i = getYearFromStartDate; i <= getYearFromEndDate; i++) {
            URL myURL = new URL("http://www.nbp.pl/kursy/xml/dir" + i + ".txt");
            listOfDirTextFiles.add(myURL);
        }
        return urlXmlLinks(listOfDirTextFiles, startDate, endDate);
//        return listOfDirTextFiles;
    }

    private static List urlXmlLinks(List<URL> listOfDirFiles, Date startDate, Date endDate) {
        listOfDirFiles.forEach(url -> {
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
                String line;
                final List<String> listOfAllStrings = new ArrayList<>();
                while ((line = in.readLine()) != null) {
                    listOfAllStrings.add(line);
                }
                in.close();

                List<String> listOfAllStringsForCTable = listOfAllStrings.stream()
                        .filter(string -> string.startsWith("c"))
                        .collect(Collectors.toList());


                List<Integer> listOfAllIntegersAsDate = listOfAllStringsForCTable.stream()
                        .map(string -> Integer.parseInt(string.substring(5, 11)))
                        .collect(Collectors.toList());

                List<Integer> listOfWantedDates = listOfAllIntegersAsDate.stream()
                        .filter(integer -> integer >= generateDate(startDate) && integer <= generateDate(endDate))
                        .collect(Collectors.toList());

                List<String> result = new ArrayList<>();

                for (String xmlUrl : listOfAllStringsForCTable) {
                    for (Integer dates : listOfWantedDates) {
                        if (xmlUrl.contains(String.valueOf(dates))) {
                            result.add(xmlUrl);
                        }
                    }
                }

                List<String> listOfAllWantedXmlUrls = listOfAllStringsForCTable.stream()
                        .filter(s -> listOfWantedDates.stream()
                                .map(String::valueOf)
                                .anyMatch(s::contains))
                        .collect(Collectors.toList());


                System.out.println(listOfAllWantedXmlUrls);

                /*List<String> listOfDates = listOfAllStringsAsDate.stream()
                        .map(s -> new Date(Integer.parseInt(s.substring(0, 1)), Integer.parseInt(s.substring(2, 3)), Integer.parseInt(s.substring(4, 5))))
                        .filter(s -> s.after(startDate) && s.before(endDate))
                        .map(s -> generateDateFormat(s))
                        .collect(Collectors.toList());*/

               /* List<String> listOfXmlFiles = listOfAllStringsForCTable.stream()
                        .filter(string -> string.endsWith(listOfDates.stream().findAny().get()))
                        .collect(Collectors.toList());

                System.out.println(listOfXmlFiles);*/


            } catch (IOException e) {
                e.printStackTrace();
            }

        });
        return null;
    }

}
