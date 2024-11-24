public class SavingAccount extends Account {
    private double interestRate;

    public SavingAccount(int accountNumber, int pin, double availableBalance, double totalBalance, double interestRate) {
        super(accountNumber, pin, availableBalance, totalBalance); // Call the parent constructor
        this.interestRate = interestRate;
    }

    public SavingAccount(int accountNumber, int pin, double availableBalance, double totalBalance) {
        super(accountNumber, pin, availableBalance, totalBalance); // Call the parent constructor
        this.interestRate = 0.005; //defalut value of 0.5% per annum
    }


    @Override
    public void debit(double amount) {
        super.availableBalance -= amount;//subtract from available balance from superclass Account
        super.totalBalance -= amount;//subtract from total balance from superclass Account
    }

    @Override
    public boolean credit(double amount) {
        super.availableBalance += amount;//subtract from available balance from superclass Account
        super.totalBalance += amount;//subtract from total balance from superclass Account
        return true;
    }

    public double interest() {
        return totalBalance * interestRate; // Interest calculation
    }


    public double getavailableBalance() {
        return availableBalance;
    }

    public double gettotalBalance() {
        return totalBalance;
    }


    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(double amount) {
        interestRate = amount;
    }
}
