public class ChequeAccount extends Account{
    private double chequeLimit;

    public ChequeAccount(int accountNumber, int pin, double availableBalance, double totalBalance, double limit){
        super(accountNumber, pin, availableBalance, totalBalance); // Call the parent constructor
        this.chequeLimit = limit;// Default limit per cheque
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
     //Issues a cheque
       public void issueCheque(double amount){
        if (amount > chequeLimit){
            System.out.println("Cheque amount exceeds the limit of HK$" + this.chequeLimit + ".");
        }
        else{
            debit(amount); //Debit the amount from the account
            System.out.println("Issued cheque of HK$" + amount + ".");
        }
    }

}
