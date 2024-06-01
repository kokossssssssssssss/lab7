package DB;

import Commands.Command;

import java.util.HashMap;
import java.util.Map;

public class DBInvoker {
    Map<String, Command> commandMap= new HashMap<>();

    public DBInvoker(Command add, Command show, Command removeById, Command help, Command clear, Command filterTurnover, Command update, Command exit, Command insertAt, Command sort, Command info, Command register, Command printAscending, Command removeByType, Command removeLower) {
        commandMap.put("add", add);
        commandMap.put("show", show);
        commandMap.put("remove_by_id", removeById);
        commandMap.put("help", help);
        commandMap.put("clear", clear);
        commandMap.put("filter_greater_than_annual_turnover", filterTurnover);
        commandMap.put("update", update);
        commandMap.put("exit", exit);
        commandMap.put("insert_at", insertAt);
        commandMap.put("sort", sort);
        commandMap.put("info", info);
        commandMap.put("register", register);
        commandMap.put("remove_lower", removeLower);
        commandMap.put("remove_any_by_type", removeByType);
        commandMap.put("print_ascending", printAscending);
    }

    public Map<String, Command> getCommandMap() {
        return commandMap;
    }

    public void executeCommand(String commandName){
        Command command = commandMap.get(commandName);
        command.execute();
    }
}
