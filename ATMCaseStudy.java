// ATMCaseStudy.java
// Driver program for the ATM case study

import javax.swing.*;

public class ATMCaseStudy
{
   // main method creates and runs the ATM
   public static void main( String[] args )
   {
      ATMFrame.getInstance();
      ATM theATM = new ATM();

      theATM.run();


   } // end main
} // end class ATMCaseStudy 
