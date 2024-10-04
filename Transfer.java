public class Transfer extends Transaction
{
  private double amount;
  private Keypad keypad;

  //constant variable that return to menu
  private final static int CANCELED = 0;

  public Transfer(int remitterAccountNumber, Screen atmScreen, BankDatabase atmBankDatabase, Keypad atmKeypad){
    // initialize superclass variables
    super(remitterAccountNumber, atmScreen, atmBankDatabase);

    keypad = atmKeypad;
  }
  // perform transfer
  public void execute() {

    boolean transferCompleted = false;
    double availableBalance;
    // get reference to bank database and screen
    Screen screen = getScreen();
    BankDatabase bankDatabase = getBankDatabase();
     // do while loop until transfer succesful or user cancels
     do {
      // get beneficiary account number using getBeneficiaryAccountNumber method.
      int beneficiaryAccountNumber = getBeneficiaryAccountNumber();
    
      if (beneficiaryAccountNumber == CANCELED) { 
        screen.displayMessageLine("Transaction aborted");
        break; 
      }
      //get transfer amount number using getTransferAmount method
      amount = getTransferAmount();

      if(amount != CANCELED) {

          transferCompleted = confirmTransfer(beneficiaryAccountNumber, amount);

          if(transferCompleted == true) {
            screen.displayMessageLine("Returning back to menu...");
            return;
          }

          else {
            continue;
          }
      }

      else {
        screen.displayMessageLine("Transaction canceled");
        transferCompleted = true;
      }
    } while (!transferCompleted);
  }

  private int getBeneficiaryAccountNumber(){
    
    Screen screen = getScreen();
    BankDatabase bankDatabase = getBankDatabase();

    boolean validAccountNumber = false;
    int accountNumber;
    while(!validAccountNumber) {
      screen.displayMessageLine("Enter beneficiary account number: ");
      screen.displayMessageLine("0 - Cancel transaction");
      accountNumber = keypad.getInput();

      if(getAccountNumber() == accountNumber ) {
        screen.displayMessageLine("You are not allowed to transfer to your own account");
      }

      else if(bankDatabase.accountExists(accountNumber) != false || accountNumber == 0) { 
        validAccountNumber = true; return accountNumber;
      }

      else { 
        screen.displayMessageLine("Account not found! Please try again."); }
    }
    return 0;
  }

private double getTransferAmount() {
    Screen screen = getScreen();
    BankDatabase bankDatabase = getBankDatabase();
    double input = -1; // Initialize to an invalid value
    boolean validInput = false;

    while (!validInput) {
        screen.displayMessageLine("Enter transfer amount: ");
        screen.displayMessageLine("0 - Cancel transaction");

            double availableBalance = bankDatabase.getAvailableBalance(getAccountNumber());
            input = keypad.getInput();

            if (input > availableBalance) {
                screen.displayMessageLine("Insufficient balance");
            } else {
                validInput = true; // Valid input received
            }
        }     

    return input;
  } 
private boolean confirmTransfer(int beneficiaryAccountNumber, double amount){
  Screen screen = getScreen();
  Keypad keypad = this.keypad;
  BankDatabase bankDatabase = getBankDatabase();

  screen.displayMessageLine("You are transferring " + amount + " to account number " + beneficiaryAccountNumber);
  screen.displayMessageLine("1 - Confirm");
  screen.displayMessageLine("2 - Re-enter details");
  screen.displayMessageLine("0 - Cancel transaction");
  int input;

  
  input = keypad.getInput();

  switch(input) {
    case 1:
      bankDatabase.debit(getAccountNumber(), amount);
      bankDatabase.credit(beneficiaryAccountNumber, amount);
      screen.displayMessageLine("Transfer completed");
      return true;
    case 2:
      screen.displayMessageLine("returning...");
      return false;
    case CANCELED:
      screen.displayMessageLine("Transaction canceled");
      return true;
    default: 
      return confirmTransfer(beneficiaryAccountNumber, amount);
  }
}
}

