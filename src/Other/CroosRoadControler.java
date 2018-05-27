package Other;

import Other.Event64;

public class CroosRoadControler extends Thread {

    enum HolShabat {ON_SHABAT, ON_CHOL}
    enum Group {GroupA, GroupB, GroupC}
    enum GroupState {InGreen, WaitintForRed, WaitingAdditionalSecond}

    HolShabat holShabat;
    Group outState;
    GroupState inState;

    Integer[] GroupA;
    Integer[] GroupB;
    Integer[] GroupC;

    Event64[] evTogreen, evToRed, evToShabat, evToChol, evAtRed;

    int timeInGreen = 10000;
    int timeInSecond = 1000;

    public CroosRoadControler(Integer[] GroupA, Integer[] GroupB, Integer[] GroupC, Event64[] evTogreen, Event64[] evToRed, Event64[] evToShabat, Event64[] evToChol, Event64[] evAtRed) {

        this.GroupA = GroupA;
        this.GroupB = GroupB;
        this.GroupC = GroupC;
        this.evTogreen = evTogreen;
        this.evToRed = evToRed;
        this.evToShabat = evToShabat;
        this.evToChol = evToChol;
        this.evAtRed = evAtRed;
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
                                            wait(timeInGreen);
                                            inState = GroupState.WaitintForRed;
                                            break;
                                        case WaitintForRed:
                                            doARed();
                                            this.WaitAForRed();
                                            inState = GroupState.WaitingAdditionalSecond;
                                            break;
                                        case WaitingAdditionalSecond:
                                            wait(timeInSecond);
                                            outState = Group.GroupB;
                                            inState = GroupState.InGreen;
                                            break;
                                    }
                                    break;
                                case GroupB:
                                    switch (inState) {
                                        case InGreen:
                                            doBGreen();
                                            wait(timeInGreen);
                                            inState = GroupState.WaitintForRed;
                                            break;
                                        case WaitintForRed:
                                            doBRed();
                                            this.WaitBForRed();
                                            inState = GroupState.WaitingAdditionalSecond;
                                            break;
                                        case WaitingAdditionalSecond:
                                            wait(timeInSecond);
                                            outState = Group.GroupC;
                                            inState = GroupState.InGreen;
                                            break;
                                    }
                                    break;
                                case GroupC:
                                    switch (inState) {
                                        case InGreen:
                                            doCGreen();
                                            wait(timeInGreen);
                                            inState = GroupState.WaitintForRed;
                                            break;
                                        case WaitintForRed:
                                            doCRed();
                                            this.WaitCForRed();
                                            inState = GroupState.WaitingAdditionalSecond;
                                            break;
                                        case WaitingAdditionalSecond:
                                            wait(timeInSecond);
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

    private boolean AWalker()
    {return true;}
    private boolean BWalker()
    {return true;}
    private boolean CWalker()
    {return true;}

}