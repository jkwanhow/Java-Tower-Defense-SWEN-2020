import bagel.*;
import bagel.map.TiledMap;
import bagel.util.Point;
import bagel.util.Rectangle;

public class Shop {
    //I SHOULDNT HAVE MADE STRING LOCATION BUT ALREADY DID SO TOO LATE :( also kind lazy
    private final String BUY_PANEL_LOCATION = "res/images/buypanel.png";
    private final String TANK_IMAGE_LOCATION = "res/images/tank.png";
    private final String SUPERTANK_IMAGE_LOCATION = "res/images/supertank.png";
    private final String PLANE_IMAGE_LOCATION = "res/images/airsupport.png";

    private final Image BUY_PANEL = new Image(BUY_PANEL_LOCATION);
    private final Image TANK_IMAGE = new Image(TANK_IMAGE_LOCATION);
    private final Image SUPERTANK_IMAGE = new Image(SUPERTANK_IMAGE_LOCATION);
    private final Image PLANE_IMAGE = new Image(PLANE_IMAGE_LOCATION);

    private final Rectangle PANEL_RECT = BUY_PANEL.getBoundingBoxAt(new Point(512, 50));
    private final Rectangle TANK_RECT = TANK_IMAGE.getBoundingBoxAt(new Point(64, 40));
    private final Rectangle SUPERTANK_RECT = SUPERTANK_IMAGE.getBoundingBoxAt(new Point(164, 40));
    private final Rectangle PLANE_RECT = PLANE_IMAGE.getBoundingBoxAt(new Point(264, 40));

    public final int TANK_PRICE = 250;
    public final int SUPERTANK_PRICE = 600;
    public final int PLANE_PRICE = 500;

    private Rectangle buyingRectangle;
    private Image buyingImage;

    private final Font font = new Font("res/fonts/DejaVuSans-Bold.ttf", 24);
    private final Font fontMONEY = new Font("res/fonts/DejaVuSans-Bold.ttf", 48);
    private final Font instructionFont = new Font("res/fonts/DejaVuSans-Bold.ttf", 16);

    public Point mousePosition;
    public boolean buying = false;
    public String itemBeingPurchased;


    private PlayerInformation player;

    /**
     * Link PlayerInformation to the shop class
     * @param info
     */
    public Shop(PlayerInformation info){
        player = info;
    }

    /**
     * Draw all the options
     */
    public void draw_menu(){
        BUY_PANEL.draw(512, 50);
        tankshop_draw();
        supertank_draw();
        plane_draw();
        money_draw();
        instructions();

    }

    private void instructions(){
        instructionFont.drawString("Key binds:\n\nS - Start Wave\nL - Increase Timescale\nS - Decrease Timescale"
        ,500, 25);
    }

    /**
     * TANK PANEL
     */
    private void tankshop_draw(){
        TANK_IMAGE.draw(64,40);
        font.drawString("$" + Integer.toString(TANK_PRICE), 35, 90);

    }

    /**
     * SUPERTANK PANEL
     */
    private void supertank_draw(){
        SUPERTANK_IMAGE.draw(164,40);
        font.drawString("$" + Integer.toString(SUPERTANK_PRICE), 135, 90);

    }

    /**
     * PLANE PANEL
     */
    private void plane_draw(){
        PLANE_IMAGE.draw(264,40);
        font.drawString("$" + Integer.toString(PLANE_PRICE), 235, 90);

    }

    /**
     * Display money
     */
    private void money_draw(){
        String money = Integer.toString(player.playerMoney);
        fontMONEY.drawString("$" + money, 824, 65);

    }


    public void getMousePoint(Input input){
        this.mousePosition = input.getMousePosition();
    }

    private boolean mousePressed(Input input){
        return input.wasPressed(MouseButtons.LEFT);
    }

    private boolean mouseInSquare(Rectangle rectangle){
        return rectangle.intersects(mousePosition);
    }

    /**
     * check if a panel was pressed on
     * @param input
     * @param rectangle
     * @return
     */
    private boolean pressedOn(Input input, Rectangle rectangle){
        getMousePoint(input);
        if (mouseInSquare(rectangle)){
            if(mousePressed(input)){
                return true;
            }
        }
        return false;
    }

    /**
     * Check if the tower was bought
     * @param input
     */
    public void towerBuyCheck(Input input){

        if(!this.buying) {


            if (pressedOn(input, TANK_RECT) && player.playerMoney >= TANK_PRICE) {
                this.buyingImage = new Image(TANK_IMAGE_LOCATION);
                this.itemBeingPurchased = "tank";
                this.buying = true;


            } else if (pressedOn(input, SUPERTANK_RECT) && player.playerMoney >= SUPERTANK_PRICE) {
                this.buyingImage = new Image(SUPERTANK_IMAGE_LOCATION);
                this.itemBeingPurchased = "supertank";
                this.buying = true;

            } else if (pressedOn(input, PLANE_RECT) && player.playerMoney >= PLANE_PRICE) {
                this.itemBeingPurchased = "plane";
                this.buyingImage = new Image(PLANE_IMAGE_LOCATION);
                this.buying = true;

            }
        }

        return;
    }

    /**
     * Toggle off indeed.
     * @param input
     */
    public void toggleOff(Input input){
        if (input.wasPressed(MouseButtons.RIGHT)){
            this.buying = false;
        }
    }


    /**
     * draws the icon on the mouse and gets the bounding box to find "INTERSECTIONS"
     */
    public void buyingInteraction(){
        this.buyingImage.draw(mousePosition.x, mousePosition.y);
        this.buyingRectangle = this.buyingImage.getBoundingBoxAt(mousePosition);


    }



}
