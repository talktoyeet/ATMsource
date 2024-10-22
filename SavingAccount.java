public class SavingAccount extends Account{
    private static double interestRate;

    public SavingAccount(int accountNumber, int pin, double availableBalance, double totalBalance, double interestRate){
        super(accountNumber, pin, availableBalance, totalBalance); // Call the parent constructor
        this.interestRate = interestRate;
    }

    public SavingAccount(int accountNumber, int pin, double availableBalance, double totalBalance){
        super(accountNumber, pin, availableBalance, totalBalance); // Call the parent constructor
        this.interestRate = 0.5; //defalut value of 0.5% per annum
    }

    @Override
    public void debit(double amount) {
        super.availableBalance -= amount;
        super.totalBalance -= amount;
    }//minus to the available balance and total balance
    
    @Override
    public void credit( double amount ){ 
           super.availableBalance += amount; 
           super.totalBalance += amount; 
    } // add to the available balance and total balance 
    
    public double getavailableBalance(){ 
        return availableBalance;
    }
    
       public double gettotalBalance(){ 
        return totalBalance;
    }
  
    
    public double getInterestRate()
    {
      return interestRate;
    }

    public void setInterestRate( double amount )
    {
      interestRate = amount;
    }
}
