package organization;

import db.DBParser;

import java.util.List;

public class UpdaterOfCollection {
    public static void updateCollection(OrganizationCollection organizationCollection) {
        List<Organization> organizations = DBParser.getOrganizationsFromDB();
        organizationCollection.setList(organizations);
    }
}
