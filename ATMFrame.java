import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ATMFrame extends JFrame implements ActionListener {
    // Static variable to hold the single instance of ATMFrame
    private static ATMFrame instance;

    // Initiate components
    static final ATMTextField textField = new ATMTextField();
    static KeypadPanel keypadPanel;
    static final ATMMessageField messageField = new ATMMessageField();

    // Initiate variables
    static String finalInput = "0";
    private static boolean waitingForInput = false;

    private ATMFrame() {
        keypadPanel = new KeypadPanel(this);
        // Set Frame dimension
        this.setSize(500, 600);
        this.setSize(1000, 1000); // Increased size for more space
        this.setLayout(null); // Use null layout
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Set bounds for components
        messageField.setBounds(50, 50, 900, 300); // Large enough for approximately 15 lines
        textField.setBounds(50, 360, 900, 50); // Positioned below the message field
        keypadPanel.setBounds(50, 420, 900, 500); // Positioned below the text field

        // Add components
        this.add(messageField);
        this.add(textField);
        this.add(keypadPanel);

        // Set visible
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        for (int i = 0; i < 10; i++) {
            if (source == keypadPanel.numberButtons[i]) { // Check if a number button was pressed
                textField.appendInput(String.valueOf(i)); // Append pressed number to userInput
                return;
            }
        }

        if (source == keypadPanel.actionButtons[0]) { // Cancel Button
            textField.clearInput();

        } else if (source == keypadPanel.actionButtons[1]) { // Clear Button
            textField.clearInput();

        } else if (source == keypadPanel.actionButtons[2]) { // Enter Button
            if (waitingForInput) { // Only process if waiting for input
                processInput(); // Call method to process the input when Enter is pressed
            }
        }
    }

    // Public method to provide access to the instance
    public static ATMFrame getInstance() {
        if (instance == null) {
            instance = new ATMFrame();
        }
        return instance;
    }

    // Append MessageField
    public static void appendMessage(String text) {
        messageField.appendMessage(text);
    }

    // Set and clear MessageField
    public static void setAndClearMessageField(String text) {
        messageField.clearScreen();
        messageField.appendMessage(text);
    }

    private void processInput() {
            String inputValue = textField.getUserInput();
            finalInput = inputValue;
            waitingForInput = false; // Stop waiting for input after processing
            textField.clearInput();
        }

    public static int getInt() {
        waitingForInput = true; // Set flag indicating we're waiting for input
        while (waitingForInput) { // Loop until we receive valid input
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return Integer.parseInt(finalInput); // Return parsed integer
    }
}

