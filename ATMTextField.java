import javax.swing.*;
import java.awt.*;

public class ATMTextField extends JTextField {

    private String userInput = "";
    private boolean isPinInput = false; // Flag to indicate if we are in PIN input mode

    ATMTextField() {
        super();
        setEditable(false);
        setFont(new Font("Arial", Font.PLAIN, 18));
        setHorizontalAlignment(JTextField.RIGHT);
        setPreferredSize(new Dimension(300, 50));
        setBackground(Color.WHITE);
        setForeground(Color.BLACK);
    }

    public void appendInput(String input) {
        userInput += input; // Append new input
        if (isPinInput) {
            setText("*".repeat(userInput.length())); // Display asterisks for PIN input
        } else {
            setText(userInput); // Display actual account number input
        }
    }

    public void clearInput() {
        userInput = ""; // Clear stored input
        this.setText(""); // Clear the display
    }

    public String getUserInput() {
        return userInput; // Return current input as string
    }

    public void setPinInputMode(boolean isPinInput) {
        this.isPinInput = isPinInput; // Set the mode for PIN input
        if (!isPinInput) {
            clearInput(); // Clear input when switching back to account number mode
        }
    }
}
