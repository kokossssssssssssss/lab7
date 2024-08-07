package commands.dbcommands;

import commands.Command;

import db.DBReceiver;

public class ShowCollectionCommand implements Command {
    /**
     * A field that refers to an object with implementations of all commands
     */
    DBReceiver dbReceiver;

    public ShowCollectionCommand(DBReceiver dbReceiver) {
        this.dbReceiver = dbReceiver;
    }

    /**
     * Method that returns command description
     *
     * @return Command description
     */
    @Override
    public String description() {
        return "show: вывести в стандартный поток вывода все элементы коллекции в строковом представлении";
    }

    /**
     * The command that calls the required method from {@link DBReceiver}
     */
    @Override
    public void execute() {
        dbReceiver.show();
    }
}
