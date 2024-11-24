public class ChequeAccount extends Account {
    private double chequeLimit;

    public ChequeAccount(int accountNumber, int pin, double availableBalance, double totalBalance, double limit) {
        super(accountNumber, pin, availableBalance, totalBalance); // Call the parent constructor
        this.chequeLimit = limit;// Default limit per cheque
    }

    public ChequeAccount(int accountNumber, int pin, double availableBalance, double totalBalance) {
        super(accountNumber, pin, availableBalance, totalBalance); // Call the parent constructor
        this.chequeLimit = 10000;

        // available cheque limit
        if (availableBalance < 0) {
            chequeLimit = availableBalance + chequeLimit;
        }
    }


    @Override
    public void debit(double amount) {


        // Subtract from available and total balance
        super.availableBalance -= amount; // Subtract from available balance
        super.totalBalance -= amount; // Subtract from total balance
    } // end method debit

    @Override
    public boolean credit(double amount) {
        if(amount + totalBalance > chequeLimit){
            return false;
        }
        // Add to available and total balance
        super.availableBalance += amount; // Add to the available balance
        super.totalBalance += amount; // Add to the total balance
        return true;
    } // end method credit


    public double getChequeLimit() {
        return chequeLimit;
    }

    public void setChequeLimit(double amount) {
        chequeLimit = amount;
    }


}
