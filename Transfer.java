public class Transfer extends Transaction{
  private double amount;
  private Keypad keypad;

  public Transfer(int remitterAccountNumber, Screen atmScreen, BankDatabase atmBankDatabase, Keypad atmKeypad){
    super(remitterAccountNumber, atmScreen, atmBankDatabase);
    keypad = atmKeypad;
  }

  public void execute(){

    boolean transferCompleted = false;
    double availableBalance;

    Screen screen = getScreen();
    BankDatabase bankDatabase = getBankDatabase();
    
     do{
      int beneficiaryAccountNumber = getBeneficiaryAccountNumber();
      amount = getTransferAmount();

      if(amount > 0){

        availableBalance = bankDatabase.getAvailableBalance(getAccountNumber());

        if(amount <= availableBalance){
          bankDatabase.debit(getAccountNumber(), amount);
          bankDatabase.credit(beneficiaryAccountNumber, amount);
          transferCompleted = true;
        }

        else{
          screen.displayMessageLine("Insufficient funds in your account."); 
        }
      }
      else{
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
    while(!validAccountNumber){
      screen.displayMessageLine("Enter beneficiary account number");
      accountNumber = keypad.getInput();

        if(bankDatabase.accountExists(accountNumber) != false){ 
          validAccountNumber = true; return accountNumber;}

        else { screen.displayMessageLine("Account Number not found! Please try again."); }
    }
    return 0;
  }

  private double getTransferAmount(){
    Screen screen = getScreen();

    screen.displayMessageLine("Enter transfer amount: ");
    return keypad.getInput();
  }

}
