package SpaceShip;
import java.awt.Dimension;
import javax.swing.JFrame;

public class main {
    
     public static void main(String[] args) {
        // variables
        int tileSize = 32; 
        int rows = 16;
        int columns = 16;
        int boardHeight = columns*tileSize;
        int boardWidth = rows*tileSize;
         
        JFrame frame = new JFrame();
        frame.setTitle("SpaceShip");
        frame.setResizable(true);
        frame.setPreferredSize(new Dimension(boardWidth, boardHeight));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        SpaceInvaders space_shipers = new SpaceInvaders();
        frame.add(space_shipers);
        frame.pack();
        space_shipers.requestFocus();
        frame.setVisible(true);
    }

}
