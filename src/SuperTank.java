import bagel.util.Point;

public class SuperTank extends Tower {
    private static final String location = "res/images/supertank.png";
    private double range = 150;
    public int DAMAGE = 2;
    private int COOLDOWN = 30;


    private double angle = 0;


    public SuperTank(Point point){
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


