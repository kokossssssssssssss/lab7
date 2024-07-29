package organization;

public enum OrganizationType {
    COMMERCIAL("commercial"),
    GOVERNMENT("government"),
    TRUST("trust"),
    OPEN_JOINT_STOCK_COMPANY("open_joint_stock_company");
    public String name;

    OrganizationType(String name) {
        this.name = name;
    }

}
