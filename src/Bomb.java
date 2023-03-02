import bagel.Image;
import bagel.util.Point;

/**
 * BOMB ya it's the bomb class
 */

public class Bomb {
    private final Image image = new Image("res/images/explosive.png");
    private final Point BOMB_LOCATION;
    private final double BOMB_RADIUS = 200;
    private int bombLife = 120;
    private boolean exploded = false;
    public final int DAMAGE = 25;


    public Bomb(Point point){
        this.image.draw(point.x, point.y);
        this.BOMB_LOCATION = point;
    }


    public void update(){
        if(exploded){
            return;
        }
        if (bombLife <= 0){
            exploded = true;
        }
        image.draw(BOMB_LOCATION.x, BOMB_LOCATION.y);
        bombLife = bombLife - ShadowDefend.getTimescale();
    }

    public boolean hasExploded(){return this.exploded;}

    /**
     * for a given Slicer find if it is in blast radius
     * @param enemy
     * @return
     */
    public boolean withinRange(Sprite enemy){
        Point point = enemy.getCenter();

        double distance = BOMB_LOCATION.distanceTo(point);
        if (distance <= BOMB_RADIUS){
            return true;
        }

        return false;
    }
}
