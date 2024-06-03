package Organization;

public class CheckerOfOrganization {
    static OrganizationCollection organizationCollection;

    public CheckerOfOrganization(OrganizationCollection organizationCollection) {
        this.organizationCollection = organizationCollection;
    }

    public static boolean checkById(int id) {
        boolean containsId = false;
        for (Organization org : organizationCollection.getCollection()) {
            if (org.getId() == id) {
                containsId = true;
                break;
            }
        }
        if (!containsId) {
            System.out.println("There isn't organization with such id...");
        }
        return containsId;
    }
}
