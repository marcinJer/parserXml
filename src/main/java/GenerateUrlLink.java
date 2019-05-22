import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class GenerateUrlLink {

    private static String generateDateFormat(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyMMdd");
        return formatter.format(date);
    }

    private static String getXmlFilename(Date date) throws IOException {
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

        List<String> filtered = list.stream()
                .filter(String -> String.startsWith("c") && String.endsWith(generateDateFormat(date)))
                .collect(Collectors.toList());
        return filtered.get(0);
    }

    public static URL generateUrlToXmlFile(Date date) throws IOException {
        String xmlFilename = getXmlFilename(date);
        URL myURL = new URL("http://www.nbp.pl/kursy/xml/" + xmlFilename + ".xml");
        return myURL;
    }
}
