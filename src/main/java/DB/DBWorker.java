package DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBWorker {
//    private static final String DB_USERNAME = "s409333";
//    private static final String DB_PASSWORD = "c2tTRoqLv3E5TpqF";
//    private static final String DB_URL = "jdbc:postgresql://db:5432/studs";

    private static final String DB_USERNAME = "postgres";
    private static final String DB_PASSWORD = "root";
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/postgres";

    static Connection connection;


    public DBWorker() {

        // Устанавливаем соединение с базой данных
        try {
            connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
//            if(!connection.isClosed())
//                System.out.println("Connected to DB successfully");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Connection getConnection() {
        return connection;
    }

}


