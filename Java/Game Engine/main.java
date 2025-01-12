
package Game Engine;

import java.awt.Dimension;
import javax.swing.JFrame;

public class main {
    
     public static void main(String[] args) {

        int tileSize = 32; 
        int rows = 44;
        int columns = 20;
        int boardHeight = columns*tileSize;
        int boardWidth = rows*tileSize;

        JFrame frame = new JFrame();
        frame.setTitle("Games");
        frame.setResizable(true);
        frame.setPreferredSize(new Dimension(boardWidth, boardHeight));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        GameEngine engine = new GameEngine();
        frame.add(engine);
        frame.pack();
        engine.requestFocus();
        
        frame.setVisible(true);
    }

}
