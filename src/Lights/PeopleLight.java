package Lights;

import java.awt.Color;
import javax.swing.JPanel;
import Other.*;

public class PeopleLight extends Thread
{
    Ramzor ramzor;
    JPanel panel;
    Event64 evTogreen,evToRed,evToShabat,evToChol,evAtRed;

    enum OutState{ON_SHABAT,ON_CHOL}
    enum InState{ON_RED,ON_GREEN}
    OutState outState;
    InState inState;

    public PeopleLight( Ramzor ramzor,JPanel panel,Event64 evTogreen,Event64 evToRed,Event64 evToShabat,Event64 evToChol,Event64 evAtRed )
    {
        this.ramzor=ramzor;
        this.panel=panel;
        this.evToChol=evToChol;
        this.evTogreen=evTogreen;
        this.evToRed=evToRed;
        this.evToShabat=evToShabat;
        this.evAtRed=evAtRed;
        start();
    }

    public void run() {
        try {
            outState = OutState.ON_CHOL;
            while (true) {
                switch (outState) {
                    case ON_CHOL:
                        inState = InState.ON_RED;
                        SetToRed();
                        while (outState == OutState.ON_CHOL) {
                            switch (inState) {
                                case ON_GREEN:
                                    while (true) {
                                        if (evToRed.arrivedEvent()) {
                                            evToRed.waitEvent();
                                            SetToRed();
                                            evAtRed.sendEvent();
                                            inState = InState.ON_RED;
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
                                case ON_RED:
                                    while (true) {
                                        if (evTogreen.arrivedEvent()) {
                                            evTogreen.waitEvent();
                                            SetToGreen();
                                            inState = InState.ON_GREEN;
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
                            }
                        }
                    case ON_SHABAT:
                        evToChol.waitEvent();
                        outState = OutState.ON_CHOL;
                        break;
                }
            }
        } catch (Exception e) {}
    }


    private void SetToGreen()
    {
        setLight(1,Color.GRAY);
        setLight(2,Color.GREEN);
    }

    private void SetToRed()
    {
        setLight(1,Color.GRAY);
        setLight(2,Color.GREEN);
    }

    private void SetToGray()
    {
        setLight(1,Color.GRAY);
        setLight(2,Color.GRAY);
    }

    public void setLight(int place, Color color)
    {
        ramzor.colorLight[place-1]=color;
        panel.repaint();
    }

}
