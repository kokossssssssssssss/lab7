package Commands.ConcreteCommands;

import Commands.Command;
import Commands.CommandManager;

public class RemoveLowerCommand implements Command {
    /**
     * A field that refers to an object with implementations of all commands
     */
    CommandManager commandManager;

    public RemoveLowerCommand(CommandManager commandManager) {
        this.commandManager = commandManager;
    }
    /**
     * The command that calls the required method from {@link CommandManager}
     */
    @Override
    public void execute() {
        commandManager.removeLover();
    }
    /**
     * Method that returns command description
     * @return Command description
     */
    @Override
    public String description() {
        return "remove_lower {element}: удалить из коллекции все элементы, меньшие, чем заданный";
    }
}
