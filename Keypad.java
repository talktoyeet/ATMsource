// Keypad.java
// Represents the keypad of the ATM
import java.util.Scanner; // program uses Scanner to obtain user input

public class Keypad {
    private Scanner input; // reads data from the command line

    // no-argument constructor initializes the Scanner
    public Keypad() {
        input = new Scanner(System.in);
    } // end no-argument Keypad constructor

    // return a positive integer value entered by user 
    public int getPositiveInteger() {
        while (true) {
            String userInput = input.nextLine();

            if (userInput.equalsIgnoreCase("q")) {
                System.out.println("\nExiting the program.");
                System.exit(0); // terminate the program
            }

            try {
                int value = Integer.parseInt(userInput);
                if (value >= -1) {
                    return value; // return the positive integer
                } else {
                    System.out.println("Please enter a positive integer.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a positive integer.");
            }
        }
    } // end method getPositiveInteger

    // return a positive decimal value entered by user with up to two decimal places
    public double getPositiveDecimal() {
        while (true) {
            String userInput = input.nextLine();

            if (userInput.equalsIgnoreCase("q")) {
                System.out.println("Exiting the program.");
                System.exit(0); // terminate the program
            }

            // Validate decimal input with up to two decimal places
            if (userInput.matches("^[0-9]+(\\.[0-9]{1,2})?$")) {
                double value = Double.parseDouble(userInput);
                if (value >= 0) {
                    return value; // return the positive decimal
                } else {
                    System.out.println("Please enter a positive decimal number.");
                }
            } else {
                System.out.println("Invalid input. Please enter a positive decimal number with up to 2 decimal places.");
            }
        }
    } // end method getPositiveDecimal
} // end class Keypad
