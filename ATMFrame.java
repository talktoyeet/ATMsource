import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ATMFrame extends JFrame implements ActionListener {
    // Static variable to hold the single instance of ATMFrame
    private static ATMFrame instance;

    // Initiate components
    static final ATMTextField textField = new ATMTextField();
    static KeypadPanel keypadPanel;
    static final ATMMessageField messageField = new ATMMessageField();

    // Initiate variables
    private boolean userAuthenticated; // whether user is authenticated
    private int currentAccountNumber; // current user's account number
    private static Screen screen; // ATM's screen
    private final Keypad keypad; // ATM's keypad
    private final CashDispenser cashDispenser; // ATM's cash dispenser
    private final BankDatabase bankDatabase; // account information database
    private Transaction temp = null;

    // constants corresponding to main menu options
    private static final int BALANCE_INQUIRY = 1;
    private static final int WITHDRAWAL = 2;
    private static final int TRANSFER = 3;
    private static final int EXIT = 4;

    private ATMFrame() {
        keypadPanel = new KeypadPanel(this);
        // Set Frame dimension
        this.setSize(500, 600);
        this.setSize(1000, 1000); // Increased size for more space
        this.setLayout(null); // Use null layout
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Set bounds for components
        messageField.setBounds(50, 50, 900, 300); // Large enough for approximately 15 lines
        textField.setBounds(50, 360, 900, 50); // Positioned below the message field
        keypadPanel.setBounds(50, 420, 900, 500); // Positioned below the text field

        // Add components
        this.add(messageField);
        this.add(textField);
        this.add(keypadPanel);

        // Set visible
        this.setVisible(true);

        //
        userAuthenticated = false; // user is not authenticated to start
        currentAccountNumber = 0; // no current account number to start
        screen = new Screen(); // create screen
        keypad = new Keypad(); // create keypad
        cashDispenser = new CashDispenser(); // create cash dispenser
        bankDatabase = new BankDatabase(); // create acct info database
        GlobalState.ATMState = "login"; // Change ui state to login
    }

    // start ATM
    public static void run() {
        screen.displayMessageLine("\nWelcome!");
        screen.displayMessage("\nPlease enter your account number: ");
    }

    private int accountNumber = 0;
    private boolean isPinInput = false;

    private void authenticateUser(int input)
    {
        if (!isPinInput) {
            // Expecting account number input
            accountNumber = input; // Set the account number from input
            screen.displayMessage("\nEnter your PIN: ");
            isPinInput = true; // Change state to expect PIN next
        } else {
            // Expecting PIN input
            int pin = input; // Set the pin from input

            // Authenticate user with account number and pin
            userAuthenticated = bankDatabase.authenticateUser(accountNumber, pin);

            // Check whether authentication succeeded
            if (userAuthenticated) {
                currentAccountNumber = accountNumber; // Save user's account #
                screen.displayMessageLine("Authentication successful.");
                accountNumber = 0; // Reset account number
                isPinInput = false; // Reset flag for next authentication attempt
                userAuthenticated = true; // Reset authentication status
                displayMainMenu();
            } else {
                screen.displayMessageLine("Invalid account number or PIN. Please try again.");
                accountNumber = 0; // Reset account number
                isPinInput = false; // Reset flag for next authentication attempt
                userAuthenticated = false; // Reset authentication status
            }
        }
    }

    static void displayMainMenu()
    {
        GlobalState.ATMState = "Menu";
        screen.clearScreen();
        screen.displayMessageLine( "Main menu:" );
        screen.displayMessageLine( "1 - View my balance" );
        screen.displayMessageLine( "2 - Withdraw cash" );
        screen.displayMessageLine( "3 - Transfer" );
        screen.displayMessageLine( "4 - Exit\n" );
        screen.displayMessage( "Enter a choice: " );

    } // end method displayMainMenu

    private void menuSelection(int choice)
    {
            switch ( choice )
            {
                // user chose to perform one of three transaction types
                case BALANCE_INQUIRY:
                    GlobalState.ATMState = "Inquiry";
                    temp = new BalanceInquiry(currentAccountNumber, screen, bankDatabase);
                    temp.execute();
                    break;
                case WITHDRAWAL:
                    GlobalState.ATMState = "Withdrawal";
                    temp = new Withdrawal( currentAccountNumber, screen, bankDatabase, keypad, cashDispenser );
                    temp.execute(0);
                    break;
                case TRANSFER:
                    GlobalState.ATMState = "Transfer";
                    break;
                case EXIT: // user chose to terminate session
                    screen.displayMessageLine( "\nExiting the system..." );
                    break;
                default: // user did not enter an integer from 1-4
                    screen.displayMessageLine(
                            "\nYou did not enter a valid selection. Try again." );
                    break;
            } // end switch
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        for (int i = 0; i < 10; i++) {
            if (source == keypadPanel.numberButtons[i]) { // Check if a number button was pressed
                textField.appendInput(String.valueOf(i)); // Append pressed number to userInput
                return;
            }
        }

        if (source == keypadPanel.actionButtons[0]) { // Cancel Button
            textField.clearInput();

        } else if (source == keypadPanel.actionButtons[1]) { // Clear Button
            textField.clearInput();

        } else if (source == keypadPanel.actionButtons[2]) { // Enter Button
            handler(textField.getUserInput());
            textField.clearInput();
        }
    }

    private void handler(String text){
        System.out.println(GlobalState.ATMState);
        if("login".equals(GlobalState.ATMState)) {
            int input = 0;
            try {
                input = Integer.parseInt(text);
            } catch (NumberFormatException e) {
                return;
            }
            authenticateUser(input);
        }

        else if("Menu".equals(GlobalState.ATMState)){
            int input = 0;
            try {
                input = Integer.parseInt(text);
            } catch (NumberFormatException e) {
                return;
            }
            menuSelection(input);
        }

        else if("Inquiry".equals(GlobalState.ATMState)){
            displayMainMenu();
        }

        else if ("returning".equals(GlobalState.ATMState)){
            displayMainMenu();
        }

        else if("Withdrawal".equals(GlobalState.ATMState)){
            System.out.println(temp.getState());
            if("waitingChoice".equals(temp.getState())){
                int input = 0;
                try {
                    input = Integer.parseInt(text);
                } catch (NumberFormatException e) {
                    return;
                }
                temp.execute(input);
            }
            else if("waitingPreferredAmount".equals(temp.getState())){
                int input = 0;
                try {
                    input = Integer.parseInt(text);
                } catch (NumberFormatException e) {
                    return;
                }
                temp.execute(input);
            }

        }
    }

    // Public method to provide access to the instance
    public static void getInstance() {
        if (instance == null) {
            instance = new ATMFrame();
        }
    }

    // Append MessageField
    public static void appendMessage(String text) {
        messageField.appendMessage(text);
    }

    // Set and clear MessageField
    public static void setAndClearMessageField(String text) {
        messageField.clearScreen();
        messageField.appendMessage(text);
    }
}

