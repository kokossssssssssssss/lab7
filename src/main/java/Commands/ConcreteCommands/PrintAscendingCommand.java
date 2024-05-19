package Commands.ConcreteCommands;

import Commands.Command;
import Commands.CommandManager;

/**
 *
 */
public class PrintAscendingCommand implements Command {
    /**
     * A field that refers to an object with implementations of all commands
     */
    CommandManager commandManager;

    public PrintAscendingCommand(CommandManager commandManager) {
        this.commandManager = commandManager;
    }

    /**
     * The command that calls the required method from {@link CommandManager}
     */
    @Override
    public void execute() {
        commandManager.printAscending();
    }
    /**
     * Method that returns command description
     * @return Command description
     */
    @Override
    public String description() {
        return "print_ascending: вывести элементы коллекции в порядке возрастания";
    }
}
