package DB;

import Commands.Command;
import Commands.DBCommands.AddCommand;
import Commands.DBCommands.HelpCommand;
import Commands.DBCommands.RemoveByIdCommand;
import Commands.DBCommands.ShowCollectionCommand;
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
                new HelpCommand(dbReceiver)
        );
        dbReceiver.setCommands(dbInvoker.getCommandMap());

        while(true){
            System.out.println("Enter the command: ");

            Scanner scanner = new Scanner(System.in);
            String request = scanner.nextLine();
            String[] tokens = request.split(" ");
            dbReceiver.setTokens(tokens);

            Command command = dbInvoker.commandMap.get(tokens[0]);
            command.execute();
        }
    }
}
