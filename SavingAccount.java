public class SavingAccount extends Account{
    private double interestRate;

    public SavingAccount(int accountNumber, int pin, double availableBalance, double totalBalance, double interestRate){
        super(accountNumber, pin, availableBalance, totalBalance); // Call the parent constructor
        this.interestRate = interestRate;
    }
}
