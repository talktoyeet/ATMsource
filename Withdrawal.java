public class Withdrawal extends Transaction {
    // constant corresponding to menu option to cancel
    private final static int CANCELED = 5;
    private final static double MAX_WITHDRAWAL = 5000; // maximum cash that can be taken
    private static double ATM_RESERVE = 10000; // ATM reserve amount
    private int amount; // amount to withdraw
    private Keypad keypad; // reference to keypad
    private CashDispenser cashDispenser; // reference to cash dispenser
    //Withdrawal state
    private String state;

    // Withdrawal constructor
    public Withdrawal(int userAccountNumber, Screen atmScreen,
                      BankDatabase atmBankDatabase, Keypad atmKeypad,
                      CashDispenser atmCashDispenser) {
        // initialize superclass variables
        super(userAccountNumber, atmScreen, atmBankDatabase);

        // initialize references to keypad and cash dispenser
        this.state = "";
        keypad = atmKeypad;
        cashDispenser = atmCashDispenser;
    } // end Withdrawal constructor

    // perform transaction
    @Override
    public void execute(int input) {
        Screen screen = getScreen();

        if ("".equals(state)) {
            this.state = "waitingChoice";
            displayMenuOfAmounts();
        } else if ("waitingChoice".equals(state)) {
            int userChoice = 0; // local variable to store return value

            // array of amounts to correspond to menu numbers
            int amounts[] = {0, 100, 500, 1000};

            switch (input) {
                case 1: // if the user chose a withdrawal amount
                case 2: // (i.e., chose option 1, 2, 3, 4 or 5), return the
                case 3: // corresponding amount from amounts array
                    userChoice = amounts[input]; // save user's choice
                    runWithdrawal(userChoice);
                    break;
                case 4: // user chose to input a preferred cash amount
                    state = "waitingPreferredAmount";
                    screen.clearScreen();
                    screen.displayMessage("\n*Less or equal $5000*");
                    screen.displayMessage("Enter your preferred cash amount (must be in $100, $ 500, or $1000 increments): ");
                    break;
                case CANCELED: // the user chose to cancel
                    userChoice = CANCELED; // save user's choice
                    break;
                default: // the user did not enter a value from 1-6
                    screen.clearScreen();
                    screen.displayMessageLine("\nInvalid selection. Try again.");
                    returnMainMenu();
            }
        } else if ("waitingPreferredAmount".equals(state)) {
            int preferredAmount = checkPreferredCashAmount(input);
            if (preferredAmount == 0) {
                screen.clearScreen();
                screen.displayMessageLine("\nInvalid amount. Please enter a valid amount.");
                returnMainMenu();
            } else runWithdrawal(preferredAmount);


        }
    }

    private void runWithdrawal(int amount) {
        // get references to bank database and screen
        BankDatabase bankDatabase = getBankDatabase();
        Screen screen = getScreen();
        boolean cashDispensed = false; // cash was not dispensed yet
        double availableBalance; // amount available for withdrawal
        if (amount != CANCELED) {
            // get available balance of account involved
            availableBalance = bankDatabase.getAvailableBalance(getAccountNumber());

            // check whether the user has enough money in the account 
            if (amount <= availableBalance && amount <= MAX_WITHDRAWAL) {
                // Check if ATM reserve is sufficient
                if (ATM_RESERVE < amount) {
                    // Not enough cash in the ATM reserve
                    screen.clearScreen();
                    screen.displayMessageLine("\nATM cash not enough, now available ($" + ATM_RESERVE + ").");
                    screen.displayMessageLine("Please wait for replenishment.");
                    disableWithdrawals(); // Disable all withdrawals momentarily
                } else if (cashDispenser.isSufficientCashAvailable(amount)) {
                    // Update ATM reserve after successful withdrawal
                    bankDatabase.debit(getAccountNumber(), amount);
                    dispenseCash(amount); // dispense cash
                    ATM_RESERVE -= amount; // decrease ATM reserve
                    screen.clearScreen();
                    screen.displayMessageLine("\nPlease take your card now.");
                    screen.displayMessageLine("\nCard ejected.");
                    screen.displayMessageLine("\nPlease take your cash now.");
                    screen.displayMessageLine("\nCash dispensed.");
                
                } else { // cash dispenser does not have enough cash
                    screen.displayMessageLine("\nInsufficient cash available in the ATM." +
                            "\n\nPlease choose a smaller amount.");
                }
            } else { // not enough money available in user's account or exceeds max withdrawal
                screen.displayMessageLine("\nInsufficient funds in your account or exceeds maximum withdrawal limit." +
                        "\n\nPlease choose a smaller amount.");
            }
        } else { // user chose cancel menu option 
            screen.displayMessageLine("\nCanceling transaction...");
            return; // return to main menu because user canceled
        }
        returnMainMenu();
    }

    // dispense cash based on the amount
    private void dispenseCash(int amount) {
        int[] denominations = {1000, 500, 100}; // available denominations
        int[] counts = new int[denominations.length]; // counts for each denomination

        // Calculate how to dispense the requested amount
        for (int i = 0; i < denominations.length; i++) {
            while (amount >= denominations[i] && cashDispenser.isSufficientCashAvailable(denominations[i])) {
                amount -= denominations[i];
                counts[i]++;
            }
        }

        // Display the dispensed amounts
        StringBuilder dispensedMessage = new StringBuilder("\nDispensed:\n");
        for (int i = 0; i < counts.length; i++) {
            if (counts[i] > 0) {
                dispensedMessage.append("$").append(denominations[i]).append(" x ").append(counts[i]).append("\n");
            }
        }
        getScreen().displayMessageLine(dispensedMessage.toString());
    }

    private int checkPreferredCashAmount(int input) {
        Screen screen = getScreen();
        // Validate the preferred amount
        if (input > 0 && input <= MAX_WITHDRAWAL &&
                (input % 100 == 0 || input % 500 == 0 || input % 1000 == 0)) {
            return input; // Return valid preferred amount
        } else {
            return 0; // Return 0 to indicate invalid input
        }
    }

    // disable all withdrawals momentarily
    private void disableWithdrawals() {
        // Implement logic to disable all withdrawals momentarily
        // This could involve setting a flag, sending a notification, or performing some other action
        // For demonstration purposes, we'll simply print a message
        System.out.println("All withdrawals have been disabled momentarily.");
    }

    // display a menu of withdrawal amounts and the option to cancel;
    // return the chosen amount or 0 if the user chooses to cancel
    private void displayMenuOfAmounts() {
        Screen screen = getScreen(); // get screen reference
        screen.clearScreen();
        screen.displayMessageLine("Withdrawal Menu:");
        screen.displayMessageLine("\n1 - $100");
        screen.displayMessageLine("2 - $500");
        screen.displayMessageLine("3 - $1000");
        screen.displayMessageLine("4 - Preferred cash");
        screen.displayMessageLine("5 - Cancel transaction");
        screen.displayMessage("\nChoose a withdrawal amount: ");

    } // end method displayMenuOfAmounts

    private void returnMainMenu() {
        Screen screen = getScreen();
        screen.displayMessageLine("Press enter to return to main menu");
        GlobalState.ATMState = "returning";
    }

    @Override
    public String getState() {
        return state;
    }

} // end class Withdrawal
