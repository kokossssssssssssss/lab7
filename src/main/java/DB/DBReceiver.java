package DB;

import Commands.Command;
import Organization.Organization;
import Organization.OrganizationCollection;
import Organization.UpdaterOfCollection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.logging.Logger;



public class DBReceiver {
    private static final Logger logger = Logger.getLogger(DBReceiver.class.getName());


    private OrganizationCollection organizationCollection;
    private String[] tokens;
    private Map<String, Command> commands;

    public DBReceiver(OrganizationCollection organizationCollection) {
        this.organizationCollection = organizationCollection;
    }

    public void setTokens(String[] tokens) {
        this.tokens = tokens;
    }

    public void setCommands(Map<String, Command> commands) {
        this.commands = commands;
    }

    public void add(){
        try{
            DBFiller.fill();
            UpdaterOfCollection.updateCollection(organizationCollection);
        } catch (SQLException e) {
            System.out.println("Failed to add organization...");
        }
    }

    public void show(){
        for(Organization organization: organizationCollection.getCollection()){
            System.out.println(organization);
        }
    }

    public void removeById(){
        int id = Integer.parseInt(tokens[1]);

        String query = "DELETE  FROM organizations.\"Organization\" WHERE id = " + id;
        try {
            Statement statement = new DBWorker().getConnection().createStatement();
            statement.execute(query);
            UpdaterOfCollection.updateCollection(organizationCollection);
        } catch (SQLException e) {
            System.out.println("Failed to remove organization...");
        }
    }


    public void help(){
        for(Command command: commands.values()){
            System.out.println(command.description());
        }
    }

}
