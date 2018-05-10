package Lights;

import javax.swing.*;
import java.awt.*;

public class FlashingLight extends Thread
{
    private Ramzor ramzor;
    private JPanel panel;
    private enum State {ON,OFF}
    private State state = State.OFF;

    private int timeToChange = 1000;//how long until the light will change

    public FlashingLight(Ramzor ramzor,JPanel panel)
    {
        this.ramzor = ramzor;
        this.panel = panel;
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
                        sleep(timeToChange);
                        SetOff();
                        state = State.OFF;
                        break;
                    case OFF:
                        sleep(timeToChange);
                        SetOn();
                        state = State.ON;
                        break;
                }
            }
        } catch (InterruptedException e) {}

    }
    public void setLight(int place, Color color)
    {
        ramzor.colorLight[place-1] = color;
        panel.repaint();
    }
    private void SetOn() {
        setLight(1,Color.YELLOW);
    }
    private void SetOff() {
        setLight(1,Color.GRAY);
    }
}
