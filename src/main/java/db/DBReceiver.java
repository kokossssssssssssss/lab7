package db;

import commands.Command;
import exceptions.NoLoginException;
import exceptions.NullValueException;
import organization.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.postgresql.util.PSQLException;

import java.io.File;
import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatterBuilder;
import java.util.*;
import java.util.logging.Logger;


public class DBReceiver {
    private static final Logger logger = Logger.getLogger(DBReceiver.class.getName());
    private OrganizationCollection organizationCollection;
    private String[] tokens;
    private Map<String, Command> commands;
    private Set<String> scriptHistory = new HashSet<>();
    private String[] compositeCommand = new String[9];
    private boolean isScriptWorking = false;
    private String name;
    private String password;

    public DBReceiver(OrganizationCollection organizationCollection) {
        this.organizationCollection = organizationCollection;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public void setTokens(String[] tokens) {
        this.tokens = tokens;
    }

    public void setCommands(Map<String, Command> commands) {
        this.commands = commands;
    }

    public void clearCompositeCommand() {
        compositeCommand = new String[9];
    }

    public void setCompositeCommand(String[] compositeCommand) {
        this.compositeCommand = compositeCommand;
    }

    public String[] getCompositeCommand() {
        return compositeCommand;
    }

    public boolean isScriptWorking() {
        return isScriptWorking;
    }

    public void add() {
        try {
            DBFiller.fill(this);
            UpdaterOfCollection.updateCollection(organizationCollection);
        } catch (SQLException e) {
            System.out.println("Failed to add organization...");
        }
    }

    public void show() {
        System.out.println("Текущий пользователь: " + name);
        System.out.println("Текущая дата: " + LocalDate.now());
        for (Organization organization : organizationCollection.getCollection()) {
            System.out.print(organization);
        }
    }

    public void removeById() {
        int id = 0;
        if (isScriptWorking) {
            id = Integer.parseInt(compositeCommand[0]);
            compositeCommand = Arrays.copyOfRange(compositeCommand, 1, compositeCommand.length);
        } else {
            id = Integer.parseInt(tokens[1]);
        }
        String correctName;
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
        int annualTurnover = 0;
        if (isScriptWorking) {
            annualTurnover = Integer.parseInt(compositeCommand[0]);
        } else {
            annualTurnover = Integer.parseInt(tokens[1]);
        }
        Iterator<Organization> iterator = organizationCollection.getCollection().iterator();
        while (iterator.hasNext()) {
            Organization organization = iterator.next();
            if (organization.getAnnualTurnover() > annualTurnover) {
                System.out.println(organization);
            }
        }
    }

    public void update() {
        int id;
        if (isScriptWorking) {
            id = Integer.parseInt(compositeCommand[0]);
            compositeCommand = Arrays.copyOfRange(compositeCommand, 1, compositeCommand.length);
        } else {
            id = Integer.parseInt(tokens[1]);
        }
        String correctName;
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
                    DBFiller.fillForUpdate(this, id);
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
        int index;
        if (isScriptWorking) {
            index = Integer.parseInt(compositeCommand[0]);
            compositeCommand = Arrays.copyOfRange(compositeCommand, 1, compositeCommand.length);
        } else {
            index = Integer.parseInt(tokens[1]);
        }
        if (!(index >= 0 && index <= organizationCollection.getCollection().size())) {
            System.out.println("Invalid index...");
            return;
        }
        try {
            DBFiller.fill(this);
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
        String[] data;
        try{
           data = registration();
        }
        catch (NullValueException e){
            return;
        }
        String name = data[0];
        String password1 = data[1];
        String password2 = data[2];
        if (password1.equals(password2)) {
            String hash_password = DigestUtils.sha3_224Hex(password2);
            String query = "INSERT INTO s409333.\"User\" (name, password) VALUES ('" + name + "', '" + hash_password + "');";
            try {
                Connection connection = new DBWorker().getConnection();
                Statement statement = connection.createStatement();
                statement.execute(query);
                this.name = name;
                this.password = password1;
                DBUser.setLogged(true);
                System.out.println("Новый пользователь успешно авторизован");
            } catch (PSQLException e) {
                System.out.println("Такое имя пользователя уже есть");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            System.out.println("Введенные пароли не совпадают...");
        }
    }

    public void removeLower() {
        long id;
        try {
            if (isScriptWorking) {
                id = Long.parseLong(compositeCommand[0]);
                compositeCommand = Arrays.copyOfRange(compositeCommand, 1, compositeCommand.length);
            } else {
                id = Long.parseLong(tokens[1]);
            }
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
            if (isScriptWorking) {
                organizationType = compositeCommand[0];
                compositeCommand = Arrays.copyOfRange(compositeCommand, 1, compositeCommand.length);
            } else {
                organizationType = tokens[1];
            }
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
            String correctName = "";
            for (int i = 0; i < organization.size(); i++) {
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
                    if (correctName.equals(name)) {
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

    public void executeScript() {
        try {
            File script;
            if (isScriptWorking) {
                script = new File(compositeCommand[0]);
            } else {
                script = new File(tokens[1]);
            }
            if (!script.exists()) {
                System.out.println("Specified file is not exist");
                return;
            }
            if (scriptHistory.contains(compositeCommand[0])) {
                System.out.println("This script has already been executed");
                return;
            }
            if (isScriptWorking) {
                scriptHistory.add(compositeCommand[0]);
            } else {
                scriptHistory.add(tokens[1]);
            }
            isScriptWorking = true;
            ScriptManager scriptManager = new ScriptManager(script, commands, this);
            System.out.println("Script is executing");
            if (isScriptWorking) {
                clearCompositeCommand();
            }
            scriptManager.executeScript();
            isScriptWorking = false;
            System.out.println("Script executed successfully");
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Please, enter the file name in the command");
        }
    }

    public void authorization() throws NoLoginException {
        if (isScriptWorking) {
            String name = compositeCommand[0];
            String password = compositeCommand[1];
            boolean isLogged = DBUserChecker.checkUser(name, password);
            if(!isLogged){
                throw new NoLoginException();
            }
            DBUser.setLogged(true);
            this.name = name;
            this.password = password;
            compositeCommand = Arrays.copyOfRange(compositeCommand, 2, compositeCommand.length);
        } else {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter your name: ");
            String name = scanner.nextLine();
            System.out.println("Enter password: ");
            String password = scanner.nextLine();
            boolean isLogged = DBUserChecker.checkUser(name, password);
            if(!isLogged){
                throw new NoLoginException();
            }
            DBUser.setLogged(true);
            this.name = name;
            this.password = password;
        }
        }


    public String[] registration() throws NullValueException{
        String[] data = new String[3];
        if (isScriptWorking) {
            data[0] = compositeCommand[0];
            data[1] = compositeCommand[1];
            data[2] = compositeCommand[2];
        } else {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter your name: ");
            String name = scanner.nextLine();
            if(name.trim().isEmpty()){
                System.out.println("Empty login");
                throw new NullValueException();
            }
            System.out.println("Enter password: ");
            String password = scanner.nextLine();
            if(password.trim().isEmpty()){
                System.out.println("Empty password");
                throw new NullValueException();
            }
            System.out.println("Repeat password: ");
            String password2 = scanner.nextLine();
            if(password2.trim().isEmpty()){
                System.out.println("Empty password");
                throw new NullValueException();
            }
            data[0] = name;
            data[1] = password;
            data[2] = password2;
        }
        return data;
    }
    public void logout(){
        DBUser.setLogged(false);
        System.out.println("Вы вышли из профиля");
    }
}


