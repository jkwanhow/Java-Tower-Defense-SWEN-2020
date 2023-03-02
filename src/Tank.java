import bagel.Image;
import bagel.util.Point;

public class Tank extends Tower {
    private static final String location = "res/images/tank.png";
    private double range = 100;
    public int DAMAGE = 1;
    private int COOLDOWN = 60;


    private double angle = 0;


    public Tank(Point point){
        super(location, point);
        super.range = range;
        super.COOLDOWN = COOLDOWN;
        super.DAMAGE = DAMAGE;

    }

    //JUST WORKS LIKE THIS DUNNO WHAT ELSE TO DO
    @Override
    public void fire(){
        super.waitTime = COOLDOWN;
        super.FiredProjectile = true;
    }

}
