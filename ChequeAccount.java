public class ChequeAccount extends Account{
    private int limit;

    public ChequeAccount(int accountNumber, int pin, double availableBalance, double totalBalance, int limit){
        super(accountNumber, pin, availableBalance, totalBalance); // Call the parent constructor
        this.limit = limit;
    }

    public ChequeAccount(int accountNumber, int pin, double availableBalance, double totalBalance){
        super(accountNumber, pin, availableBalance, totalBalance); // Call the parent constructor
        this.limit = 10000;
    }
}

