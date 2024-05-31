package Commands.DBCommands;

import Commands.Command;
import Commands.CommandManager;
import DB.DBReceiver;

public class InsertAnIndexCommand implements Command {
    /**
     * A field that refers to an object with implementations of all commands
     */
    DBReceiver dbReceiver;

    public InsertAnIndexCommand(DBReceiver dbReceiver) {
        this.dbReceiver = dbReceiver;
    }

    /**
     * The command that calls the required method from {@link CommandManager}
     */
    @Override
    public void execute() {
        dbReceiver.insertAt();
    }
    /**
     * Method that returns command description
     * @return Command description
     */
    @Override
    public String description() {
        return "insert_at index {element}: добавить новый элемент в заданную позицию";
    }
}
