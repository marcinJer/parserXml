package pl.parser.nbp;

import java.io.IOException;
import java.sql.Date;

public class MainClass {
    public static void main(String[] args) throws IOException {
        GenerateUrlLink generateUrlLink = new GenerateUrlLink();
        System.out.println(generateUrlLink.dirTxtList("USD", Date.valueOf("2014-02-04"), Date.valueOf("2014-02-09")));
    }
}
