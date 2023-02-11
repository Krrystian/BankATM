import javax.xml.transform.TransformerException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


public class ATM {
    private static final String FILENAME = "src/properties";

    public static HashMap<String, String> typeCastConvert(Properties prop) {
        Map<String, String> result = (Map<String, String>) (Map) prop;
        return new HashMap<>(result);
    }
    public Properties loadProperties() throws IOException {
        FileReader fr = new FileReader(FILENAME);
        Properties prop = new Properties();
        prop.load(fr);
        return prop;
    }
    private void atmNameChange(Properties p,String name) throws IOException {
        p.setProperty("atmName",name);
        FileOutputStream fos = new FileOutputStream(FILENAME);
        p.store(fos, null);
        fos.close();
    }
    private void transferLimit(Properties p, int num) throws IOException {
        p.setProperty("transferLimit",num+"");
        FileOutputStream fos = new FileOutputStream(FILENAME);
        p.store(fos, null);
        fos.close();

    }
    private void currency(Properties p,String currency) throws IOException {
        p.setProperty("currency",currency);
        switch (currency.toLowerCase()) {
            case "euro":
                p.setProperty("currencyShortcut", "€");
                break;
            case "zloty":
                p.setProperty("currencyShortcut", "PLN");
                break;
            case "dollar":
                p.setProperty("currencyShortcut", "$");
                break;
        }

        FileOutputStream fos = new FileOutputStream(FILENAME);
        p.store(fos, null);
    }

    public static boolean menu(BankAccount.Account account) throws IOException, TransformerException {
        ATM atm = new ATM();
        Properties p = atm.loadProperties();
        HashMap<String, String> prop = typeCastConvert(p);
        Scanner sc = new Scanner(System.in);
        int operation;
        boolean check = false;

        //System.out.println(prop);
        System.out.println("Welcome to "+prop.get("atmName")+ " ATM");
        System.out.println("What do you want to do:");
        System.out.println("0 -- Bank transfer");
        System.out.println("1 -- Withdraw");
        System.out.println("2 -- Deposit");
        System.out.println("3 -- Check Balance");
        System.out.println("4 -- Change your PIN");
        System.out.println("5 -- Settings");
        System.out.println("6 -- Log out");
        System.out.print("Your option: ");
        do {
            try {
                operation = sc.nextInt();
                check = (operation>=0 && operation<=6);
                if(!check) {
                    System.out.println("Choose right number!");
                    System.out.print("Your option: ");
                    sc.nextLine();
                }
            }catch (InputMismatchException ex) {
                System.out.println("Number! Not word!");
                operation = -1;
                sc.nextLine();
                System.out.print("Your option: ");}
        }while(!check);
        switch(operation){
            case 0:{
                System.out.println( "Transfer receiver ID: ");
                String transferID = sc.next();
                BankAccount.Account tempAccount = new BankAccount.Account();
                if (BankAccount.containsAccount(transferID,tempAccount)){
                    int value=0;
                    int tryCounter=0;
                    System.out.println("How much money would you like to transfer? ");
                    do {
                        tryCounter++;
                        if(tryCounter >1) System.out.println("Try Again: ");
                        value = sc.nextInt();
                    }while (value<0 || value > Integer.parseInt(prop.get("transferLimit")) || value > Integer.parseInt(account.balance) || tryCounter == 5);
                    if(tryCounter==5){
                        System.out.println("Too many tries.");}
                    else{
                        BankAccount.BankTransfer(account,tempAccount,value);}
                }
                else {
                    System.out.println("Wrong ID!");
                }

                break;
            }
            case 1:{
                System.out.println("How much you want to withdraw: ");
                int tempWD = sc.nextInt();
                System.out.println("Processing...");
                if(Integer.parseInt(account.balance)>tempWD && tempWD <= Integer.parseInt(prop.get("transferLimit"))){
                    account.balance = String.valueOf(Integer.parseInt(account.balance) - tempWD);
                    System.out.println("Completed!");
                }
                else
                    System.out.println("Operation is impossible!");
                break;
            }
            case 2:{
                System.out.println("How much you want to deposit: ");
                int tempDP = sc.nextInt();
                System.out.println("Processing...");
                if(tempDP <= Integer.parseInt(prop.get("transferLimit"))){
                    account.balance = String.valueOf(Integer.parseInt(account.balance) + tempDP);
                    System.out.println("Completed!");}
                else {
                    System.out.println("Operation is impossible!");
                }
                break;
            }
            case 3:{
                System.out.println(account.firstname +" "+ account.lastname +" your balance is: "+ account.balance);
                break;
            }
            case 4:{
                System.out.println("Your new PIN [4 characters]: ");
                String tempPIN = sc.next();
                System.out.println("Processing...");
                if(tempPIN.length()==4){
                    account.passwd = tempPIN;
                    System.out.println("Completed!");
                }
                else
                    System.out.println("Operation is impossible!");
                break;
            }
            case 5:{
                int option;
                System.out.println("Options");
                System.out.println("1 -- Change ATM name");
                System.out.println("2 -- Change transfer limit");
                System.out.println("3 -- Change currency");
                System.out.println("4 -- Back");

                option = sc.nextInt();
                if(option == 1){
                    System.out.println("New ATM name: ");
                    String ATMname = sc.next();
                    atm.atmNameChange(p,ATMname);
                }
                if(option == 2){
                    System.out.println("New transfer limit: ");
                    int transferlimit = sc.nextInt();
                    atm.transferLimit(p,transferlimit);
                }
                if(option == 3){
                    System.out.println("New currency [euro,dollar,zloty]: ");
                    String currency = sc.next();
                    atm.currency(p,currency);
                }
                break;
            }
            case 6:
            {
                return false;
            }

        }
        return true;
    }
}
