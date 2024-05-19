package Commands;

import Organization.Filler;
import Organization.*;
import XML.XmlCommandsDOM;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.util.*;

/**
 * A class that stores implementations of all program commands.
 * It can work with commands both from the console and from a script,
 * responsible for this {@link CommandManager#isScriptWorking}
 */
public class CommandManager {
    /**
     * A field that refers to an object whose fields contain the collection with which the program works.
     */
    private Set<String> scriptHistory = new HashSet<>();
    private OrganizationCollection collection;
    /**
     * A field that refers to an object whose methods work with the xml file.
     */
    private XmlCommandsDOM xmlCommands;
    /**
     * A field that refers to a HashMap with keys - command names and the possibility of calling them
     */
    private Map<String, Command> commands;
    /**
     * An array of strings into which an array of commands from the script is passed{@link ScriptManager#executeScript()}
     */
    private String[] compositeCommand = new String[9];
    /**
     * A field that can be used to change the implementation of commands for working with a script
     *
     * @see CommandManager#executeScript()
     */
    private boolean isScriptWorking = false;
    /**
     * String array to which commands are sent from the console
     */
    private String[] tokens;

    public CommandManager(OrganizationCollection collection, XmlCommandsDOM xmlCommands, Map<String, Command> commands) {
        this.collection = collection;
        this.xmlCommands = xmlCommands;
        this.commands = commands;
    }

    public void setTokens(String[] tokens) {
        this.tokens = tokens;
    }

    /**
     * The method resets the variable{@link CommandManager#compositeCommand}
     */
    public void clearCompositeCommand() {
        compositeCommand = new String[9];
    }

    public void setCompositeCommand(String[] compositeCommand) {
        this.compositeCommand = compositeCommand;
    }

    /**
     * Command to call up descriptions of all commands{@link Commands.ConcreteCommands.HelpCommand}
     */
    public void help() {
        for (Command com : commands.values()) {
            System.out.println(com.description());
        }
    }

    /**
     * Command that saves the current collection instance to a file{@link Commands.ConcreteCommands.SaveToXmlCommand}
     *
     * @see XmlCommandsDOM
     */
    public void save() {
        try {
            if (xmlCommands.toSaveToXML() < 0) {
                System.out.println("Specified file is not exist");
                return;
            }
        } catch (ParserConfigurationException e) {
            System.out.println("Ошибка сохранения " + e.getMessage());
        }
        System.out.println("Saved successfully");
    }

    /**
     * Command to exit the application {@link Commands.ConcreteCommands.ExitCommand}
     */
    public void exit() {
        System.out.println("Session ended");
        System.exit(1);
    }

    /**
     * Command to clear a collection {@link Commands.ConcreteCommands.ClearCollectionCommand}
     */
    public void clear() {
        collection.getCollection().clear();
        collection.updateData();
        System.out.println("Collection cleared");
    }

    /**
     * A command that prints to the console all objects in a collection and their fields {@link Commands.ConcreteCommands.ShowCollectionCommand}
     */
    public void show() {
        System.out.println("The collection: ");
        System.out.println(collection.getCollection());
    }

    /**
     * A command that adds a new object to the collection, created with {@link Filler} class methods
     * {@link Commands.ConcreteCommands.AddCommand}
     */
    public void add() {
        if (isScriptWorking) {
            collection.getCollection().add(Filler.toBuildOrganization(compositeCommand));
            clearCompositeCommand();
        } else {
            collection.getCollection().add(Filler.fill());
        }
        collection.updateData();
        System.out.println("Organization successfully added");
    }

    /**
     * Command showing information about the current state of a collection {@link Commands.ConcreteCommands.InfoCommand}
     *
     * @see OrganizationCollection
     */
    public void info() {
        System.out.print("Collection information:");
        System.out.println(collection);
    }

    /**
     * A command that allows you to insert a new object at a given index in the collection {@link Commands.ConcreteCommands.InsertAnIndexCommand}
     */
    public void insertAt() {
        try {
            int index;
            if (isScriptWorking) {
                index = Integer.parseInt(compositeCommand[0]);
                compositeCommand = Arrays.copyOfRange(compositeCommand, 1, compositeCommand.length);
            } else {
                index = Integer.parseInt(tokens[1]);
            }
            if (index < 0) {
                throw new ArithmeticException();
            }
            if (collection.setLastIndexWorkedWith(index) < 0) {
                System.out.println("There is no index " + index + " in the collection");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Index must be integer");
            return;
        } catch (ArithmeticException e) {
            System.out.println("Index cannot be negative");
            return;
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Please, enter the index in the command");
            return;
        }
        if (isScriptWorking) {
            collection.getCollection().add(collection.getLastIndexWorkedWith(), Filler.toBuildOrganization(compositeCommand));
            clearCompositeCommand();
        } else {
            collection.getCollection().add(collection.getLastIndexWorkedWith(), Filler.fill());
        }
        collection.updateData();
        System.out.println("Added to index: " + collection.getLastIndexWorkedWith() + " successfully");
    }

    /**
     * A command that allows you to update an object with a given id {@link Commands.ConcreteCommands.UpdateCommand}
     */
    public void update() {
        try {
            long id;
            if (isScriptWorking) {
                id = Long.parseLong(compositeCommand[0]);
                compositeCommand = Arrays.copyOfRange(compositeCommand, 1, compositeCommand.length);
            } else {
                id = Long.parseLong(tokens[1]);
            }
            if (id < 0) {
                throw new ArithmeticException();
            }
            if (collection.setLastIdWorkedWith(id) < 0) {
                System.out.println("Organization with id " + id + " is not exist");
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
        Organization org = collection.getCollection().stream().filter(o -> o.getId() == collection.getLastIdWorkedWith()).findFirst().get();
        collection.setLastIndexWorkedWith(collection.getCollection().indexOf(org));
        collection.getCollection().remove(org);
        if (isScriptWorking) {
            collection.getCollection().add(collection.getLastIndexWorkedWith(), Filler.toBuildOrganization(compositeCommand));
            clearCompositeCommand();
        } else {
            collection.getCollection().add(collection.getLastIndexWorkedWith(), Filler.fill());
        }
        collection.getCollection().get(collection.getLastIndexWorkedWith()).setId(collection.getLastIdWorkedWith());
        System.out.println("Organization with ID: " + collection.getLastIdWorkedWith() + " successfully updated");
    }

    /**
     * A command that creates an object of the {@link ScriptManager} class and starts the script process
     *
     * @see Commands.ConcreteCommands.ExecuteScriptCommand
     */
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
            }
            else {
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

    /**
     * A command that allows you to remove elements smaller than the one specified by id {@link Commands.ConcreteCommands.RemoveLowerCommand}
     */
    public void removeLover() {
        try {
            long id;
            if (isScriptWorking) {
                id = Long.parseLong(compositeCommand[0]);
                clearCompositeCommand();
            } else {
                id = Long.parseLong(tokens[1]);
            }
            if (id < 0) {
                throw new ArithmeticException();
            }
            if (collection.setLastIdWorkedWith(id) < 0) {
                System.out.println("Organization with id " + id + " is not exist");
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
        Organization organization = collection.getCollection().stream().filter(o -> o.getId() == collection.getLastIdWorkedWith()).findFirst().get();
        List<Organization> list = collection.getCollection().stream().filter(o -> o.getAnnualTurnover() + o.getEmployeesCount()
                >= organization.getAnnualTurnover() + organization.getEmployeesCount()).toList();
        if (list.size() == collection.getCollection().size()) {
            System.out.println("No one organization was deleted");
            return;
        }
        System.out.println(collection.getCollection().size() - list.size() + " organizations smaller than the specified one were successfully deleted");
        collection.setList(list);
    }

    /**
     * A command that allows you to delete an object by its id {@link Commands.ConcreteCommands.RemoveByIdCommand}
     */
    public void removeById() {
        try {
            long id;
            if (isScriptWorking) {
                id = Long.parseLong(compositeCommand[0]);
                clearCompositeCommand();
            } else {
                id = Long.parseLong(tokens[1]);
            }
            if (id < 0) {
                throw new ArithmeticException();
            }
            if (collection.setLastIdWorkedWith(id) < 0) {
                System.out.println("Organization with id " + id + " is not exist");
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
        collection.setList(collection.getCollection().stream().filter(o -> o.getId() != collection.getLastIdWorkedWith()).toList());
        System.out.println("Organization with id: " + collection.getLastIdWorkedWith() + " was successfully removed");
    }

    /**
     * A command that sorts a collection in the standard way {@link Commands.ConcreteCommands.SortCommand}
     *
     * @see Organization#compareTo(Organization)
     */
    public void sort() {
        LinkedList<Organization> toSort = new LinkedList<>(collection.getCollection());
        Collections.sort(toSort);
        collection.setList(toSort);
    }

    /**
     * A command that removes items from a collection that have less than a specified annual turnover{@link Commands.ConcreteCommands.FilterGreaterThanAnnualTurnoverCommand}
     */
    public void filterTurnover() {
        try {
            long annualTurnover;
            if (isScriptWorking) {
                annualTurnover = Long.parseLong(compositeCommand[0]);
                clearCompositeCommand();
            } else {
                annualTurnover = Long.parseLong(tokens[1]);
            }
            collection.setLastAnnualTurnoverWorkedWith(annualTurnover);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Please, enter the annual turnover the command");
            return;
        } catch (NumberFormatException e) {
            System.out.println("Annual Turnover must be Integer");
            return;
        }
        List<Organization> list = collection.getCollection().stream().filter(o -> o.getAnnualTurnover() > collection.getLastAnnualTurnoverWorkedWith()).toList();
        if (!list.isEmpty()) {
            System.out.println(list);
        } else {
            System.out.println("There aren't such organizations");
        }
    }

    /**
     * A command that removes one object of a given type from a collection{@link Commands.ConcreteCommands.RemoveAnyByTypeCommand}
     */
    public void removeByType() {
        try {
            String organizationType;
            if (isScriptWorking) {
                organizationType = compositeCommand[0];
                clearCompositeCommand();
            } else {
                organizationType = tokens[1];
            }
            int count = 0;
            for (OrganizationType orgT : OrganizationType.values()) {
                if (organizationType.equalsIgnoreCase(orgT.name)) {
                    collection.setLastOrganizationTypeWorkedWith(orgT);
                    break;
                }
                count++;
            }
            if (count == OrganizationType.values().length - 1) {
                System.out.println("There isn't such type");
                return;
            }
            int amount = collection.getAmountOfElements();
            Optional<Organization> organization = collection.getCollection().stream().filter(o -> o.getType() == collection.getLastOrganizationTypeWorkedWith()).findAny();
            if (organization.isEmpty()) {
                System.out.println("There isn't any organization with type: " + collection.getLastOrganizationTypeWorkedWith().name);
            } else {
                collection.getCollection().remove(organization.get());
                collection.updateData();
            }
            if (amount != collection.getAmountOfElements()) {
                System.out.println("One organization with type: \"" + collection.getLastOrganizationTypeWorkedWith().name + "\" was successfully deleted");
            }
        } catch (NoSuchElementException e) {
            System.out.println("Type not entered");
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Please, enter the type in the command");
        }
    }

    /**
     * A command that displays a list of objects in a collection in ascending order {@link Commands.ConcreteCommands.PrintAscendingCommand}
     */
    public void printAscending() {
        sort();
        System.out.println(collection.getCollection());
    }
}





