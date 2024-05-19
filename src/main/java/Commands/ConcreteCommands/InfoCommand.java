package Commands.ConcreteCommands;

import Commands.Command;
import Commands.CommandManager;

public class InfoCommand implements Command {
    /**
     * A field that refers to an object with implementations of all commands
     */
    CommandManager commandManager;

    public InfoCommand(CommandManager commandManager) {
        this.commandManager = commandManager;
    }
    /**
     * The command that calls the required method from {@link CommandManager}
     */
    @Override
    public void execute() {
        commandManager.info();
    }
    /**
     * Method that returns command description
     * @return Command description
     */
    @Override
    public String description() {
        return "info: вывести в стандартный поток вывода информацию о коллекции";
    }
}
