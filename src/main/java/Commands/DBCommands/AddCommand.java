package Commands.DBCommands;

import Commands.Command;

import DB.DBReceiver;

public class AddCommand implements Command {
    /**
     * A field that refers to an object with implementations of all commands
     */
    DBReceiver dbReceiver;

    public AddCommand(DBReceiver dbReceiver) {
        this.dbReceiver = dbReceiver;
    }

    /**
     * The command that calls the required method from {@link CommandManager}
     */
    @Override
    public void execute() {
        dbReceiver.add();
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
