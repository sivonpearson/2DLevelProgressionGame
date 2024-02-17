import java.util.*;
public class Level {
    static wndwDimsAndCalculations Obj=new wndwDimsAndCalculations();
    //Player P=new Player("test");
    
    //the same for all around for borderWidth
    static int borderWidth_w=100;
    static int borderWidth_h=50;
    static int borders[][]={ //block #, (x, y, w, h)
        {0, 0, Obj.relativeWINDOW_WIDTH, borderWidth_h}, //top
        {0, Obj.relativeWINDOW_HEIGHT-borderWidth_h, Obj.relativeWINDOW_WIDTH, borderWidth_h}, //bottom
        {Obj.relativeWINDOW_WIDTH-borderWidth_w, borderWidth_h, borderWidth_w, Obj.relativeWINDOW_HEIGHT-(borderWidth_h*2)}, //right
        {0, borderWidth_h, borderWidth_w, Obj.relativeWINDOW_HEIGHT-(2*borderWidth_h)} //left
    };
    static int topBorder_indx=0;
    static int bottomBorder_indx=1;
    static int rightBorder_indx=2;
    static int leftBorder_indx=3;
    
    static int x_indx=0;
    static int y_indx=1;
    static int w_indx=2;
    static int h_indx=3;
    
    static int playerSpawn_x=borderWidth_w+(int)((Obj.relativeWINDOW_WIDTH-(borderWidth_w*2)-Player.getW())/2);
    static int playerSpawn_y=(borders[bottomBorder_indx][y_indx]-(Player.getH()*5));
    
    static int enemyAISpawn_x=borderWidth_w+(int)((Obj.relativeWINDOW_WIDTH-(borderWidth_w*2)-EnemyAI.getW())/2);
    static int enemyAISpawn_y=(borders[bottomBorder_indx][y_indx]-(EnemyAI.getH()*15));
    
    public static int getPlayerSpawn_x() {
        return playerSpawn_x;
    }
    public static int getPlayerSpawn_y() {
        return playerSpawn_y;
    }
    public static int getEnemyAISpawn_x() {
        return enemyAISpawn_x;
    }
    public static int getEnemyAISpawn_y() {
        return enemyAISpawn_y;
    }
}










