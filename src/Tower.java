import bagel.DrawOptions;
import bagel.Image;
import bagel.Input;
import bagel.util.Point;
import bagel.util.Rectangle;
import bagel.util.Vector2;

/**
 * Represents a generic tower class
 */
public abstract class Tower {
    public int DAMAGE;
    private final Image image;
    private final Point towerLocation;
    private final double pointX;
    private final double pointY;
    private double angle = 0;
    public double range;
    public boolean hasTarget = false;
    private Sprite target;
    public int waitTime = 0;
    public int COOLDOWN;
    public boolean FiredProjectile = false;

    /**
     *
     * Creates a new tower at a given location
     */
    public Tower(String imageLocation, Point point){
        this.image = new Image(imageLocation);
        towerLocation = point;
        pointX = point.x;
        pointY = point.y;


    }

    /**
     * Check if an enemy is within the towers range
     * @param enemy
     * @return
     */
    public boolean withinRange(Sprite enemy){
        Point point = enemy.getCenter();

        double distance = towerLocation.distanceTo(point);
        if (distance <= range){
            return true;
        }

        return false;
    }

    /**
     * Lock on to a target
     * @param enemy
     */
    public void lockOn(Sprite enemy){
        this.target = enemy;
        this.hasTarget = true;
        if(waitTime <= 0) {
            fire();
        }

    }

    /**
     * Update the image and position of tank
     */
    public void update(){
        this.FiredProjectile = false;
        if(waitTime >= 0){
            this.waitTime = this.waitTime - ShadowDefend.getTimescale();
        }
        if (hasTarget) {
            Point lockedPoint = target.getCenter();
            this.angle = Math.atan2(lockedPoint.y - pointY, lockedPoint.x - pointX);
        }
        image.draw(pointX, pointY, new DrawOptions().setRotation(angle + Math.PI/2));
        this.hasTarget = false;

    }

    /**
     * Fire a projectile
     */
    public void fire(){
        this.waitTime = COOLDOWN;
        this.FiredProjectile = true;
    }

    public int getDAMAGE(){return this.DAMAGE;}

    public Point location(){return this.towerLocation;}

    public Sprite getTarget(){return this.target;}




}
