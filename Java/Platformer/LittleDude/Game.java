package kirby;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.font.ImageGraphicAttribute;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Game extends JPanel implements ActionListener, KeyListener{

    int tileSize = 32; 
    int rows = 40;
    int columns = 19;
    int boardHeight = columns*tileSize;
    int boardWidth = rows*tileSize;

    int p = 0;
    int b = 0;

    @Override
    public void keyTyped(KeyEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public class Block {
        int x, y, width, height;
        Image img;
        boolean player;

        Block(Image img, int x, int y, int width, int height, boolean player) {
            this.img = img;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.player = player;
        }
    }
    class Tile {
        int x;
        int y;
        int x2;
        int y2;
        Image img;
        int idImg;
        String type;
        
        Tile(int x, int y, int x2, int y2, Image img, int idImg, String type) {
            this.x = x;
            this.y = y;
            this.x2 = x2;
            this.y2 = y2;
            this.img = img;
            this.idImg = idImg;
            this.type = type;
        }
    }

    Block player;
    Image player_img = new ImageIcon(getClass().getResource("../SpaceShip/assets/ship.png")).getImage();
    int playerVelocityX = 0;
    int playerVelocityY = 0;
    boolean isStanding = true;

    Image chosen = new ImageIcon(getClass().getResource("../GameEngines/assets/Ground/Blue.png")).getImage();
    Image chosen2 = new ImageIcon(getClass().getResource("../GameEngines/assets/Ground/Brown.png")).getImage();
    Image chosen3 = new ImageIcon(getClass().getResource("../GameEngines/assets/Ground/Pink.png")).getImage();
    Image chosen4 = new ImageIcon(getClass().getResource("../GameEngines/assets/Ground/Green.png")).getImage();
    Image chosen5 = new ImageIcon(getClass().getResource("../GameEngines/assets/Ground/Gray.png")).getImage();
    int idImg = 1;

    ArrayList<Tile> tiles = new ArrayList<Tile>();

    int gra=1;

    public Game() {

        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setFocusable(true);
        addKeyListener(this);

        try (BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\mohamedkaouachi4\\Desktop\\jgame\\kirby\\tile_data5.csv"))) {
            String line;
            int d = 39;
            int g = 18;
            int times = 0;
            int bv = 0;
            int g2 = 0;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                for (int i = 0; i < values.length; i++) {
                        int value = Integer.parseInt(values[i]);
                        if (value == 1 ) {
                            tiles.add(new Tile(p * tileSize, tiles.size() / rows * tileSize+tileSize*b, tileSize, tileSize, null, idImg, "bg"));
                        }
                        if (value == 2 ) {
                            tiles.add(new Tile(p * tileSize, tiles.size() / rows * tileSize+tileSize*b, tileSize, tileSize, chosen2, idImg, "platform"));
                        }
                        if (value == 3 ) {
                            tiles.add(new Tile(p * tileSize, tiles.size() / rows * tileSize+tileSize*b, tileSize, tileSize, chosen3, idImg, "platform"));
                        } 
                        if (value == 4 ) {
                            tiles.add(new Tile(p * tileSize, tiles.size() / rows * tileSize+tileSize*b, tileSize, tileSize, chosen4, idImg, "platform"));
                        } 
                        if (value == 5 ) {
                            tiles.add(new Tile(p * tileSize, tiles.size() / rows * tileSize+tileSize*b, tileSize, tileSize, chosen5, idImg, "platform"));
                        } 
                        b += 1;
                        if (i == d) {
                            b-=1;
                            d += 40;
                        }
                        if ( i == g) {
                            p += 1;
                            g += 19;
                            times += 1;
                            b = bv;
                            if (times == 2) {
                                bv -= 1;
                                times = 0;
                            }
                            if (i == 398+38*g2) {
                                b += 1;
                                g2+=1;
                            }
                        }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        player = new Block(player_img, 0, 500, (int)(tileSize*1.5), (int)(tileSize*1.5), true);
        Timer gameLoop = new Timer(500/60, this); //1000/60 = 16.6
        gameLoop.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (Tile tile : tiles) {
            g.drawImage(tile.img, tile.x, tile.y, tile.x2, tile.y2, null);
        }
        g.drawImage(player.img, player.x, player.y, player.width, player.height, null);

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_SPACE) {
            if (isStanding) {
                playerVelocityY += -15;
            }
        }
        if (key == KeyEvent.VK_LEFT) {
            playerVelocityX = -5;
        }

        if (key == KeyEvent.VK_RIGHT) {
            playerVelocityX = 5;
        }
    }
    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_RIGHT) {
            playerVelocityX = 0;
        }
        
        if (key == KeyEvent.VK_SPACE) {
            playerVelocityY = 0;
        }
    }
    
    public boolean detectCollision(Block a, Tile b) {
        return  a.x < b.x + b.y2 &&  //a's top left corner doesn't reach b's top right corner
                a.x + a.width > b.x &&  //a's top right corner passes b's top left corner
                a.y < b.y + b.y2 && //a's top left corner doesn't reach b's bottom left corner
                a.y + a.height > b.y;   //a's bottom left corner passes b's top left corner
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        gra = 1;
        player.x += playerVelocityX;
        player.y += playerVelocityY;
        for (Tile tile : tiles) {
            if (tile.type == "platform") {
                if (detectCollision(player, tile)) {
                    player.y -= playerVelocityY;
                    player.x -= playerVelocityX;
                    isStanding = true;
                    gra = 0;
                    break;
                } else {
                    isStanding = false;
                }
            }
        }
        if (gra != 0) {
            playerVelocityY += gra;
        } else {
            playerVelocityY = 0;
        }
        repaint();
    }
    
}
