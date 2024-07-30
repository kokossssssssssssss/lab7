package commands.dbcommands.usercommands;

import commands.Command;
import db.DBReceiver;

public class LogoutCommand implements Command {
    DBReceiver dbReceiver;

    public LogoutCommand(DBReceiver dbReceiver) {
        this.dbReceiver = dbReceiver;
    }

    @Override
    public void execute() {
        dbReceiver.logout();
    }

    @Override
    public String description() {
        return "logout: выйти из профиля пользователя";
    }
}
