package Commands.ConcreteCommands;

import Commands.Command;
import Commands.CommandManager;

public class SortCommand implements Command {
    /**
     * A field that refers to an object with implementations of all commands
     */
    CommandManager commandManager;

    public SortCommand(CommandManager commandManager) {
        this.commandManager = commandManager;
    }
    /**
     * Method that returns command description
     * @return Command description
     */
    @Override
    public String description() {
        return "sort: отсортировать коллекцию в естественном порядке";
    }
    /**
     * The command that calls the required method from {@link CommandManager}
     */
    @Override
    public void execute() {
        commandManager.sort();
    }
}
