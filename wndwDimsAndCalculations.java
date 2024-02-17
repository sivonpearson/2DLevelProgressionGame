import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.text.*;
public class wndwDimsAndCalculations {
    final int relativeWINDOW_WIDTH=1170;
    final int relativeWINDOW_HEIGHT=900;
    final int WINDOW_WIDTH=relativeWINDOW_WIDTH+7;
    final int WINDOW_HEIGHT=relativeWINDOW_HEIGHT+29;
    public wndwDimsAndCalculations() {
        
    }
    public int calcDistance(int x1, int y1, int x2, int y2) {
        int x=(int)(Math.sqrt(((x2-x1)*(x2-x1))+((y2-y1)*(y2-y1))));
        return x;
    }
    public boolean checkCollisions_box(int b1_x, int b1_y, int b1_w, int b1_h, int b2_x, int b2_y, int b2_w, int b2_h) {
        boolean HorizontalCheck_1= b1_x<=b2_x+1 && b1_x+b1_w>=b2_x+b2_w-1; //B P B
        boolean HorizontalCheck_2= b1_x>=b2_x+1 && b1_x+b1_w<=b2_x+b2_w-1; //PBP
        boolean HorizontalCheck_3= b1_x<=b2_x+1 && b1_x+b1_w>=b2_x+1; //B PBP
        boolean HorizontalCheck_4= b1_x<=b2_x+b2_w-1 && b1_x+b1_w>=b2_x+b2_w-1; //PBP B
        boolean HorizontalCheck_T= HorizontalCheck_1 || HorizontalCheck_2 || HorizontalCheck_3 || HorizontalCheck_4;
        
        boolean VerticalCheck_1= b1_y<=b2_y+1 && b1_y+b1_h>=b2_y+b2_h-1; //B P B
        boolean VerticalCheck_2= b1_y>=b2_y+1 && b1_y+b1_h<=b2_y+b2_h-1; //PBP
        boolean VerticalCheck_3= b1_y<=b2_y+1 && b1_y+b1_h>=b2_y+1; //B PBP
        boolean VerticalCheck_4= b1_y<=b2_y+b2_h-1 && b1_y+b1_h>=b2_y+b2_h-1; //PBP B
        boolean VerticalCheck_T= VerticalCheck_1 || VerticalCheck_2 || VerticalCheck_3 || VerticalCheck_4;
        
        boolean colliding= HorizontalCheck_T && VerticalCheck_T;
        return colliding;
    }
    public boolean checkCollisions_circle(int circleX, int circleY, int circle_r, int P_x, int P_y, int P_w, int P_h) {
        int circleDistance_x=Math.abs(circleX-P_x);
        int circleDistance_y=Math.abs(circleY-P_y);
        if(circleDistance_x > (P_w/2 + circle_r)) { return false; }
        if(circleDistance_y > (P_h/2 + circle_r)) { return false; }
        
        if(circleDistance_x <= (P_w/2)) { return true; }
        if(circleDistance_y <= (P_h/2)) { return true; }
        
        int cornerDistance_sq=(circleDistance_x - P_w/2)^2 + (circleDistance_y - P_h/2)^2;
        
        return (cornerDistance_sq <= (circle_r^2));
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
}














