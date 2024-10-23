public class Withdrawal extends Transaction {
    private int amount; // amount to withdraw
    private Keypad keypad; // reference to keypad
    private CashDispenser cashDispenser; // reference to cash dispenser

    // constant corresponding to menu option to cancel
    private final static int CANCELED = 5;
    private final static double MAX_WITHDRAWAL = 5000; // maximum cash that can be taken
    private static double ATM_RESERVE = 10000; // ATM reserve amount

    // Withdrawal constructor
    public Withdrawal(int userAccountNumber, Screen atmScreen, 
                      BankDatabase atmBankDatabase, Keypad atmKeypad, 
                      CashDispenser atmCashDispenser) {
        // initialize superclass variables
        super(userAccountNumber, atmScreen, atmBankDatabase);
        
        // initialize references to keypad and cash dispenser
        keypad = atmKeypad;
        cashDispenser = atmCashDispenser;
    } // end Withdrawal constructor

    // perform transaction
    public void execute() {
        boolean cashDispensed = false; // cash was not dispensed yet
        double availableBalance; // amount available for withdrawal

        // get references to bank database and screen
        BankDatabase bankDatabase = getBankDatabase(); 
        Screen screen = getScreen();

        // loop until cash is dispensed or the user cancels
        do {
            // obtain a chosen withdrawal amount from the user 
            amount = displayMenuOfAmounts();

            // check whether user chose a withdrawal amount or canceled
            if (amount != CANCELED) {
                // get available balance of account involved
                availableBalance = bankDatabase.getAvailableBalance(getAccountNumber());

                // check whether the user has enough money in the account 
                if (amount <= availableBalance && amount <= MAX_WITHDRAWAL) {
                    // check whether the cash dispenser has enough money
                    if (cashDispenser.isSufficientCashAvailable(amount)) {
                        // Update ATM reserve after successful withdrawal
                        if (ATM_RESERVE >= amount) {
                            bankDatabase.debit(getAccountNumber(), amount);
                            dispenseCash(amount); // dispense cash
                            ATM_RESERVE -= amount; // decrease ATM reserve
                            cashDispensed = true; // cash was dispensed
                            
                            // instruct user to take cash
                            screen.displayMessageLine("\nPlease take your cash now.");
                        } else {
                            // Not enough cash in the ATM reserve
                            screen.displayMessageLine("\nATM cash not enough. Please wait for replenishment.");
                            disableWithdrawals(); // Disable all withdrawals momentarily
                        }
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
        } while (!cashDispensed);
    } // end method execute

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

    private int getPreferredCashAmount() {
        Screen screen = getScreen();
        int preferredAmount = 0;

        // Prompt user for their preferred cash amount
        screen.displayMessage("\n*Less or equal $5000*");
        screen.displayMessage("Enter your preferred cash amount (must be in $100, $ 500, or $1000 increments): ");
        
        // Get user input
        preferredAmount = keypad.getInput(); // Assume this method gets the input as an integer

        // Validate the preferred amount
        if (preferredAmount > 0 && preferredAmount <= MAX_WITHDRAWAL && 
            (preferredAmount % 100 == 0 || preferredAmount % 500 == 0 || preferredAmount % 1000 == 0)) {
            return preferredAmount; // Return valid preferred amount
        } else {
            screen.displayMessageLine("\nInvalid amount. Please enter a valid amount.");
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
    private int displayMenuOfAmounts() {
        int userChoice = 0; // local variable to store return value
        Screen screen = getScreen(); // get screen reference
        
        // array of amounts to correspond to menu numbers
        int amounts[] = {0, 100, 500, 1000};

        // loop while no valid choice has been made
        while (userChoice == 0) {
            // display the menu
            screen.displayMessageLine("\nWithdrawal Menu:");
            screen.displayMessageLine("1 - $100");
            screen.displayMessageLine("2 - $500");
            screen.displayMessageLine("3 - $1000");
            screen.displayMessageLine("4 - Preferred cash");
            screen.displayMessageLine("5 - Cancel transaction");
            screen.displayMessage("\nChoose a withdrawal amount: ");

            int input = keypad.getInput(); // get user input through keypad

            // determine how to proceed based on the input value
            switch (input) {
                case 1: // if the user chose a withdrawal amount 
                case 2: // (i.e., chose option 1, 2, 3, 4 or 5), return the
                case 3: // corresponding amount from amounts array
                    userChoice = amounts[input]; // save user's choice
                    break;       
                case 4: // user chose to input a preferred cash amount
                    userChoice = getPreferredCashAmount(); // call method to get preferred amount
                    break;
                case CANCELED: // the user chose to cancel
                    userChoice = CANCELED; // save user's choice
                    break;
                default: // the user did not enter a value from 1-6
                    screen.displayMessageLine("\nInvalid selection. Try again.");
            } // end switch
        } // end while

        return userChoice; // return withdrawal amount or CANCELED
    } // end method displayMenuOfAmounts
} // end class Withdrawal
