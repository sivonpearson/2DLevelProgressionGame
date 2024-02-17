import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.text.*;
public class start {
    public static void main(String args[]) {
        JFrame frame=new JFrame("Untitled Game");
        game GameBoard = new game();
        wndwDimsAndCalculations Obj=new wndwDimsAndCalculations();
        
        frame.add(GameBoard);
        frame.setSize(Obj.WINDOW_WIDTH, Obj.WINDOW_HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setResizable(false);
    }
}