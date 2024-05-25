package DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBWorker {
    private static final String DB_USERNAME = "postgres";
    private static final String DB_PASSWORD = "kjgfcnbyj";
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/postgres";

    private static Connection connection;


    public DBWorker() {

        // Устанавливаем соединение с базой данных
        try{
            connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            if(!connection.isClosed())
                System.out.println("Соединение установлено");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Connection getConnection() {
        return connection;
    }

}


