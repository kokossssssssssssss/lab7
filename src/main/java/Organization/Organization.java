package Organization;

import java.util.Date;

/**
 * The class whose objects the command works with
 */
public class Organization implements Comparable<Organization> {
    private long id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private Date creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private long annualTurnover; //Значение поля должно быть больше 0
    private long employeesCount; //Значение поля должно быть больше 0
    private OrganizationType type; //Поле не может быть null
    private Address postalAddress; //Поле не может быть null

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAnnualTurnover(long annualTurnover) {
        this.annualTurnover = annualTurnover;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public void setType(OrganizationType type) {
        this.type = type;
    }

    public void setPostalAddress(Address postalAddress) {
        this.postalAddress = postalAddress;
    }

    public void setEmployeesCount(long employeesCount) {
        this.employeesCount = employeesCount;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public long getAnnualTurnover() {
        return annualTurnover;
    }

    public long getEmployeesCount() {
        return employeesCount;
    }

    public OrganizationType getType() {
        return type;
    }

    public Address getPostalAddress() {
        return postalAddress;
    }

    @Override
    public String toString() {
        return "\nOrganization:\n" +
                "id: " + id +
                ", name: " + name +
                ", coordinates: " + coordinates +
                ", creationDate: " + creationDate +
                ", annualTurnover: " + annualTurnover +
                ", employeesCount: " + employeesCount +
                ", type: " + type +
                ", postalAddress: " + postalAddress +
                '}';
    }

    /**
     * this method allows to compare organizations
     *
     * @param or the object to be compared.
     * @return
     */
    @Override
    public int compareTo(Organization or) {
        if (id == or.getId()) {
            return 0;
        }
        if (id < or.getId()) {
            return -1;
        }
        return 1;
    }
}
