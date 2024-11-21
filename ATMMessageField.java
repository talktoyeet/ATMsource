import javax.swing.*;
import java.awt.*;

public class ATMMessageField extends JTextArea {
    ATMMessageField() {
        this.setEditable(false); // Make it read-only
        this.setFont(new Font("Arial", Font.PLAIN, 16));
        this.setLineWrap(true);
        this.setWrapStyleWord(true);
    }

    // Method to append action messages
    public void appendMessage(String message) {
        this.append(message + "\n");
    }

    // Method to clear all messages
    public void clearScreen(){
        this.selectAll();
        this.replaceSelection("");
    }


    public ATMMessageField getDisplay(){
        return this;
    }
}
