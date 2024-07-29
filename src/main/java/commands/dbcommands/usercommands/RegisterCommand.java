package commands.dbcommands.usercommands;

import commands.Command;
import db.DBReceiver;

public class RegisterCommand implements Command {

    DBReceiver dbReceiver;

    public RegisterCommand(DBReceiver dbReceiver) {
        this.dbReceiver = dbReceiver;
    }

    @Override
    public void execute() {
        dbReceiver.register();
    }

    @Override
    public String description() {
        return "register: зарегистрировать нового пользователя";
    }
}
