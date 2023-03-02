import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.File;
/**
 * Read the level file and parse all the information into
 * a list of waves
 */
public class Level {

    List<Wave> levelContent;
    private Wave currentWave;
    private int numberOfWaves = 0;

    /**
     * Reads the whole file in the constructor by taking in the filepath
     */
    public Level(String levelFile) throws FileNotFoundException {
        levelContent = new ArrayList<Wave>();
        File textFile = new File(levelFile);
        Scanner scannedLevel = new Scanner(textFile);
        //scan line by line and parse the information
        while (scannedLevel.hasNextLine()){
            String line = scannedLevel.nextLine();
            parseLine(line);
        }
        levelContent.add(currentWave);
    }

    /**
     *
     * for each line in the level file parses whether it is an enemy object or
     * a waitObject and depending on so ques it with on to the wave.
     */
    private void parseLine(String line){
        String splitLine[] = line.split(",");

        //if a new wave > 1 then add the previous wave into the list of waves
        int waveNumber = Integer.parseInt(splitLine[0]);
        if (waveNumber > numberOfWaves){
            if (waveNumber > 1){
                levelContent.add(currentWave);
            }

            currentWave = new Wave(waveNumber);
            numberOfWaves++;
        }

        String argument = splitLine[1];
        if (argument.equals("spawn")){
            //if a spawn argument is called adds slicers to the wave followed by waitObjects

            int numberOfEnemies = Integer.parseInt(splitLine[2]);
            String enemyType = splitLine[3];
            int waitTime = Integer.parseInt(splitLine[4]);

            currentWave.addEnemy(enemyType, numberOfEnemies, waitTime);


        }else if (argument.equals("delay")){
            // if a delay argument is called then adds the waitObject
            int waitTime = Integer.parseInt(splitLine[2]);
            currentWave.addWait(waitTime);

        }else{
            throw new IllegalArgumentException(argument + " is not a valid argument");
        }


    }

    /**
     * a getter for the wave
     */
    public List<Wave> getLevelWave() {

        return this.levelContent;
    }


}
