package Commands.DBCommands;

import Commands.Command;
import Commands.CommandManager;
import DB.DBReceiver;

public class RemoveByIdCommand implements Command {
    /**
     * A field that refers to an object with implementations of all commands
     */
    DBReceiver dbReceiver;

    public RemoveByIdCommand(DBReceiver dbReceiver) {
        this.dbReceiver = dbReceiver;
    }

    /**
     * The command that calls the required method from {@link CommandManager}
     */
    @Override
    public void execute() {
        dbReceiver.removeById();
    }
    /**
     * Method that returns command description
     * @return Command description
     */
    @Override
    public String description() {
        return "remove_by_id id: удалить элемент из коллекции по его id";
    }
}
