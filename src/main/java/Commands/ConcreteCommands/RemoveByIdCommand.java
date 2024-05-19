package Commands.ConcreteCommands;

import Commands.Command;
import Commands.CommandManager;

public class RemoveByIdCommand implements Command {
    /**
     * A field that refers to an object with implementations of all commands
     */
    CommandManager commandManager;

    public RemoveByIdCommand(CommandManager commandManager) {
        this.commandManager = commandManager;
    }
    /**
     * The command that calls the required method from {@link CommandManager}
     */
    @Override
    public void execute() {
        commandManager.removeById();
    }
    /**
     * Method that returns command description
     * @return Command description
     */
    @Override
    public String description() {
        return "remove_by_id id: удалить элемент из коллекции по его id";
    }
}
