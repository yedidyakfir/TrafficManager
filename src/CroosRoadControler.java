
public class CroosRoadControler extends Thread
{
    enum Group{GroupA,GroupB,GroupC}
    enum GroupState{InGreen,WaitintForRed,WaitingAdditionalSecond}
    Group outState;
    GroupState inState;

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
