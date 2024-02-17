
public class Player {
    //Level Lvl=new Level();
    
    String name="";
    
    static boolean dead=false;
    
    boolean moveUp=false;
    boolean moveDown=false;
    boolean moveRight=false;
    boolean moveLeft=false;
    
    int x; //Level.playerSpawn_x
    int y; //Level.playerSpawn_y
    static int w=30;
    static int h=30;
    static double xVel=0;
    static double yVel=0;
    static double terminalVel=2.25; //3.25
    static double applySpeed=0.1;
    
    int gun_w = 36; //30
    int gun_h = 16; //15
    int[] gun_xPoints=new int[4]; //top left, top right, bottom right, bottom left
    int[] gun_yPoints=new int[4]; //top left, top right, bottom right, bottom left
    int gun_centerX= x + (w / 2);
    int gun_centerY= y + (h / 2);
    
    int gunReadyLight_w=10; //12
    int gunReadyLight_x=x;
    int gunReadyLight_y=y;
    
    public Player(String name) {
        this.name=name;
        
    }
    public void die() {
        dead=true;
        moveUp=false;
        moveDown=false;
        moveRight=false;
        moveLeft=false;
        xVel=0;
        yVel=0;
    }
    public void respawn(int spawnX, int spawnY) {
        x=spawnX;
        y=spawnY;
        moveUp=false;
        moveDown=false;
        moveRight=false;
        moveLeft=false;
        xVel=0;
        yVel=0;
        dead=false;
    }
    public void actualize() {
        if(!dead) {
            //right and left border properties
            if(x<Level.borders[Level.leftBorder_indx][Level.x_indx]+Level.borderWidth_w) { 
                x=Level.borders[Level.leftBorder_indx][Level.x_indx]+Level.borderWidth_w;
                //x=Level.borders[Level.rightBorder_indx][Level.x_indx]-w;
            }
            if(x+w>Level.borders[Level.rightBorder_indx][Level.x_indx]) {
                x=Level.borders[Level.rightBorder_indx][Level.x_indx]-w;
                //x=Level.borders[Level.leftBorder_indx][Level.x_indx]+Level.borderWidth_w;
            }
            //top and bottom border properties
            if(y<Level.borders[Level.topBorder_indx][Level.y_indx]+Level.borderWidth_h) {
                y=Level.borders[Level.topBorder_indx][Level.y_indx]+Level.borderWidth_h;
            }
            if(y+h>Level.borders[Level.bottomBorder_indx][Level.y_indx]) {
                y=Level.borders[Level.bottomBorder_indx][Level.y_indx]-h;
            }
            
            if(moveUp==true && moveDown==false) { //move up
                yVel-=applySpeed;
                if(y==Level.borders[Level.topBorder_indx][Level.y_indx]+Level.borderWidth_h) {
                    yVel=0;
                }
            }
            if(moveUp==false && moveDown==true) { //move down
                yVel+=applySpeed;
                if(y==Level.borders[Level.bottomBorder_indx][Level.y_indx]-h) {
                    yVel=0;
                }
            }
            if((moveUp==false && moveDown==false) || (moveUp==true && moveDown==true)) {
                yVel=0;
            }
            if(moveLeft==true && moveRight==false) { //move left
                xVel-=applySpeed;
                if(x==Level.borders[Level.leftBorder_indx][Level.x_indx]+Level.borderWidth_w) {
                    xVel=0;
                }
            }
            if(moveLeft==false && moveRight==true) { //move right
                xVel+=applySpeed;
                if(x+w==Level.borders[Level.rightBorder_indx][Level.x_indx]) {
                    xVel=0;
                }
            }
            if((moveLeft==false && moveRight==false) || (moveLeft==true && moveRight==true)) {
                xVel=0;
            }
            
            if(xVel>terminalVel) {
                xVel=terminalVel;
            }
            if(yVel>terminalVel) {
                yVel=terminalVel;
            }
            if(xVel<-terminalVel) {
                xVel=-terminalVel;
            }
            if(yVel<-terminalVel) {
                yVel=-terminalVel;
            }
            
            x+=xVel;
            if(y+h<=Level.borders[Level.bottomBorder_indx][Level.y_indx] && y>=Level.borders[Level.topBorder_indx][Level.y_indx]+Level.borderWidth_h) {
                y+=yVel;
            }
            
            
            //gun points actuallized
            gun_centerX= x + (w / 2);
            gun_centerY= y + (h / 2);
            double rotationAngle_rads=Projectiles.getAngle_rad(x, y, w, h, game.mouseX, game.mouseY, 0, 0, 0);
            gun_xPoints[0]= gun_centerX + (int)((gun_h / 2) * Math.cos(rotationAngle_rads - (Math.PI / 2))); //top left    -
            gun_xPoints[1]= gun_xPoints[0] + (int)(gun_w * Math.cos(rotationAngle_rads)); //top right
            gun_xPoints[3]= gun_centerX + (int)((gun_h / 2) * Math.cos(rotationAngle_rads + (Math.PI / 2))); //bottom left
            gun_xPoints[2]= gun_xPoints[3] + (int)(gun_w * Math.cos(rotationAngle_rads)); //bottom right
            
            gun_yPoints[0]= gun_centerY + (int)((gun_h / 2) * Math.sin(rotationAngle_rads - (Math.PI / 2))); //top left     -
            gun_yPoints[1]= gun_yPoints[0] + (int)(gun_w * Math.sin(rotationAngle_rads)); //top right      
            gun_yPoints[3]= gun_centerY + (int)((gun_h / 2) * Math.sin(rotationAngle_rads + (Math.PI / 2))); //bottom left
            gun_yPoints[2]= gun_yPoints[3] + (int)(gun_w * Math.sin(rotationAngle_rads)); //bottom right
            
            //gun ready light actuallized
            gunReadyLight_x=gun_centerX + (int)((gun_w - 3 - gunReadyLight_w / 2) * Math.cos(rotationAngle_rads)) - (gunReadyLight_w / 2);
            gunReadyLight_y=gun_centerY + (int)((gun_w - 3 - gunReadyLight_w / 2) * Math.sin(rotationAngle_rads)) - (gunReadyLight_w / 2);
        }
        
        
    }
    public void teleport(int MouseX, int MouseY) {
        x=MouseX;
        y=MouseY;
    }
    public void setX(int x) {
        this.x=x;
    }
    public void setY(int y) {
        this.y=y;
    }
    public static int getW() {
        return w;
    }
    public static int getH() {
        return h;
    }
    
    public void movingUp(boolean status) {
        moveUp=status;
    }
    public void movingDown(boolean status) {
        moveDown=status;
    }
    public void movingRight(boolean status) {
        moveRight=status;
    }
    public void movingLeft(boolean status) {
        moveLeft=status;
    }
}























