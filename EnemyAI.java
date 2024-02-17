import java.util.Random;

public class EnemyAI {
    static Random rand=new Random();
    
    boolean dead=false;
    
    static double x=100; //100
    static double y=75; //75
    static int w=50;
    static int h=50;
    
    //boolean moveUp=false;
    //boolean moveDown=false;
    //boolean moveRight=false;
    //boolean moveLeft=false;
    
    boolean movementEnabled=false;
    
    static double xVel=0;
    static double yVel=0;
    
    static double minVel=0.25-0.1;
    static double maxVel=1-0.1;
    
    static double totalVel=(double)(rand.nextInt((int)((maxVel-minVel)*10))/10)+minVel;
    
    static double maxVel_absolute=10;
    
    int chanceToChangeDir=1000; //1 or more
    double angle_rads= rand.nextDouble()*2*Math.PI;
    
    int maxHP = 3 + (game.levelNum - 1);
    int HP = maxHP;
    
    int healthDisplay_w=25;
    int healthDisplay_h=25;
    int healthDisplay_x=(int)x+(w-healthDisplay_w)/2;
    int healthDisplay_y=(int)y+(h-healthDisplay_h)/2;
    
    public EnemyAI() {
        movementEnabled=true;
    }
    
    public void actualize() {
        if(dead==false) {
            if(x+w>Level.borders[Level.rightBorder_indx][Level.x_indx]) { //past right border
                x=Level.borders[Level.rightBorder_indx][Level.x_indx]-w;
                angle_rads+=Math.PI;
            }
            if(x<Level.borders[Level.leftBorder_indx][Level.x_indx]+Level.borderWidth_w) { //past left border
                x=Level.borders[Level.leftBorder_indx][Level.x_indx]+Level.borderWidth_w;
                angle_rads+=Math.PI;
            }
            if(y<Level.borders[Level.topBorder_indx][Level.y_indx]+Level.borderWidth_h) { //past top border
                y=Level.borders[Level.topBorder_indx][Level.y_indx]+Level.borderWidth_h;
                angle_rads+=Math.PI;
            }
            if(y+h>Level.borders[Level.bottomBorder_indx][Level.y_indx]) { //past bottom border
                y=Level.borders[Level.bottomBorder_indx][Level.y_indx]-h;
                angle_rads+=Math.PI;
            }
            
            xVel= totalVel * Math.cos(angle_rads);
            yVel= totalVel * Math.sin(angle_rads);
            
            if(movementEnabled==true) {
                if(inBounds_x()==true) {
                    x+=xVel;
                }
                if(inBounds_y()==true) {
                    y+=yVel;
                }
            }
            
            changeMovementDir();
        }
        
        healthDisplay_x=(int)x+(w-healthDisplay_w)/2;
        healthDisplay_y=(int)y+(h-healthDisplay_h)/2;
    }
    public boolean inBounds_x() {
        boolean inBounds_x= x+w<=Level.borders[Level.rightBorder_indx][Level.x_indx] && x>=Level.borders[Level.leftBorder_indx][Level.x_indx]+Level.borderWidth_w;
        return inBounds_x;
    }
    public boolean inBounds_y() {
        boolean inBounds_y= y+h<=Level.borders[Level.bottomBorder_indx][Level.y_indx] && y>=Level.borders[Level.topBorder_indx][Level.y_indx]+Level.borderWidth_h;
        return inBounds_y;
    }
    public boolean onBorder() {
        boolean topBorderCheck= y<=Level.borders[Level.topBorder_indx][Level.y_indx]+Level.borderWidth_h;
        boolean bottomBorderCheck= y+h>=Level.borders[Level.bottomBorder_indx][Level.y_indx];
        boolean rightBorderCheck= x+w>=Level.borders[Level.rightBorder_indx][Level.x_indx];
        boolean leftBorderCheck= x<=Level.borders[Level.leftBorder_indx][Level.x_indx]+Level.borderWidth_w+1;
        boolean onBorder= topBorderCheck || bottomBorderCheck || rightBorderCheck || bottomBorderCheck;
        return onBorder;
    }
    public void changeMovementDir() {
        if(rand.nextInt(chanceToChangeDir)==0) {
            angle_rads=rand.nextDouble()*2*Math.PI;
        }
    }
    public void upgrade() {
        if(rand.nextInt(2)==0) {
            minVel+=0.025;
            maxVel+=0.025;
        }
        maxHP = 3 + (game.levelNum - 1);
        totalVel=(double)(rand.nextInt((int)((maxVel-minVel)*10))/10)+minVel;
        if(totalVel>maxVel_absolute) {
            totalVel=maxVel_absolute;
        }
        movementEnabled=true;
        HP=maxHP;
        Projectiles.enemyAI_upgrade();
        int randNum=rand.nextInt(5)+1;
        if(chanceToChangeDir-randNum>50) {
            chanceToChangeDir-=randNum;
        }
        angle_rads= rand.nextDouble()*2*Math.PI;
        dead=false;
    }
    public void reset(int spawnX, int spawnY) {
        totalVel=(double)(rand.nextInt((int)((maxVel-minVel)*10))/10)+minVel;
        x=spawnX;
        y=spawnY;
        angle_rads=rand.nextDouble()*2*Math.PI;
        movementEnabled=true;
    }
    public void die() {
        if(HP==0) {
            dead=true;
            xVel=0;
            yVel=0;
            movementEnabled=false;
        }
    }
    public void lowerHP() {
        if(HP>0) {
            HP--;
        }
        if(HP==0) {
            dead=true;
            xVel=0;
            yVel=0;
            movementEnabled=false;
            shields.AI_hide();
        }
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
}




