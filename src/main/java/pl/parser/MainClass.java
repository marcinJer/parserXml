package pl.parser;

import java.io.IOException;
import java.sql.Date;

public class MainClass {
    public static void main(String[] args) throws IOException {
        System.out.println(GenerateUrlLink.dirTxtList("USD", Date.valueOf("2013-12-28"),Date.valueOf("2013-12-27")));
    }
}
