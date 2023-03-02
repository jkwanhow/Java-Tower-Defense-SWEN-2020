import bagel.*;
import bagel.map.TiledMap;
import bagel.util.Point;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * ShadowDefend, a tower defence game.
 */
public class ShadowDefend extends AbstractGame {

    private static final int HEIGHT = 768;
    private static final int WIDTH = 1024;
    private static final String WAVE_FILE = "res/levels/waves.txt";
    private static final String MAP_FILE1 = "res/levels/1.tmx";
    private static final String MAP_FILE2 = "res/levels/2.tmx";
    // Change to suit system specifications. This could be
    // dynamically determined but that is out of scope.
    public static final double FPS = 60;
    // The spawn delay (in seconds) to spawn slicers
    private static double SPAWN_DELAY;
    //Initial was spelt wrong but number of corrections required to make to fix it will be a painnnnnn, or is it a pun?
    private static final int INTIAL_TIMESCALE = 1;
    private static int MAX_SLICERS;
    // Timescale is made static because it is a universal property of the game and the specification
    // says everything in the game is affected by this
    private static int timescale = INTIAL_TIMESCALE;
    private Wave currentWave;
    private List<Wave> waveList;
    private static Level LevelInfo;
    private TiledMap map;
    private List<Bomb> bombs;
    private List<Plane> planes;
    private List<Point> polyline;
    public List<Sprite> slicers;
    public List<Tower> towers;
    public List<Projectile> projectiles;
    private double frameCount;
    private double frameCountReal;
    private int spawnedSlicers;
    private boolean waveStarted;
    private int waveNumber = 0;
    private int withinWaveIndexer = 0;
    private boolean wait = false;
    private int waitPeriod;
    private int withinWaveIndexerCap;
    private boolean spawnState = false;
    private String unitType;
    private boolean ended = false;
    private final int LEVELS = 2;
    private int currentLevel = 1;



    public PlayerInformation player;
    private Shop shop;

    /**
     * Creates a new instance of the ShadowDefend game
     */
    public ShadowDefend(String MapFILE) throws FileNotFoundException {
        super(WIDTH, HEIGHT, "ShadowDefend");
        loadLevel(MapFILE);

    }

    /**
     * Kinda lazy method but it works for loading new levels... I think
     * @param MapFILE
     */
    public void loadLevel(String MapFILE){
        this.map = new TiledMap(MapFILE);
        this.polyline = map.getAllPolylines().get(0);
        this.bombs = new ArrayList<>();
        this.planes = new ArrayList<>();
        this.projectiles = new ArrayList<>();
        this.slicers = new ArrayList<>();
        this.towers = new ArrayList<>();
        this.player = new PlayerInformation();
        this.shop = new Shop(player);
        this.spawnedSlicers = 0;
        this.waveStarted = false;
        this.frameCount = Integer.MAX_VALUE;
        // Temporary fix for the weird slicer map glitch (might have to do with caching textures)
        // This fix is entirely optional
        // ^^^^ okay then ima keep it if you say so
        new Slicer(polyline, polyline.get(0));
    }

    /**
     * The entry-point for the game
     *
     * @param args Optional command-line arguments
     */
    public static void main(String[] args) throws FileNotFoundException {
        LevelInfo = new Level(WAVE_FILE);
        new ShadowDefend(MAP_FILE1).run();
    }

    public static int getTimescale() {
        return timescale;
    }

    /**
     * Increases the timescale
     */
    private void increaseTimescale() {
        timescale++;
    }

    /**
     * Decreases the timescale but doesn't go below the base timescale
     */
    private void decreaseTimescale() {
        if (timescale > INTIAL_TIMESCALE) {
            timescale--;
        }
    }

    private int tileXValue(double doubleX){
        //int xValue = ((int) doubleX) / TileWidth;

        return (int) doubleX;
    }

    private int tileYValue(double doubleY){
        //int yValue = ((int) doubleY) / TileHeight;

        return (int) doubleY;
    }


    /**
     * place a tower onto the map
     */
    private void placeTowerChecker(Input input){
        shop.getMousePoint(input);
        shop.toggleOff(input);
        Point position = input.getMousePosition();
        int TileX = tileXValue(input.getMouseX());
        int TileY= tileYValue(input.getMouseY());

        //This if statement handles the special case of towers to not be visible over blocked tiles
        if (!map.hasProperty(TileX, TileY, "blocked")){
            shop.buyingInteraction();
            if(input.wasPressed(MouseButtons.LEFT)) {
                if (shop.itemBeingPurchased.equals("tank")) {
                    player.playerMoney = player.playerMoney - shop.TANK_PRICE;
                    towers.add(new Tank(position));
                } else if (shop.itemBeingPurchased.equals("supertank")) {
                    player.playerMoney = player.playerMoney - shop.SUPERTANK_PRICE;
                    towers.add((new SuperTank(position)));

                }
                    shop.buying = false;
                }
            } //Planes just do whatever tbh and can be seen everywhere also this indentation is hideous
                 else if(shop.itemBeingPurchased.equals("plane")) {
                    shop.buyingInteraction();
                    if(input.wasPressed(MouseButtons.LEFT)) {
                        player.playerMoney = player.playerMoney - shop.PLANE_PRICE;
                        planes.add((new Plane(input, player)));
                        shop.buying = false;
                    }



        }
        return;
    }

    /**
     * Update the state of the game, potentially reading from input
     *
     * @param input The current mouse/keyboard state
     */
    @Override
    protected void update(Input input) {
        if ((waveNumber == LevelInfo.getLevelWave().size() - 1) && (withinWaveIndexer == withinWaveIndexerCap)){
            ended = true;
            spawnState = false;
            if(slicers.isEmpty()) {
                if(currentLevel == LEVELS){
                    player.playerStatus = "Winner!";
                }else {
                    currentLevel++;
                    loadLevel(MAP_FILE2);
                    ended = false;
                    withinWaveIndexer = 0;
                    waveNumber = 0;
                }
            }
        }
        player.waveNumber = waveNumber + 1;
        if (player.playerHealth <= 0){
            Window.close();
        }
       // System.out.println(frameCountReal);
        //Load an item from the wave list
        //Code is quite long for it maybe should be put as a method later... we will see
        //Maybe also consider renaming some variables later im not very creative i dunno
        //also this whole thing was so botched but I dont want to restructure it, I'm sorry...
        if(frameCountReal == 0 && waveStarted && waveNumber < LevelInfo.getLevelWave().size() && !ended){
            if (withinWaveIndexer >= withinWaveIndexerCap && waveNumber < LevelInfo.getLevelWave().size() - 1){
                spawnState = false;
                waveNumber++;
                currentWave = LevelInfo.getLevelWave().get(waveNumber);
                withinWaveIndexer = 0;
                withinWaveIndexerCap = currentWave.getContent().size();

            } else{

            }

            Object current = currentWave.getContent().get(withinWaveIndexer);
            if(Wave.isWait(current)){
                wait = true;
                WaitObject waiter = (WaitObject) current;
                waitPeriod = (waiter.getWaitTime() / 1000) * 60;
            }else{
                String enemyInfo[] = (String[]) current;
                MAX_SLICERS = Wave.getUnitInt(enemyInfo);
                unitType = Wave.getSlicerType(enemyInfo);
                SPAWN_DELAY = ((double) Wave.getWaitTime(enemyInfo))/1000;
                spawnState = true;

            }
            frameCount = 0;
            withinWaveIndexer++;
        }
        // Increase the frame counter by the current timescale
        frameCount += getTimescale();
        frameCountReal += getTimescale();

        // Draw map from the top left of the window
        map.draw(0, 0, 0, 0, WIDTH, HEIGHT);
        player.statusUpdate();
        shop.draw_menu();

        //Im not gonna lie this is really not a nice way of doing things down here
        if(shop.buying) {
            player.playerStatus = "Placing";
            placeTowerChecker(input);

        }else if (spawnState){
            player.playerStatus = "Wave In Progress";
        }else{
            player.playerStatus = "Awaiting Start";
        }
        shop.towerBuyCheck(input);


        // Handle key presses
        if (input.wasPressed(Keys.S) && waveStarted == false) {
            player.playerStatus = "Wave In Progress";
            frameCountReal = 0;
            frameCount = 0;
            waveStarted = true;
            currentWave = LevelInfo.getLevelWave().get(waveNumber);
            withinWaveIndexerCap = currentWave.getContent().size();

        }
        //increase game speed
        if (input.wasPressed(Keys.L) && timescale < 5) {
            increaseTimescale();
        }
        //decrease game speed
        if (input.wasPressed(Keys.K) && timescale > 1) {
            decreaseTimescale();
        }

        // Check if a waitObject or slicer has to be loaded
        if(wait && waveStarted){
            if (frameCount >= waitPeriod){
                frameCount = 0;
                frameCountReal = 0;
                wait = false;
            }
        }
        //Load a slicer if the conditions are right
        else if (spawnState && waveStarted && frameCount / FPS >= SPAWN_DELAY && spawnedSlicers != MAX_SLICERS) {
            if(unitType.equals("slicer")){
                slicers.add(new Slicer(polyline, polyline.get(0)));
            }else if(unitType.equals("superslicer")){
                slicers.add(new SuperSlicer(polyline, polyline.get(0)));
            }else if(unitType.equals("megaslicer")){
                slicers.add(new MegaSlicer(polyline, polyline.get(0)));
            }else if(unitType.equals("apexslicer")){
                slicers.add(new ApexSlicer(polyline, polyline.get(0)));
            }

            spawnedSlicers += 1;
            // Reset frame counter
            frameCount = 0;
            if (spawnedSlicers == MAX_SLICERS){
                frameCountReal = 0;
                spawnedSlicers = 0;
            }

        }

        // Update all sprites, and remove them if they've finished or killed
        for (int i = slicers.size() - 1; i >= 0; i--) {
            Sprite s = slicers.get(i);
            s.update(input);
            //for each tower compare if it is within range and lock on and fire depending if it is on
            //cool down
            for (int j = towers.size() - 1; j >= 0; j--){
                Tower t = towers.get(j);
                if (t.withinRange(s) && !t.hasTarget){
                    t.lockOn(s);
                }

            }
            //for each bomb on map if it detonated then damage slicers within range
            for (int j = bombs.size() - 1; j >= 0; j--){
                Bomb b = bombs.get(j);
                if (b.hasExploded() && b.withinRange(s)){
                    s.takeDamage(b.DAMAGE);
                }
            }
            //Actions performed when killing a slicer
            //This is also probably not the best method to do so an maybe it's not very reusable as code but it has
            //been done so ya.
            if (s.isDead()) {
                slicers.remove(i);
                player.addValue(s.getValue());
                //Death of superslicer
                if(s.getName().equals("superslicer")){
                    for (int spawns = 0; spawns < 2; spawns++) {
                        Sprite newEnemy = new Slicer(polyline, s.getCenter());
                        newEnemy.setTargetPointIndex(s.getTargetPointIndex());
                        slicers.add(newEnemy);
                    }
                //Death of meagaslicer
                }else if(s.getName().equals("megaslicer")){
                    for (int spawns = 0; spawns < 2; spawns++){
                        Sprite newEnemy = new SuperSlicer(polyline, s.getCenter());
                        newEnemy.setTargetPointIndex(s.getTargetPointIndex());
                        slicers.add(newEnemy);
                    }
                 //death of apexslicer
                }else if(s.getName().equals("apexslicer")) {
                    for (int spawns = 0; spawns < 4; spawns++) {
                        Sprite newEnemy = new MegaSlicer(polyline, s.getCenter());
                        newEnemy.setTargetPointIndex(s.getTargetPointIndex());
                        slicers.add(newEnemy);
                    }
                }

            }

            if (s.isFinished()) {
                player.playerHealth = player.playerHealth - s.getDamage();
                slicers.remove(i);
            }
        }
        // for each tower print it down and reduce the wait time
        for (int i = towers.size() - 1; i >= 0; i--) {
            Tower t = towers.get(i);
            if(t.FiredProjectile){
                projectiles.add(new Projectile(t.getDAMAGE(), t.location(), t.getTarget()));

            }
            t.update();
        }

        //update the planes check if they are done also check if it should drop a bomb
        for (int i = planes.size() - 1; i >= 0; i--){
            Plane z = planes.get(i);
            z.update();

            if(z.dropBomb){
                bombs.add(new Bomb(z.getPoint()));
                z.resetCooldown();
                z.dropBomb = false;
            }

            if (z.finished){
                planes.remove(i);
            }
        }

        // basically remove the bomb if it already exploded the damage handling is done within the slicer loop
        for (int i = bombs.size() - 1; i >= 0; i--){
            Bomb b = bombs.get(i);
            if (b.hasExploded()){
                bombs.remove(i);
            }
            b.update();


        }

        //update projectiles if they are on the map
        for (int i = projectiles.size() - 1; i >= 0; i--){
            Projectile p = projectiles.get(i);
            p.update();

            if (p.isFinished()){
                projectiles.remove(i);
            }
        }
    }
}
