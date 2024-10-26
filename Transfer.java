public class Transfer extends Transaction {
    private double amount; // Amount to be transferred
    private Keypad keypad; // Keypad for user input

    // Constant variable that indicates a canceled transaction
    private final static int CANCELED = 0;

    // Constructor to initialize the transfer transaction
    public Transfer(int remitterAccountNumber, Screen atmScreen, BankDatabase atmBankDatabase, Keypad atmKeypad) {
        // Initialize superclass variables
        super(remitterAccountNumber, atmScreen, atmBankDatabase);
        this.keypad = atmKeypad; // Set the keypad for user input
    }

    // Execute the transfer process
    public void execute() {
        boolean transferCompleted = false; // Flag to track if the transfer is completed

        // Loop until the transfer is completed or canceled
        while (!transferCompleted) {
            int beneficiaryAccountNumber = getBeneficiaryAccountNumber(); // Get beneficiary account number
            if (beneficiaryAccountNumber == CANCELED) {
                getScreen().displayMessageLine("Transaction aborted"); // Inform user of cancellation
                break; // Exit the loop if canceled
            }

            amount = getTransferAmount(); // Get the transfer amount
            if (amount != CANCELED) {
                transferCompleted = confirmTransfer(beneficiaryAccountNumber, amount); // Confirm the transfer
                if (transferCompleted) {
                    getScreen().displayMessageLine("Returning back to menu..."); // Notify user of completion
                }
            } else {
                getScreen().displayMessageLine("Transaction canceled"); // Inform user of cancellation
                transferCompleted = true; // Set flag to true to exit loop
            }
        }
    }

    // Get the beneficiary account number from user input
    private int getBeneficiaryAccountNumber() {
        Screen screen = getScreen(); // Reference to the screen for displaying messages
        BankDatabase bankDatabase = getBankDatabase(); // Reference to bank database for account validation
        int accountNumber;

        while (true) { // Loop until a valid account number is entered
            screen.displayMessageLine("0 - Cancel transaction\n");
            screen.displayMessageLine("Enter beneficiary account number:");
            accountNumber = keypad.getPositiveInteger(); // Get input from keypad

            if (accountNumber == 0) {
                return CANCELED; // Return canceled constant if user chooses to cancel
            } else if (getAccountNumber() == accountNumber) {
                screen.displayMessageLine("You are not allowed to transfer to your own account"); // Prevent self-transfer
            } else if (bankDatabase.accountExists(accountNumber)) {
                return accountNumber; // Return valid beneficiary account number
            } else {
                screen.displayMessageLine("Account not found! Please try again."); // Prompt for re-entry if invalid
            }
        }
    }

    // Get the transfer amount from user input
    private double getTransferAmount() {
        Screen screen = getScreen(); // Reference to the screen for displaying messages
        BankDatabase bankDatabase = getBankDatabase(); // Reference to bank database for balance check
        double input;

        while (true) { // Loop until a valid transfer amount is entered
            screen.displayMessageLine("0 - Cancel transaction\n");
            screen.displayMessageLine("Enter transfer amount:");

            input = keypad.getPositiveDecimal(); // Get input from keypad
            if (input == 0) {
                return CANCELED; // Return canceled constant if user chooses to cancel
            }

            double availableBalance = bankDatabase.getAvailableBalance(getAccountNumber()); // Check available balance
            if (input > availableBalance) {
                screen.displayMessageLine("Insufficient balance"); // Notify user of insufficient funds
            } else {
                return input; // Return valid transfer amount
            }
        }
    }

    // Confirm the transfer details with the user before proceeding
    private boolean confirmTransfer(int beneficiaryAccountNumber, double amount) {
        Screen screen = getScreen(); // Reference to the screen for displaying messages
        BankDatabase bankDatabase = getBankDatabase(); // Reference to bank database for updating balances

        screen.displayMessageLine("You are transferring " + amount + " to account number " + beneficiaryAccountNumber);
        screen.displayMessageLine("1 - Confirm");
        screen.displayMessageLine("2 - Re-enter details");
        screen.displayMessageLine("0 - Cancel transaction\n");

        int input = keypad.getPositiveInteger(); // Get confirmation input from keypad

        switch (input) {
            case 1: // User confirms transfer
                bankDatabase.debit(getAccountNumber(), amount); // Debit amount from remitter's account
                bankDatabase.credit(beneficiaryAccountNumber, amount); // Credit amount to beneficiary's account
                screen.displayMessageLine("Transfer completed"); // Notify user of successful transfer
                return true; 
            case 2: 
                screen.displayMessageLine("Returning..."); 
                return false; 
            case CANCELED: 
                screen.displayMessageLine("Transaction canceled"); 
                return true; 
            default: 
                return confirmTransfer(beneficiaryAccountNumber, amount); // Retry confirmation on invalid input
        }
    }
}
