import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.w3c.dom.Document;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/**
 * Bank side of the ATM program.
 * Used for accessing or creating bank accounts
 * From the technical part. It's mostly responsible for operating XML file
 */

public class BankAccount {
    private static final String FILENAME = "src/accounts.xml";
    private static Document doc;

    static class Account{
        String firstname;
        String lastname;
        String bankID;
        String passwd;
        String balance;
    }

    private void accountList() throws ParserConfigurationException, IOException, SAXException {

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(new File(FILENAME));

        //System.out.println("Root Element: " + doc.getDocumentElement().getNodeName());

        NodeList list = doc.getElementsByTagName("account");
        for (int temp = 0; temp < list.getLength(); temp++) {
            Node node = list.item(temp);
            Element element = (Element) node;

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                String id = element.getAttribute("id");
                String firstname = element.getElementsByTagName("firstname").item(0).getTextContent();
                String lastname = element.getElementsByTagName("firstname").item(0).getTextContent();
                String bankID = element.getElementsByTagName("firstname").item(0).getTextContent();
                String pin = element.getElementsByTagName("pin").item(0).getTextContent();
                String balance = element.getElementsByTagName("balance").item(0).getTextContent();

                System.out.println("Element: " + node.getNodeName());
                System.out.println("ID: " + id);
                System.out.println("First Name: " + firstname);
                System.out.println("Last Name: " + lastname);
                System.out.println("Bank ID: " + bankID);
                System.out.println("PIN: " + pin);
                System.out.println("Balance: " + balance);
                System.out.println(" ");
            }
        }
    }
    private static void saveChanges(Account account) throws TransformerException {
        NodeList list = doc.getElementsByTagName("account");
        for (int temp = 0; temp < list.getLength(); temp++) {
            Node node = list.item(temp);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                String tempID = element.getElementsByTagName("bankID").item(0).getTextContent();

                if (account.bankID.equals(tempID)) {
                    Node pin = element.getElementsByTagName("pin").item(0).getFirstChild();
                    pin.setNodeValue(account.passwd);

                    Node m = element.getElementsByTagName("balance").item(0).getFirstChild();
                    m.setNodeValue(account.balance);
                }
            }
        }
        writeToXML();
    }
    private static void writeToXML() throws TransformerException {
        doc.getDocumentElement().normalize();
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult result= new StreamResult(new File(FILENAME));
//        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
//        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
//        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        transformer.transform(source, result);
//        System.out.println("Data updated successfully!");
    }

    private void readAccounts() throws IOException, SAXException, ParserConfigurationException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        doc = db.parse(new File(FILENAME));
    }
    private void makeNewAccount(Account account) throws TransformerException {
        Element root=doc.getDocumentElement();

        Element el=doc.createElement("account");

        Node firstname = doc.createElement("firstname");
        firstname.appendChild(doc.createTextNode(account.firstname));
        Node lastname = doc.createElement("lastname");
        lastname.appendChild(doc.createTextNode(account.lastname));
        Node bankID = doc.createElement("bankID");
        bankID.appendChild(doc.createTextNode(account.bankID));
        Node pin = doc.createElement("pin");
        pin.appendChild(doc.createTextNode(account.passwd));
        Node balance = doc.createElement("balance");
        balance.appendChild(doc.createTextNode("0"));


        el.appendChild(doc.createTextNode("\n \t"));
        el.appendChild(firstname);
        el.appendChild(doc.createTextNode("\n \t"));
        el.appendChild(lastname);
        el.appendChild(doc.createTextNode("\n \t"));
        el.appendChild(bankID);
        el.appendChild(doc.createTextNode("\n \t"));
        el.appendChild(pin);
        el.appendChild(doc.createTextNode("\n \t"));
        el.appendChild(balance);
        el.appendChild(doc.createTextNode("\n  "));

        root.appendChild(doc.createTextNode("  "));
        root.appendChild(el);
        root.appendChild(doc.createTextNode("\n"));
        writeToXML();
    }

    private boolean containsAccount(String id,Account account,String passwd) {
        NodeList list = doc.getElementsByTagName("account");
        for (int temp = 0; temp < list.getLength(); temp++) {
            Node node = list.item(temp);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                String tempID = element.getElementsByTagName("bankID").item(0).getTextContent();

                if (id.equals(tempID)) {
                    String tempPIN = element.getElementsByTagName("pin").item(0).getTextContent();
                    if (tempPIN.equals(passwd)) {
                        System.out.println("Logged in");
                        account.bankID = tempID;
                        account.passwd = tempPIN;
                        account.firstname = element.getElementsByTagName("firstname").item(0).getTextContent();
                        account.lastname = element.getElementsByTagName("lastname").item(0).getTextContent();
                        account.balance = element.getElementsByTagName("balance").item(0).getTextContent();

                        return true;
                    }
                }
            }
        }
        return false;
    }
    protected static boolean containsAccount(String id,Account account) {
        NodeList list = doc.getElementsByTagName("account");
        for (int temp = 0; temp < list.getLength(); temp++) {
            Node node = list.item(temp);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                String tempID = element.getElementsByTagName("bankID").item(0).getTextContent();

                if (id.equals(tempID)) {
                    account.bankID = tempID;
                    account.passwd = element.getElementsByTagName("pin").item(0).getTextContent();
                    account.firstname = element.getElementsByTagName("firstname").item(0).getTextContent();
                    account.lastname = element.getElementsByTagName("lastname").item(0).getTextContent();
                    account.balance = element.getElementsByTagName("balance").item(0).getTextContent();
                    return true;
                    }
            }
        }
        return false;
    }

    private static boolean containsAccount(String id) {
        NodeList list = doc.getElementsByTagName("account");
        for (int temp = 0; temp < list.getLength(); temp++) {
            Node node = list.item(temp);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                String tempID = element.getElementsByTagName("bankID").item(0).getTextContent();
                if(tempID.equals(id)){
                    return true;
                }
            }
        }
        return false;
    }

    public static void BankTransfer(Account from, Account receiver, int value) throws TransformerException {
        from.balance = String.valueOf(Integer.parseInt(from.balance) - value);
        receiver.balance = String.valueOf(Integer.parseInt(receiver.balance) + value);
        System.out.println("Operation completed!");
        saveChanges(receiver);
        saveChanges(from);
    }


    public void bankMenu() throws IOException, ParserConfigurationException, SAXException, TransformerException {
        Scanner sc = new Scanner(System.in);
        System.out.println("1 -- Sign up");
        System.out.println("2 -- Sign in");
        int temp = sc.nextInt();
        readAccounts();
        switch (temp){
            case 1:
            {
                Account newAccount = new Account();
                System.out.println("First name: ");
                newAccount.firstname = sc.next();
                System.out.println("Last name: ");
                newAccount.lastname = sc.next();
                String bankID;
                do {
                    System.out.println("Bank ID: ");
                    bankID = sc.next();
                }while (bankID.length()!=4 || containsAccount(bankID));
                newAccount.bankID=bankID;
                String pin;
                do {
                    System.out.println("Pin: ");
                    pin = sc.next();
                }while (pin.length()!=4);
                newAccount.passwd=pin;
                makeNewAccount(newAccount);
                bankMenu();

            }
            case 2:
            {
                System.out.println("-------------------------------------");
                System.out.println("Account ID: ");
                String ID = sc.next();
                System.out.println("PIN: ");
                String passwd = sc.next();
                Account account = new Account();
                boolean login = true;
                if (containsAccount(ID,account,passwd)){
                    while (login)
                    {
                    login = ATM.menu(account);
                    }
                }
                else{
                    System.out.println("-------------------------------------");
                    System.out.println("Incorrect ID or PIN!");
                    System.out.println("-------------------------------------");
                    bankMenu();
                }
                saveChanges(account);
                System.out.println("-------------------------------------");
                System.out.println("Data updated successfully!");
                System.out.println("-------------------------------------");
                System.out.println("1 -- Exit program \n2 -- Change account");
                int ex;
                do {
                    ex = sc.nextInt();
                }while (!(ex>=1 && ex <=2));
                if(ex == 1){
                    break;
                }
                else{
                    System.out.println("-------------------------------------");
                    bankMenu();
                    break;
                }
            }
        }
    }
}
