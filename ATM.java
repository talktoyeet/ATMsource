import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ATM
{
   private boolean userAuthenticated; // whether user is authenticated
   private int currentAccountNumber; // current user's account number
   private Screen screen; // ATM's screen
   private Keypad keypad; // ATM's keypad
   private CashDispenser cashDispenser; // ATM's cash dispenser
   private BankDatabase bankDatabase; // account information database

   // constants corresponding to main menu options
   private static final int BALANCE_INQUIRY = 1;
   private static final int WITHDRAWAL = 2;
   private static final int TRANSFER = 3;
   private static final int EXIT = 4;

   // no-argument ATM constructor initializes instance variables
   public ATM()
   {
      userAuthenticated = false; // user is not authenticated to start
      currentAccountNumber = 0; // no current account number to start
      screen = new Screen(); // create screen
      keypad = new Keypad(); // create keypad
      cashDispenser = new CashDispenser(); // create cash dispenser
      bankDatabase = new BankDatabase(); // create acct info database
      GlobalState.ATMState = "login"; // Change ui state to login
   } // end no-argument ATM constructor

   // start ATM
   public void run()
   {
      screen.displayMessageLine( "\nWelcome!" );
      screen.displayMessage( "\nPlease enter your account number: " );
      //performTransactions(); // user is now authenticated
      //userAuthenticated = false; // reset before next ATM session
      //currentAccountNumber = 0; // reset before next ATM session
      //GlobalState.ATMState = "login"; // reset UI state back to login
      //screen.displayMessageLine( "\nThank you! Goodbye!" );
   } // end method run

   // attempts to authenticate user against database
   private int accountNumber = 0;
   private boolean isPinInput = false;
   private void authenticateUser(int input)
   {
      if (!isPinInput) {
         // Expecting account number input
         accountNumber = input; // Set the account number from input
         screen.displayMessage("\nEnter your PIN: ");
         isPinInput = true; // Change state to expect PIN next
      } else {
         // Expecting PIN input
         int pin = input; // Set the pin from input

         // Authenticate user with account number and pin
         userAuthenticated = bankDatabase.authenticateUser(accountNumber, pin);

         // Check whether authentication succeeded
         if (userAuthenticated) {
            currentAccountNumber = accountNumber; // Save user's account #
            screen.displayMessageLine("Authentication successful.");
            accountNumber = 0; // Reset account number
            isPinInput = false; // Reset flag for next authentication attempt
            userAuthenticated = true; // Reset authentication status
         } else {
            screen.displayMessageLine("Invalid account number or PIN. Please try again.");
            accountNumber = 0; // Reset account number
            isPinInput = false; // Reset flag for next authentication attempt
            userAuthenticated = false; // Reset authentication status
         }
      }
   } // end method authenticateUser

   // display the main menu and perform transactions
   private void performTransactions()
   {
      // local variable to store transaction currently being processed
      Transaction currentTransaction = null;

      boolean userExited = false; // user has not chosen to exit

      // loop while user has not chosen option to exit system
      while ( !userExited )
      {
         // show main menu and get user selection
         int mainMenuSelection = displayMainMenu();

         // decide how to proceed based on user's menu selection
         switch ( mainMenuSelection )
         {
            // user chose to perform one of three transaction types
            case BALANCE_INQUIRY:
            case WITHDRAWAL:
            case TRANSFER:

               // initialize as new object of chosen type
               currentTransaction =
                       createTransaction( mainMenuSelection );

               currentTransaction.execute(); // execute transaction
               break;
            case EXIT: // user chose to terminate session
               screen.displayMessageLine( "\nExiting the system..." );
               userExited = true; // this ATM session should end
               break;
            default: // user did not enter an integer from 1-4
               screen.displayMessageLine(
                       "\nYou did not enter a valid selection. Try again." );
               break;
         } // end switch
      } // end while
   } // end method performTransactions

   // display the main menu and return an input selection
   private int displayMainMenu()
   {
      screen.displayMessageLine( "\nMain menu:" );
      screen.displayMessageLine( "1 - View my balance" );
      screen.displayMessageLine( "2 - Withdraw cash" );
      screen.displayMessageLine( "3 - Transfer" );
      screen.displayMessageLine( "4 - Exit\n" );
      screen.displayMessage( "Enter a choice: " );
      return keypad.getPositiveInteger(); // return user's selection
   } // end method displayMainMenu

   // return object of specified Transaction subclass
   private Transaction createTransaction(int type )
   {
      Transaction temp = null; // temporary Transaction variable

      // determine which type of Transaction to create     
      switch ( type )
      {
         case BALANCE_INQUIRY: // create new BalanceInquiry transaction
            temp = new BalanceInquiry(
                    currentAccountNumber, screen, bankDatabase );
            break;
         case WITHDRAWAL: // create new Withdrawal transaction
            temp = new Withdrawal( currentAccountNumber, screen,
                    bankDatabase, keypad, cashDispenser );
            break;
         case TRANSFER:
            temp = new Transfer(
                    currentAccountNumber, screen, bankDatabase, keypad);
            break;
      } // end switch

      return temp; // return the newly created object
   } // end method createTransaction


   private void handler(String text){
      if("login".equals(GlobalState.ATMState)) {
          int input = 0;
          try {
              input = Integer.parseInt(text);
          } catch (NumberFormatException e) {
            return;
          }
          authenticateUser(input);
      }
   }
} // end class ATM
