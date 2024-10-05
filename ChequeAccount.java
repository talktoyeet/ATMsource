public class ChequeAccount extends Account{
    private double chequeLimit;

    public ChequeAccount(int accountNumber, int pin, double availableBalance, double totalBalance, double limit){
        super(accountNumber, pin, availableBalance, totalBalance); // Call the parent constructor
        this.chequeLimit = limit;
    }

    public ChequeAccount(int accountNumber, int pin, double availableBalance, double totalBalance){
        super(accountNumber, pin, availableBalance, totalBalance); // Call the parent constructor
        this.chequeLimit = 10000;
    }
    
    public double getChequeLimit()
    {
      return chequeLimit;
    }

    public void setChequeLimit( double amount )
    {
      chequeLimit = amount;
    }

}

