// BalanceInquiry.java
// Represents a balance inquiry ATM transaction

public class BalanceInquiry extends Transaction
{
   // BalanceInquiry constructor
   public BalanceInquiry( int userAccountNumber, Screen atmScreen, 
      BankDatabase atmBankDatabase )
   {
      super( userAccountNumber, atmScreen, atmBankDatabase );
   } // end BalanceInquiry constructor

   // performs the transaction
   public void execute()
   {
      // get references to bank database and screen
      BankDatabase bankDatabase = getBankDatabase();
      Screen screen = getScreen();

      //class name constant
      final String savingAccountClassName = "SavingAccount";
      final String chequeAccountClassName = "ChequeAccount";

      // get the available balance for the account involved
      double availableBalance = 
         bankDatabase.getAvailableBalance( getAccountNumber() );

      // get the total balance for the account involved
      double totalBalance = 
         bankDatabase.getTotalBalance( getAccountNumber() );

      String accountType =
         bankDatabase.getAccountType( getAccountNumber() );
      
      // display the balance information on the screen
      screen.clearScreen();
      screen.displayMessageLine( "\nBalance Information:" );
      screen.displayMessage( " - Account type: " );
      screen.displayMessage( accountType );
      screen.displayMessage( "\n - Available balance: " ); 
      screen.displayDollarAmount( availableBalance );
      screen.displayMessage( "\n - Total balance:     " );
      screen.displayDollarAmount( totalBalance );

      if(accountType.equals( savingAccountClassName ))
      {
        screen.displayMessage( "\n - Current interest rate: " );
        screen.displayPercentage( bankDatabase.getInterestRate( getAccountNumber() ) );
      }

      try {
         Thread.sleep(5000);
      } catch (InterruptedException e) {
         throw new RuntimeException(e);
      }
   } // end method execute
} // end class BalanceInquiry
