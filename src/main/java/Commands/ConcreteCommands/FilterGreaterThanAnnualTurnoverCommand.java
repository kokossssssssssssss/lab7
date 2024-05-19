package Commands.ConcreteCommands;

import Commands.Command;
import Commands.CommandManager;

public class FilterGreaterThanAnnualTurnoverCommand implements Command {
    /**
     * A field that refers to an object with implementations of all commands
     */
    CommandManager commandManager;

    public FilterGreaterThanAnnualTurnoverCommand(CommandManager commandManager) {
        this.commandManager = commandManager;
    }
    /**
     * The command that calls the required method from {@link CommandManager}
     */
    @Override
    public void execute() {
        commandManager.filterTurnover();
    }
    /**
     * Method that returns command description
     * @return Command description
     */
    @Override
    public String description() {
        return "filter_greater_than_annual_turnover annualTurnover: вывести элементы, значение поля annualTurnover которых больше заданного";
    }
}
