import Lights.CarsLight;
import Lights.PeopleLight;
import Other.Event64;

public class CroosRoadControler extends Thread
{
    enum Group{GroupA,GroupB,GroupC}
    enum GroupState{InGreen,WaitintForRed,WaitingAdditionalSecond}
    Group outState;
    GroupState inState;

    CarsLight[] GroupA;
    CarsLight[] GroupB;
    CarsLight[] GroupC;

    PeopleLight[] GroupApeople;
    PeopleLight[] GroupBpeople;
    PeopleLight[] GroupCpeople;


    Event64[] evTogreen ,evToRed , evToShabat,evToChol ,evAtRed;

    public CroosRoadControler( CarsLight[] GroupA, CarsLight[] GroupB, CarsLight[] GroupC,PeopleLight[] GroupApeople,PeopleLight[] GroupBpeople,PeopleLight[] GroupCpeople,
                               Event64[] evTogreen,Event64[] evToRed,Event64[] evToShabat,Event64[] evToChol,Event64[] evAtRed)
    {

        this.GroupA = GroupA;
        this.GroupB = GroupB;
        this.GroupC = GroupC;
        this.GroupApeople = GroupApeople;
        this.GroupBpeople = GroupBpeople;
        this.GroupCpeople = GroupCpeople;//
        this.evTogreen=evTogreen;
        this.evToRed=evToRed;
        this.evToShabat=evToShabat;
        this.evToChol=evToChol;
        this.evAtRed=evAtRed;
        start();
    }

    public void run()
    {
        try
        {
            switch (outState)
            {
                case GroupA:
                    switch (inState)
                    {
                        case InGreen:
                            
                    }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
