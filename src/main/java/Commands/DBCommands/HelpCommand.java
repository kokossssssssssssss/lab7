package Commands.DBCommands;

import Commands.Command;

import DB.DBReceiver;

public class HelpCommand implements Command {
    /**
     * A field that refers to an object with implementations of all commands
     */
    DBReceiver dbReceiver;

    public HelpCommand(DBReceiver dbReceiver) {
        this.dbReceiver = dbReceiver;
    }

    /**
     * The command that calls the required method from {@link CommandManager}
     */
    @Override
    public void execute() {
        dbReceiver.help();
    }
    /**
     * Method that returns command description
     * @return Command description
     */
    @Override
    public String description() {
        return "help: помощь";
    }
}
