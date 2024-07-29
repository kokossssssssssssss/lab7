package organization;

public class Address {
    private String zipCode; //Поле не может быть null

    public Address(String zipCode) {
        this.zipCode = zipCode;
    }

    /**
     * @return zipCode
     */
    public String getZipCode() {
        return zipCode;
    }

    @Override
    public String toString() {
        return zipCode;
    }
}
