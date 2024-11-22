// Transaction.java
// Abstract superclass Transaction represents an ATM transaction

public abstract class Transaction {
    private String state;
    private int accountNumber; // indicates account involved
    private Screen screen; // ATM's screen
    private BankDatabase bankDatabase; // account info database

    // Transaction constructor invoked by subclasses using super()
    public Transaction(int userAccountNumber, Screen atmScreen,
                       BankDatabase atmBankDatabase) {
        accountNumber = userAccountNumber;
        screen = atmScreen;
        bankDatabase = atmBankDatabase;
    } // end Transaction constructor

    // return account number
    public int getAccountNumber() {
        return accountNumber;
    } // end method getAccountNumber

    // return reference to screen
    public Screen getScreen() {
        return screen;
    } // end method getScreen

    // return reference to bank database
    public BankDatabase getBankDatabase() {
        return bankDatabase;
    } // end method getBankDatabase

    // perform the transaction (overridden by each subclass)
    public void execute() {

    }

    public void execute(int input) {

    }

    public void execute(double input) {

    }

    public String getState() {
        return "";
    }

} // end class Transaction
