package Other;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JRadioButton;


public class MyActionListener implements ActionListener
{
    private Event64 evShabat,evChol,evRegel;

    public MyActionListener(Event64 evShabat, Event64 evChol, Event64 evRegel) {
        this.evShabat = evShabat;
        this.evChol = evChol;
        this.evRegel = evRegel;
    }

    public void actionPerformed(ActionEvent e)
    {
        JRadioButton butt=(JRadioButton)e.getSource();
        if(butt.getName().equals("16"))
        {
            if(butt.isSelected())
                evShabat.sendEvent();
            else
                evChol.sendEvent();
        }
        else
            evRegel.sendEvent(butt.getName());
        System.out.println(butt.getName());
        //		butt.setEnabled(false);
        //		butt.setSelected(false);
    }

}
