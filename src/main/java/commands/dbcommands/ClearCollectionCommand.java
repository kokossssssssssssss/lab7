package commands.dbcommands;

import commands.Command;

import db.DBReceiver;

public class ClearCollectionCommand implements Command {
    /**
     * A field that refers to an object with implementations of all commands
     */

    DBReceiver dbReceiver;

    public ClearCollectionCommand(DBReceiver dbReceiver) {
        this.dbReceiver = dbReceiver;
    }

    /**
     * The command that calls the required method from {@link DBReceiver}
     */
    @Override
    public void execute() {
        dbReceiver.clear();
    }

    /**
     * Method that returns command description
     *
     * @return Command description
     */
    @Override
    public String description() {
        return "clear: очистить коллекцию";
    }
}
