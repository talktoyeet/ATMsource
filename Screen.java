// Screen.java
// Represents the screen of the ATM

public class Screen {
    // displays a message without a carriage return
    public void displayMessage(String message) {
        System.out.print(message);
        ATMFrame.appendMessage(message);
    } // end method displayMessage

    // display a message with a carriage return
    public void displayMessageLine(String message) {
        System.out.println(message);
        ATMFrame.appendMessage(message);
    } // end method displayMessageLine

    public void clearScreen() {

        ATMFrame.messageField.clearScreen();

    }

    public void displayPercentage(double percentage) {
        System.out.printf("%.2f%%", percentage);
        ATMFrame.appendMessage(String.format("%.2f%%", percentage));
    }

    // display a dollar amount
    public void displayDollarAmount(double amount) {
        System.out.printf("HK$%,.2f", amount);
        ATMFrame.appendMessage(String.format("HK$%,.2f", amount));
    } // end method displayDollarAmount
    //
} // end class Screen
