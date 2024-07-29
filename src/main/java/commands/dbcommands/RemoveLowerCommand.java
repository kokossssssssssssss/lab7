package commands.dbcommands;

import commands.Command;
import db.DBReceiver;

public class RemoveLowerCommand implements Command {
    DBReceiver dbReceiver;

    public RemoveLowerCommand(DBReceiver dbReceiver) {
        this.dbReceiver = dbReceiver;
    }

    @Override
    public void execute() {
        dbReceiver.removeLower();
    }

    @Override
    public String description() {
        return "remove_lower {element} : удалить из коллекции все элементы, меньшие, чем заданный";
    }
}
