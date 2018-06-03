package Other;

import Other.Event64;

import java.util.ArrayList;

public class CroosRoadControler extends Thread {

    enum HolShabat {ON_SHABAT, ON_CHOL}
    enum State {AInGreen,AToRed,AAtRed,cond1,BInGreen,BToRed,BAtRed,cond2,CInGreen,CToRed,CAtRed,cond3}

    HolShabat holShabat;
    State state;

    ArrayList<Integer> GroupA;
    ArrayList<Integer> GroupB;
    ArrayList<Integer> GroupC;

    Event64[] evTogreen, evToRed, evToShabat, evToChol, evAtRed;
    Event64 evBtnShabat,evBtnChol,evBtnRegel;
    Event64 evTimer;

    int timeInGreen = 3000;
    int timeInSecond = 1000;
    int ramzorNum;

    public CroosRoadControler(ArrayList<Integer> groupA, ArrayList<Integer> groupB,
                              ArrayList<Integer> groupC, Event64[] evTogreen, Event64[] evToRed, Event64[] evToShabat,
                              Event64[] evToChol, Event64[] evAtRed, Event64 evBtnShabat, Event64 evBtnChol, Event64 evBtnRegel)
    {
        GroupA = groupA;
        GroupB = groupB;
        GroupC = groupC;
        this.evTogreen = evTogreen;
        this.evToRed = evToRed;
        this.evToShabat = evToShabat;
        this.evToChol = evToChol;
        this.evAtRed = evAtRed;
        this.evBtnShabat = evBtnShabat;
        this.evBtnChol = evBtnChol;
        this.evBtnRegel = evBtnRegel;
        this.evTimer = evTimer;
        this.timeInGreen = timeInGreen;
        this.timeInSecond = timeInSecond;
        this.ramzorNum = ramzorNum;
        start();
    }

    public void run() {
        try {
            holShabat = HolShabat.ON_CHOL;

            while (true) {
                switch (holShabat) {
                    case ON_CHOL:
                        state = State.AInGreen;

                        doAGreen();

                        evTimer = new Event64();
                        new MyTimer72(timeInGreen,evTimer);
                        while (holShabat == HolShabat.ON_CHOL) {
                            switch (state) {
                                case AInGreen:
                                    while(true){
                                        if(evTimer.arrivedEvent())
                                        {
                                            evTimer.waitEvent();
                                            doARed();
                                            state = State.AToRed;
                                            break;
                                        }
                                        else if(evBtnShabat.arrivedEvent())
                                        {
                                            evBtnShabat.waitEvent();
                                            sendEvToShabat();
                                            holShabat = HolShabat.ON_SHABAT;
                                            break;
                                        }
                                        else
                                            yield();
                                    }
                                    break;
                                case AToRed:
                                    while(true){
                                        if(evGroupAArrived())
                                        {
                                            resetGroupAArrived();
                                            evTimer = new Event64();
                                            new MyTimer72(timeInSecond,evTimer);
                                            state = State.AAtRed;
                                            break;
                                        }
                                        else if(evBtnShabat.arrivedEvent())
                                        {
                                            evBtnShabat.waitEvent();
                                            sendEvToShabat();
                                            holShabat = HolShabat.ON_SHABAT;
                                            break;
                                        }
                                        else
                                            yield();
                                    }
                                    break;
                                case AAtRed:
                                    while(true){
                                        if(evTimer.arrivedEvent())
                                        {
                                            evTimer.waitEvent();
                                            doBGreen();
                                            evTimer = new Event64();
                                            new MyTimer72(timeInGreen,evTimer);
                                            state = State.BInGreen;
                                            break;
                                        }
                                        else if(evBtnShabat.arrivedEvent())
                                        {
                                            evBtnShabat.waitEvent();
                                            sendEvToShabat();
                                            holShabat = HolShabat.ON_SHABAT;
                                            break;
                                        }
                                        else if(evBtnRegel.arrivedEvent())
                                        {
                                            ramzorNum = ((int)evBtnRegel.waitEvent());
                                            evTimer = new Event64();
                                            new MyTimer72(timeInGreen,evTimer);
                                            state = State.cond1;
                                            break;
                                        }
                                        else
                                            yield();
                                    }
                                    break;
                                case cond1:
                                    if(isBWalker()) {
                                        state = State.BInGreen;
                                        doBGreen();
                                    }
                                    else if(isCWalker()) {
                                        state = State.CInGreen;
                                        doCGreen();
                                    }
                                    else if(isAWalker()) {
                                        state = State.AInGreen;
                                        doAGreen();
                                    }
                                case BInGreen:
                                    while(true){
                                        if(evTimer.arrivedEvent())
                                        {
                                            evTimer.waitEvent();
                                            doBRed();
                                            state = State.BToRed;
                                            break;
                                        }
                                        else if(evBtnShabat.arrivedEvent())
                                        {
                                            evBtnShabat.waitEvent();
                                            sendEvToShabat();
                                            holShabat = HolShabat.ON_SHABAT;
                                            break;
                                        }
                                        else
                                            yield();
                                    }
                                    break;
                                case BToRed:
                                    while(true){
                                        if(evGroupBArrived())
                                        {
                                            resetGroupBArrived();
                                            evTimer = new Event64();
                                            new MyTimer72(timeInSecond,evTimer);
                                            state = State.BAtRed;
                                            break;
                                        }
                                        else if(evBtnShabat.arrivedEvent())
                                        {
                                            evBtnShabat.waitEvent();
                                            sendEvToShabat();
                                            holShabat = HolShabat.ON_SHABAT;
                                            break;
                                        }
                                        else
                                            yield();
                                    }
                                    break;
                                case BAtRed:
                                    while(true){
                                        if(evTimer.arrivedEvent())
                                        {
                                            evTimer.waitEvent();
                                            doCGreen();
                                            evTimer = new Event64();
                                            new MyTimer72(timeInGreen,evTimer);
                                            state = State.CInGreen;
                                            break;
                                        }
                                        else if(evBtnShabat.arrivedEvent())
                                        {
                                            evBtnShabat.waitEvent();
                                            sendEvToShabat();
                                            holShabat = HolShabat.ON_SHABAT;
                                            break;
                                        }
                                        else if(evBtnRegel.arrivedEvent())
                                        {
                                            ramzorNum = ((int)evBtnRegel.waitEvent());
                                            evTimer = new Event64();
                                            new MyTimer72(timeInGreen,evTimer);
                                            state = State.cond2;
                                            break;
                                        }
                                        else
                                            yield();
                                    }
                                    break;
                                case cond2:
                                    if(isCWalker()) {
                                        state = State.CInGreen;
                                        doCGreen();
                                    }
                                    else if(isAWalker()) {
                                        state = State.AInGreen;
                                        doAGreen();
                                    }
                                    else if(isBWalker()) {
                                        state = State.BInGreen;
                                        doBGreen();
                                    }
                                case CInGreen:
                                    while(true){
                                        if(evTimer.arrivedEvent())
                                        {
                                            evTimer.waitEvent();
                                            doCRed();
                                            state = State.CToRed;
                                            break;
                                        }
                                        else if(evBtnShabat.arrivedEvent())
                                        {
                                            evBtnShabat.waitEvent();
                                            sendEvToShabat();
                                            holShabat = HolShabat.ON_SHABAT;
                                            break;
                                        }
                                        else
                                            yield();
                                    }
                                    break;
                                case CToRed:
                                    while(true){
                                        if(evGroupCArrived())
                                        {
                                            resetGroupCArrived();
                                            evTimer = new Event64();
                                            new MyTimer72(timeInSecond,evTimer);
                                            state = State.CAtRed;
                                            break;
                                        }
                                        else if(evBtnShabat.arrivedEvent())
                                        {
                                            evBtnShabat.waitEvent();
                                            sendEvToShabat();
                                            holShabat = HolShabat.ON_SHABAT;
                                            break;
                                        }
                                        else
                                            yield();
                                    }
                                    break;
                                case CAtRed:
                                    while(true){
                                        if(evTimer.arrivedEvent())
                                        {
                                            evTimer.waitEvent();
                                            doAGreen();
                                            evTimer = new Event64();
                                            new MyTimer72(timeInGreen,evTimer);
                                            state = State.AInGreen;
                                            break;
                                        }
                                        else if(evBtnShabat.arrivedEvent())
                                        {
                                            evBtnShabat.waitEvent();
                                            sendEvToShabat();
                                            holShabat = HolShabat.ON_SHABAT;
                                            break;
                                        }
                                        else if(evBtnRegel.arrivedEvent())
                                        {
                                            ramzorNum = ((int)evBtnRegel.waitEvent());
                                            evTimer = new Event64();
                                            new MyTimer72(timeInGreen,evTimer);
                                            state = State.cond3;
                                            break;
                                        }
                                        else
                                            yield();
                                    }
                                    break;
                                case cond3:
                                    if(isAWalker()) {
                                        state = State.AInGreen;
                                        doAGreen();
                                    }
                                    else if(isBWalker()) {
                                        state = State.BInGreen;
                                        doBGreen();
                                    }
                                    else if(isCWalker()) {
                                        state = State.CInGreen;
                                        doCGreen();
                                    }
                                    break;

                            }
                        }
                        holShabat = HolShabat.ON_SHABAT;
                    case ON_SHABAT:
                        while (holShabat == HolShabat.ON_SHABAT)
                        {
                            if(evBtnChol.arrivedEvent())
                            {
                                evBtnChol.waitEvent();
                                holShabat = HolShabat.ON_CHOL;
                                break;
                            }
                            yield();
                        }
                        sendEvToChol();
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

    private boolean evGroupAArrived(){
        boolean flag = true;
        for(int i: GroupA) {
            flag &= evAtRed[i].arrivedEvent();
        }
        return flag;
    }

    private boolean evGroupBArrived(){
        boolean flag = true;
        for(int i: GroupB) {
            flag &= evAtRed[i].arrivedEvent();
        }
        return flag;
    }

    private boolean evGroupCArrived(){
        boolean flag = true;
        for(int i: GroupC) {
            flag &= evAtRed[i].arrivedEvent();
        }
        return flag;
    }

    private void resetGroupAArrived() {
        for(int i : GroupA) {
            evAtRed[i].waitEvent();
        }
    }

    private void resetGroupBArrived() {
        for(int i : GroupB) {
            evAtRed[i].waitEvent();
        }
    }

    private void resetGroupCArrived() {
        for(int i : GroupC) {
            evAtRed[i].waitEvent();
        }
    }

    private boolean isAWalker()
    {
        if(this.GroupA.contains(ramzorNum))
            return true;
        else
            return false;
    }
    private boolean isBWalker()
    {
        if(this.GroupB.contains(ramzorNum))
            return true;
        else
            return false;
    }
    private boolean isCWalker()
    {
        if(this.GroupC.contains(ramzorNum))
            return true;
        else
            return false;
    }
}