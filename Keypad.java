import java.util.Scanner; // program uses Scanner to obtain user input

public class Keypad {
    private Scanner input; // reads data from the command line
    private boolean passwordChecked = false;

    // no-argument constructor initializes the Scanner
    public Keypad() {
        input = new Scanner(System.in);
    } // end no-argument Keypad constructor

    // return an integer value entered by user 
    public int getInput() {
        int userInput = 0;
        boolean validInput = false;

        while (!validInput) {
            System.out.print("\nPlease enter an integer  ");
            System.out.print("(or 'q' to quit):");
            String userInputString = input.nextLine(); // read user input as a string

            // Check if the user wants to quit
            if (userInputString.equalsIgnoreCase("q")) {
                System.out.println("Exiting the program.");
                System.exit(0); // terminate the program
            }

            try {
                userInput = Integer.parseInt(userInputString); // attempt to convert string to integer
                validInput = true; // input is valid, exit loop
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer."); // error message
            }
        }
        return userInput; // return the valid integer
    } // end method getInput
} // end class Keypad
