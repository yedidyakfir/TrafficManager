package Other;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JRadioButton;


public class MyActionListener implements ActionListener
{
    public void actionPerformed(ActionEvent e)
    {
        JRadioButton butt=(JRadioButton)e.getSource();
        System.out.println(butt.getName());
        //		butt.setEnabled(false);
        //		butt.setSelected(false);
    }

}
