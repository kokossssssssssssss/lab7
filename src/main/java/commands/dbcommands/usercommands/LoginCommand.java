package commands.dbcommands.usercommands;

import commands.Command;
import db.DBReceiver;
import exceptions.NoLoginException;

public class LoginCommand implements Command {
    DBReceiver dbReceiver;

    public LoginCommand(DBReceiver dbReceiver) {
        this.dbReceiver = dbReceiver;
    }

    @Override
    public void execute() {
        try {
            dbReceiver.authorization();
        } catch (NoLoginException e) {
        }
    }

    @Override
    public String description() {
        return "login: войти в профиль пользователя";
    }
}
