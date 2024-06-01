package Commands.DBCommands;

import Commands.Command;

import DB.DBReceiver;

public class InfoCommand implements Command {
    /**
     * A field that refers to an object with implementations of all commands
     */
    DBReceiver dbReceiver;

    public InfoCommand(DBReceiver dbReceiver) {
        this.dbReceiver = dbReceiver;
    }

    /**
     * The command that calls the required method from {@link CommandManager}
     */
    @Override
    public void execute() {
        dbReceiver.info();
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
