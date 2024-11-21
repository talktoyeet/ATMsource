import javax.swing.*;
import java.awt.*;

public class ATMTextField extends JTextField {
    ATMTextField() {
        super();
        setEditable(false);
        setFont(new Font("Arial", Font.PLAIN, 18));
        setHorizontalAlignment(JTextField.RIGHT);
        setPreferredSize(new Dimension(300, 50));
        setBackground(Color.WHITE);
        setForeground(Color.BLACK);
    }
}
