package Commands.ConcreteCommands;

import Commands.Command;
import Commands.CommandManager;

public class UpdateCommand implements Command {
    /**
     * A field that refers to an object with implementations of all commands
     */
    CommandManager commandManager;

    public UpdateCommand(CommandManager commandManager) {
        this.commandManager = commandManager;
    }
    /**
     * The command that calls the required method from {@link CommandManager}
     */
    @Override
    public void execute() {
        commandManager.update();
    }
    /**
     * Method that returns command description
     * @return Command description
     */
    @Override
    public String description() {
        return "update id {element}: обновить значение элемента коллекции, id которого равен заданному";
    }
}
