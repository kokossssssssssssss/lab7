package db;

import organization.OrganizationType;

import java.sql.*;
import java.util.Arrays;
import java.util.Scanner;

public class DBFiller {

    private static DBReceiver dbReceiver;

    public static void fillForUpdate(DBReceiver dbReceiver1, int id) throws SQLException {
        dbReceiver = dbReceiver1;
        String query = "update s409333.\"Organization\" set \"name\" =?, \"Coordinates_ID\"=?, \"annualTurnover\"=?, \"employeesCount\"=?,  \"type\"=?, \"postalAddress_ID\"=? where id =" + id;
        Connection connection = new DBWorker().getConnection(); // Получаем соединение с базой данных
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        String orgName = fillName();
        int coordinatesID = fillCoordinates();
        long annualTurnover = fillAnnualTurnover();
        long employeesCount = fillEmployeesCount();
        OrganizationType type = fillOrganizationType();
        int addressID = fillAddress();
        preparedStatement.setString(1, orgName);
        preparedStatement.setInt(2, coordinatesID);
        preparedStatement.setLong(3, annualTurnover);
        preparedStatement.setLong(4, employeesCount);
        preparedStatement.setObject(5, type, java.sql.Types.OTHER);
        preparedStatement.setInt(6, addressID);
        preparedStatement.executeUpdate();
    }

    public static void fill(DBReceiver dbReceiver1) throws SQLException {
        dbReceiver = dbReceiver1;
        String[] data = dbReceiver.authorization();
        String name = data[0];
        String password = data[1];
        if (DBUserChecker.checkUser(name, password)) {
            String query = "insert into s409333.\"Organization\" ( \"name\", \"Coordinates_ID\", \"annualTurnover\", \"employeesCount\",  \"type\", \"postalAddress_ID\", \"user_name\") values ( ?, ?, ?, ?, ?, ?, ?)";
            Connection connection = new DBWorker().getConnection(); // Получаем соединение с базой данных
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            String orgName = fillName();
            int coordinatesID = fillCoordinates();
            long annualTurnover = fillAnnualTurnover();
            long employeesCount = fillEmployeesCount();
            OrganizationType type = fillOrganizationType();
            int addressID = fillAddress();
            preparedStatement.setString(1, orgName);
            preparedStatement.setInt(2, coordinatesID);
            preparedStatement.setLong(3, annualTurnover);
            preparedStatement.setLong(4, employeesCount);
            preparedStatement.setObject(5, type, java.sql.Types.OTHER);
            preparedStatement.setInt(6, addressID);
            preparedStatement.setString(7, name);
            preparedStatement.executeUpdate();
        }
    }

    private static int fillCoordinates() {
        int coordinatesID = 0;
        Scanner coordScanner = new Scanner(System.in);
        int x;
        float y;
        while (true) {
            if (dbReceiver.isScriptWorking()) {
                String[] compositeCommand = dbReceiver.getCompositeCommand();
                try {
                    x = Integer.parseInt(compositeCommand[0]);
                } catch (NumberFormatException e) {
                    System.out.println("Wrong x coordinate");
                    x = 1;
                }
                dbReceiver.setCompositeCommand(Arrays.copyOfRange(compositeCommand, 1, compositeCommand.length));
                break;
            } else {
                System.out.println("Enter coordinate x: ");
                try {
                    x = Integer.parseInt(coordScanner.nextLine());
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("Coordinate must be a number...");
                }
            }
        }
        while (true) {
            if (dbReceiver.isScriptWorking()) {
                String[] compositeCommand = dbReceiver.getCompositeCommand();
                try {
                    y = Float.parseFloat(compositeCommand[0]);
                } catch (NumberFormatException e) {
                    System.out.println("Wrong y coordinate");
                    y = 1;
                }
                dbReceiver.setCompositeCommand(Arrays.copyOfRange(compositeCommand, 1, compositeCommand.length));
                break;
            } else {
                System.out.println("Enter coordinate y: ");
                try {
                    y = Float.parseFloat(coordScanner.nextLine());
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("Coordinate must be a number...");
                }
            }
        }
        String queryCoord = "insert into s409333.\"Coordinates\" (x, y) values (" + x + " ," + y + ")";
        try {
            Statement coordStatement = DBWorker.getConnection().createStatement();
            coordStatement.execute(queryCoord);
            String queryCoordId = "select * from s409333.\"Coordinates\" where x = " + x + " and y =" + y;
            try {
                Statement coordIdStatement = DBWorker.getConnection().createStatement();
                ResultSet resultSetId = coordIdStatement.executeQuery(queryCoordId);
                if (resultSetId.next()) {
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
        String zipCode = "";
        while (true) {
            if (dbReceiver.isScriptWorking()) {
                String[] compositeCommand = dbReceiver.getCompositeCommand();
                zipCode = compositeCommand[0];
                dbReceiver.setCompositeCommand(Arrays.copyOfRange(compositeCommand, 1, compositeCommand.length));
            } else {
                System.out.println("Enter zip code: ");
                zipCode = addressScanner.nextLine();
            }
            if (zipCode == null || zipCode.isEmpty()) {
                System.out.println("Zip code can't be null ...");
            } else {
                break;
            }
        }

        String insertAddressQuery = "INSERT INTO s409333.\"Address\" ( \"zipCode\") VALUES ( ?)";

        try {
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

    private static String fillName() {
        String orgName = "";
        if (dbReceiver.isScriptWorking()) {
            String[] compositeCommand = dbReceiver.getCompositeCommand();
            orgName = compositeCommand[0];
            dbReceiver.setCompositeCommand(Arrays.copyOfRange(compositeCommand, 1, compositeCommand.length));
        } else {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.println("Enter name of organization: ");
                orgName = scanner.nextLine();
                if (orgName == null || orgName.isEmpty()) {
                    System.out.println("Name can't be null ...");
                } else {
                    break;
                }
            }
        }
        return orgName;
    }

    private static long fillAnnualTurnover() {
        long annualTurnover = 500;
        while (true) {
            if (dbReceiver.isScriptWorking()) {
                String[] compositeCommand = dbReceiver.getCompositeCommand();
                try {
                    annualTurnover = Long.parseLong(compositeCommand[0]);
                    if (annualTurnover < 0) {
                        System.out.println("Annual turnover must be more than 0 ...");
                        annualTurnover = 1000;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Wrong annual turnover");
                    annualTurnover = 1000;
                }
                dbReceiver.setCompositeCommand(Arrays.copyOfRange(compositeCommand, 1, compositeCommand.length));
                break;
            } else {
                System.out.println("Enter annual turnover: ");
                Scanner scanner = new Scanner(System.in);
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
        }
        return annualTurnover;
    }

    private static long fillEmployeesCount() {
        long employeesCount = 777;
        while (true) {
            if (dbReceiver.isScriptWorking()) {
                String[] compositeCommand = dbReceiver.getCompositeCommand();
                try {
                    employeesCount = Long.parseLong(compositeCommand[0]);
                    if (employeesCount < 0) {
                        System.out.println("Number of employees must be more than 0 ...");
                        employeesCount = 1000;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Wrong employees count");
                    employeesCount = 1000;
                }
                dbReceiver.setCompositeCommand(Arrays.copyOfRange(compositeCommand, 1, compositeCommand.length));
                break;
            } else {
                System.out.println("Enter number of employees: ");
                Scanner scanner = new Scanner(System.in);
                try {
                    employeesCount = Long.parseLong(scanner.nextLine());
                    if (employeesCount > 0) {
                        break;
                    } else {
                        System.out.println("Number of employees must be more than 0 ...");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Number of employees must be a number ...");
                }
            }
        }
        return employeesCount;
    }

    private static OrganizationType fillOrganizationType() {
        OrganizationType type;
        while (true) {
            if (dbReceiver.isScriptWorking()) {
                String[] compositeCommand = dbReceiver.getCompositeCommand();
                try {
                    type = OrganizationType.valueOf(compositeCommand[0]);
                } catch (IllegalArgumentException e) {
                    System.out.println("Wrong organization type");
                    type = OrganizationType.TRUST;
                }
                dbReceiver.setCompositeCommand(Arrays.copyOfRange(compositeCommand, 1, compositeCommand.length));
                break;
            } else {
                Scanner scanner = new Scanner(System.in);
                System.out.println("Choose type of Organization: COMMERCIAL, GOVERNMENT, TRUST or OPEN_JOINT_STOCK_COMPANY");
                try {
                    type = OrganizationType.valueOf(scanner.nextLine());
                    break;
                } catch (IllegalArgumentException e) {
                    System.out.println("Entered a wrong type ...");
                }
            }
        }
        return type;
    }
}
