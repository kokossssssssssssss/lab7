package Organization;

import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * A class with which objects of the Organization type are created using console input and using commands from a script.
 */
public class Filler {
    /**
     * Field, created to ensure the uniqueness of the created id
     */
    public static int idCount = 0;

    /**
     * A method that build the Organization object from console input using the method {@link Filler#toBuildOrganization(String[])}
     * @return new Organization object
     */
    public static Organization fill() {
        String[] output = new String[7];
        try {
            Scanner scanner = new Scanner(System.in);
            //name
            int count = 0;
            while (true) {
                if(count == 3){
                    System.out.println("Number of attempts exceeded");
                    System.exit(1);
                }
                System.out.println("Enter name");
                String name = scanner.nextLine();
                if (name == null || name.isEmpty()) {
                    System.out.println("Name can't be null");
                    count++;
                } else {
                    output[0] = name;
                    break;
                }
            }
            //coordinates
            count = 0;
            while (true) {
                if(count == 3){
                    System.out.println("Number of attempts exceeded");
                    System.exit(1);
                }
                try {
                    System.out.println("Enter x");
                    String request = scanner.nextLine();
                    int x = Integer.parseInt(request);
                    if (x == 0) {
                        System.out.println("x can't be 0");
                        count++;
                    } else {
                        output[1] = request;
                        break;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Coordinates must be numbers");
                    count++;

                }
            }
            count = 0;
            while (true) {
                if(count == 3){
                    System.out.println("Number of attempts exceeded");
                    System.exit(1);
                }
                try {
                    System.out.println("Enter y");
                    String request = scanner.nextLine();
                    double y = Double.parseDouble(request);
                    if (y < -841) {
                        System.out.println("y must be more than -841");
                        count++;
                    } else {
                        output[2] = request;
                        break;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Coordinates must be numbers");
                    count++;
                }
            }

            //annualTurnover
            count = 0;
            while (true) {
                if(count == 3){
                    System.out.println("Number of attempts exceeded");
                    System.exit(1);
                }
                try {
                    System.out.println("Enter annualTurnover");
                    String annualTurnover = scanner.nextLine();
                    if (Long.parseLong(annualTurnover) <= 0) {
                        System.out.println("AnnualTurnover must be more than 0");
                        count++;
                    } else {
                        output[3] = annualTurnover;
                        break;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("AnnualTurnover must be a number");
                    count++;
                }
            }

            //employeesCount
            count = 0;
            while (true) {
                if(count == 3){
                    System.out.println("Number of attempts exceeded");
                    System.exit(1);
                }
                try {
                    System.out.println("Enter employeesCount");
                    String employeesCount = scanner.nextLine();
                    if (Long.parseLong(employeesCount) < 0) {
                        System.out.println("EmployeesCount must be more than 0");
                        count++;
                    } else {
                        output[4] = employeesCount;
                        break;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("AnnualTurnover must be a number");
                    count++;
                }
            }

            //type
            count = 0;
            boolean check = true;
            while (check) {
                if(count == 3){
                    System.out.println("Number of attempts exceeded");
                    System.exit(1);
                }
                System.out.println("Choose type of organization: ");
                System.out.println("1.COMMERCIAL");
                System.out.println("2.GOVERNMENT");
                System.out.println("3.TRUST");
                System.out.println("4.OPEN_JOINT_STOCK_COMPANY");
                String typeName = scanner.nextLine();
                for (OrganizationType type : OrganizationType.values()) {
                    if (typeName.equalsIgnoreCase(type.name)) {
                        output[5] = typeName;
                        check = false;
                        break;
                    }
                }
                if (check) {
                    System.out.println("You entered the wrong name");
                    count++;
                }
            }

            count = 0;
            while (true) {
                if(count == 3){
                    System.out.println("Number of attempts exceeded");
                    System.exit(1);
                }
                System.out.println("Enter address");
                String adr = scanner.nextLine();
                if (adr.isEmpty()) {
                    System.out.println("Address can't be 0");
                    count++;
                } else {
                    output[6] = adr;
                    break;
                }
            }
        }
        catch (NoSuchElementException e){
            System.out.println("No line found, exit the program");
            System.exit(1);
        }
        return toBuildOrganization(output);

    }
    /**
     * This method built new {@link Organization} object with data.
     * @param commands
     * @return
     */
    public static Organization toBuildOrganization(String[] commands){
            Organization organization = new Organization();
            organization.setId(new Date().getTime() + idCount);
            organization.setName(commands[0]);
            organization.setCoordinates(new Coordinates(Integer.parseInt(commands[1]), Float.parseFloat(commands[2])));
            organization.setCreationDate(new Date());
            organization.setAnnualTurnover(Long.parseLong(commands[3]));
            organization.setEmployeesCount(Long.parseLong(commands[4]));
            organization.setType(OrganizationType.valueOf(commands[5].toUpperCase()));
            organization.setPostalAddress(new Address(commands[6]));
            return organization;
    }
}

