package kirby;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;

public class main {

    public static void main(String[] args) {
        int tileSize = 32;
        int rows = 44;
        int columns = 20;
        int boardHeight = columns * tileSize;
        int boardWidth = rows * tileSize;

        JFrame frame = new JFrame();
        frame.setTitle("Games");
        frame.setResizable(true);
        frame.setPreferredSize(new Dimension(boardWidth, boardHeight));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Use CardLayout for panel switching
        CardLayout cardLayout = new CardLayout();
        JPanel mainPanel = new JPanel(cardLayout);

        // Create Levels and Game panels
        Levels levelScreen = new Levels(boardWidth, boardHeight, cardLayout, mainPanel);
        Game game = new Game("1", null);

        // Add panels to mainPanel
        mainPanel.add(levelScreen, "Levels");
        mainPanel.add(game, "Game");

        // Add mainPanel to the frame
        frame.add(mainPanel);
        frame.pack();
        frame.setVisible(true);

        // Request focus for the starting panel
        levelScreen.requestFocusInWindow();
    }

    // Levels Panel
    public static class Levels extends JPanel implements KeyListener {

        int level;
        Image bg = new ImageIcon(getClass().getResource("../GameEngines/assets/Ground/Blue.png")).getImage();

        ArrayList<Level> levelTiles = new ArrayList<>();
        int index = 0;

        Image chosen = new ImageIcon(getClass().getResource("../GameEngines/assets/Ground/Blue.png")).getImage();
        Image chosen2 = new ImageIcon(getClass().getResource("../GameEngines/assets/Ground/Brown.png")).getImage();
        Image chosen3 = new ImageIcon(getClass().getResource("../GameEngines/assets/Ground/Pink.png")).getImage();
        Image chosen4 = new ImageIcon(getClass().getResource("../GameEngines/assets/Ground/Green.png")).getImage();
        Image chosen5 = new ImageIcon(getClass().getResource("../GameEngines/assets/Ground/Gray.png")).getImage();
        Image chosen6 = new ImageIcon(getClass().getResource("./assets/Player/playerV1img1.png")).getImage();

        Image[] img = {chosen, chosen2, chosen3, chosen4, chosen5, chosen6};

        // CardLayout and main panel reference
        CardLayout cardLayout;
        JPanel mainPanel;

        public Levels(int width, int height, CardLayout cardLayout, JPanel mainPanel) {
            this.cardLayout = cardLayout;
            this.mainPanel = mainPanel;

            setBackground(Color.BLACK);
            setPreferredSize(new Dimension(width, height));
            setFocusable(true);
            addKeyListener(this);

            // Create level tiles for a 3x2 grid
            for (int row = 0; row < 2; row++) {
                for (int col = 0; col < 3; col++) {
                    levelTiles.add(new Level(col * 100 + col * 370, row * 100 + row * 230, 300, 300, img[index]));
                    index++;
                }
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            for (Level tile : levelTiles) {
                g.drawImage(tile.img, tile.x, tile.y, tile.width, tile.height, null);
            }
        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() >= KeyEvent.VK_1 && e.getKeyCode() <= KeyEvent.VK_6) {
                level = e.getKeyCode() - KeyEvent.VK_0; // Convert key to number (1 to 6)
                bg = img[level - 1]; // Set background based on level
                Game game = new Game(String.valueOf(level), bg); // Create a new Game panel
                mainPanel.add(game, "Game"); // Add new Game panel
                cardLayout.show(mainPanel, "Game"); // Switch to the Game panel
                game.requestFocusInWindow(); // Request focus for the Game panel
                System.out.println(level);
            }
        }

        @Override
        public void keyTyped(KeyEvent e) {}

        @Override
        public void keyReleased(KeyEvent e) {}

        // Level Tile Class
        class Level {
            int x, y, width, height;
            Image img;

            Level(int x, int y, int width, int height, Image img) {
                this.x = x;
                this.y = y;
                this.width = width;
                this.height = height;
                this.img = img;
            }
        }
    }
}
