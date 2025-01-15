package kirby;

import java.awt.Dimension;
import javax.swing.JFrame;

public class main {
    
    public static void main(String[] args) {
        // 0144276313
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

        Game game = new Game();
        frame.add(game);
        frame.pack();
        game.requestFocus();
        
        frame.setVisible(true);
    }

}
