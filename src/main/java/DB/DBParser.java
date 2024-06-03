package DB;

import Organization.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class DBParser {
    private static final Logger logger = Logger.getLogger(DBParser.class.getName());

    public static LinkedList<Organization> getOrganizationsFromDB() {
        LinkedList<Organization> organizations = new LinkedList<>();

        String query = "SELECT * FROM s409333.\"Organization\"";
        try (Connection connection = new DBWorker().getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                try {
                    Organization organization = new Organization();
                    organization.setId(resultSet.getLong(1));
                    organization.setName(resultSet.getString(2));

                    Coordinates coordinates = getCoordinatesFromDB(connection, resultSet.getInt(3));
                    if (coordinates != null) {
                        organization.setCoordinates(coordinates);
                    } else {
                        logger.log(Level.SEVERE, "Failed to retrieve coordinates for organization ID: " + resultSet.getLong("id"));
                        continue;
                    }

                    organization.setCreationDate(resultSet.getDate(4));
                    organization.setAnnualTurnover(resultSet.getLong(5));
                    organization.setEmployeesCount(resultSet.getLong(6));

                    String typeStr = resultSet.getString(7);
                    try {
                        organization.setType(OrganizationType.valueOf(typeStr));
                    } catch (IllegalArgumentException e) {
                        logger.log(Level.SEVERE, "Invalid organization type for organization ID: " + resultSet.getLong("id"), e);
                        continue;
                    }

                    Address address = getAddressFromDB(connection, resultSet.getInt(8));
                    if (address != null) {
                        organization.setPostalAddress(address);
                    } else {
                        logger.log(Level.SEVERE, "Failed to retrieve address for organization ID: " + resultSet.getLong("id"));
                        continue;
                    }

                    organizations.add(organization);
                } catch (SQLException e) {
                    logger.log(Level.SEVERE, "Error parsing organization with ID: " + resultSet.getLong("id"), e);
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Something went wrong with parsing Organization ...", e);
        }
        return organizations;
    }

    public static Coordinates getCoordinatesFromDB(Connection connection, int coordId) {
        String queryCoord = "SELECT * FROM s409333.\"Coordinates\" WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(queryCoord)) {
            statement.setInt(1, coordId);
            try (ResultSet resultSetCoord = statement.executeQuery()) {
                if (resultSetCoord.next()) {
                    int x = resultSetCoord.getInt("x");
                    int y = resultSetCoord.getInt("y");
                    return new Coordinates(x, y);
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Something went wrong with parsing Coordinates ...", e);
        }
        return null;
    }

    public static Address getAddressFromDB(Connection connection, int addressId) {
        String queryAddress = "SELECT * FROM s409333.\"Address\" WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(queryAddress)) {
            statement.setInt(1, addressId);
            try (ResultSet resultSetAddress = statement.executeQuery()) {
                if (resultSetAddress.next()) {
                    String zipCode = resultSetAddress.getString(2);
                    return new Address(zipCode);
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Something went wrong with parsing Address ...", e);
        }
        return null;
    }
}