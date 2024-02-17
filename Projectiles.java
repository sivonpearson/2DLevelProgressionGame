import java.util.*;

public class Projectiles {
    static Random rand=new Random();
    //List<Obj> list = new ArrayList<Obj> ();
    
    static int AI_maxNumProj=35;
    double[] enemyAI_x = new double[AI_maxNumProj];
    double[] enemyAI_y = new double[AI_maxNumProj];
    double[] enemyAI_xVel = new double[AI_maxNumProj];
    double[] enemyAI_yVel = new double[AI_maxNumProj];
    int enemyAI_diameter = 20;
    double enemyAI_speed=2.5;
    double[] enemyAI_angleRad = new double[AI_maxNumProj];
    static boolean[] enemyAI_visible = new boolean[AI_maxNumProj];
    int[] enemyAI_projCooldown = new int[AI_maxNumProj];
    static int enemyAI_timeBetweenShots_round = 200; //ms                min-25
    static int enemyAI_timeBetweenShots_current=enemyAI_timeBetweenShots_round;
    static int enemyAI_numShotsInSequences = 1; //                         max=AI_maxNumProj
    static int enemyAI_currentShot=0;
    static double enemyAI_intervalsBetweenShots_angleRad_max=0.2*Math.PI;
    static double enemyAI_intervalsBetweenShots_angleRad_min=0.05;
    double enemyAI_startAngle; //rads
    boolean enemyAI_shootSequence_archDir; //false = left;   true = right
    
    static int P_maxNumProj=3;
    double player_x[] = new double[P_maxNumProj]; //0
    double player_y[] = new double[P_maxNumProj]; //0
    int player_diameter = 15; //15
    double player_speed=3;
    double player_xVel[] = new double[P_maxNumProj];
    double player_yVel[] = new double[P_maxNumProj];
    double player_angleRad[] = new double[P_maxNumProj];
    boolean player_visible[] = new boolean[P_maxNumProj];
    int p_projCooldown=2;
    
    double playerSpec_x = 0;
    double playerSpec_y = 0;
    int playerSpec_diameter = 20;
    double playerSpec_speed = 1.75;
    double playerSpec_xVel = 0;
    double playerSpec_yVel = 0;
    double playerSpec_angleRad = 0;
    boolean playerSpec_visible = false;
    final int p_specCooldown_max = 30;
    int p_specCooldown = 0;
    
    public void initialize() {
        for(int aiProj=0; aiProj<AI_maxNumProj; aiProj++) {
            enemyAI_destroy(aiProj);
        }
        for(int i=0; i<P_maxNumProj; i++) {
            P_destroy(i);
        }
        P_specDestroy();
        p_specCooldown = 0;
        p_projCooldown = 0;
    }
    public void actualize() {
        for(int i=0; i<P_maxNumProj; i++) {
            if(player_visible[i]==true) {
                player_x[i] += player_xVel[i];
                player_y[i] += player_yVel[i];
            }
            if((player_outBounds_x(i)==true || player_outBounds_y(i)==true)) {
                P_destroy(i);
            }
        }
        if(playerSpec_visible == true) {
            playerSpec_x += playerSpec_xVel;
            playerSpec_y += playerSpec_yVel;
        }
        if(playerSpec_outBounds_x()==true || playerSpec_outBounds_x()==true) {
            P_specDestroy();
        }
        for(int i=0; i<AI_maxNumProj; i++) {
            if(enemyAI_outBounds_x(i)==true || enemyAI_outBounds_y(i)==true) {
                enemyAI_destroy(i);
            }
            if(enemyAI_visible[i]==true) {
                enemyAI_x[i] += enemyAI_xVel[i];
                enemyAI_y[i] += enemyAI_yVel[i];
            }
        }
    }
    public static double getAngle_rad(int point1_x, int point1_y, int point1_w, int point1_h, int point2_x, int point2_y, int point2_w, int point2_h, int proj_diameter) { //angle is relative to point1
        double center1_x = point1_x + ((point1_w - proj_diameter) / 2);
        double center1_y = point1_y + ((point1_h - proj_diameter) / 2);
        double center2_x = point2_x + (point2_w / 2);
        double center2_y = point2_y + (point2_h / 2);
        
        double dx = center1_x - (center2_x + point2_w / 2);
        double dy = (center2_y + point2_h / 2) - center1_y;
        double inRads = Math.atan2(dy, dx);
        //0 degrees is at 3:00, 270 at 12:00
        if(inRads<0) {
            inRads = inRads; //inRads = Math.abs(inRads)
        }
        else{
            inRads = 2 * Math.PI - inRads;
        }
        return Math.toRadians(Math.abs(180-Math.toDegrees(inRads)));
    }
    public static double getSpawnLocation_x(int point_x, int point_w, int proj_diameter) {
        return ( point_x + ((point_w - proj_diameter) / 2) );
    }
    public static double getSpawnLocation_y(int point_y, int point_h, int proj_diameter) {
        return (point_y + ((point_h - proj_diameter) / 2));
    }
    public void P_shoot(int P_x, int P_y, int P_w, int P_h, int mouseX, int mouseY, boolean dead) {
        boolean runLoop = true;
        int indx = 0;
        int indx_f = -1;
        
        if(p_projCooldown==0 && dead==false) {
            do{
                if(player_visible[indx]==false) {
                    indx_f = indx;
                }
                indx++;
            }while(runLoop==true && indx<P_maxNumProj);
            if(indx_f != -1) {
                player_angleRad[indx_f]=getAngle_rad(P_x, P_y, P_w, P_h, mouseX, mouseY, 0, 0, player_diameter); //getAngle_rad(spawnPosForProj_x, spawnPosForProj_y, mouseX, mouseY, 0, 0)
                
                p_projCooldown=2;
                player_x[indx_f]= getSpawnLocation_x(P_x, P_w, player_diameter);
                player_y[indx_f]= getSpawnLocation_y(P_y, P_h, player_diameter);
                player_xVel[indx_f]= player_speed * Math.cos(player_angleRad[indx_f]);
                player_yVel[indx_f]= player_speed * Math.sin(player_angleRad[indx_f]);
                player_visible[indx_f]=true;
            }
            
        }
    }
    public void P_shootSpec(int P_x, int P_y, int P_w, int P_h, int mouseX, int mouseY, boolean dead) {
        if(p_specCooldown==0 && dead==false && playerSpec_visible==false) {
            p_specCooldown=p_specCooldown_max;
            playerSpec_angleRad=getAngle_rad(P_x, P_y, P_w, P_h, mouseX, mouseY, 0, 0, playerSpec_diameter);
            playerSpec_x= getSpawnLocation_x(P_x, P_w, playerSpec_diameter);
            playerSpec_y= getSpawnLocation_y(P_y, P_h, playerSpec_diameter);
            playerSpec_xVel= playerSpec_speed * Math.cos(playerSpec_angleRad);
            playerSpec_yVel= playerSpec_speed * Math.sin(playerSpec_angleRad);
            playerSpec_visible=true;
            
        }
    }
    public void enemyAI_shoot(int AI_x, int AI_y, int enemyAI_w, int enemyAI_h, int P_x, int P_y, int P_w, int P_h) {
        boolean runLoop=true;
        int indx=0;
        int indx_f= -1;
        
        enemyAI_currentShot++;
        do{
            if(enemyAI_visible[indx]==false) {
                indx_f=indx;
            }
            indx++;
        }while(runLoop==true && indx<AI_maxNumProj);
        if(indx_f != -1) {
            double enemyAI_angleRad_i;
            if(playerSpec_visible == false) {
                enemyAI_angleRad_i=getAngle_rad(AI_x, AI_y, enemyAI_w, enemyAI_h, P_x, P_y, P_w, P_h, enemyAI_diameter);
            }
            else{
                enemyAI_angleRad_i=getAngle_rad(AI_x, AI_y, enemyAI_w, enemyAI_h, (int)playerSpec_x, (int)playerSpec_y, playerSpec_diameter, playerSpec_diameter, enemyAI_diameter);
            }
            
            if(rand.nextInt(2)==0) {
                enemyAI_angleRad[indx_f]=enemyAI_angleRad_i+(enemyAI_intervalsBetweenShots_angleRad_min+(enemyAI_intervalsBetweenShots_angleRad_max-enemyAI_intervalsBetweenShots_angleRad_min)*rand.nextDouble());
            }
            else{
                enemyAI_angleRad[indx_f]=enemyAI_angleRad_i-(enemyAI_intervalsBetweenShots_angleRad_min+(enemyAI_intervalsBetweenShots_angleRad_max-enemyAI_intervalsBetweenShots_angleRad_min)*rand.nextDouble());
            }
            enemyAI_x[indx_f]= getSpawnLocation_x(AI_x, enemyAI_w, enemyAI_diameter);
            enemyAI_y[indx_f]= getSpawnLocation_x(AI_y, enemyAI_h, enemyAI_diameter);
            enemyAI_xVel[indx_f]= enemyAI_speed * Math.cos(enemyAI_angleRad[indx_f]);
            enemyAI_yVel[indx_f]= enemyAI_speed * Math.sin(enemyAI_angleRad[indx_f]);
            enemyAI_visible[indx_f]= true;
        }
        
        if(enemyAI_currentShot<enemyAI_numShotsInSequences) {
            enemyAI_timeBetweenShots_current=(int)Math.ceil( (double)enemyAI_timeBetweenShots_round / 3 );
        }
        if(enemyAI_currentShot==enemyAI_numShotsInSequences) {
            enemyAI_timeBetweenShots_current=enemyAI_timeBetweenShots_round;
            enemyAI_currentShot=0;
        }
    }
    public static void enemyAI_upgrade() {
        int randTime=rand.nextInt(12)+1;
        if(enemyAI_timeBetweenShots_round-randTime>0) {
            enemyAI_timeBetweenShots_round-=randTime;
        }
        if(rand.nextInt(2)==0) {
            if(enemyAI_numShotsInSequences+1<AI_maxNumProj) {
                enemyAI_numShotsInSequences+=1;
            }
        }
        else{
            double randNum=((double)rand.nextInt(3)+3)/100;
            if(enemyAI_intervalsBetweenShots_angleRad_max-randNum>enemyAI_intervalsBetweenShots_angleRad_min) {
                enemyAI_intervalsBetweenShots_angleRad_max-=randNum;
            }
        }
        enemyAI_timeBetweenShots_current=enemyAI_timeBetweenShots_round;
        enemyAI_currentShot=0;
    }
    public void P_destroy(int indx) {
        player_visible[indx]=false;
        player_xVel[indx]=0;
        player_yVel[indx]=0;
        
    }
    public void P_specDestroy() {
        playerSpec_visible=false;
        playerSpec_xVel=0;
        playerSpec_yVel=0;
    }
    public void enemyAI_destroy(int indx) {
        enemyAI_visible[indx]=false;
        enemyAI_xVel[indx]=0;
        enemyAI_yVel[indx]=0;
    }
    public void P_cooldownReduce() {
        if(p_projCooldown>0) {
            p_projCooldown--;
        }
        if(p_specCooldown>0 && playerSpec_visible == false) {
            p_specCooldown--;
        }
    }
    public void enemyAI_cooldownReduce(int AI_x, int AI_y, int AI_w, int AI_h, int P_x, int P_y, int P_w, int P_h) {
        if(enemyAI_timeBetweenShots_current==0) {
            //enemyAI_timeBetweenShots_current=enemyAI_timeBetweenShots_round;
            enemyAI_shoot(AI_x, AI_y, AI_w, AI_h, P_x, P_y, P_w, P_h);
        }
        if(enemyAI_timeBetweenShots_current>0) {
            enemyAI_timeBetweenShots_current--;
        }
    }
    public void updatePlayerSpecTraj() {
        if(playerSpec_visible==true) {
            
        }
    }
    public boolean player_outBounds_x(int indx) {
        boolean inBounds_x= player_x[indx]+(player_diameter/2)>Level.borders[Level.rightBorder_indx][Level.x_indx] || player_x[indx]+(player_diameter/2)<Level.borders[Level.leftBorder_indx][Level.x_indx]+Level.borderWidth_w;
        return inBounds_x;
    }
    public boolean player_outBounds_y(int indx) {
        boolean inBounds_y= player_y[indx]+(player_diameter/2)>Level.borders[Level.bottomBorder_indx][Level.y_indx] || player_y[indx]+(player_diameter/2)<Level.borders[Level.topBorder_indx][Level.y_indx]+Level.borderWidth_h;
        return inBounds_y;
    }
    public boolean enemyAI_outBounds_x(int indx) {
        boolean inBounds_x= enemyAI_x[indx]+(enemyAI_diameter/2)>Level.borders[Level.rightBorder_indx][Level.x_indx] || enemyAI_x[indx]+(enemyAI_diameter/2)<Level.borders[Level.leftBorder_indx][Level.x_indx]+Level.borderWidth_w;
        return inBounds_x;
    }
    public boolean enemyAI_outBounds_y(int indx) {
        boolean inBounds_y= enemyAI_y[indx]+(enemyAI_diameter/2)>Level.borders[Level.bottomBorder_indx][Level.y_indx] || enemyAI_y[indx]+(enemyAI_diameter/2)<Level.borders[Level.topBorder_indx][Level.y_indx]+Level.borderWidth_h;
        return inBounds_y;
    }
    public boolean playerSpec_outBounds_x() {
        boolean inBounds_x= playerSpec_x+(playerSpec_diameter/2)>Level.borders[Level.rightBorder_indx][Level.x_indx] || playerSpec_x+(playerSpec_diameter/2)<Level.borders[Level.leftBorder_indx][Level.x_indx]+Level.borderWidth_w;
        return inBounds_x;
    }
    public boolean playerSpec_outBounds_y() {
        boolean inBounds_y= playerSpec_y+(playerSpec_diameter/2)>Level.borders[Level.bottomBorder_indx][Level.y_indx] || playerSpec_y+(playerSpec_diameter/2)<Level.borders[Level.topBorder_indx][Level.y_indx]+Level.borderWidth_h;
        return inBounds_y;
    }
}






