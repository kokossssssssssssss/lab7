package DB;

import Commands.Command;
import Organization.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.postgresql.util.PSQLException;

import java.sql.*;
import java.util.*;
import java.util.logging.Logger;


public class DBReceiver {
    private static final Logger logger = Logger.getLogger(DBReceiver.class.getName());


    private OrganizationCollection organizationCollection;
    private String[] tokens;
    private Map<String, Command> commands;

    public DBReceiver(OrganizationCollection organizationCollection) {
        this.organizationCollection = organizationCollection;
    }

    public void setTokens(String[] tokens) {
        this.tokens = tokens;
    }

    public void setCommands(Map<String, Command> commands) {
        this.commands = commands;
    }

    public void add() {

        try {
            DBFiller.fill();
            UpdaterOfCollection.updateCollection(organizationCollection);
        } catch (SQLException e) {
            System.out.println("Failed to add organization...");
        }
    }


    public void show() {
        for (Organization organization : organizationCollection.getCollection()) {
            System.out.println(organization);
        }
    }

    public void removeById() {
        int id = Integer.parseInt(tokens[1]);
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your name: ");
        String name = scanner.nextLine();
        String correctName;
        System.out.println("Enter password: ");
        String password = scanner.nextLine();
        DBUserChecker.checkUser(name, password);
        if (!new CheckerOfOrganization(organizationCollection).checkById(id)) {
            return;
        }
        String queryUser = "select \"user_name\" from s409333.\"Organization\" where id = " + id;
        try {
            Statement statement = new DBWorker().getConnection().createStatement();
            ResultSet rs = statement.executeQuery(queryUser);
            if (rs.next()) {
                correctName = rs.getString(1);
            } else {
                return;
            }
        } catch (SQLException e) {
            System.out.println("Can't define the creator of this organization...");
            return;
        }
        if (DBUserChecker.checkUser(name, password)) {
            if (name.equals(correctName)) {
                String query = "DELETE  FROM s409333.\"Organization\" WHERE id = " + id;
                try {
                    Statement statement = new DBWorker().getConnection().createStatement();
                    statement.execute(query);
                    UpdaterOfCollection.updateCollection(organizationCollection);
                } catch (SQLException e) {
                    System.out.println("Failed to remove organization...");
                }
            } else {
                System.out.println("You haven't access to this organization...");
            }
        }
    }


    public void help() {
        for (Command command : commands.values()) {
            System.out.println(command.description());
        }
    }

    public void clear() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your name: ");
        String name = scanner.nextLine();
        System.out.println("Enter password: ");
        String password = scanner.nextLine();
        if (DBUserChecker.checkUser(name, password)) {
            String queryOrgs = "DELETE FROM s409333.\"Organization\" where \"user_name\" = '" + name + "'";
            try {
                Statement statement = new DBWorker().getConnection().createStatement();


                int deletedOrgs = statement.executeUpdate(queryOrgs);
                System.out.println("Number of deleted organizations: " + deletedOrgs);
                UpdaterOfCollection.updateCollection(organizationCollection);


            } catch (SQLException e) {
                System.out.println("Error by removing...");
                e.printStackTrace();
            }
        }
    }

    public void filterTurnover() {
        int annualTurnover = Integer.parseInt(tokens[1]);

        Iterator<Organization> iterator = organizationCollection.getCollection().iterator();
        while (iterator.hasNext()) {
            Organization organization = iterator.next();
            if (organization.getAnnualTurnover() > annualTurnover) {
                System.out.println(organization);
            }
        }
    }

    public void update() {
        int id = Integer.parseInt(tokens[1]);
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your name: ");
        String name = scanner.nextLine();
        String correctName;
        System.out.println("Enter password: ");
        String password = scanner.nextLine();
        DBUserChecker.checkUser(name, password);
        if (!new CheckerOfOrganization(organizationCollection).checkById(id)) {
            return;
        }

        String queryUser = "select \"user_name\" from s409333.\"Organization\" where id = " + id;
        try {
            Statement statement = new DBWorker().getConnection().createStatement();
            ResultSet rs = statement.executeQuery(queryUser);
            if (rs.next()) {
                correctName = rs.getString(1);
            } else {
                return;
            }
        } catch (SQLException e) {
            System.out.println("Can't define the creator of this organization...");
            return;
        }
        if (DBUserChecker.checkUser(name, password)) {
            if (name.equals(correctName)) {
                try {
                    DBFillerForUpdate.fill(id);
                    UpdaterOfCollection.updateCollection(organizationCollection);
                } catch (SQLException e) {
                    System.out.println("Error by updating organization...");
                }
            } else {
                System.out.println("You haven't access to this organization...");
            }
        }

    }

    public void exit() {
        System.out.println("Program exit...");
        System.exit(0);
    }

    public void insertAt() {
        int index = Integer.parseInt(tokens[1]);
        if (!(index >= 0 && index <= organizationCollection.getCollection().size())) {
            System.out.println("Invalid index...");
            return;
        }
        try {
            DBFiller.fill();
        } catch (SQLException e) {
            System.out.println("Error by creating organization...");
        }
        UpdaterOfCollection.updateCollection(organizationCollection);
        Organization maxIdOrganization = organizationCollection.getCollection().stream()
                .max(Comparator.comparingLong(Organization::getId))
                .orElseThrow(NoSuchElementException::new);


        // Вставляем элемент по индексу
        organizationCollection.getCollection().add(index, maxIdOrganization);

        // Удаляем последний элемент, если коллекция стала больше нужного размера
        if (organizationCollection.getCollection().size() > index + 1) {
            organizationCollection.getCollection().remove(organizationCollection.getCollection().size() - 1);
        }

    }

    public void sort() {
        LinkedList<Organization> toSort = new LinkedList<>(organizationCollection.getCollection());
        Collections.sort(toSort);
        organizationCollection.setList(toSort);
    }

    public void info() {
        System.out.print("Collection information:");
        System.out.println(organizationCollection);
    }

    public void register() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter name: ");
        String name = scanner.nextLine();

        System.out.println("Enter password: ");
        String password1 = scanner.nextLine();
        System.out.println("Repeat password: ");
        String password2 = scanner.nextLine();
        if (password1.equals(password2)) {
            String hash_password = DigestUtils.sha3_224Hex(password2);
            String query = "INSERT INTO s409333.\"User\" (name, password) VALUES ('" + name + "', '" + hash_password + "');";
            try {
                Connection connection = new DBWorker().getConnection();
                Statement statement = connection.createStatement();
                statement.execute(query);
            } catch (PSQLException e) {
                System.out.println("Такое имя пользователя уже есть");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            System.out.println("Entered passwords don't match...");
        }
    }

    public void removeLower() {
        long id;
        try {
            id = Long.parseLong(tokens[1]);
            if (id < 0) {
                throw new ArithmeticException();
            }
            if (!new CheckerOfOrganization(organizationCollection).checkById((int) id)) {
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Id must be integer");
            return;
        } catch (ArithmeticException e) {
            System.out.println("Id cannot be negative");
            return;
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Please, enter the id in the command");
            return;
        }
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your name: ");
        String name = scanner.nextLine();
        String correctName;
        System.out.println("Enter password: ");
        String password = scanner.nextLine();
        if (DBUserChecker.checkUser(name, password)) {
            try {
                Optional<Organization> optionalOrganization = organizationCollection.getCollection().stream().filter(o -> o.getId() == id).findFirst();
                if (optionalOrganization.isEmpty()) {
                    System.out.println("В коллекции нет организации с таким ID");
                    return;
                }
                Organization organization = optionalOrganization.get();
                List<Organization> list = organizationCollection.getCollection().stream().filter(o -> o.getAnnualTurnover() + o.getEmployeesCount()
                        < organization.getAnnualTurnover() + organization.getEmployeesCount()).toList();
                long id2;
                for (int i = 0; i < list.size(); i++) {
                    id2 = list.get(i).getId();
                    String query = "DELETE FROM s409333.\"Organization\" WHERE id = " + id2 + " and user_name = '" + name + "'";
                    Statement statement = new DBWorker().getConnection().createStatement();
                    statement.execute(query);
                    UpdaterOfCollection.updateCollection(organizationCollection);
                }
            } catch (SQLException e) {
                System.out.println("Error by updating organization...");
            }
        }
    }

    public void removeByType() {
        try {
            String organizationType;
            organizationType = tokens[1];
            int count = 0;
            for (OrganizationType orgT : OrganizationType.values()) {
                if (organizationType.equalsIgnoreCase(orgT.name)) {
                    organizationCollection.setLastOrganizationTypeWorkedWith(orgT);
                    break;
                }
                count++;
            }
            if (count == OrganizationType.values().length - 1) {
                System.out.println("There isn't such type");
                return;
            }
            int amount = organizationCollection.getAmountOfElements();
            long id = 0;
            List<Organization> organization = organizationCollection.getCollection().stream().filter(o -> o.getType() == organizationCollection.getLastOrganizationTypeWorkedWith()).toList();
            if (organization.isEmpty()) {
                System.out.println("There isn't any organization with type: " + organizationCollection.getLastOrganizationTypeWorkedWith().name);
                return;
            }
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter your name: ");
            String name = scanner.nextLine();
            String correctName = "";
            System.out.println("Enter password: ");
            String password = scanner.nextLine();
            for(int i = 0; i<organization.size(); i++){
                id = organization.get(i).getId();
                String queryUser = "select \"user_name\" from s409333.\"Organization\" where id = " + id;
                try {
                    Statement statement = new DBWorker().getConnection().createStatement();
                    ResultSet rs = statement.executeQuery(queryUser);
                    if (rs.next()) {
                        correctName = rs.getString(1);
                    } else {
                        return;
                    }
                    if (correctName.equals(name)){
                        break;
                    }
                } catch (SQLException e) {
                    System.out.println("Can't define the creator of this organization...");
                    return;
                }
            }
            if (DBUserChecker.checkUser(name, password)) {
                if (name.equals(correctName)) {
                    try {
                        String query = "DELETE  FROM s409333.\"Organization\" WHERE id = " + id;
                        Statement statement = new DBWorker().getConnection().createStatement();
                        statement.execute(query);
                        UpdaterOfCollection.updateCollection(organizationCollection);
                        organizationCollection.updateData();
                        if (amount != organizationCollection.getAmountOfElements()) {
                            System.out.println("One organization with type: \"" + organizationCollection.getLastOrganizationTypeWorkedWith().name + "\" was successfully deleted");
                        }
                    } catch (SQLException e) {
                        System.out.println("Error by updating organization...");
                    }
                } else {
                    System.out.println("You haven't access to this organization...");
                }
            }
        } catch (NoSuchElementException e) {
            System.out.println("Type not entered");
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Please, enter the type in the command");
        }
    }

    public void printAscending() {
        sort();
        System.out.println(organizationCollection.getCollection());
    }
}


