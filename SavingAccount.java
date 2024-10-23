public class SavingAccount extends Account{
    private double interestRate;
    private long lastModifiedTime = System.currentTimeMillis();
    
    public SavingAccount(int accountNumber, int pin, double availableBalance, double totalBalance, double interestRate){
        super(accountNumber, pin, availableBalance, totalBalance); // Call the parent constructor
        this.interestRate = interestRate;
    }

    public SavingAccount(int accountNumber, int pin, double availableBalance, double totalBalance){
        super(accountNumber, pin, availableBalance, totalBalance); // Call the parent constructor
        this.interestRate = 0.005; //defalut value of 0.5% per annum
    }
    
    public void applysecInterest() {//update user account per 1sec
    long now = System.currentTimeMillis(); // get the current time
    long temp;
    if (now - lastModifiedTime >= 1000) { // =1sec
        // Calculate number of days since last daily interest application
        temp = (now - lastModifiedTime) / 1000;
        // Apply daily interest for each day
        for (int i = 1; i <= temp; i++) {
            totalBalance = totalBalance * (1 + interestRate); 
            availableBalance = availableBalance * (1 + interestRate); // add the daily interest to the balance
        }
        // Update last daily interest time
        lastModifiedTime = now; // save the time
    }
    }    
        
    @Override
    public void debit(double amount){
        applysecInterest();
        super.availableBalance -= amount;//subtract from available blance from superclass Account
        super.totalBalance -= amount;//subtract from total blance from superclass Account
    }
    
    @Override
    public void credit(double amount)
    {
        applysecInterest();
        super.availableBalance += amount;//subtract from available blance from superclass Account
        super.totalBalance += amount;//subtract from total blance from superclass Account
    }
    
    public double interest() {
        return totalBalance * interestRate; // Interest calculation
    }
    
   
    public double getavailableBalance(){ 
        applysecInterest();
        return availableBalance;
    }
    
       public double gettotalBalance(){ 
        applysecInterest();
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
