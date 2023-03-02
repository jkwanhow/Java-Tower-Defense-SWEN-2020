import bagel.Image;
import bagel.util.Point;
import bagel.util.Rectangle;
import bagel.util.Vector2;

/**
 * Projectile c;ass
 */
public class Projectile {
    private final Image PROJECTILE_IMAGE = new Image("res/images/tank_projectile.png");
    private final int DAMAGE;
    private final Sprite TARGET;
    private Rectangle rectangle;
    private Point position;
    private final int SPEED = 10;
    private boolean finished = false;

    /**
     * Create a projectile draw it at the middle of the tower
     * @param Damage
     * @param start
     * @param target
     */
    public Projectile(int Damage, Point start, Sprite target){
        this.DAMAGE = Damage;
        this.position = start;
        this.TARGET = target;
        PROJECTILE_IMAGE.draw(position.x, position.y);
        updateRectangle();

    }

    /**
     * update the position of the rectangle
     */
    private void updateRectangle(){
       this.rectangle = PROJECTILE_IMAGE.getBoundingBoxAt(position);
    }

    /**
     * find the vector path towards the target
     * @param endingPoint
     * @return
     */
    public Vector2 getVectorPath(Point endingPoint){
        Vector2 currentVector = this.position.asVector();
        Vector2 endVector = endingPoint.asVector();
        Vector2 path = endVector.sub(currentVector);
        return path;
    }

    /**
     * Move towards the target
     */
    public void travelVectorPath(){
        Vector2 vectorPath = getVectorPath(TARGET.getCenter()).normalised();
        Vector2 currentVector = this.position.asVector();
        currentVector = currentVector.add(vectorPath.mul(10));
        this.position = currentVector.asPoint();

    }

    /**
     * Move towards target and check for collision
     */
    public void update(){
        travelVectorPath();
        PROJECTILE_IMAGE.draw(position.x, position.y);
        updateRectangle();

        if (TARGET.getRect().intersects(rectangle)){
            TARGET.takeDamage(DAMAGE);
            this.finished = true;
        }

    }

    public boolean isFinished(){return finished;}


}
