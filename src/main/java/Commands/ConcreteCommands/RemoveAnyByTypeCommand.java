package Commands.ConcreteCommands;

import Commands.Command;
import Commands.CommandManager;

public class RemoveAnyByTypeCommand implements Command {
    /**
     * A field that refers to an object with implementations of all commands
     */
    CommandManager commandManager;

    public RemoveAnyByTypeCommand(CommandManager commandManager) {
        this.commandManager = commandManager;
    }

    /**
     * Method that returns command description
     * @return Command description
     */
    @Override
    public String description() {
        return "remove_any_by_type type: удалить из коллекции один элемент, значение поля type которого эквивалентно заданному";
    }
    /**
     * The command that calls the required method from {@link CommandManager}
     */
    @Override
    public void execute() {
        commandManager.removeByType();
    }
}
