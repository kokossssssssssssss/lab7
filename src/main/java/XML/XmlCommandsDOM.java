package XML;

import Organization.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;

/**
 * The class that is used to work with xml file,
 * including initial parsing and file rewriting while the program is running.
 */
public class XmlCommandsDOM {
    /**
     * Field for accessing an object containing in fields a collection with which the program works
     */
    OrganizationCollection collection;
    /**
     * Static field that specifies the path to the file that is set in this method{@link XmlCommandsDOM#toParse(String)}
     */
    private static String PATH = "";

    public XmlCommandsDOM(OrganizationCollection collection) {
        this.collection = collection;
    }

    /**
     * This method update xml file with working collection
     * by converting elements of collection to Element objects
     * and building document with this Element's
     *
     * @throws ParserConfigurationException This exception
     * @see XmlCommandsDOM#saveDocumentToFile(Document),
     * @see XmlCommandsDOM#toXmlElement(Organization, org.w3c.dom.Document)
     */
    public int toSaveToXML() throws ParserConfigurationException {
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document doc = docBuilder.newDocument();
        Element orgElement = doc.createElement("OrganizationCollection");
        for (Organization organization : collection.getCollection()) {
            orgElement.appendChild(XmlCommandsDOM.toXmlElement(organization, doc));
        }
        doc.appendChild(orgElement);
        try {
            if (saveDocumentToFile(doc) < 0) {
                return -1;
            }
        } catch (TransformerException e) {
            System.out.println("Ошибка трансформера" + '\n' + e.getLocalizedMessage());
        }
        return 1;
    }

    /**
     * This private method used in toSaveToXML method,
     * it updates xml file with built document
     *
     * @param doc The collected document is saved to a file
     * @throws TransformerException Transformer error
     * @see XmlCommandsDOM#toSaveToXML()
     */
    private int saveDocumentToFile(Document doc) throws TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        File file = new File(XmlCommandsDOM.PATH);
        if (!file.exists()) {
            return -1;
        }
        StreamResult result = new StreamResult(file);
        try {
            result.setWriter(new FileWriter((XmlCommandsDOM.PATH)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        transformer.transform(source, result);
        return 1;
    }

    /**
     * this private method used in toSaveToXML method,
     * it transforms organization elements to Element objects
     *
     * @param organization Organization type object from the collection
     * @param doc          Document type object to build the document with fields of organization
     * @return Organization represented as an Element
     * @see XmlCommandsDOM#toSaveToXML()
     */
    private static Element toXmlElement(Organization organization, Document doc) {
        Element OrganizationElement = doc.createElement("organization");
        OrganizationElement.appendChild(doc.createElement("id"));
        OrganizationElement.getChildNodes().item(0).setTextContent(String.valueOf(organization.getId()));
        OrganizationElement.appendChild(doc.createElement("name"));
        OrganizationElement.getChildNodes().item(1).setTextContent(String.valueOf(organization.getName()));
        OrganizationElement.appendChild(doc.createElement("coordinates"));
        OrganizationElement.getChildNodes().item(2).appendChild(doc.createElement("x"));
        OrganizationElement.getChildNodes().item(2).getChildNodes().item(0).setTextContent(String.valueOf(organization.getCoordinates().getX()));
        OrganizationElement.getChildNodes().item(2).appendChild(doc.createElement("y"));
        OrganizationElement.getChildNodes().item(2).getChildNodes().item(1).setTextContent(String.valueOf(organization.getCoordinates().getY()));
        OrganizationElement.appendChild(doc.createElement("creationDate"));
        OrganizationElement.getChildNodes().item(3).setTextContent(String.valueOf(organization.getCreationDate().getTime()));
        OrganizationElement.appendChild(doc.createElement("annualTurnover"));
        OrganizationElement.getChildNodes().item(4).setTextContent(String.valueOf(organization.getAnnualTurnover()));
        OrganizationElement.appendChild(doc.createElement("employeesCount"));
        OrganizationElement.getChildNodes().item(5).setTextContent(String.valueOf(organization.getEmployeesCount()));
        OrganizationElement.appendChild(doc.createElement("organizationType"));
        OrganizationElement.getChildNodes().item(6).setTextContent(organization.getType().name());
        OrganizationElement.appendChild(doc.createElement("postalAddress"));
        OrganizationElement.getChildNodes().item(7).setTextContent(organization.getPostalAddress().getZipCode());
        return OrganizationElement;
    }

    /**
     * This static method pars xml file and return LinkedList
     *
     * @return LinkedList that was parsed from XML file
     */
    public static LinkedList<Organization> toParse(String path) {
        PATH = path;
        File db = new File(PATH);
        if (!db.exists()) {
            System.out.println("File doesn't exists");
            return new LinkedList<>();
        }
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        Document document = null;
        LinkedList<Organization> organizations = new LinkedList<>();
        try {
            document = dbf.newDocumentBuilder().parse(db);
        } catch (SAXException e) {
            System.out.println("sax");
        } catch (IOException e) {
            System.out.println("File reading error");
            return new LinkedList<>();
        } catch (ParserConfigurationException e) {
            System.out.println("Parsing error");
            return new LinkedList<>();
        }
        try {
            Node mainNode = document.getFirstChild();
            NodeList firstList = mainNode.getChildNodes();
            for (int i1 = 0; i1 < firstList.getLength(); i1++) {
                if (firstList.item(i1).getNodeType() != Node.ELEMENT_NODE) {
                    continue;
                }
                NodeList secondList = firstList.item(i1).getChildNodes();
                long id = 0;
                String name = "";
                Coordinates coordinates = null;
                long creationDate = 0;
                long annualTurnover = 0;
                long employeesCount = 0;
                OrganizationType type = null;
                String postalAddress = "";
                for (int i2 = 0; i2 < secondList.getLength(); i2++) {
                    if (secondList.item(i2).getNodeType() != Node.ELEMENT_NODE) {
                        continue;
                    }
                    switch (secondList.item(i2).getNodeName()) {
                        case "id":
                            id = Long.parseLong(secondList.item(i2).getTextContent());
                            break;
                        case "name":
                            name = secondList.item(i2).getTextContent();
                            break;
                        case "coordinates":
                            int x = 0;
                            double y = 0d;
                            for (int coord = 0; coord < secondList.item(i2).getChildNodes().getLength(); coord++) {
                                if (secondList.item(i2).getChildNodes().item(coord).getNodeType() != Node.ELEMENT_NODE) {
                                    continue;
                                }
                                switch (secondList.item(i2).getChildNodes().item(coord).getNodeName()) {
                                    case "x":
                                        x = Integer.parseInt(secondList.item(i2).getChildNodes().item(coord).getTextContent());
                                    case "y":
                                        y = Double.parseDouble(secondList.item(i2).getChildNodes().item(coord).getTextContent());
                                }
                            }
                            coordinates = new Coordinates(x, y);
                            break;
                        case "creationDate":
                            creationDate = Long.parseLong(secondList.item(i2).getTextContent());
                            break;
                        case "annualTurnover":
                            annualTurnover = Long.parseLong(secondList.item(i2).getTextContent());
                            break;
                        case "employeesCount":
                            employeesCount = Long.parseLong(secondList.item(i2).getTextContent());
                            break;
                        case "organizationType":
                            type = OrganizationType.valueOf(secondList.item(i2).getTextContent());
                            break;
                        case "postalAddress":
                            postalAddress = secondList.item(i2).getTextContent();
                            break;
                    }
                }
                Organization result = new Organization();
                result.setId(id);
                result.setName(name);
                result.setCoordinates(coordinates);
                result.setCreationDate(new Date(creationDate));
                result.setAnnualTurnover(annualTurnover);
                result.setEmployeesCount(employeesCount);
                result.setType(type);
                result.setPostalAddress(new Address(postalAddress));
                organizations.add(result);
            }
        } catch (NullPointerException e) {
            System.out.println("Some field of the object is the null, xml parsing error");
            return new LinkedList<>();
        } catch (NumberFormatException e) {
            System.out.println(e.getMessage() + "\nParsing error");
        }
        return organizations;
    }
}
