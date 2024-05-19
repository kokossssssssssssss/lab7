package Commands.ConcreteCommands;

import Commands.Command;
import Commands.CommandManager;

public class AddCommand implements Command {
    /**
     * A field that refers to an object with implementations of all commands
     */
    CommandManager commandManager;

    public AddCommand(CommandManager commandManager) {
        this.commandManager = commandManager;
    }


    /**
     * The command that calls the required method from {@link CommandManager}
     */
    @Override
    public void execute() {
        commandManager.add();
    }
    /**
     * Method that returns command description
     * @return Command description
     */
    @Override
    public String description() {
        return "add {element}: добавить новый элемент в коллекцию";
    }
}
