import bagel.DrawOptions;
import bagel.Image;
import bagel.Input;
import bagel.util.Point;
import bagel.util.Rectangle;
import bagel.util.Vector2;

/**
 * I also feel like the spite's childrend have a lot of repeated code that they dont need
 * especially for the update. :/
 */

/**
 * Represents a game entity
 */
public abstract class Sprite {

    private final Image image;
    private final Rectangle rect;
    private double angle;
    private boolean finished;
    private boolean dead;
    private int health;
    public int VALUE;
    public int targetPointIndex;
    public String NAME;
    private int DAMAGE;

    /**
     * Creates a new Sprite (game entity)
     *
     * @param point    The starting point for the entity
     * @param imageSrc The image which will be rendered at the entity's point
     */
    public Sprite(Point point, String imageSrc) {
        this.image = new Image(imageSrc);
        this.rect = image.getBoundingBoxAt(point);
        this.angle = 0;
    }


    public Rectangle getRect() {
        return new Rectangle(rect);
    }

    /**
     * Moves the Sprite by a specified delta
     *
     * @param dx The move delta vector
     */
    public void move(Vector2 dx) {
        rect.moveTo(rect.topLeft().asVector().add(dx).asPoint());
    }

    public Point getCenter() {
        return getRect().centre();
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    /**
     * Updates the Sprite. Default behaviour is to render the Sprite at its current position.
     */
    public void update(Input input) {
        image.draw(getCenter().x, getCenter().y, new DrawOptions().setRotation(angle));
    }

    public boolean isFinished() {
        return finished;
    }

    public void takeDamage(int Damage){this.health--;}

    public boolean isDead(){return dead;}

    public int getHealth(){return this.health;}

    public int getValue(){return VALUE;}

    public int getTargetPointIndex(){return targetPointIndex;}

    public String getName(){return this.NAME;}

    public void setTargetPointIndex(int target){this.targetPointIndex = target;}

    public int getDamage(){ return this.DAMAGE;}


}