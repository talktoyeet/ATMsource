public class ChequeAccount extends Account {
    private double chequeLimit;
    private double chequeLimitAvilable;

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
        // Adjust the cheque limit based on the conditions using the ternary operator
        chequeLimitAvilable = (amount > super.totalBalance)
                ? ((amount - availableBalance) >= chequeLimitAvilable) ? 0 : chequeLimitAvilable - amount
                : chequeLimitAvilable;

        // Subtract from available and total balance
        super.availableBalance -= amount; // Subtract from available balance
        super.totalBalance -= amount; // Subtract from total balance
    } // end method debit

    @Override
    public void credit(double amount) {
        // Adjust the cheque limit based on the condition using the ternary operator
        chequeLimitAvilable = (super.totalBalance <= 0)
                ? amount + chequeLimitAvilable
                : chequeLimitAvilable;

        // Add to available and total balance
        super.availableBalance += amount; // Add to the available balance
        super.totalBalance += amount; // Add to the total balance

        // Reset the cheque limit if it exceeds the maximum allowed
        chequeLimitAvilable = (chequeLimitAvilable > chequeLimit)
                ? chequeLimit
                : chequeLimitAvilable; // Maintain the current value if within limit
    } // end method credit


    public double getChequeLimit() {
        return chequeLimit;
    }

    public void setChequeLimit(double amount) {
        chequeLimit = amount;
    }


}
