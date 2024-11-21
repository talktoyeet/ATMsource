import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class KeypadPanel extends JPanel implements ActionListener {
    //initialize variables
    private JButton[] numberButtons = new JButton[12];
    private JButton[] actionButtons = new JButton[4];
    private JPanel numberPanel = new JPanel();
    private JPanel actionPanel = new JPanel();
    private String userInput = "";

    KeypadPanel(JTextField userInputField) {
        this.setLayout(new BorderLayout());

        //Number panel
        numberPanel.setPreferredSize(new Dimension(300, 400));
        numberPanel.setLayout(new GridLayout(4, 3));


        for (int i = 1; i < 10; i++) {
            numberButtons[i] = new JButton(String.valueOf(i));
            numberButtons[i].addActionListener(this);
            numberPanel.add(numberButtons[i]);
        }

        //Button 0
        numberButtons[0] = new JButton("0");
        numberButtons[0].addActionListener(this);

        //Empty buttons
        numberButtons[10] = new JButton("");
        numberButtons[10].addActionListener(this);
        numberButtons[11] = new JButton("");
        numberButtons[11].addActionListener(this);


        numberPanel.add(numberButtons[10]);
        numberPanel.add(numberButtons[0]);
        numberPanel.add(numberButtons[11]);

        //action panel
        actionPanel.setPreferredSize(new Dimension(100, 400));
        actionPanel.setLayout(new GridLayout(4, 1));
        actionButtons[0] = new JButton("Cancel");
        actionButtons[0].addActionListener(this);
        actionButtons[1] = new JButton("Clear");
        actionButtons[1].addActionListener(this);
        actionButtons[2] = new JButton("Enter");
        actionButtons[2].addActionListener(this);

        //empty button
        actionButtons[3] = new JButton("");
        actionButtons[3].addActionListener(this);

        actionPanel.add(actionButtons[0]);
        actionPanel.add(actionButtons[1]);
        actionPanel.add(actionButtons[2]);
        actionPanel.add(actionButtons[3]);

        this.add(numberPanel, BorderLayout.CENTER);
        this.add(actionPanel, BorderLayout.EAST);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        for (int i = 0; i < 10; i++) {
            if (source == numberButtons[i]) {
                userInput += String.valueOf(i); // Append the pressed number to userInput
                return;                                              
            }
        }
    }
}






