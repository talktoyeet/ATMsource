public class Transfer extends Transaction {
    private String state = "";
    private double amount; // Amount to be transferred
    private Keypad keypad; // Keypad for user input
    private int beneficiaryAccountNumber = 0;
    // Constant variable that indicates a canceled transaction
    private final static int CANCELED = 0;

    // Constructor to initialize the transfer transaction
    public Transfer(int remitterAccountNumber, Screen atmScreen, BankDatabase atmBankDatabase, Keypad atmKeypad) {
        // Initialize superclass variables
        super(remitterAccountNumber, atmScreen, atmBankDatabase);
        this.keypad = atmKeypad; // Set the keypad for user input
    }

    // Execute the transfer process
    public void execute(int input) {
        Screen screen = getScreen();
        if("".equals(state)){
            screen.clearScreen();
            screen.displayMessageLine("Enter beneficiary account number:");
            state = "waitingInput";
        }
        else if("waitingInput".equals(state) && beneficiaryAccountNumber == 0){
            screen.clearScreen();
            beneficiaryAccountNumber = checkBeneficiaryAccountNumber(input);
            if (beneficiaryAccountNumber == 0) {
                returnMainMenu();
            }
            else {
                screen.displayMessageLine("Enter transfer amount:");
            }
        }
        else if("waitingInput".equals(state)){
            screen.clearScreen();
            amount = checkTransferAmount(input);
            screen.displayMessageLine("You are transferring " + amount + " to account number " + beneficiaryAccountNumber);
            screen.displayMessageLine("Enter - Confirm");
            screen.displayMessageLine("Cancel - Cancel transaction\n");
            state = "waitingConfirmation";
        }
        else if("waitingConfirmation".equals(state)){
            screen.clearScreen();
            confirmTransfer(beneficiaryAccountNumber, amount);
            screen.displayMessageLine("Transfer Completed!");
            returnMainMenu();
        }
    }

    private int checkBeneficiaryAccountNumber(int input) {
        Screen screen = getScreen(); // Reference to the screen for displaying messages
        BankDatabase bankDatabase = getBankDatabase(); // Reference to bank database for account validation
            int accountNumber = input;

            if (accountNumber == 0) {
                return CANCELED; // Return canceled constant if user chooses to cancel
            } else if (getAccountNumber() == accountNumber) {
                screen.displayMessageLine("\nYou are not allowed to transfer to your own account\n");// Prevent self-transfer
            } else if (bankDatabase.accountExists(accountNumber)) {
                return accountNumber; // Return valid beneficiary account number
            }  else if (bankDatabase.supportOverdrawn(getAccountNumber())){
                 screen.displayMessage("\nThis is Cheque account, the available overdrawn limit: 100000 \n");
                 screen.displayDollarAmount(bankDatabase.accountOverdrawnLimit(getAccountNumber()));
                 }
            else {
                screen.displayMessageLine("\nAccount not found! Please try again.\n"); // Prompt for re-entry if invalid
            }
        return 0;
    }

    // Get the transfer amount from user input
    private double checkTransferAmount(double input) {
        Screen screen = getScreen(); // Reference to the screen for displaying messages
        BankDatabase bankDatabase = getBankDatabase(); // Reference to bank database for balance check
            double availableBalance = bankDatabase.getAvailableBalance(getAccountNumber()); // Check available balance
            if (input > availableBalance) {
                screen.displayMessageLine("\nInsufficient balance\n"); // Notify user of insufficient funds
                returnMainMenu();
                return 0;
            } else {
                return input; // Return valid transfer amount
            }
        }

    // Confirm the transfer details with the user before proceeding
    private void confirmTransfer(int beneficiaryAccountNumber, double amount) {
        BankDatabase bankDatabase = getBankDatabase(); // Reference to bank database for updating balances

        bankDatabase.debit(getAccountNumber(), amount); // Debit amount from remitter's account
        bankDatabase.credit(beneficiaryAccountNumber, amount); // Credit amount to beneficiary's account

    }
    private void returnMainMenu(){
        Screen screen = getScreen();
        screen.displayMessageLine("Press enter to return to main menu");
        GlobalState.ATMState = "returning";
    }

    @Override
    public String getState() {
        return state;
    }
}
