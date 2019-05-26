import org.xml.sax.SAXException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class GenerateUrlLink {

    private static Integer dateToIntYear(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
        return Integer.parseInt(formatter.format(date));
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

    private static MyResult unMarshalingExample(URL url, String currencyCode) {
        JAXBContext jaxbContext = null;
        BigDecimal buyingCourseWithDot = null;
        BigDecimal sellingCourseWithDot = null;
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

            buyingCourseWithDot = currencies.getCurrencies().stream()
                    .filter(currency -> currency.getKod_waluty().equals(currencyCode))
                    .map(currency -> new BigDecimal(currency.getKurs_kupna().replaceAll(",", ".")))
                    .findFirst().get();

            sellingCourseWithDot = currencies.getCurrencies().stream()
                    .filter(currency -> currency.getKod_waluty().equals(currencyCode))
                    .map(currency -> new BigDecimal(currency.getKurs_sprzedazy().replaceAll(",", ".")))
                    .findFirst().get();

        } catch (JAXBException | SAXException | IOException | ParserConfigurationException e) {
            e.printStackTrace();
        }
        return new MyResult(buyingCourseWithDot, sellingCourseWithDot);
    }

    public static List dirTxtList(Date startDate, Date endDate, String currencyCode) throws MalformedURLException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
        final int getYearFromStartDate = Integer.parseInt(formatter.format(startDate));
        final int getYearFromEndDate = Integer.parseInt(formatter.format(endDate));

        HashMap<Integer, URL> listOfDirTextFiles = new HashMap<>();
        for (int i = getYearFromStartDate; i <= getYearFromEndDate; i++) {
            URL myURL = new URL("http://www.nbp.pl/kursy/xml/dir" + i + ".txt");
            listOfDirTextFiles.put(i, myURL);
        }
        return urlXmlLinks(listOfDirTextFiles, startDate, endDate, currencyCode);
    }

    private static List urlXmlLinks(HashMap<Integer, URL> listOfDirFiles, Date startDate, Date endDate, String currencyCode) {
        List<BigDecimal> allBuyingCourseValues = new ArrayList<>();
        List<BigDecimal> allSellingCourseValues = new ArrayList<>();
        List<Integer> listOfWantedDates = new ArrayList<>();
        List<URL> listOfAllWantedXmlUrls = new ArrayList<>();

        listOfDirFiles.forEach((integer, url) -> {
            try {

                if ((integer.equals(dateToIntYear(endDate)) && listOfDirFiles.size() == 1)) {
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

                    listOfWantedDates.addAll(listOfAllIntegersAsDate.stream()
                            .filter(integer1 -> integer1 >= generateDate(startDate) && integer1 <= generateDate(endDate))
                            .collect(Collectors.toList()));

                    listOfAllWantedXmlUrls.addAll(listOfAllStringsForCTable.stream()
                            .filter(s -> listOfWantedDates.stream()
                                    .map(String::valueOf)
                                    .anyMatch(s::contains))
                            .map(GenerateUrlLink::generateUrlToXmlFile)
                            .collect(Collectors.toList()));

                    listOfAllWantedXmlUrls.forEach(url1 -> {
                        allBuyingCourseValues.add(unMarshalingExample(url1, currencyCode).getBuyingCourse());
                        allSellingCourseValues.add(unMarshalingExample(url1, currencyCode).getSellingCourse());
                    });
                    return;

                } else if (integer.equals(dateToIntYear(endDate))) {
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

                    listOfAllIntegersAsDate.forEach(integer1 -> {
                        if (integer1 >= listOfAllIntegersAsDate.get(0) && integer1 <= generateDate(endDate)) {
                            listOfWantedDates.add(integer1);

                            listOfAllWantedXmlUrls.addAll(listOfAllStringsForCTable.stream()
                                    .filter(s -> listOfWantedDates.stream()
                                            .map(String::valueOf)
                                            .anyMatch(s::contains))
                                    .map(s -> {
                                        allBuyingCourseValues.add(unMarshalingExample(generateUrlToXmlFile(s), currencyCode).getBuyingCourse());
                                        allSellingCourseValues.add(unMarshalingExample(generateUrlToXmlFile(s), currencyCode).getSellingCourse());
                                        return generateUrlToXmlFile(s);
                                    })
                                    .collect(Collectors.toList()));

                            /*listOfAllWantedXmlUrls.forEach(url1 -> {
                                allBuyingCourseValues.add(unMarshalingExample(url1, currencyCode).getBuyingCourse());
                                allSellingCourseValues.add(unMarshalingExample(url1, currencyCode).getSellingCourse());
                            });*/

                        } else return;
                    });
//                            .filter(integer1 -> integer1 >= listOfAllIntegersAsDate.get(0) && integer1 <= generateDate(endDate))
//                            .collect(Collectors.toList()));


                    return;

                } else {
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

                    listOfWantedDates.addAll(listOfAllIntegersAsDate.stream()
                            .filter(integer1 -> integer1 >= generateDate(startDate) && integer1 <= listOfAllIntegersAsDate.get(listOfAllIntegersAsDate.size() - 1))
                            .collect(Collectors.toList()));

                    listOfAllWantedXmlUrls.addAll(listOfAllStringsForCTable.stream()
                            .filter(s -> listOfWantedDates.stream()
                                    .map(String::valueOf)
                                    .anyMatch(s::contains))
                            .map(GenerateUrlLink::generateUrlToXmlFile)
                            .collect(Collectors.toList()));

                    listOfAllWantedXmlUrls.forEach(url1 -> {
                        allBuyingCourseValues.add(unMarshalingExample(url1, currencyCode).getBuyingCourse());
                        allSellingCourseValues.add(unMarshalingExample(url1, currencyCode).getSellingCourse());
                    });
                    return;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        });
        return allBuyingCourseValues;
    }

    /*private static List<String> getStringsFromDirFile(URL url) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
        String line;
        final List<String> listOfAllStrings = new ArrayList<>();
        while ((line = in.readLine()) != null) {
            listOfAllStrings.add(line);
        }
        in.close();

        return listOfAllStrings;
    }*/
}
