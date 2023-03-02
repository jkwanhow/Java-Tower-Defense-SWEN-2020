import bagel.DrawOptions;
import bagel.Image;
import bagel.Input;
import bagel.util.Point;

import java.util.Random;

public class Plane {
    private final Image image = new Image("res/images/airsupport.png");
    private final int SPEED = 3;
    private boolean horizontal;
    private double xposition;
    private double yposition;
    private double cooldown;
    public boolean dropBomb = false;
    public boolean finished = false;


    /**
     * figure if the plane is going horizontal or vertical from PlayerInformation then fix it's X or Y position based on
     * that
     * @param input
     * @param player
     */
    public Plane(Input input, PlayerInformation player){
        Random r = new Random();
        this.cooldown = (1 + (2 - 1) * r.nextDouble()) * 60;
        if (player.planeHorizontal){
            this.horizontal = true;
            this.yposition = input.getMouseY();
            this.xposition = -3;


        }else{
            this.horizontal = false;
            this.xposition = input.getMouseX();
            this.yposition = -3;
        }
        player.triggerOrientation();

    }

    public Point getPoint(){
        return new Point(xposition, yposition);
    }

    /**
     * Move plane across screen and periodically set the drop bomb to true
     */
    public void update(){
        finishCheck();
        if (finished){
            return;
        }

        if(cooldown <= 0){
            dropBomb = true;
        }else{
            cooldown = cooldown - ShadowDefend.getTimescale();
        }

        if(horizontal){
            this.xposition = this.xposition + SPEED * ShadowDefend.getTimescale();
            image.draw(xposition, yposition, new DrawOptions().setRotation(Math.PI/2));

        }else{
            this.yposition = this.yposition + SPEED * ShadowDefend.getTimescale();
            image.draw(xposition, yposition, new DrawOptions().setRotation(Math.PI));

        }
    }

    /**
     * get a random number between 1 and 2 seconds (60 and 120 frame) for the cooldown
     */
    public void resetCooldown(){
        Random r = new Random();
        this.cooldown = (1 + (2 - 1) * r.nextDouble()) * 60;
    }

    /**
     * plane go bye bye
     */
    private void finishCheck(){
        if (this.xposition > 1024){
            finished = true;
        }
        if (this.yposition > 768){
            finished = true;
        }
    }

}
