package Commands.DBCommands;

import Commands.Command;
import DB.DBReceiver;

public class PrintAscendingCommand implements Command {
    DBReceiver dbReceiver;

    public PrintAscendingCommand(DBReceiver dbReceiver) {
        this.dbReceiver = dbReceiver;
    }

    @Override
    public void execute() {
        dbReceiver.printAscending();
    }

    @Override
    public String description() {
        return "print_ascending : вывести элементы коллекции в порядке возрастания";
    }
}
