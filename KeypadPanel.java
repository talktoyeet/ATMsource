import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class KeypadPanel extends JPanel {
    // Initialize variables
    JButton[] numberButtons = new JButton[13]; // Increased size to accommodate the decimal button
    JButton[] actionButtons = new JButton[4];
    private JPanel numberPanel = new JPanel();
    private JPanel actionPanel = new JPanel();
    private String userInput = "";

    KeypadPanel(ActionListener actionListener) {
        this.setLayout(new BorderLayout());

        // Number panel
        numberPanel.setPreferredSize(new Dimension(30, 40));
        numberPanel.setLayout(new GridLayout(4, 3));

        for (int i = 1; i < 10; i++) {
            numberButtons[i] = new JButton(String.valueOf(i));
            numberButtons[i].addActionListener(actionListener);
            numberPanel.add(numberButtons[i]);
        }

        // Button 0
        numberButtons[0] = new JButton("0");
        numberButtons[0].addActionListener(actionListener);

        // Button for decimal point
        numberButtons[10] = new JButton(".");
        numberButtons[10].addActionListener(actionListener);

        // Button for "00"
        numberButtons[11] = new JButton("00");
        numberButtons[11].addActionListener(actionListener);

        numberPanel.add(numberButtons[0]);
        numberPanel.add(numberButtons[10]);
        numberPanel.add(numberButtons[11]);

        // Action panel
        actionPanel.setPreferredSize(new Dimension(100, 400));
        actionPanel.setLayout(new GridLayout(4, 1));

        // Action buttons with colors
        actionButtons[0] = new JButton("Cancel");
        actionButtons[0].setBackground(Color.RED); // Set background color to red
        actionButtons[0].setForeground(Color.WHITE); // Set text color to white
        actionButtons[0].addActionListener(actionListener);

        actionButtons[1] = new JButton("Clear");
        actionButtons[1].setBackground(Color.YELLOW); // Set background color to yellow
        actionButtons[1].setForeground(Color.BLACK); // Set text color to black
        actionButtons[1].addActionListener(actionListener);

        actionButtons[2] = new JButton("Enter");
        actionButtons[2].setBackground(Color.GREEN); // Set background color to green
        actionButtons[2].setForeground(Color.WHITE); // Set text color to white
        actionButtons[2].addActionListener(actionListener);

        // Empty button
        actionButtons[3] = new JButton("");
        actionButtons[3].addActionListener(actionListener);

        // Add action buttons to action panel
        actionPanel.add(actionButtons[0]);
        actionPanel.add(actionButtons[1]);
        actionPanel.add(actionButtons[2]);
        actionPanel.add(actionButtons[3]);

        // Add panels to main panel
        this.add(numberPanel, BorderLayout.CENTER);
        this.add(actionPanel, BorderLayout.EAST);
    }

    // Method to disable all buttons
    public void disableAllButtons() {
        for (JButton button : numberButtons) {
            if (button != null) { // Check if button is not null
                button.setEnabled(false); // Disable the button
            }
        }

        for (JButton button : actionButtons) {
            if (button != null) { // Check if button is not null
                button.setEnabled(false); // Disable the button
            }
        }
    }

    // Method to enable all buttons
    public void enableAllButtons() {
        for (JButton button : numberButtons) {
            if (button != null) {
                button.setEnabled(true); // Enable the button
            }
        }

        for (JButton button : actionButtons) {
            if (button != null) {
                button.setEnabled(true); // Enable the button
            }
        }
    }
}
