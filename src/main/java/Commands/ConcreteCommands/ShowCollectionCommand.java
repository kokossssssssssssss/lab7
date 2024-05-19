package Commands.ConcreteCommands;

import Commands.Command;
import Commands.CommandManager;

public class ShowCollectionCommand implements Command {
    /**
     * A field that refers to an object with implementations of all commands
     */
    CommandManager commandManager;

    public ShowCollectionCommand(CommandManager commandManager) {
        this.commandManager = commandManager;
    }
    /**
     * Method that returns command description
     * @return Command description
     */
    @Override
    public String description() {
        return "show: вывести в стандартный поток вывода все элементы коллекции в строковом представлении";
    }
    /**
     * The command that calls the required method from {@link CommandManager}
     */
    @Override
    public void execute() {
        commandManager.show();
    }
}
