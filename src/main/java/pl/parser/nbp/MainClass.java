package pl.parser.nbp;

import java.io.IOException;
import java.sql.Date;

public class MainClass {
    public static void main(String[] args) throws IOException {
        System.out.println(new DataProcessing().getDirFiles(args[0], Date.valueOf(args[1]), Date.valueOf(args[2])));
    }
}
