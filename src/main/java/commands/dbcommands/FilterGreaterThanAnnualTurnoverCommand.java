package commands.dbcommands;

import commands.Command;

import db.DBReceiver;

public class FilterGreaterThanAnnualTurnoverCommand implements Command {
    /**
     * A field that refers to an object with implementations of all commands
     */
    DBReceiver dbReceiver;

    public FilterGreaterThanAnnualTurnoverCommand(DBReceiver dbReceiver) {
        this.dbReceiver = dbReceiver;
    }

    /**
     * The command that calls the required method from {@link DBReceiver}
     */
    @Override
    public void execute() {
        dbReceiver.filterTurnover();
    }

    /**
     * Method that returns command description
     *
     * @return Command description
     */
    @Override
    public String description() {
        return "filter_greater_than_annual_turnover annualTurnover: вывести элементы, значение поля annualTurnover которых больше заданного";
    }
}
