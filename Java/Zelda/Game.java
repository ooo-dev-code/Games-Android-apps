package Zelda;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Game extends JPanel implements ActionListener, KeyListener {

    int tileSize = 32;
    int rows = 40;
    int columns = 19;
    int boardWidth = rows*tileSize;
    int boardHeight = columns*tileSize;

    Timer gameLoop;
    int p = 0;
    int b = 0;
    private long lastKeyPressTime = 0; 
    private final int cooldownTime = 200; 

    class Tile {
        int x, y, width, height, idImg;
        String type;
        Image img;
        
        Tile(int x, int y, int width, int height, Image img, int idImg, String type) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.img = img;
            this.idImg = idImg;
            this.type = type;
        }
    }

    int indexL = 0;
    Image idle1_Left = new ImageIcon(getClass().getResource("./assets/Link/Movement/Left/idle1_Left.png")).getImage();
    Image idle2_Left = new ImageIcon(getClass().getResource("./assets/Link/Movement/Left/idle2_Left.png")).getImage();
    Image idle3_Left = new ImageIcon(getClass().getResource("./assets/Link/Movement/Left/idle3_Left.png")).getImage();
    Image idle4_Left = new ImageIcon(getClass().getResource("./assets/Link/Movement/Left/idle4_Left.png")).getImage();

    int indexR = 0;
    Image idle1_Right = new ImageIcon(getClass().getResource("./assets/Link/Movement/Right/idle1_Right.png")).getImage();
    Image idle2_Right = new ImageIcon(getClass().getResource("./assets/Link/Movement/Right/idle2_Right.png")).getImage();
    Image idle3_Right = new ImageIcon(getClass().getResource("./assets/Link/Movement/Right/idle3_Right.png")).getImage();
    Image idle4_Right = new ImageIcon(getClass().getResource("./assets/Link/Movement/Right/idle4_Right.png")).getImage();

    int indexF = 0;
    Image idle1_Front = new ImageIcon(getClass().getResource("./assets/Link/Movement/Front/idle1_Front.png")).getImage();
    Image idle2_Front = new ImageIcon(getClass().getResource("./assets/Link/Movement/Front/idle2_Front.png")).getImage();
    Image idle3_Front = new ImageIcon(getClass().getResource("./assets/Link/Movement/Front/idle3_Front.png")).getImage();
    Image idle4_Front = new ImageIcon(getClass().getResource("./assets/Link/Movement/Front/idle4_Front.png")).getImage();
    
    int indexB = 0;
    Image idle1_Back = new ImageIcon(getClass().getResource("./assets/Link/Movement/Back/idle1_Back.png")).getImage();
    Image idle2_Back = new ImageIcon(getClass().getResource("./assets/Link/Movement/Back/idle2_Back.png")).getImage();
    Image idle3_Back = new ImageIcon(getClass().getResource("./assets/Link/Movement/Back/idle3_Back.png")).getImage();
    Image idle4_Back = new ImageIcon(getClass().getResource("./assets/Link/Movement/Back/idle4_Back.png")).getImage();

    ArrayList<Image> front = new ArrayList<Image>();
    ArrayList<Image> right = new ArrayList<Image>();
    ArrayList<Image> left = new ArrayList<Image>();
    ArrayList<Image> back = new ArrayList<Image>();

    Image grass = new ImageIcon(getClass().getResource("./assets/Map/grass.png")).getImage();

    ArrayList<Tile> tiles = new ArrayList<Tile>();
    int idImg = 0;

    Tile player = new Tile((int)(boardWidth/2), (int)(boardHeight/2), tileSize, 48, idle1_Front, 0, "character");
    int playerVelocityX = 0;
    int playerVelocityY = 0;

    public Game() {

        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setFocusable(true);
        addKeyListener(this);
        
        try (BufferedReader br = new BufferedReader(new FileReader("Zelda\\tile_data1.csv"))) {
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
                            tiles.add(new Tile(p * tileSize, tiles.size() / rows * tileSize+tileSize*b, tileSize, tileSize, grass, idImg, "bg"));
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

        gameLoop = new Timer(750/60, this);
        gameLoop.start();

        front.add(idle1_Front);
        front.add(idle2_Front);
        front.add(idle3_Front);
        front.add(idle4_Front);

        right.add(idle1_Right);
        right.add(idle2_Right);
        right.add(idle3_Right);
        right.add(idle4_Right);

        back.add(idle1_Back);
        back.add(idle2_Back);
        back.add(idle3_Back);
        back.add(idle4_Back);

        left.add(idle1_Left);
        left.add(idle2_Left);
        left.add(idle3_Left);
        left.add(idle4_Left);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (Tile tile : tiles) {
            g.drawImage(tile.img, tile.x, tile.y, tile.width, tile.height, null);
        }
        g.drawImage(player.img, player.x, player.y, player.width, player.height, null);
    }

    @Override
    public void keyTyped(KeyEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void keyPressed(KeyEvent e) {
        long currentTime = System.currentTimeMillis();
        
        if (currentTime - lastKeyPressTime < cooldownTime) {
            return;
        }
    
        lastKeyPressTime = currentTime; 

        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {
            playerVelocityX = -2;

            player.img = left.get(indexL);

            indexL += 1;
            if (indexL > 3) {
                indexL = 0;
            }
            repaint();
        }
        if (key == KeyEvent.VK_RIGHT) {
            playerVelocityX = 2;

            player.img = idle1_Right;player.img = right.get(indexR);

            indexR += 1;
            if (indexR > 3) {
                indexR = 0;
            }
            repaint();
        }
        if (key == KeyEvent.VK_DOWN) {
            playerVelocityY = 2;

            player.img = front.get(indexF);

            indexF += 1;
            if (indexF > 3) {
                indexF = 0;
            }
            repaint();
        }
        if (key == KeyEvent.VK_UP) {
            playerVelocityY = -2;

            player.img = back.get(indexB);

            indexB += 1;
            if (indexB > 3) {
                indexB = 0;
            }
            repaint();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {
            playerVelocityX = 0;
        }
        if (key == KeyEvent.VK_RIGHT) {
            playerVelocityX = 0;
        }
        if (key == KeyEvent.VK_DOWN) {
            playerVelocityY = 0;
        }
        if (key == KeyEvent.VK_UP) {
            playerVelocityY = 0;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        player.x += playerVelocityX;
        player.y += playerVelocityY;
        repaint();
    }
    
}

