package GUI;

import Lights.Ramzor;

import javax.swing.JFrame;
import Lights.*;

public class TrafficLightFrame extends JFrame
{
    private final int WIDTH = 800, HEIGHT = 750;
    public TrafficLightPanel myPanel;

    public TrafficLightFrame(String h, Ramzor[] ramzorim)
    {
        super(h);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocation(90, -15);
        myPanel = new TrafficLightPanel(ramzorim);
        add(myPanel);
        pack();
        setSize(WIDTH, HEIGHT);
        setResizable(false);
        setVisible(true);
    }
}

