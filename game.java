import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.MouseInfo;
import java.util.*;
import java.io.*;
import java.text.*;

import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.Color;
    
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
public class game extends JPanel{
    Random rand=new Random();
    wndwDimsAndCalculations Obj=new wndwDimsAndCalculations();
    Player P=new Player("test");
    Level Lvl=new Level();
    EnemyAI AI=new EnemyAI();
    Projectiles Proj=new Projectiles();
    shields shld = new shields();
    
    static int mouseX=MouseInfo.getPointerInfo().getLocation().x;
    static int mouseY=MouseInfo.getPointerInfo().getLocation().y;
    
    int playerColor_r=192;
    int playerColor_g=192;
    int playerColor_b=192;
    
    static int levelNum=0;
    int nextLvlTimer=3;
    
    Polygon P_gun = new Polygon();
    
    public game() {
        initGame();
        
        java.util.Timer ms_1000_timer=new java.util.Timer();
        ms_1000_timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                if(AI.dead==true && nextLvlTimer>0) {
                    nextLvlTimer--;
                }
                if(AI.dead==true && nextLvlTimer==0) {
                    nextLevel();
                    nextLvlTimer=3;
                }
                Proj.P_cooldownReduce();
                repaint();
            }
        },0,1000);
        
        java.util.Timer ms_20_timer=new java.util.Timer();
        ms_20_timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                if(AI.dead==false && P.dead==false) {
                    Proj.enemyAI_cooldownReduce((int)AI.x, (int)AI.y, AI.w, AI.h, P.x, P.y, P.w, P.h);
                }
                repaint();
            }
        },0,20);
        
        java.util.Timer ms_7_timer=new java.util.Timer();
        ms_7_timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                P.actualize();
                Proj.actualize();
                if(AI.dead==false) {
                    AI.actualize();
                }
                repaint();
            }
        },0,7);
        
        java.util.Timer ms_1_timer=new java.util.Timer();
        ms_1_timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                mouseX=MouseInfo.getPointerInfo().getLocation().x;
                mouseY=MouseInfo.getPointerInfo().getLocation().y-38; //50 or 38
                
                if(Obj.checkCollisions_box((int)AI.x, (int)AI.y, AI.w, AI.h, P.x, P.y, P.w, P.h)==true && AI.dead==false) {
                    P.die();
                }
                for(int i=0; i<Proj.P_maxNumProj; i++) {
                    if(Proj.player_visible[i]==true && AI.dead==false) {
                        if(Obj.checkCollisions_box((int)Proj.player_x[i], (int)Proj.player_y[i], (int)Proj.player_diameter, (int)Proj.player_diameter, (int)AI.x, (int)AI.y, AI.w, AI.h)==true) {
                            AI.lowerHP();
                            Proj.P_destroy(i);
                        }
                        for(int h=0; h<shields.AI_currentNumShield; h++) {
                            if(shields.AI_visible[h] == true && Obj.checkCollisions_box((int)Proj.player_x[i], (int)Proj.player_y[i], (int)Proj.player_diameter, (int)Proj.player_diameter, (int)shields.AI_x[h], (int)shields.AI_y[h], shields.AI_diameter, shields.AI_diameter)==true) {
                                shields.AI_reduceHP(h);
                                Proj.P_destroy(i);
                            }
                        }
                    }
                }
                
                if(Obj.checkCollisions_box((int)Proj.playerSpec_x, (int)Proj.playerSpec_y, (int)Proj.playerSpec_diameter, (int)Proj.playerSpec_diameter, (int)AI.x, (int)AI.y, AI.w, AI.h)==true && Proj.playerSpec_visible==true) {
                    Proj.P_specDestroy();
                    for(int i=0; i<3; i++) { //3 damage to AI HP
                        AI.lowerHP();
                    }
                }
                for(int h=0; h<shields.AI_maxNumShield; h++) {
                    if(shields.AI_visible[h] == true && Proj.playerSpec_visible==true && Obj.checkCollisions_box((int)Proj.playerSpec_x, (int)Proj.playerSpec_y, (int)Proj.playerSpec_diameter, (int)Proj.playerSpec_diameter, (int)shields.AI_x[h], (int)shields.AI_y[h], shields.AI_diameter, shields.AI_diameter)==true) {
                        shields.AI_setHP(h, 0);
                    }
                }
                
                for(int e=0; e<Proj.AI_maxNumProj; e++) { //AI projectiles
                    if(AI.dead==true) {
                        Proj.enemyAI_destroy(e);
                    }
                    if(AI.dead==false && Proj.enemyAI_visible[e]==true) {
                        if(Obj.checkCollisions_box((int)Proj.enemyAI_x[e], (int)Proj.enemyAI_y[e], (int)Proj.enemyAI_diameter, (int)Proj.enemyAI_diameter, (int)P.x, (int)P.y, P.w, P.h)==true && P.dead==false) {
                            //AI proj collide w/ player
                            P.die();
                            Proj.enemyAI_destroy(e);
                        }
                        
                        if(Obj.checkCollisions_box((int)Proj.enemyAI_x[e], (int)Proj.enemyAI_y[e], (int)Proj.enemyAI_diameter, (int)Proj.enemyAI_diameter, (int)Proj.playerSpec_x, (int)Proj.playerSpec_y, (int)Proj.playerSpec_diameter, (int)Proj.playerSpec_diameter)==true && Proj.playerSpec_visible==true) {
                            //AI proj collide w/ player special
                            Proj.P_specDestroy();
                            Proj.enemyAI_destroy(e);
                        }
                    }
                }
                //AI.die(); //only will happen if AI hp==0
                
                shld.actualize();
                
                repaint(); //optional
            }
        },0,1);
    }
    public void initGame() {
        addKeyListener(new key_listener());
        addMouseListener(new mouse_listener());
        setBackground(new Color(0, 0, 0));
        setFocusable(true);
        //System.out.println();
        //P.respawn(Lvl.playerSpawn_x, Lvl.playerSpawn_y);
        nextLevel();
        
        //P_gun.translate(50, 50);
        
    }
    public void nextLevel() {
        shld.AI_upgrade();
        P.setX(Lvl.playerSpawn_x);
        P.setY(Lvl.playerSpawn_y);
        AI.setX(Lvl.enemyAISpawn_x);
        AI.setY(Lvl.enemyAISpawn_y);
        levelNum++;
        Proj.initialize();
        AI.upgrade();
        shld.initialize();
    }
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Font currentFont=g.getFont();
        Font youDiedFont=currentFont.deriveFont(currentFont.getSize()*4.5F); //you died label
        FontMetrics fm_youDiedFont=g.getFontMetrics(youDiedFont);
        Font levelLabelFont=currentFont.deriveFont(currentFont.getSize()*2.4F); //level num label
        FontMetrics fm_levelLabelFont=g.getFontMetrics(levelLabelFont);
        
        String levelLabelText="Level: "+levelNum;
        g.setFont(levelLabelFont);
        g.setColor(new Color(0, 60, 0));
        g.drawString(levelLabelText, (Obj.relativeWINDOW_WIDTH - fm_levelLabelFont.stringWidth(levelLabelText))/2, Lvl.borders[Lvl.topBorder_indx][Lvl.y_indx]+Lvl.borders[Lvl.topBorder_indx][Lvl.h_indx]+fm_levelLabelFont.getAscent()); //(Obj.relativeWINDOW_WIDTH - fm_levelLabelFont.stringWidth(levelLabelText))/2, (Obj.relativeWINDOW_HEIGHT - fm_levelLabelFont.getAscent())/2)
        
        g.setColor(new Color(0, 192, 255));
        for(int i=0; i<Proj.P_maxNumProj; i++) {
            if(Proj.player_visible[i]==true) { //player projectile
                g.fillOval((int)Proj.player_x[i], (int)Proj.player_y[i], Proj.player_diameter, Proj.player_diameter);
            }
        }
        g.setColor(new Color(0, 162, 225));
        if(Proj.playerSpec_visible==true) {
            g.fillOval((int)Proj.playerSpec_x, (int)Proj.playerSpec_y, Proj.playerSpec_diameter, Proj.playerSpec_diameter);
        }

        g.setColor(new Color(180, 0, 0));
        for(int i=0; i<shld.AI_currentNumShield; i++) { //AI shields
            if(shld.AI_visible[i] == true) {
                g.fillRect((int)shld.AI_x[i], (int)shld.AI_y[i], shld.AI_diameter, shld.AI_diameter);
            }
        }
        
        g.setColor(new Color(220, 0, 0));
        for(int e=0; e<Proj.AI_maxNumProj; e++) {
            if(Proj.enemyAI_visible[e]==true) {
                g.fillOval((int)Proj.enemyAI_x[e], (int)Proj.enemyAI_y[e], Proj.enemyAI_diameter, Proj.enemyAI_diameter);
            }
        }
        
        if(P.dead==false) {
            g.setColor(new Color(playerColor_r, playerColor_g, playerColor_b));
            g.fillRect(P.x, P.y, P.w, P.h); //main square
            
            P_gun.reset();
            for(int i=0; i<4; i++) {
                P_gun.addPoint(P.gun_xPoints[i], P.gun_yPoints[i]);
            }
            g.fillPolygon(P_gun);
            
            g.setColor(new Color(0, 0, 0));
            g.drawOval(P.x, P.y, P.w, P.h); //circle
            
            if(Proj.p_projCooldown != 0) //if true
                g.setColor(new Color(255, 0, 0)); //red
            else
                g.setColor(new Color(0, 255, 0)); //green
            g.fillOval(P.gunReadyLight_x, P.gunReadyLight_y, P.gunReadyLight_w, P.gunReadyLight_w);
            g.setColor(new Color(0, 0, 0));
            g.drawOval(P.gunReadyLight_x, P.gunReadyLight_y, P.gunReadyLight_w, P.gunReadyLight_w);
        }
        else{
            g.setColor(new Color(0, 255, 0));
            g.setFont(youDiedFont);
            g.drawString("YOU DIED", (Obj.relativeWINDOW_WIDTH-fm_youDiedFont.stringWidth("YOU DIED"))/2, (Obj.relativeWINDOW_HEIGHT+fm_youDiedFont.getAscent())/2);
        }
        
        if(AI.dead==false) {
            g.setColor(new Color(255, 0, 0));
            g.fillRect((int)AI.x, (int)AI.y, AI.w, AI.h);
            
            g.setColor(new Color((int)(255*((double)AI.HP / (double)AI.maxHP)), 0, 0));
            g.fillOval(AI.healthDisplay_x, AI.healthDisplay_y, AI.healthDisplay_w, AI.healthDisplay_h);
            g.setColor(new Color(0, 0, 0));
            g.drawOval(AI.healthDisplay_x, AI.healthDisplay_y, AI.healthDisplay_w, AI.healthDisplay_h);
        }
        
        g.setColor(new Color(0, 30, 0));
        for(int bNum=0; bNum<Lvl.borders.length; bNum++) {
            g.fillRect(Lvl.borders[bNum][0], Lvl.borders[bNum][1], Lvl.borders[bNum][2], Lvl.borders[bNum][3]);
        }
        
        //FOR TESTING:
        /*
        g.setFont(currentFont);
        g.setColor(new Color(255, 255, 255));
        g.drawString("p_specCooldown: "+Proj.p_specCooldown, 0, 12);
        g.drawString("playerSpec_visible: "+Proj.playerSpec_visible, 0, 24);
        g.drawString("mX: "+mouseX+", mY: "+mouseY, 0, 36);
        g.drawString("pX: "+P.x+", pY: "+P.y, 0, 48);
        g.drawString("numShields: "+shld.AI_currentNumShield, 0, 60);
        g.drawString("shldHP_max: "+shld.maxNumHP_round, 0, 72);
        g.drawString("shldHP: "+shld.AI_HP[0], 0, 84);
        g.drawString("shldX: "+shld.AI_x[0]+", shldY: "+shld.AI_y[0]+", "+shld.AI_visible[0]+", "+shld.AI_HP[0], 0, 96);
        g.drawString("shldX: "+shld.AI_x[1]+", shldY: "+shld.AI_y[1]+", "+shld.AI_visible[1]+", "+shld.AI_HP[1], 0, 108);
        g.drawString("shldX: "+shld.AI_x[2]+", shldY: "+shld.AI_y[2]+", "+shld.AI_visible[2]+", "+shld.AI_HP[2], 0, 120);
        */
        //repaint();
    }
    
    public class key_listener implements KeyListener {
        @Override
        public void keyTyped(KeyEvent e) {  
        } 
        @Override
        public void keyPressed(KeyEvent e) {
            if(P.dead==false) {
                if(e.getKeyCode()==KeyEvent.VK_W || e.getKeyCode()==KeyEvent.VK_UP) { //move up
                    P.movingUp(true);
                }
                if(e.getKeyCode()==KeyEvent.VK_S || e.getKeyCode()==KeyEvent.VK_DOWN) { //move down
                    P.movingDown(true);
                }
                if(e.getKeyCode()==KeyEvent.VK_D || e.getKeyCode()==KeyEvent.VK_RIGHT) { //move right
                    P.movingRight(true);
                }
                if(e.getKeyCode()==KeyEvent.VK_A || e.getKeyCode()==KeyEvent.VK_LEFT) { //move left
                    P.movingLeft(true);
                }
                if(e.getKeyCode()==KeyEvent.VK_T) { //teleport
                    P.teleport(mouseX, mouseY);
                    //P.setX(mouseX); P.setY(mouseY);
                }
                if(e.getKeyCode()==KeyEvent.VK_K) { //suicide
                    P.die();
                }
                if(e.getKeyCode()==KeyEvent.VK_SPACE) {
                    if(AI.dead==false) {
                        Proj.P_shootSpec(P.x, P.y, P.w, P.h, mouseX, mouseY, P.dead);
                    }
                }
            }
            if(P.dead==true) {
                if(e.getKeyCode()==KeyEvent.VK_R) { //respawn
                    P.respawn(Lvl.playerSpawn_x, Lvl.playerSpawn_y);
                    AI.reset(Lvl.enemyAISpawn_x, Lvl.enemyAISpawn_y);
                    //initGame();
                }
            }
            if(e.getKeyCode()==KeyEvent.VK_ESCAPE) {
                System.exit(0);
            }
            
            //TESTING ONLY
            if(AI.dead==false) { //TESTING ONLY
                if(e.getKeyCode()==KeyEvent.VK_P) {
                    AI.lowerHP();
                }
            }
            //TESTING ONLY ^
            
            
            repaint();
        }
        @Override
        public void keyReleased(KeyEvent e) {
            if(P.dead==false) {
                if(e.getKeyCode()==KeyEvent.VK_W || e.getKeyCode()==KeyEvent.VK_UP) { //move up
                    P.movingUp(false);
                }
                if(e.getKeyCode()==KeyEvent.VK_S || e.getKeyCode()==KeyEvent.VK_DOWN) { //move down
                    P.movingDown(false);
                }
                if(e.getKeyCode()==KeyEvent.VK_D || e.getKeyCode()==KeyEvent.VK_RIGHT) { //move right
                    P.movingRight(false);
                }
                if(e.getKeyCode()==KeyEvent.VK_A || e.getKeyCode()==KeyEvent.VK_LEFT) { //move left
                    P.movingLeft(false);
                }
                if(e.getKeyCode()==KeyEvent.VK_T) { //teleport
                    P.teleport(mouseX, mouseY);
                    //P.setX(mouseX); P.setY(mouseY);
                }
                if(e.getKeyCode()==KeyEvent.VK_K) { //suicide
                    P.die();
                }
                if(e.getKeyCode()==KeyEvent.VK_SPACE) {
                    if(AI.dead==false) {
                        Proj.P_shootSpec(P.x, P.y, P.w, P.h, mouseX, mouseY, P.dead);
                    }
                }
            }
            if(P.dead==true) {
                if(e.getKeyCode()==KeyEvent.VK_R) { //respawn
                    P.respawn(Lvl.playerSpawn_x, Lvl.playerSpawn_y);
                    AI.reset(Lvl.enemyAISpawn_x, Lvl.enemyAISpawn_y);
                    //initGame();
                }
            }
            if(e.getKeyCode()==KeyEvent.VK_ESCAPE) {
                System.exit(0);
            }
            
            //TESTING ONLY
            if(AI.dead==false) { //TESTING ONLY
                if(e.getKeyCode()==KeyEvent.VK_P) {
                    AI.lowerHP();
                }
            }
            //TESTING ONLY ^
            
            repaint();
        }
    }
    public class mouse_listener implements MouseListener {
        @Override
        public void mousePressed(MouseEvent e) {
            
        }
        @Override
        public void mouseReleased(MouseEvent e) {
            
        }
        @Override
        public void mouseEntered(MouseEvent e) {
            
        }
        @Override
        public void mouseExited(MouseEvent e) {
            
        }
        @Override
        public void mouseClicked(MouseEvent e) {
            if(AI.dead==false) {
                Proj.P_shoot(P.x, P.y, P.w, P.h, mouseX, mouseY, P.dead);
            }
            
            repaint();
        }
    }
}








