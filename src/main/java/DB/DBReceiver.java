package DB;

import Commands.Command;
import Organization.Organization;
import Organization.OrganizationCollection;
import Organization.UpdaterOfCollection;

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


    public void show(){
        for(Organization organization: organizationCollection.getCollection()){
            System.out.println(organization);
        }
    }

    public void removeById() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your name: ");
        String name = scanner.nextLine();
        String correctName;
        System.out.println("Enter password: ");
        String password = scanner.nextLine();
        int id = Integer.parseInt(tokens[1]);
        String queryUser = "select \"user_name\" from organizations.\"Organization\" where id = " + id;
        try {
            Statement statement = new DBWorker().getConnection().createStatement();
            ResultSet rs = statement.executeQuery(queryUser);
            if(rs.next()){
                correctName = rs.getString(1);
            }else{
                return;
            }
        } catch (SQLException e) {
            System.out.println("Can't define the creator of this organization...");
            return;
        }
        if (DBUserChecker.checkUser(name, password)) {
            if (name.equals(correctName)) {
                String query = "DELETE  FROM organizations.\"Organization\" WHERE id = " + id;
                try {
                    Statement statement = new DBWorker().getConnection().createStatement();
                    statement.execute(query);
                    UpdaterOfCollection.updateCollection(organizationCollection);
                } catch (SQLException e) {
                    System.out.println("Failed to remove organization...");
                }
            }else{
                System.out.println("You haven't access to this organization...");
            }
        }
    }


    public void help(){
        for(Command command: commands.values()){
            System.out.println(command.description());
        }
    }

    public void clear(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your name: ");
        String name = scanner.nextLine();
        System.out.println("Enter password: ");
        String password = scanner.nextLine();
        if (DBUserChecker.checkUser(name, password)) {
            String queryOrgs = "DELETE FROM organizations.\"Organization\" where \"user_name\" = '" + name + "'";
            try{
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

    public void filterTurnover(){
        int annualTurnover = Integer.parseInt(tokens[1]);

        Iterator<Organization> iterator = organizationCollection.getCollection().iterator();
        while(iterator.hasNext()){
            Organization organization = iterator.next();
            if(organization.getAnnualTurnover() > annualTurnover){
                System.out.println(organization);
            }
        }
    }

    public void update(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your name: ");
        String name = scanner.nextLine();
        String correctName;
        System.out.println("Enter password: ");
        String password = scanner.nextLine();
        int id = Integer.parseInt(tokens[1]);
        String queryUser = "select \"user_name\" from organizations.\"Organization\" where id = " + id;
        try {
            Statement statement = new DBWorker().getConnection().createStatement();
            ResultSet rs = statement.executeQuery(queryUser);
            if(rs.next()){
                correctName = rs.getString(1);
            }else{
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
            }else{
                System.out.println("You haven't access to this organization...");
            }
        }

    }

    public void exit(){
        System.out.println("Program exit...");
        System.exit(0);
    }

    public void insertAt() {
        int index = Integer.parseInt(tokens[1]);

        try {
            DBFiller.fill();
        } catch (SQLException e) {
            System.out.println("Error by creating organization...");
        }
        UpdaterOfCollection.updateCollection(organizationCollection);
        Organization maxIdOrganization = organizationCollection.getCollection().stream()
                .max(Comparator.comparingLong(Organization::getId))
                .orElseThrow(NoSuchElementException::new);

        // Проверяем, что индекс в допустимом диапазоне
        if (index >= 0 && index <= organizationCollection.getCollection().size()) {
            // Вставляем элемент по индексу
            organizationCollection.getCollection().add(index, maxIdOrganization);

            // Удаляем последний элемент, если коллекция стала больше нужного размера
            if (organizationCollection.getCollection().size() > index + 1) {
                organizationCollection.getCollection().remove(organizationCollection.getCollection().size() - 1);
            }
        } else {
            System.out.println("Invalid index");
        }
    }

    public void sort(){
        LinkedList<Organization> toSort = new LinkedList<>(organizationCollection.getCollection());
        Collections.sort(toSort);
        organizationCollection.setList(toSort);
    }

    public void info(){
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
            String query = "INSERT INTO organizations.\"User\" (name, password) VALUES ('" + name + "', crypt('" + password1 + "', ('sha224')));";

            try {
                Connection connection = new DBWorker().getConnection();
                Statement statement = connection.createStatement();
                statement.execute(query);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }else{
            System.out.println("Entered passwords don't match...");
        }
    }

}


