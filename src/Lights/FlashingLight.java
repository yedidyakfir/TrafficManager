package Lights;

import javax.swing.*;
import java.awt.*;

public class FlashingLight extends Thread
{
    Ramzor ramzor;
    JPanel panel;
    enum State {ON,OFF}
    State state = State.OFF;

    public FlashingLight(Ramzor ramzor,JPanel panel)
    {
        this.ramzor=ramzor;
        this.panel=panel;
        start();
    }

    public void run()
    {
        try
        {
            SetOn();
            while (true)
            {
                switch (state) {
                    case ON:
                        sleep(1000);
                        SetOff();
                        state=State.OFF;
                        break;
                    case OFF:
                        sleep(1000);
                        SetOn();
                        state=State.ON;
                        break;
                }
            }
        } catch (InterruptedException e) {}

    }
    public void setLight(int place, Color color)
    {
        ramzor.colorLight[place-1]=color;
        panel.repaint();
    }
    private void SetOn() {
        setLight(1,Color.YELLOW);
    }
    private void SetOff() {
        setLight(1,Color.GRAY);
    }
}
