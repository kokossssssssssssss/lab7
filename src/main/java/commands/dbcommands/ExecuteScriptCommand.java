package commands.dbcommands;

import commands.Command;
import db.DBReceiver;

public class ExecuteScriptCommand implements Command {
    DBReceiver dbReceiver;

    public ExecuteScriptCommand(DBReceiver dbReceiver) {
        this.dbReceiver = dbReceiver;
    }

    @Override
    public void execute() {
        dbReceiver.executeScript();
    }

    @Override
    public String description() {
        return "execute_script file_name : считать и исполнить скрипт из указанного файла";
    }
}
