package Commands.DBCommands;

import Commands.Command;

import DB.DBReceiver;

public class UpdateCommand implements Command {
    /**
     * A field that refers to an object with implementations of all commands
     */
    DBReceiver dbReceiver;

    public UpdateCommand(DBReceiver dbReceiver) {
        this.dbReceiver = dbReceiver;
    }

    /**
     * The command that calls the required method from {@link CommandManager}
     */
    @Override
    public void execute() {
        dbReceiver.update();
    }
    /**
     * Method that returns command description
     * @return Command description
     */
    @Override
    public String description() {
        return "update id {element}: обновить значение элемента коллекции, id которого равен заданному";
    }
}
