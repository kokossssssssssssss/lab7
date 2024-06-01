package DB;

import Commands.Command;
import Commands.DBCommands.*;
import Commands.DBCommands.UserCommands.RegisterCommand;
import Organization.OrganizationCollection;

import java.util.Scanner;

public class DBUser {
    public static void toStart(){
        OrganizationCollection organizationCollection = new OrganizationCollection(DBParser.getOrganizationsFromDB());
        DBReceiver dbReceiver = new DBReceiver(organizationCollection);
        DBInvoker dbInvoker = new DBInvoker(
                new AddCommand(dbReceiver),
                new ShowCollectionCommand(dbReceiver),
                new RemoveByIdCommand(dbReceiver),
                new HelpCommand(dbReceiver),
                new ClearCollectionCommand(dbReceiver),
                new FilterGreaterThanAnnualTurnoverCommand(dbReceiver),
                new UpdateCommand(dbReceiver),
                new ExitCommand(dbReceiver),
                new InsertAnIndexCommand(dbReceiver),
                new SortCommand(dbReceiver),
                new InfoCommand(dbReceiver),
                new RegisterCommand(dbReceiver),
                new PrintAscendingCommand(dbReceiver),
                new RemoveAnyByTypeCommand(dbReceiver),
                new RemoveLowerCommand(dbReceiver)
        );
        dbReceiver.setCommands(dbInvoker.getCommandMap());

        while(true){
            System.out.println("Enter the command: ");

            Scanner scanner = new Scanner(System.in);
            String request = scanner.nextLine();
            String[] tokens = request.split(" ");
            dbReceiver.setTokens(tokens);

            Command command = dbInvoker.commandMap.get(tokens[0]);
            try{
                command.execute();
            } catch (Exception e) {
                System.out.println("Unknown command...");
            }

        }
    }
}
