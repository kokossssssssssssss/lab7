import Commands.Console;
import DB.DBParser;
import DB.DBWorker;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {
    private static final String DB_USERNAME = "postgres";
    private static final String DB_PASSWORD = "kjgfcnbyj";
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/postgres";

    /**
     * This method is start of the program
     * It creates console object and use toStart method
     * @param args parameters from the console
     */
    public static void main(String[] args) {
//        Console console  = new Console();
//        console.toStart();

        System.out.println(DBParser.getOrganizationsFromDB());
    }
}