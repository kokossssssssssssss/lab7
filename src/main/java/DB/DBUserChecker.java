package DB;

import org.apache.commons.codec.digest.DigestUtils;

import java.sql.*;
import java.util.Scanner;

public class DBUserChecker {
    public static boolean checkUser(String name, String password) {
        if (checkUserName(name)) {
            if (checkPassword(name, password)) {
                System.out.println("Password is correct...");
                return true;
            } else {
                System.out.println("Password isn't correct...");
                return false;
            }
        } else {
            System.out.println("There isn't a user with such name...");
            return false;
        }
    }

    private static boolean checkUserName(String userNameToCheck) {//проверка наличия пользователя с указанным именем
        String query = "select exists (select 1 from s409333.\"User\" where name =? )";
        try {
            Connection conn = new DBWorker().getConnection();
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, userNameToCheck);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getBoolean(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static boolean checkPassword(String username, String password) {
        String query = "select password from s409333.\"User\" where name = ?";
        try (Connection connection = DBWorker.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String passwordToCheck = rs.getString("password");
                String hashedPassword = hashPassword(password);
                return passwordToCheck.equals(hashedPassword);
            }

        } catch (SQLException e) {
            System.out.println("Error by checking pasword...");
        }

        return false;
    }

    private static String hashPassword(String password) {
        return DigestUtils.sha3_224Hex(password);
    }
}
