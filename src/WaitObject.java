/**
 * Because having a simple integer to hold a wait command is boring
 */
public class WaitObject {
    private final int waitTime;

    public WaitObject(int time){
        this.waitTime = time;
    }

    public int getWaitTime() {
        return this.waitTime;
    }
}
