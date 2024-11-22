import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ATMFrame extends JFrame implements ActionListener {
    // Initiate components
    static final ATMTextField textField = new ATMTextField();
    static final ATMMessageField messageField = new ATMMessageField();
    // constants corresponding to main menu options
    private static final int BALANCE_INQUIRY = 1;
    private static final int WITHDRAWAL = 2;
    private static final int TRANSFER = 3;
    private static final int EXIT = 4;
    static KeypadPanel keypadPanel;
    // Static variable to hold the single instance of ATMFrame
    private static ATMFrame instance;
    private static Screen screen; // ATM's screen
    private final Keypad keypad; // ATM's keypad
    private final CashDispenser cashDispenser; // ATM's cash dispenser
    private final BankDatabase bankDatabase; // account information database
    // Initiate variables
    private boolean userAuthenticated; // whether user is authenticated
    private int currentAccountNumber; // current user's account number
    private Transaction temp = null;
    private int accountNumber = 0;
    private boolean isPinInput = false;

    private ATMFrame() {
        keypadPanel = new KeypadPanel(this);
        // Set Frame dimension
        this.setSize(600, 600);
        this.setSize(500, 700); // Increased size for more space
        this.setLayout(null); // Use null layout
        this.setResizable(false);
        this.setBackground(Color.BLUE);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Set bounds for components
        messageField.setBounds(50, 50, 400, 300); // Large enough for approximately 15 lines
        messageField.setBackground(Color.BLUE);
        textField.setBounds(50, 360, 400, 50); // Positioned below the message field
        keypadPanel.setBounds(50, 420, 400, 200); // Positioned below the text field

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
        screen.displayMessageLine("Welcome!");
        screen.displayMessage("Please enter your account number: ");
    }

    static void displayMainMenu() {
        GlobalState.ATMState = "Menu";
        screen.clearScreen();
        screen.displayMessageLine("Main menu:");
        screen.displayMessageLine("1 - View my balance");
        screen.displayMessageLine("2 - Withdraw cash");
        screen.displayMessageLine("3 - Transfer");
        screen.displayMessageLine("4 - Exit\n");
        screen.displayMessage("Enter a choice: ");

    } // end method displayMainMenu

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

    private void authenticateUser(int input) {
        GlobalState.allowDecimal = false;
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
                userAuthenticated = true;
                displayMainMenu();
            } else {
                screen.clearScreen();
                screen.displayMessageLine("Invalid account number or PIN. Please try again.");
                screen.displayMessage("\nPlease enter your account number: ");
                accountNumber = 0; // Reset account number
                isPinInput = false; // Reset flag for next authentication attempt
                userAuthenticated = false; // Reset authentication status
            }
        }
    }

    private void menuSelection(int choice) {
        switch (choice) {
            // user chose to perform one of three transaction types
            case BALANCE_INQUIRY:
                GlobalState.ATMState = "Inquiry";
                temp = new BalanceInquiry(currentAccountNumber, screen, bankDatabase);
                temp.execute();
                break;
            case WITHDRAWAL:
                GlobalState.ATMState = "Withdrawal";
                temp = new Withdrawal(currentAccountNumber, screen, bankDatabase, keypad, cashDispenser);
                temp.execute(0);
                break;
            case TRANSFER:
                GlobalState.ATMState = "Transfer";
                temp = new Transfer(currentAccountNumber, screen, bankDatabase, keypad);
                temp.execute(0);
                break;
            case EXIT: // user chose to terminate session
                screen.clearScreen();
                screen.displayMessageLine("\nExiting the system...");
                screen.displayMessageLine("\nWelcome!");
                screen.displayMessage("\nPlease enter your account number: ");
                GlobalState.ATMState = "login";
                userAuthenticated = false;
                break;
            default: // user did not enter an integer from 1-4
                screen.clearScreen();
                screen.displayMessageLine(
                        "\nYou did not enter a valid selection. Press enter to try again.");
                GlobalState.ATMState = "returning";
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
            if ("login".equals(GlobalState.ATMState)) {
                GlobalState.allowDecimal = false;
                screen.clearScreen();
                screen.displayMessageLine("\nWelcome!");
                screen.displayMessage("\nPlease enter your account number: ");
                accountNumber = 0; // Reset account number
                isPinInput = false; // Reset flag for next authentication attempt
                userAuthenticated = false;
            } else if ("Menu".equals(GlobalState.ATMState)) {
                GlobalState.allowDecimal = false;
                screen.clearScreen();
                screen.displayMessageLine("\nWelcome!");
                screen.displayMessage("\nPlease enter your account number: ");
                GlobalState.ATMState = "login";
            } else {
                GlobalState.allowDecimal = false;
                displayMainMenu();
            }

        } else if (source == keypadPanel.actionButtons[1]) { // Clear Button
            textField.clearInput();

        } else if (source == keypadPanel.actionButtons[2]) { // Enter Button
            handler(textField.getUserInput());
            textField.clearInput();

        } else if (source == keypadPanel.numberButtons[10]) { // Decimal Button
            if (!textField.getUserInput().contains(".")) {
                if (GlobalState.allowDecimal) {
                    textField.appendInput("."); // Append decimal point to user input
                }
            }

        } else if (source == keypadPanel.numberButtons[11]) { // "00" Button
            textField.appendInput("00"); // Append "00" to user input
        }
    }

    private void handler(String text) {
        System.out.println(GlobalState.ATMState);
        if ("login".equals(GlobalState.ATMState)) {
            int input = 0;
            try {
                input = Integer.parseInt(text);
            } catch (NumberFormatException e) {
                return;
            }
            authenticateUser(input);
        } else if ("Menu".equals(GlobalState.ATMState)) {
            int input = 0;
            try {
                input = Integer.parseInt(text);
            } catch (NumberFormatException e) {
                return;
            }
            menuSelection(input);
        } else if ("Inquiry".equals(GlobalState.ATMState)) {
            displayMainMenu();
        } else if ("returning".equals(GlobalState.ATMState)) {
            displayMainMenu();
        } else if ("Withdrawal".equals(GlobalState.ATMState)) {
            System.out.println(temp.getState());
            if ("waitingChoice".equals(temp.getState())) {
                int input = 0;
                try {
                    input = Integer.parseInt(text);
                } catch (NumberFormatException e) {
                    return;
                }
                temp.execute(input);
            } else if ("waitingPreferredAmount".equals(temp.getState())) {
                int input = 0;
                try {
                    input = Integer.parseInt(text);
                } catch (NumberFormatException e) {
                    return;
                }
                temp.execute(input);
            }

        } else if ("Transfer".equals(GlobalState.ATMState)) {
            if ("waitingInput".equals(temp.getState())) {
                int input = 0;
                try {
                    input = Integer.parseInt(text);
                } catch (NumberFormatException e) {
                    return;
                }
                temp.execute(input);
            } else if ("waitingConfirmation".equals(temp.getState())) {
                temp.execute(0);
            } else if ("waitingDoubleInput".equals(temp.getState())) {
                double input = 0;
                try {
                    input = Double.parseDouble(text);
                } catch (NumberFormatException e) {
                    return;
                }
                temp.execute(input);
                GlobalState.allowDecimal = false;
            }
        }
    }


}

