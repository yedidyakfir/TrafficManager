package Lights;

import java.awt.Color;
import Other.*;
import javax.swing.JPanel;


public class CarsLight extends Thread
{
    Ramzor ramzor;
    JPanel panel;
    Event64 evTogreen,evToRed,evToShabat,evToChol,evAtRed;
    int count;
    private boolean stop=true;

    enum OutState{ON_SHABAT,ON_CHOL}
    enum InState{ON_RED,ON_GREEN,ON_ORANGE,ON_YELLOW,YELLOW_ON,YELLOW_OFF,GREEN_BLINK}
    enum InInState{GREEN_ON,GREEN_OFF}
    OutState outState;
    InState inState;
    InInState inInState;

    public CarsLight( Ramzor ramzor,JPanel panel,int key,Event64 evTogreen,Event64 evToRed,Event64 evToShabat,Event64 evToChol,Event64 evAtRed)
    {
        this.ramzor=ramzor;
        this.panel=panel;
        this.evTogreen=evTogreen;
        this.evToRed=evToRed;
        this.evToShabat=evToShabat;
        this.evToChol=evToChol;
        this.evAtRed=evAtRed;
        start();
    }

    public void run()
    {
        try {
            outState = OutState.ON_CHOL;
            while (true) {
                switch (outState) {
                    case ON_CHOL:
                        SetToRed();
                        inState = InState.ON_RED;
                        while (outState == OutState.ON_CHOL) {
                            switch (inState) {
                                case ON_RED:
                                    while (true) {
                                        if (evTogreen.arrivedEvent()) {
                                            evTogreen.waitEvent();
                                            SetToOrange();
                                            inState = InState.ON_ORANGE;
                                            break;
                                        } else if (evToShabat.arrivedEvent()) {
                                            evToShabat.waitEvent();
                                            SetToGray();
                                            outState = OutState.ON_SHABAT;
                                            break;
                                        } else
                                            yield();
                                    }
                                    break;
                                case ON_GREEN:
                                    while (true) {
                                        if (evToRed.arrivedEvent()) {
                                            evToRed.waitEvent();
                                            inState = InState.GREEN_BLINK;
                                            break;
                                        }
                                        if (evToShabat.arrivedEvent()) {
                                            evToShabat.waitEvent();
                                            SetToGray();
                                            outState = OutState.ON_SHABAT;
                                            break;
                                        } else
                                            yield();
                                    }
                                    break;
                                case ON_ORANGE:
                                    while (true) {
                                        if (evToShabat.arrivedEvent()) {
                                            evToShabat.waitEvent();
                                            SetToGray();
                                            outState = OutState.ON_SHABAT;
                                            break;
                                        } else {
                                            sleep(1000);
                                            SetToGreen();
                                            inState = InState.ON_GREEN;
                                            break;
                                        }
                                    }
                                    break;
                                case ON_YELLOW:
                                    while (true) {
                                        if (evToShabat.arrivedEvent()) {
                                            evToShabat.waitEvent();
                                            SetToGray();
                                            outState = OutState.ON_SHABAT;
                                            break;
                                        } else {
                                            sleep(1000);
                                            SetToRed();
                                            evAtRed.sendEvent();
                                            inState = InState.ON_RED;
                                            break;
                                        }
                                    }
                                    break;
                                case GREEN_BLINK:
                                    SetGreenOn();
                                    count = 3;
                                    inInState = InInState.GREEN_ON;
                                    while (count > 0 && outState != OutState.ON_SHABAT) {
                                        switch (inInState) {
                                            case GREEN_ON:
                                                if (evToShabat.arrivedEvent()) {
                                                    evToShabat.waitEvent();
                                                    outState = OutState.ON_SHABAT;
                                                    SetToGray();
                                                } else if (count > 0) {
                                                    sleep(500);
                                                    SetGreenOff();
                                                    inInState = InInState.GREEN_OFF;
                                                }
                                                break;
                                            case GREEN_OFF:
                                                if (evToShabat.arrivedEvent()) {
                                                    evToShabat.waitEvent();
                                                    outState = OutState.ON_SHABAT;
                                                    SetToGray();
                                                } else {
                                                    sleep(500);
                                                    count--;
                                                    SetGreenOn();
                                                    inInState = InInState.GREEN_ON;
                                                }
                                                break;
                                        }
                                    }
                                    SetToYellow();
                                    inState = InState.ON_YELLOW;
                                    break;
                            }
                        }
                        break;
                    case ON_SHABAT:
                        SetYellowOn();
                        inState = InState.YELLOW_ON;
                        while (true) {
                            if (evToChol.arrivedEvent()) {
                                evToChol.waitEvent();
                                outState = OutState.ON_CHOL;
                                break;
                            }
                            switch (inState) {
                                case YELLOW_ON:
                                    sleep(1000);
                                    SetYellowOff();
                                    inState = InState.YELLOW_OFF;
                                    break;
                                case YELLOW_OFF:
                                    sleep(1000);
                                    SetYellowOn();
                                    inState = InState.YELLOW_ON;
                                    break;
                            }
                        }
                }
            }
        } catch (InterruptedException e) {}

    }
    public void setLight(int place, Color color)
    {
        ramzor.colorLight[place-1]=color;
        panel.repaint();
    }

    public boolean isStop()
    {
        return stop;
    }

    private void SetToGreen()
    {
        stop=false;
        setLight(1,Color.LIGHT_GRAY);
        setLight(2,Color.LIGHT_GRAY);
        setLight(2,Color.GREEN);
    }

    private void SetToRed()
    {
        setLight(1,Color.RED);
        setLight(2,Color.LIGHT_GRAY);
        setLight(3,Color.LIGHT_GRAY);
    }

    private void SetToGray()
    {
        stop=true;
        setLight(1,Color.LIGHT_GRAY);
        setLight(2,Color.LIGHT_GRAY);
        setLight(2,Color.LIGHT_GRAY);
    }

    private void SetToOrange() {
        setLight(2,Color.YELLOW);
    }

    private void SetToYellow()
    {
        stop = true;
        setLight(1,Color.LIGHT_GRAY);
        setLight(2,Color.YELLOW);
        setLight(3,Color.LIGHT_GRAY);
    }

    private void SetGreenOff()
    {
        setLight(3,Color.LIGHT_GRAY);
    }

    private void SetGreenOn()
    {
        setLight(3,Color.GREEN);
    }

    private void SetYellowOn()
    {
        setLight(2,Color.YELLOW);

    }

    private void SetYellowOff()
    {
        setLight(2,Color.LIGHT_GRAY);
    }




}

