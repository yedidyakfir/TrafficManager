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
                        doAGreen();
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
}