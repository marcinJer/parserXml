package pl.parser;

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
import java.math.RoundingMode;
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

    private static Course unMarshalingExample(URL url, String currencyCode) {
        JAXBContext jaxbContext = null;
        Double buyingCourseWithDot = null;
        Double sellingCourseWithDot = null;
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
                    .map(currency -> new Double(currency.getKurs_kupna().replaceAll(",", ".")))
                    .findFirst().get();

            sellingCourseWithDot = currencies.getCurrencies().stream()
                    .filter(currency -> currency.getKod_waluty().equals(currencyCode))
                    .map(currency -> new Double(currency.getKurs_sprzedazy().replaceAll(",", ".")))
                    .findFirst().get();

        } catch (JAXBException | SAXException | IOException | ParserConfigurationException e) {
            e.printStackTrace();
        }
        return new Course(buyingCourseWithDot, sellingCourseWithDot);
    }

    public static Values dirTxtList(String currencyCode, Date startDate, Date endDate) throws MalformedURLException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
        int getYearFromStartDate = Integer.parseInt(formatter.format(startDate));
        int getYearFromEndDate = Integer.parseInt(formatter.format(endDate));
        int swapYear;
        Date swapDate;

        HashMap<Integer, URL> listOfDirTextFiles = new HashMap<>();

        if ((getYearFromStartDate > getYearFromEndDate) || (generateDate(startDate) > generateDate(endDate))) {
            swapYear = getYearFromStartDate;
            getYearFromStartDate = getYearFromEndDate;
            getYearFromEndDate = swapYear;

            swapDate = startDate;
            startDate = endDate;
            endDate = swapDate;

            for (int i = getYearFromStartDate; i <= getYearFromEndDate; i++) {
                URL myURL = new URL("http://www.nbp.pl/kursy/xml/dir" + i + ".txt");
                listOfDirTextFiles.put(i, myURL);
            }
            return urlXmlLinks(listOfDirTextFiles, currencyCode, startDate, endDate);
        } else {
            for (int i = getYearFromStartDate; i <= getYearFromEndDate; i++) {
                URL myURL = new URL("http://www.nbp.pl/kursy/xml/dir" + i + ".txt");
                listOfDirTextFiles.put(i, myURL);
            }
            return urlXmlLinks(listOfDirTextFiles, currencyCode, startDate, endDate);
        }
    }

    private static Values urlXmlLinks(HashMap<Integer, URL> listOfDirFiles, String currencyCode, Date startDate, Date endDate) {
        List<Double> allBuyingCourseValues = new ArrayList<>();
        List<Double> allSellingCourseValues = new ArrayList<>();
        List<Integer> listOfWantedDates = new ArrayList<>();
        List<URL> listOfAllWantedXmlUrls = new ArrayList<>();

        listOfDirFiles.forEach((integer, url) -> {
            try {
                if ((integer.equals(dateToIntYear(endDate)) && listOfDirFiles.size() == 1)) {
                    final List<String> listOfAllStrings = getAllStringsFromTxtFile(url);

                    List<String> listOfAllStringsForCTable = getAllStringsForCTable(listOfAllStrings);

                    List<Integer> listOfAllIntegersAsDate = getAllIntegersAsDate(listOfAllStringsForCTable);

                    listOfWantedDates.addAll(listOfAllIntegersAsDate.stream()
                            .filter(integer1 -> integer1 >= generateDate(startDate) && integer1 <= generateDate(endDate))
                            .collect(Collectors.toList()));

                    getAllXmlsBetweenTwoDates(listOfWantedDates, listOfAllWantedXmlUrls, listOfAllStringsForCTable);

                    listOfAllWantedXmlUrls.forEach(url1 -> {
                        allBuyingCourseValues.add(unMarshalingExample(url1, currencyCode).getBuyingCourse());
                        allSellingCourseValues.add(unMarshalingExample(url1, currencyCode).getSellingCourse());
                    });
                    return;

                } else if (integer.equals(dateToIntYear(endDate))) {
                    final List<String> listOfAllStrings = getAllStringsFromTxtFile(url);

                    List<String> listOfAllStringsForCTable = getAllStringsForCTable(listOfAllStrings);

                    List<Integer> listOfAllIntegersAsDate = getAllIntegersAsDate(listOfAllStringsForCTable);

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

                        } else return;
                    });
                    return;

                } else {
                    final List<String> listOfAllStrings = getAllStringsFromTxtFile(url);

                    List<String> listOfAllStringsForCTable = getAllStringsForCTable(listOfAllStrings);

                    List<Integer> listOfAllIntegersAsDate = getAllIntegersAsDate(listOfAllStringsForCTable);

                    listOfWantedDates.addAll(listOfAllIntegersAsDate.stream()
                            .filter(integer1 -> integer1 >= generateDate(startDate) && integer1 <= listOfAllIntegersAsDate.get(listOfAllIntegersAsDate.size() - 1))
                            .collect(Collectors.toList()));

                    getAllXmlsBetweenTwoDates(listOfWantedDates, listOfAllWantedXmlUrls, listOfAllStringsForCTable);

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
        return countAvgAndStandardDeviation(currencyCode, startDate, endDate, allBuyingCourseValues, allSellingCourseValues);
    }

    private static Values countAvgAndStandardDeviation(String currencyCode, Date startDate, Date endDate, List<Double> buyingCourses, List<Double> sellingCourses) {

        Double average = buyingCourses.stream().mapToDouble(val -> val).average().orElse(0.0);
        BigDecimal bdAvg = new BigDecimal(average).setScale(4, RoundingMode.HALF_UP);
        double convertedAverage = bdAvg.doubleValue();

        Statistics statistics = new Statistics(sellingCourses);

        BigDecimal bdSd = new BigDecimal(statistics.getStdDev()).setScale(4, RoundingMode.HALF_UP);
        double standadDeviation = bdSd.doubleValue();

        return new Values(currencyCode, startDate, endDate, convertedAverage, standadDeviation);
    }

    private static void getAllXmlsBetweenTwoDates(List<Integer> listOfWantedDates, List<URL> listOfAllWantedXmlUrls, List<String> listOfAllStringsForCTable) {
        listOfAllWantedXmlUrls.addAll(listOfAllStringsForCTable.stream()
                .filter(s -> listOfWantedDates.stream()
                        .map(String::valueOf)
                        .anyMatch(s::contains))
                .map(GenerateUrlLink::generateUrlToXmlFile)
                .collect(Collectors.toList()));
    }

    private static List<Integer> getAllIntegersAsDate(List<String> listOfAllStringsForCTable) {
        return listOfAllStringsForCTable.stream()
                .map(string -> Integer.parseInt(string.substring(5, 11)))
                .collect(Collectors.toList());
    }

    private static List<String> getAllStringsForCTable(List<String> listOfAllStrings) {
        return listOfAllStrings.stream()
                .filter(string -> string.startsWith("c"))
                .collect(Collectors.toList());
    }

    private static List<String> getAllStringsFromTxtFile(URL url) throws IOException {
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
