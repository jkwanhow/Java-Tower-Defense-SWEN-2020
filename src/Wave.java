import java.util.ArrayList;
import java.util.List;

/**
 * holds contents of the wave
 */
public class Wave {
    List<Object> waveContent;
    private int waveNumber;

    /**
     * constructs wave with an associated wave number
     *
     */
    public Wave(int waveNumber){
        this.waveNumber = waveNumber;
        waveContent = new ArrayList<Object>();

    }

    /**
     *
     * Name is self explanatory
     */
    public void addWait(int waitTime){
        WaitObject delay = new WaitObject(waitTime);

        waveContent.add(delay);
    }

    /**
     *add an enemy, do it by giving it the enemy type, the number of enemies in the load and the delay between
     * units.
     */
    public void addEnemy(String slicerType, int count, int waitTime){
        String slicerContent[] = {String.valueOf(count), slicerType, String.valueOf(waitTime)};
        waveContent.add(slicerContent);
    }

    /**
     *checks if an object in wave is a waitObject if not pretty then it's probably a type of slicer
     */
    public static boolean isWait(Object item){
        if (item.getClass() == WaitObject.class){
            return true;
        }

        return false;
    }

    /**
     *
     *Static method that basically does a simple index
     */
    public static int getUnitInt(String enemyArray[]){
        int unitWaveCap = Integer.parseInt(enemyArray[0]);

        return unitWaveCap;
    }

    /**
     * get the unit type
     */
    public static String getSlicerType(String enemyArray[]){
        String unitType = enemyArray[1];

        return unitType;
    }

    /**
     *exactly the same as getUnitInt but for a different index
     */

    public static int getWaitTime(String enemyArray[]){
        int waitTime = Integer.parseInt(enemyArray[2]);

        return waitTime;
    }

    /**
     * Gets the "content" the good ol List of objects
     */
    public List<Object> getContent(){
        return this.waveContent;
    }

    /**
     * I honestly don't think i will ever use this but let's make it anyways
     */
    public int getWaveNumber(){
        return this.waveNumber;
    }


}
