import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CardPanel extends JPanel {
    public JButton insertCardButton = new JButton("Insert Card");
    public JButton ejectCardButton = new JButton(" Eject Card ");
    public CardPanel(ActionListener actionListener) {
        // Set layout for the panel
        this.setLayout(new FlowLayout());
        //this.setBackground(Color.LIGHT_GRAY);

        // Add action listener
        insertCardButton.addActionListener(actionListener);
        ejectCardButton.addActionListener(actionListener);

        // Add buttons to the panel
        this.add(insertCardButton);
        this.add(ejectCardButton);
    }
}
