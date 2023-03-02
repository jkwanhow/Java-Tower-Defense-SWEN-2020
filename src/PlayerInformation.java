import bagel.DrawOptions;
import bagel.Font;
import bagel.Image;
import bagel.util.Point;
import bagel.Input;
import bagel.util.Rectangle;

public class PlayerInformation {
    //PlayerInformation
    public String playerStatus = "Awaiting Start";
    public int waveNumber;
    public int playerHealth;
    public int playerMoney;
    public boolean alive;
    public boolean planeHorizontal = true;
    private final Font font = new Font("res/fonts/DejaVuSans-Bold.ttf", 18);

    private final Image PANEL_IMAGE = new Image("res/images/statuspanel.png");


    public PlayerInformation(){
        playerHealth = 25;
        playerMoney = 500;
        alive = true;
    }

    /**
     * yes, increase money
     * @param cost
     */
    public void addValue(int cost){
        this.playerMoney = this.playerMoney + cost;
    }

    /**
     * Plane orientation logic make sure it takes turns
     */
    public void triggerOrientation(){
        if (planeHorizontal){
            this.planeHorizontal = false;
        }else{
            this.planeHorizontal = true;
        }
    }

    /**
     * Collection of all the drawings within the status panel
     */
    private void PlayerPanelDraw(){
        PANEL_IMAGE.draw(512, 755.5);
        font.drawString("Lives: " + Integer.toString(playerHealth), 925, 762);
        font.drawString("Wave: " + Integer.toString(waveNumber), 25, 762);
        font.drawString(playerStatus, 500, 762);
        if (ShadowDefend.getTimescale() != 1){
            //my text be green
            font.drawString("Time Scale: " + Double.toString(ShadowDefend.getTimescale()), 250, 762
            , new DrawOptions().setBlendColour(0, 255, 0));
        }else {

            font.drawString("Time Scale: " + Double.toString(ShadowDefend.getTimescale()), 250, 762);
        }
    }

    /**
     * execute that panel
     */
    public void statusUpdate(){
        PlayerPanelDraw();
    }




}
