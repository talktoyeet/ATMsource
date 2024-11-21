import javax.swing.*;

public class ATMFrame extends JFrame {
    // Static variable to hold the single instance of ATMFrame
    private static ATMFrame instance;

    // Initiate components
    private static ATMTextField textField = new ATMTextField();
    private KeypadPanel keypadPanel = new KeypadPanel(textField);
    private static ATMMessageField messageField = new ATMMessageField();

    private ATMFrame() {
        // Set Frame dimension
        this.setSize(500, 600);
        this.setLayout(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Set bounds for components
        messageField.setBounds(50, 50, 400, 100);
        textField.setBounds(50, 160, 400, 50);
        keypadPanel.setBounds(50, 220, 400, 300);

        // Add components
        this.add(messageField);
        this.add(textField);
        this.add(keypadPanel);

        // Set visible
        this.setVisible(true);
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
}
