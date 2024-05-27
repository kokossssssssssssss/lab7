package DB;

import Organization.OrganizationType;

import java.sql.*;
import java.util.Scanner;

public class DBFiller {

    public static  void fill() throws SQLException {
        String query = "insert into organizations.\"Organization\" ( \"name\", \"Coordinates_ID\", \"annualTurnover\", \"employeesCount\",  \"type\", \"postalAddress_ID\") values ( ?, ?, ?, ?, ?, ?)";
        Connection connection = new DBWorker().getConnection(); // Получаем соединение с базой данных
        PreparedStatement preparedStatement = connection.prepareStatement(query);

        Scanner scanner = new Scanner(System.in);

        String name;
        while(true) {
            System.out.println("Enter name: ");
            name = scanner.nextLine();
            if (name == null || name.isEmpty()) {
                System.out.println("Name can't be null ...");
            } else {
                break;
            }
        }

        int coordinatesID = fillCoordinates();

        long annualTurnover;
        while(true) {
            System.out.println("Enter annual turnover: ");
            try {
                    annualTurnover = Long.parseLong(scanner.nextLine());
                    if (annualTurnover > 0) {
                    break;
                } else {
                    System.out.println("Annual turnover must be more than 0 ...");
                }
            } catch (NumberFormatException e) {
                System.out.println("Annual turnover must be a number ...");
            }
        }

        long employeesCount;
        while(true){
            System.out.println("Enter number of employees: ");
            try {
                    employeesCount = Long.parseLong(scanner.nextLine());
                    if(employeesCount > 0){
                    break;
                }else{
                    System.out.println("Number of employees must be more than 0 ...");
                }
            } catch (NumberFormatException e) {
                System.out.println("Number of employees must be a number ...");
            }
        }

        OrganizationType type;
        while(true) {
            System.out.println("Choose type of Organization: COMMERCIAL, GOVERNMENT, TRUST or OPEN_JOINT_STOCK_COMPANY");
            try{
                type = OrganizationType.valueOf(scanner.nextLine());
                break;
            } catch (IllegalArgumentException e) {
                System.out.println("Entered a wrong type ...");
            }
        }

        int addressID = fillAddress();

        preparedStatement.setString(1, name);
        preparedStatement.setInt(2, coordinatesID);
        preparedStatement.setLong(3, annualTurnover);
        preparedStatement.setLong(4, employeesCount);
        preparedStatement.setObject(5, type, java.sql.Types.OTHER);
        preparedStatement.setInt(6, addressID);

        preparedStatement.executeUpdate();
    }



    private static int fillCoordinates(){
        int coordinatesID = 0;
        Scanner coordScanner = new Scanner(System.in);
        int x;
        float y;
        while(true){
            System.out.println("Enter coordinate x: ");
            try {
                x = Integer.parseInt(coordScanner.nextLine());
                break;
            } catch (NumberFormatException e) {
                System.out.println("Coordinate must be a number...");
            }
        }
        while(true){
            System.out.println("Enter coordinate y: ");
            try {
                y = Float.parseFloat(coordScanner.nextLine());
                break;
            } catch (NumberFormatException e) {
                System.out.println("Coordinate must be a number...");
            }
        }
        String queryCoord = "insert into organizations.\"Coordinates\" (x, y) values (" +  x + " ," + y + ")";
        try{
            Statement coordStatement = DBWorker.getConnection().createStatement();
            coordStatement.execute(queryCoord);
            String queryCoordId = "select * from organizations.\"Coordinates\" where x = " + x + " and y =" + y;

            try{
                Statement coordIdStatement = DBWorker.getConnection().createStatement();
                ResultSet resultSetId = coordIdStatement.executeQuery(queryCoordId);
                if(resultSetId.next()){
                    coordinatesID = resultSetId.getInt(1);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return coordinatesID;
    }

private static int fillAddress() {
    int addressID = 0;
    Scanner addressScanner = new Scanner(System.in);
    String zipCode = null;

    while (true) {
        System.out.println("Enter zip code: ");
        zipCode = addressScanner.nextLine();
        if (zipCode == null || zipCode.isEmpty()) {
            System.out.println("Zip code can't be null ...");
        } else {
            break;
        }
    }

    String insertAddressQuery = "INSERT INTO organizations.\"Address\" ( \"zipCode\") VALUES ( ?)";

    try  {
        Connection connection = DBWorker.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(insertAddressQuery, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setString(1, zipCode);
        preparedStatement.executeUpdate();

        ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
        if (generatedKeys.next()) {
            addressID = generatedKeys.getInt(1);
        }
    } catch (SQLException e) {
        throw new RuntimeException(e);
    }

    return addressID;
}
}
