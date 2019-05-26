package pl.parser;

import java.io.IOException;
import java.sql.Date;

public class MainClass {
    public static void main(String[] args) throws IOException {
        System.out.println(GenerateUrlLink.dirTxtList(Date.valueOf("2013-01-31"),Date.valueOf("2013-01-28"), "USD"));
    }
}
