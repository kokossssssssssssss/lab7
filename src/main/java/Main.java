import Commands.Console;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

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

        try{
            Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            if(!connection.isClosed())
                System.out.println("Соединение установлено");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}