import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;

/**
 * ATM - Bank application
 * ATM module responsible for account operations
 * Bank module responsible for creating account or logging to account
 * @author krystiancichorz
 */

public class Main {
    public static void main(String[] args) throws IOException, ParserConfigurationException, TransformerException, SAXException {
        BankAccount bank = new BankAccount();
        bank.bankMenu();
    }
}