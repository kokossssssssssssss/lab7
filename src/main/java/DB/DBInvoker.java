package DB;

import Commands.Command;

import java.util.HashMap;
import java.util.Map;

public class DBInvoker {
    Map<String, Command> commandMap= new HashMap<>();

    public DBInvoker(Command add, Command show, Command removeById, Command help) {
        commandMap.put("add", add);
        commandMap.put("show", show);
        commandMap.put("remove_by_id", removeById);
        commandMap.put("help", help);
    }

    public Map<String, Command> getCommandMap() {
        return commandMap;
    }

    public void executeCommand(String commandNmae){
        Command command = commandMap.get(commandNmae);
        if(command!=null){
            command.execute();;
        }else{
            System.out.println("Unknown command...");
        }

    }
}
