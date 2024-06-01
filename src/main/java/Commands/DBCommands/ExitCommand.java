package Commands.DBCommands;

import Commands.Command;

import DB.DBReceiver;

public class ExitCommand implements Command {
    /**
     * A field that refers to an object with implementations of all commands
     */
    DBReceiver dbReceiver;

    public ExitCommand(DBReceiver dbReceiver) {
        this.dbReceiver = dbReceiver;
    }

    /**
     * The command that calls the required method from {@link CommandManager}
     */
    @Override
    public void execute() {
        dbReceiver.exit();
    }
    /**
     * Method that returns command description
     * @return Command description
     */
    @Override
    public String description() {
        return "exit: завершить программу (без сохранения в файл)";
    }
}
