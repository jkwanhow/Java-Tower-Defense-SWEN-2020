import bagel.Input;
import bagel.util.Point;
import bagel.util.Vector2;
import java.util.List;

/**
 * A regular slicer.
 */

//I DONT GET HOW TO FUCKING EXTEND THEM TO GETA THE METHODFS!!!
public class SuperSlicer extends Sprite {

    private static final String IMAGE_FILE = "res/images/superslicer.png";
    private static final double SPEED = 0.75;
    public static final int VALUE = 15;
    private static final int DAMAGE = 2;

    private final List<Point> polyline;
    private int targetPointIndex;
    private boolean finished;
    private int health = 1;
    private boolean dead = false;
    public final String NAME = "superslicer";

    /**
     * Creates a new Slicer
     *
     * @param polyline The polyline that the slicer must traverse (must have at least 1 point)
     */
    public SuperSlicer(List<Point> polyline, Point currentPos) {
        super(currentPos, IMAGE_FILE);
        this.polyline = polyline;
        this.targetPointIndex = 1;
        this.finished = false;
    }

    @Override
    public void update(Input input) {
        if (health <= 0){
            this.dead = true;
        }
        if (dead){
            return;
        }
        if (finished) {
            return;
        }
        // Obtain where we currently are, and where we want to be
        Point currentPoint = getCenter();
        Point targetPoint = polyline.get(targetPointIndex);
        // Convert them to vectors to perform some very basic vector math
        Vector2 target = targetPoint.asVector();
        Vector2 current = currentPoint.asVector();
        Vector2 distance = target.sub(current);
        // Distance we are (in pixels) away from our target point
        double magnitude = distance.length();
        // Check if we are close to the target point
        if (magnitude < SPEED * ShadowDefend.getTimescale()) {
            // Check if we have reached the end
            if (targetPointIndex == polyline.size() - 1) {
                finished = true;
                return;
            } else {
                // Make our focus the next point in the polyline
                targetPointIndex += 1;
            }
        }
        // Move towards the target point
        // We do this by getting a unit vector in the direction of our target, and multiplying it
        // by the speed of the slicer (accounting for the timescale)
        super.move(distance.normalised().mul(SPEED * ShadowDefend.getTimescale()));
        // Update current rotation angle to face target point
        setAngle(Math.atan2(targetPoint.y - currentPoint.y, targetPoint.x - currentPoint.x));
        super.update(input);
    }


    @Override
    public boolean isFinished() {
        return finished;
    }
    @Override
    public void takeDamage(int Damage){this.health--;}
    @Override
    public boolean isDead(){return dead;}
    @Override
    public int getHealth(){return this.health;}
    @Override
    public int getValue(){return VALUE;}
    @Override
    public int getTargetPointIndex(){return targetPointIndex;}
    @Override
    public String getName(){return this.NAME;}
    @Override
    public void setTargetPointIndex(int target){this.targetPointIndex = target;}
    @Override
    public int getDamage(){ return this.DAMAGE;}

}
