package Other;

import Other.Event64;

import java.util.ArrayList;

public class CroosRoadControler extends Thread {

    enum HolShabat {ON_SHABAT, ON_CHOL}
    enum Group {GroupA, GroupB, GroupC}
    enum GroupState {InGreen, ToRed, AtRed}

    HolShabat holShabat;
    Group outState;
    GroupState inState;

    ArrayList<Integer> GroupA;
    ArrayList<Integer> GroupB;
    ArrayList<Integer> GroupC;

    Event64[] evTogreen, evToRed, evToShabat, evToChol, evAtRed;
    Event64 btnEvent;
    Event64 evTimer;

    int timeInGreen = 3000;
    int timeInSecond = 1000;

    public CroosRoadControler(ArrayList<Integer> GroupA, ArrayList<Integer> GroupB, ArrayList<Integer> GroupC, Event64[] evTogreen, Event64[] evToRed, Event64[] evToShabat, Event64[] evToChol, Event64[] evAtRed,Event64 btn) {

        this.GroupA = GroupA;
        this.GroupB = GroupB;
        this.GroupC = GroupC;
        this.evTogreen = evTogreen;
        this.evToRed = evToRed;
        this.evToShabat = evToShabat;
        this.evToChol = evToChol;
        this.evAtRed = evAtRed;
        this.btnEvent = btn;
        start();
    }

    public void run() {
        try {
            holShabat = HolShabat.ON_CHOL;

            while (true) {
                switch (holShabat) {
                    case ON_CHOL:
                        sendEvToChol();
                        outState = Group.GroupA;
                        inState = GroupState.InGreen;

                        while (holShabat == HolShabat.ON_CHOL) {
                            switch (outState) {
                                case GroupA:
                                    switch (inState) {
                                        case InGreen:
                                            doAGreen();
                                            //Thread.sleep(timeInGreen);
                                            evTimer = new Event64();
                                            new MyTimer72(timeInGreen,evTimer);
                                            inState = GroupState.ToRed;
                                            break;
                                        case ToRed:
                                            doARed();
                                            this.WaitAForRed();
                                            inState = GroupState.AtRed;
                                            break;
                                        case AtRed:
                                            //Thread.sleep(timeInSecond);
                                            evTimer = new Event64();
                                            new MyTimer72(timeInSecond,evTimer);
                                            if(isBWalker())
                                                outState = Group.GroupB;
                                            else if(isCWalker())
                                                outState = Group.GroupC;
                                            else if(isAWalker())
                                                outState = Group.GroupA;
                                            else
                                                outState = Group.GroupB;
                                            inState = GroupState.InGreen;
                                            break;
                                    }
                                    break;
                                case GroupB:
                                    switch (inState) {
                                        case InGreen:
                                            doBGreen();
                                            //Thread.sleep(timeInGreen);
                                            evTimer = new Event64();
                                            new MyTimer72(timeInGreen,evTimer);
                                            inState = GroupState.ToRed;
                                            break;
                                        case ToRed:
                                            doBRed();
                                            this.WaitBForRed();
                                            inState = GroupState.AtRed;
                                            break;
                                        case AtRed:
                                           // Thread.sleep(timeInSecond);
                                            evTimer = new Event64();
                                            new MyTimer72(timeInSecond,evTimer);
                                            if(isCWalker())
                                                outState = Group.GroupC;
                                            else if(isAWalker())
                                                outState = Group.GroupA;
                                            else if(isBWalker())
                                                outState = Group.GroupB;
                                            else
                                                outState = Group.GroupC;
                                            inState = GroupState.InGreen;
                                            break;
                                    }
                                    break;
                                case GroupC:
                                    switch (inState) {
                                        case InGreen:
                                            doCGreen();
                                            //Thread.sleep(timeInGreen);
                                            evTimer = new Event64();
                                            new MyTimer72(timeInGreen,evTimer);
                                            inState = GroupState.ToRed;
                                            break;
                                        case ToRed:
                                            doCRed();
                                            this.WaitCForRed();
                                            inState = GroupState.AtRed;
                                            break;
                                        case AtRed:
                                            //Thread.sleep(timeInSecond);
                                            evTimer = new Event64();
                                            new MyTimer72(timeInGreen,evTimer);
                                            if(isAWalker())
                                                outState = Group.GroupA;
                                            else if(isBWalker())
                                                outState = Group.GroupB;
                                            else if(isCWalker())
                                                outState = Group.GroupC;
                                            else
                                                outState = Group.GroupA;
                                            inState = GroupState.InGreen;
                                            break;
                                    }
                                    break;
                            }
                        }
                    case ON_SHABAT:
                        sendEvToShabat();
                }
            }
        } catch (Exception e) {
             System.out.println(e.getMessage());
        }
    }


    //********Handle lights functions********//

    private void doARed() {
        for (int i : GroupA) {
            evToRed[i].sendEvent();
        }
    }

    private void doBRed() {
        for (int i : GroupB) {
            evToRed[i].sendEvent();
        }
    }

    private void doCRed() {
        for (int i : GroupC) {
            evToRed[i].sendEvent();
        }
    }

    private void doAGreen() {
        for (int i : GroupA) {
            evTogreen[i].sendEvent();
        }
    }

    private void doBGreen() {
        for (int i : GroupB) {
            evTogreen[i].sendEvent();
        }
    }

    private void doCGreen() {
        for (int i : GroupC) {
            evTogreen[i].sendEvent();
        }
    }

    private void sendEvToChol(){
        for(Event64 ev : evToChol) {
            ev.sendEvent();
        }
    }

    private void sendEvToShabat() {
        for(Event64 ev : evToShabat) {
            ev.sendEvent();
        }
    }

    private void WaitAForRed() {
        for(int i : GroupA) {
            evAtRed[i].waitEvent();
        }
    }

    private void WaitBForRed() {
        for(int i : GroupB) {
            evAtRed[i].waitEvent();
        }
    }

    private void WaitCForRed() {
        for(int i : GroupC) {
            evAtRed[i].waitEvent();
        }
    }

    private boolean isAWalker()
    {
        if(btnEvent.arrivedEvent())
        {
            int RamzorNumber = (int)btnEvent.waitEvent();
            if(this.GroupA.contains(RamzorNumber))
                return true;
            else
                btnEvent.sendEvent(RamzorNumber);
        }
        return false;
    }
    private boolean isBWalker()
    {
        if(btnEvent.arrivedEvent())
        {
            int RamzorNumber = (int)btnEvent.waitEvent();
            if(this.GroupB.contains(RamzorNumber))
                return true;
            else
                btnEvent.sendEvent(RamzorNumber);
        }
        return false;
    }
    private boolean isCWalker()
    {
        if(btnEvent.arrivedEvent())
        {
            int RamzorNumber = (int)btnEvent.waitEvent();
            if(this.GroupC.contains(RamzorNumber))
                return true;
            else
                btnEvent.sendEvent(RamzorNumber);
        }
        return false;
    }

}