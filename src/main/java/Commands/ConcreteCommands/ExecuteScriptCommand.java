package Commands.ConcreteCommands;

import Commands.Command;
import Commands.CommandManager;

public class ExecuteScriptCommand implements Command {
    /**
     * A field that refers to an object with implementations of all commands
     */
    CommandManager commandManager;

    public ExecuteScriptCommand(CommandManager commandManager) {
        this.commandManager = commandManager;
    }
    /**
     * The command that calls the required method from {@link CommandManager}
     */
    @Override
    public void execute() {
        commandManager.executeScript();
    }
    /**
     * Method that returns command description
     * @return Command description
     */
    @Override
    public String description() {
        return "execute_script file_name: считать и исполнить скрипт из указанного файла";
    }
}
