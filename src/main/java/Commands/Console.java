package Commands;

import Commands.ConcreteCommands.*;
import Organization.*;
import XML.XmlCommandsDOM;

import java.util.*;

/**
 * Class in which all objects necessary for work are created and the program starts working
 */
public class Console {
    /**
     * A field that refers to a HashMap with keys - command names and the possibility of calling them
     */
    Map<String, Command> commands = new HashMap<>();

    /**
     * The starting point of the program. A method in which a database file is specified, commands are specified, and commands are entered.
     */
    public void toStart() {
        String path;
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the xml file path");
        try {
            path = scanner.next();
        }
        catch (NoSuchElementException e){
            System.out.println("No line, exit from the program");
            return;
        }
        if (path.isEmpty()) {
            System.out.println("You didn't indicated the xml file");
            return;
        }
        OrganizationCollection collection = new OrganizationCollection(XmlCommandsDOM.toParse(path));
        XmlCommandsDOM xml = new XmlCommandsDOM(collection);
        CommandManager commandManager = new CommandManager(collection, xml, commands);
        commands.put("help", new HelpCommand(commandManager));
        commands.put("save", new SaveToXmlCommand(commandManager));
        commands.put("clear", new ClearCollectionCommand(commandManager));
        commands.put("show", new ShowCollectionCommand(commandManager));
        commands.put("exit", new ExitCommand(commandManager));
        commands.put("add", new AddCommand(commandManager));
        commands.put("info", new InfoCommand(commandManager));
        commands.put("insert_at", new InsertAnIndexCommand(commandManager));
        commands.put("update", new UpdateCommand(commandManager));
        commands.put("execute_script", new ExecuteScriptCommand(commandManager));
        commands.put("remove_lower", new RemoveLowerCommand(commandManager));
        commands.put("remove_by_id", new RemoveByIdCommand(commandManager));
        commands.put("sort", new SortCommand(commandManager));
        commands.put("filter_greater_than_annual_turnover", new FilterGreaterThanAnnualTurnoverCommand(commandManager));
        commands.put("remove_any_by_type", new RemoveAnyByTypeCommand(commandManager));
        commands.put("print_ascending", new PrintAscendingCommand(commandManager));
        System.out.println("Print the command");
        int count  = 0;
        while (true) {
            if(count==3){
                System.out.println("Number of attempts exceeded");
                System.exit(1);
            }

            String request = null;
            try{
            Scanner scan = new Scanner(System.in);
            request = scan.nextLine().toLowerCase();
            }
            catch (NoSuchElementException e){
                System.out.println("No line, exit from the program");
                break;
            }
            String[] tokens = request.split(" ");
            if (tokens.length == 0) {
                tokens = request.split("\n");
            }
            commandManager.setTokens(tokens);
            Command command = commands.get(tokens[0]);
            try {
                command.execute();
                count=0;
            } catch (NullPointerException e) {
                System.out.println("The entered command does not exist");
                count++;
            }
        }
    }
}