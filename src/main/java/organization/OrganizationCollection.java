package organization;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class OrganizationCollection {
    private List<Organization> list;
    private Date initializationDate;
    private String collectionType;
    private String internalFileType;
    private int amountOfElements;
    private int lastIndexWorkedWith;
    private long lastIdWorkedWith;
    private long lastAnnualTurnoverWorkedWith;
    private OrganizationType lastOrganizationTypeWorkedWith;

    public OrganizationCollection(LinkedList<Organization> list) {
        this.list = list;
        collectionType = list.getClass().getSimpleName();
        internalFileType = "organization";
        amountOfElements = list.size();
        initializationDate = new Date();
    }

    public List<Organization> getCollection() {
        return list;
    }

    public void setList(List<Organization> list) {
        this.list = list;
        updateData();
    }

    public void updateData() {
        amountOfElements = list.size();
        initializationDate = new Date();
    }

    @Override
    public String toString() {
        return "\n1. Initialization date: " + initializationDate +
                "\n2. Collection type: " + collectionType +
                "\n3. Internal files type: " + internalFileType +
                "\n4. Amount of elements: " + amountOfElements;
    }


    public OrganizationType getLastOrganizationTypeWorkedWith() {
        return lastOrganizationTypeWorkedWith;
    }

    public void setLastOrganizationTypeWorkedWith(OrganizationType lastOrganizationTypeWorkedWith) {
        this.lastOrganizationTypeWorkedWith = lastOrganizationTypeWorkedWith;
    }

    public int getAmountOfElements() {
        return amountOfElements;
    }
}
