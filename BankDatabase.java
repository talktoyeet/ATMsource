// BankDatabase.java
// Represents the bank account information database 

public class BankDatabase
{
   private Account accounts[]; // array of Accounts
   
   // no-argument BankDatabase constructor initializes accounts
   public BankDatabase()
   {
      accounts = new Account[ 4 ]; // just 2 accounts for testing
      accounts[ 0 ] = new SavingAccount(23456, 65432, 20000, 25000, 5); //SavingAccount with 5% interest
      accounts[ 1 ] = new SavingAccount(1, 1, 70000, 40000, 5);
      accounts[ 2 ] = new ChequeAccount(34567, 76543, 20000, 25000, 10000); //ChequeAccount with limit 10000
      accounts[ 3 ] = new ChequeAccount(2, 2, 93000, 34000, 10000);
      
   } // end no-argument BankDatabase constructor
   
   // retrieve Account object containing specified account number
   private Account getAccount( int accountNumber )
   {
      // loop through accounts searching for matching account number
      for ( Account currentAccount : accounts )
      {
         // return current account if match found
         if ( currentAccount.getAccountNumber() == accountNumber )
            return currentAccount;
      } // end for

      return null; // if no matching account was found, return null
   } // end method getAccount
   
   // return String of account class name
   public String getAccountType( int userAccountNumber )
   {
      return getAccount( userAccountNumber ).getAccountType();
   }

   // determine whether user-specified account number and PIN match
   // those of an account in the database
   public boolean authenticateUser( int userAccountNumber, int userPIN )
   {
      // attempt to retrieve the account with the account number
      Account userAccount = getAccount( userAccountNumber );

      // if account exists, return result of Account method validatePIN
      if ( userAccount != null )
         return userAccount.validatePIN( userPIN );
      else
         return false; // account number not found, so return false
   } // end method authenticateUser

   public boolean accountExists(int userAccountNumber) {
     return getAccount(userAccountNumber) != null; //Return false if getAccount return null
   }

   // return available balance of Account with specified account number
   public double getAvailableBalance( int userAccountNumber )
   {
      return getAccount( userAccountNumber ).getAvailableBalance();
   } // end method getAvailableBalance

   // return total balance of Account with specified account number
   public double getTotalBalance( int userAccountNumber )
   {
      return getAccount( userAccountNumber ).getTotalBalance();
   } // end method getTotalBalance
   //
   public double getInterestRate( int userAccountNumber )
   {
      if (getAccount( userAccountNumber ) instanceof SavingAccount) 
      {
        SavingAccount savingAccount = (SavingAccount) getAccount( userAccountNumber ); // Downcasting
        return savingAccount.getInterestRate(); // Call the method on the SavingAccount instance
      } 

      else
      {
        throw new IllegalArgumentException("Account is not a Saving Account.");
      }
   }
   

   // credit an amount to Account with specified account number
   public void credit( int userAccountNumber, double amount )
   {
      getAccount( userAccountNumber ).credit( amount );
   } // end method credit

   // debit an amount from of Account with specified account number
   public void debit( int userAccountNumber, double amount )
   {
      getAccount( userAccountNumber ).debit( amount );
   } // end method debit
   
} // end class BankDatabase



/**************************************************************************
 * (C) Copyright 1992-2007 by Deitel & Associates, Inc. and               *
 * Pearson Education, Inc. All Rights Reserved.                           *
 *                                                                        *
 * DISCLAIMER: The authors and publisher of this book have used their     *
 * best efforts in preparing the book. These efforts include the          *
 * development, research, and testing of the theories and programs        *
 * to determine their effectiveness. The authors and publisher make       *
 * no warranty of any kind, expressed or implied, with regard to these    *
 * programs or to the documentation contained in these books. The authors *
 * and publisher shall not be liable in any event for incidental or       *
 * consequential damages in connection with, or arising out of, the       *
 * furnishing, performance, or use of these programs.                     *
 *************************************************************************/
