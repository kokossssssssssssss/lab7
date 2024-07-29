package commands.dbcommands;

import commands.Command;
import db.DBReceiver;

public class RemoveAnyByTypeCommand implements Command {
    DBReceiver dbReceiver;

    public RemoveAnyByTypeCommand(DBReceiver dbReceiver) {
        this.dbReceiver = dbReceiver;
    }

    @Override
    public void execute() {
        dbReceiver.removeByType();
    }

    @Override
    public String description() {
        return "remove_any_by_type type : удалить из коллекции один элемент, значение поля type которого эквивалентно заданному";
    }
}
