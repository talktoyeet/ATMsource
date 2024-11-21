import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ATMTextField extends JTextField {

    String userInput = "";
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
        setText(userInput); // Update text field display
    }

    public void clearInput() {
        userInput = ""; // Clear stored input
        this.setText("");
    }

    public String getUserInput() {
        return userInput; // Return current input as string
    }

}
