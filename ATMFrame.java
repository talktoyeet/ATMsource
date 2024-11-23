import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;

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
    private final CardPanel cardPanel;
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
        cardPanel = new CardPanel(this);
        // Set Frame dimension
        this.setSize(700, 700); // Increased size for more space
        this.setLayout(null); // Use null layout
        this.setResizable(false);
        this.setBackground(Color.BLUE);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        messageField.setBounds(50, 50, 400, 300); // Large enough for approximately 15 lines
        messageField.setBackground(Color.BLUE);
        textField.setBounds(50, 360, 400, 50); // Positioned below the message field
        keypadPanel.setBounds(50, 420, 400, 200); // Positioned below the text field
        cardPanel.setBounds(500, 360, 150, 150); // Adjusted position and size
        //cardPanel.setBackground(Color.WHITE);

        // Add components
        this.add(messageField);
        this.add(textField);
        this.add(keypadPanel);
        this.add(cardPanel);


        // Set visible
        this.setVisible(true);

        //
        userAuthenticated = false; // user is not authenticated to start
        currentAccountNumber = 0; // no current account number to start
        screen = new Screen(); // create screen
        cashDispenser = new CashDispenser(); // create cash dispenser
        bankDatabase = new BankDatabase(); // create acct info database
        welcomeMessage();
    
    }


    private void wait(int mSec) {
        try {
            Thread.sleep(mSec); // Wait for 3000 milliseconds (3 seconds)
        } catch (InterruptedException e) {
            e.printStackTrace(); // Handle the exception
        }
    }


    static void displayMainMenu() {
        GlobalState.ATMState = "Menu";
        screen.clearScreen();
        screen.displayMessageLine("Main menu:");
        screen.displayMessageLine("1 - View my balance");
        screen.displayMessageLine("2 - Withdraw cash");
        screen.displayMessageLine("3 - Transfer");
        screen.displayMessageLine("Eject card to exit ATM\n");
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

    public static void welcomeMessage() {
        screen.clearScreen();
        GlobalState.ATMState = "waitingCard"; // Change ui state to login
        screen.displayMessageLine("Welcome!");
        screen.displayMessageLine("Please insert your card");
    }

    public static void loginMessage() {
        screen.displayMessage("Please Enter your account number: ");
    }

    private void authenticateUser(int input) {
         GlobalState.allowDecimal = false;
    if (!isPinInput) {
        // Expecting account number input
        accountNumber = input; // Set the account number from input
        screen.displayMessage("\nEnter your PIN: ");
        isPinInput = true; // Change state to expect PIN next
        textField.setPinInputMode(true); // Set the text field to PIN input mode
    } else {
        // Expecting PIN input
        int pin = input; // Set the pin from input

        // Authenticate user with account number and pin
        userAuthenticated = bankDatabase.authenticateUser (accountNumber, pin);

        // Check whether authentication succeeded
        if (userAuthenticated) {
            currentAccountNumber = accountNumber; // Save user's account #
            screen.displayMessageLine("Authentication successful.");
            accountNumber = 0; // Reset account number
            isPinInput = false; // Reset flag for next authentication attempt
            textField.setPinInputMode(false); // Switch back to account number mode
            displayMainMenu();
        } else {
            screen.clearScreen();
            screen.displayMessageLine("Invalid account number or PIN. Please try again.");
            screen.displayMessage("\nPlease enter your account number: ");
            accountNumber = 0; // Reset account number
            isPinInput = false; // Reset flag for next authentication attempt
            userAuthenticated = false; // Reset authentication status
            textField.setPinInputMode(false); // Switch back to account number mode
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
                temp = new Withdrawal(currentAccountNumber, screen, bankDatabase, cashDispenser);
                temp.execute(0);
                break;
            case TRANSFER:
                GlobalState.ATMState = "Transfer";
                temp = new Transfer(currentAccountNumber, screen, bankDatabase);
                temp.execute(0);
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
                screen.displayMessage("Please enter your account number: ");
                accountNumber = 0; // Reset account number
                isPinInput = false; // Reset flag for next authentication attempt
                userAuthenticated = false;
            } else if ("Menu".equals(GlobalState.ATMState)) {

            } else if ("waitingCard".equals(GlobalState.ATMState)) {

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

        } else if (source == cardPanel.insertCardButton) {
            System.out.println("aaa");
            if ("waitingCard".equals(GlobalState.ATMState)) {
                screen.clearScreen();
                screen.displayMessageLine("Card read successful!");
                GlobalState.ATMState = "login"; // Change ui state to login
                loginMessage();
            }
        } else if (source == cardPanel.ejectCardButton) {
            if (!"waitingCard".equals(GlobalState.ATMState)) {
                startCardEjectTimer();

            }
        }
    }

    private void startCardEjectTimer() {
        if (!"waitingCard".equals(GlobalState.ATMState)) {
            // Display the initial message immediately
            screen.clearScreen();
            screen.displayMessageLine("Ejecting card...");

            // Create a timer to execute actions after 3 seconds
            Timer timer = new Timer(3000, x -> {
                screen.clearScreen(); // Clear the screen after 3 seconds
                screen.displayMessageLine("Remember to take your card!"); // Display this message

                // Start another timer to wait before calling welcomeMessage
                Timer welcomeTimer = new Timer(3000, y -> {
                    welcomeMessage(); // Call welcomeMessage after another 3 seconds
                });
                welcomeTimer.setRepeats(false); // Ensure this timer only runs once
                welcomeTimer.start(); // Start the welcome timer
            });

            timer.setRepeats(false); // Ensure the first timer only runs once
            timer.start(); // Start the first timer
            GlobalState.ATMState = "waitingCard"; // Update the state
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
                double input = 0.0;
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
