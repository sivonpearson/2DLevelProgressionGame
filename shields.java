import java.util.*;

public class shields{
    static Random rand = new Random();
    
    final static int AI_maxNumShield = 7;
    static int AI_currentNumShield = 1;
    static double[] AI_x= new double[AI_maxNumShield];
    static double[] AI_y= new double[AI_maxNumShield];
    static int[] AI_HP = new int[AI_maxNumShield];
    static int maxNumHP_round=1;
    final static double AI_spinSpeed_rad = 0.0005;
    final static int AI_diameter = 14;
    final static double AI_distFromMidAI = (EnemyAI.w + AI_diameter * 2) * (Math.sqrt(2) / 2);
    static boolean[] AI_visible = new boolean[AI_maxNumShield];
    static double[] AI_angle_rad = new double[AI_maxNumShield];
    
    public static void initialize() {
        for(int i=0; i<AI_maxNumShield; i++) {
            AI_visible[i]=false;
            AI_x[i]=0;
            AI_y[i]=0;
            AI_angle_rad[i]=0;
            AI_HP[i]=0;
        }
        for(int i=0; i<AI_currentNumShield; i++) {
            AI_HP[i] = maxNumHP_round;
            AI_visible[i]=true;
            AI_angle_rad[i]= (2 * Math.PI) * (i+1 / AI_currentNumShield);
        }
    }
    public static void actualize() {
        for(int i=0; i<AI_currentNumShield; i++) {
            if(AI_visible[i]==true) {
                if(AI_HP[i]==0) {
                    AI_visible[i]=false;
                }
                AI_x[i] = EnemyAI.x + (EnemyAI.w / 2) + (AI_distFromMidAI * Math.cos(AI_angle_rad[i])) - (AI_diameter / 2);
                AI_y[i] = EnemyAI.y + (EnemyAI.h / 2) + (AI_distFromMidAI * Math.sin(AI_angle_rad[i])) - (AI_diameter / 2);
                AI_angle_rad[i] += AI_spinSpeed_rad;
            }
        }
    }
    public static void AI_upgrade() {
        if(rand.nextInt(2) == 0) {
            if(AI_currentNumShield+1 < 7) {
                AI_currentNumShield++;
            }
        }
        else{
            maxNumHP_round++;
        }
    }
    public static void AI_reduceHP(int indx) {
        if(AI_HP[indx] > 0) {
            AI_HP[indx]--;
        }
        if(AI_HP[indx] <= 0) {
            AI_visible[indx] = false;
        }
    }
    public static void AI_setHP(int indx, int amnt) {
        AI_HP[indx]= amnt;
    }
    public static void AI_hide() {
        for(int i=0; i<AI_maxNumShield; i++) {
            AI_visible[i] = false;
        }
    }
}



