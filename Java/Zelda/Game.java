package Zelda;

import java.awt.Color;
import java.awt.Dimension; 
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
    private final int cooldownTime = 50;
    boolean nextUp = false;
    boolean nextDown = false;
    boolean nextRight = false;
    boolean nextLeft = false;
    boolean center = true;
    boolean zeroY = false;
    boolean zeroX = false;
    boolean maxY = false;
    boolean maxX = false;
    boolean gameOver = false;

    int beatenTile2 = 0;
    int beatenTile3 = 0;
    int beatenTile4 = 0;
    int beatenTile5 = 0;
    
    class Character {
        Tile tile;
        int health, maxHealth, attack;

        Character(Tile tile, int health, int maxHealth, int attack) {
            this.tile = tile;
            this.health = health;
            this.maxHealth = maxHealth;
            this.attack = attack;
        }
    }

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

        Rectangle getBounds() {
            return new Rectangle(x, y, width, height);
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
    ArrayList<Image> attFront = new ArrayList<Image>();

    ArrayList<Tile> tiles = new ArrayList<Tile>();
    ArrayList<Tile> tiles2 = new ArrayList<Tile>();
    ArrayList<Tile> tiles3 = new ArrayList<Tile>();
    ArrayList<Tile> tiles4 = new ArrayList<Tile>();
    ArrayList<Tile> tiles5 = new ArrayList<Tile>();
    ArrayList<Tile> tiles6 = new ArrayList<Tile>();
    ArrayList<Tile> tiles7 = new ArrayList<Tile>();
    ArrayList<Tile> tiles8 = new ArrayList<Tile>();
    ArrayList<Tile> tiles9 = new ArrayList<Tile>();

    Image bg = new ImageIcon(getClass().getResource("./assets/Map/Bg.png")).getImage();
    Image bgGameOver = new ImageIcon(getClass().getResource("./assets/GameOver.png")).getImage();

    Image grass = new ImageIcon(getClass().getResource("./assets/Map/grass.png")).getImage();
    Image grass2 = new ImageIcon(getClass().getResource("./assets/Map/grass2.png")).getImage();
    Image flaque = new ImageIcon(getClass().getResource("./assets/Map/flaque.png")).getImage();
    Image culture = new ImageIcon(getClass().getResource("./assets/Map/culture.png")).getImage();
    Image cultureTransition = new ImageIcon(getClass().getResource("./assets/Map/cultureTransition.png")).getImage();
    Image buisson = new ImageIcon(getClass().getResource("./assets/Map/buisson.png")).getImage();
    Image fontaine1 = new ImageIcon(getClass().getResource("./assets/Map/fontaine1.png")).getImage();
    Image home = new ImageIcon(getClass().getResource("./assets/Map/home.png")).getImage();
    Image png = new ImageIcon(getClass().getResource("./assets/png/Movement/Front/idle1_Front.png")).getImage();
    Image rock = new ImageIcon(getClass().getResource("./assets/Map/rock.png")).getImage();
    Image tp = new ImageIcon(getClass().getResource("./assets/Map/tp.png")).getImage();
    Image monster1 = new ImageIcon(getClass().getResource("./assets/Monsters/monster1.png")).getImage();
    Image monster2 = new ImageIcon(getClass().getResource("./assets/Monsters/monster2.png")).getImage();

    Tile playerTile = new Tile((int)(boardWidth/2), (int)(boardHeight/2), tileSize, 48, idle1_Front, 0, "character");
    Character player = new Character(playerTile, 20, 20, 5);
    int playerVelocityX = 0;
    int playerVelocityY = 0;
    boolean openInventory = false;
    int money = 0;
    int keyCount = 0;
    String playerClass = "SwordsMan";

    int indexA = 0;
    Image defense = new ImageIcon(getClass().getResource("./assets/Link/Attack/def.png")).getImage();
    
    boolean attacking = false;
    Tile attackRange = new Tile(playerTile.x-50, playerTile.y-50, playerTile.width+50, playerTile.height+50, null, 0, "player");
    Image attFront1 = new ImageIcon(getClass().getResource("./assets/Link/Attack/Front/idle1_Front.png")).getImage();
    Image attFront2 = new ImageIcon(getClass().getResource("./assets/Link/Attack/Front/idle2_Front.png")).getImage();
    
    Image inventory = new ImageIcon(getClass().getResource("./assets/Link/Objects/menu.png")).getImage();
    Image openedInventory = new ImageIcon(getClass().getResource("./assets/Link/Objects/Inventory.png")).getImage();
    Image heart1Tile = new ImageIcon(getClass().getResource("./assets/Link/Objects/heart1.png")).getImage();
    Image heart2Tile = new ImageIcon(getClass().getResource("./assets/Link/Objects/heart2.png")).getImage();
    Image heart3Tile = new ImageIcon(getClass().getResource("./assets/Link/Objects/heart3.png")).getImage();
    Image heart4Tile = new ImageIcon(getClass().getResource("./assets/Link/Objects/heart4.png")).getImage();
    Image heart5Tile = new ImageIcon(getClass().getResource("./assets/Link/Objects/heart5.png")).getImage();

    Tile heart = new Tile(16, 16, 48, 48, heart1Tile, 0, "player");
    Tile heart2 = new Tile(16, 16, 48, 48, heart1Tile, 0, "player");
    Tile heart3 = new Tile(16, 16, 48, 48, heart1Tile, 0, "player");
    Tile heart4 = new Tile(16, 16, 48, 48, heart1Tile, 0, "player");
    Tile heart5 = new Tile(16, 16, 48, 48, heart1Tile, 0, "player");

    public Game() {

        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setFocusable(true);
        addKeyListener(this);

        tiles.add(new Tile(0, 0, boardWidth, boardHeight, bg, 0, "background"));
        tiles2.add(new Tile(0, 0, boardWidth, boardHeight, bg, 0, "background"));
        tiles3.add(new Tile(0, 0, boardWidth, boardHeight, bg, 0, "background"));
        tiles4.add(new Tile(0, 0, boardWidth, boardHeight, bg, 0, "background"));
        tiles5.add(new Tile(0, 0, boardWidth, boardHeight, bg, 0, "background"));
        tiles6.add(new Tile(0, 0, boardWidth, boardHeight, bg, 0, "background"));
        tiles7.add(new Tile(0, 0, boardWidth, boardHeight, bg, 0, "background"));
        tiles8.add(new Tile(0, 0, boardWidth, boardHeight, bg, 0, "background"));
        tiles9.add(new Tile(0, 0, boardWidth, boardHeight, bg, 0, "background"));

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
        
        attFront.add(attFront1);
        attFront.add(attFront2);

        addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            @Override
            public void mouseDragged(java.awt.event.MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                if (x > 64*5+20 && x <64*5+20+64*3 && y < 54) {
                    openInventory = true;
                }
            }
        });

        loadTiles("Zelda/tile_data1.csv", tiles);
        loadTiles("Zelda/tile_data2.csv", tiles2);
        loadTiles("Zelda/tile_data3.csv", tiles3);
        loadTiles("Zelda/tile_data4.csv", tiles4);
        loadTiles("Zelda/tile_data5.csv", tiles5);
        loadTiles("Zelda/tile_data6.csv", tiles6);
        loadTiles("Zelda/tile_data7.csv", tiles7);
        loadTiles("Zelda/tile_data8.csv", tiles8);
        loadTiles("Zelda/tile_data9.csv", tiles9);
    }

    private void loadTiles(String filePath, List<Tile> tileList) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            int p = 0, b = 0, d = 39, g = 18, times = 0, bv = 0, g2 = 0;
            
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                for (int i = 0; i < values.length; i++) {
                    int value = Integer.parseInt(values[i]);
                    Image img = getImg(value);
                    String type = getType(value);
                    
                    int width = (value == 6 || value == 12 || value == 13) ? tileSize * 2 : (value == 7 ? tileSize * 4 : tileSize);
                    int height = width;
                    
                    tileList.add(new Tile(p * tileSize, tileList.size() / rows * tileSize + tileSize * b, width, height, img, value, type));
                    
                    b += 1;
                    if (i == d) { b -= 1; d += 40; }
                    if (i == g) {
                        p += 1; g += 19; times += 1; b = bv;
                        if (times == 2) { bv -= 1; times = 0; }
                        if (i == 398 + 38 * g2) { b += 1; g2 += 1; }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Image getImg(int value) {
        return switch (value) {
            case 1 -> grass;
            case 2 -> grass2;
            case 3 -> buisson;
            case 4 -> flaque;
            case 5 -> cultureTransition;
            case 6 -> fontaine1;
            case 7 -> home;
            case 9 -> culture;
            case 10 -> png;
            case 11 -> rock;
            case 12 -> tp;
            case 13 -> monster1;
            default -> null;
        };
    }
    private String getType(int value) {
        return switch (value) {
            case 3, 4, 6, 7, 10, 11 -> "collision";
            case 12 -> "tp";
            case 13 -> "monster";
            default -> "bg";
        };
    }

 @Override
protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    List<Tile> tilesCopy = new ArrayList<>(tiles); // Copy the list
    List<Tile> tiles2Copy = new ArrayList<>(tiles2); // Copy the list
    List<Tile> tiles3Copy = new ArrayList<>(tiles3); // Copy the list
    List<Tile> tiles4Copy = new ArrayList<>(tiles4); // Copy the list
    List<Tile> tiles5Copy = new ArrayList<>(tiles5); // Copy the list
    List<Tile> tiles6Copy = new ArrayList<>(tiles6); // Copy the list
    List<Tile> tiles7Copy = new ArrayList<>(tiles7); // Copy the list
    List<Tile> tiles8Copy = new ArrayList<>(tiles8); // Copy the list
    List<Tile> tiles9Copy = new ArrayList<>(tiles9); // Copy the list

    if (center) {
        tile(tilesCopy, true, g);

        g.drawImage(playerTile.img, playerTile.x, playerTile.y, playerTile.width, playerTile.height, null);

        tile(tilesCopy, false, g);

        g.drawImage(inventory, 64*5+20, 16, 64*3, 54, null);
        if (openInventory) {
            g.drawImage(openedInventory, 64*5+20, 16, boardWidth/2, boardHeight, null);
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    g.setColor(Color.DARK_GRAY);
                    g.fillRect((64*5+50)+ 75*i, 66 + 75*j, 75, 75);
                    g.setColor(Color.BLACK);
                    g.drawRect((64*5+50)+ 75*i, 66 + 75*j, 75, 75);
                }
            }
            g.drawImage(playerTile.img, (64*5+50)+ 75, 66 + 75, 75, 75, null);
        }

    } 
    else if (!center && nextUp && !nextRight && !nextLeft) {

        
        tile(tiles2Copy, true, g);

        g.drawImage(playerTile.img, playerTile.x, playerTile.y, playerTile.width, playerTile.height, null);

        tile(tiles2Copy, false, g);
        
        g.drawImage(inventory, 64*5+20, 16, 64*3, 54, null);
        if (openInventory) {
            g.drawImage(openedInventory, 64*5+20, 16, boardWidth/2, boardHeight, null);
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    g.setColor(Color.DARK_GRAY);
                    g.fillRect((64*5+50)+ 75*i, 66 + 75*j, 75, 75);
                    g.setColor(Color.BLACK);
                    g.drawRect((64*5+50)+ 75*i, 66 + 75*j, 75, 75);
                }
            }
            g.drawImage(playerTile.img, (64*5+50)+ 75, 66, 75, 75, null);
        }


    } else if (nextDown && !nextRight && !nextLeft && !center) {

        
        tile(tiles3Copy, true, g);

        g.drawImage(playerTile.img, playerTile.x, playerTile.y, playerTile.width, playerTile.height, null);

        tile(tiles3Copy, false, g);
        
        g.drawImage(inventory, 64*5+20, 16, 64*3, 54, null);
        if (openInventory) {
            g.drawImage(openedInventory, 64*5+20, 16, boardWidth/2, boardHeight, null);
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    g.setColor(Color.DARK_GRAY);
                    g.fillRect((64*5+50)+ 75*i, 66 + 75*j, 75, 75);
                    g.setColor(Color.BLACK);
                    g.drawRect((64*5+50)+ 75*i, 66 + 75*j, 75, 75);
                }
            }
            g.drawImage(playerTile.img, (64*5+50)+ 75, 66 + 75*2, 75, 75, null);
        }


    }  else if (!center && nextRight && !nextUp && !nextDown) {

        
        tile(tiles4Copy, true, g);

        g.drawImage(playerTile.img, playerTile.x, playerTile.y, playerTile.width, playerTile.height, null);

        tile(tiles4Copy, false, g);
        
        g.drawImage(inventory, 64*5+20, 16, 64*3, 54, null);
        if (openInventory) {
            g.drawImage(openedInventory, 64*5+20, 16, boardWidth/2, boardHeight, null);
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    g.setColor(Color.DARK_GRAY);
                    g.fillRect((64*5+50)+ 75*i, 66 + 75*j, 75, 75);
                    g.setColor(Color.BLACK);
                    g.drawRect((64*5+50)+ 75*i, 66 + 75*j, 75, 75);
                }
            }
            g.drawImage(playerTile.img, (64*5+50), 66 + 75, 75, 75, null);
        }


    }  else if (!center && nextLeft && !nextUp && !nextDown) {

        
        tile(tiles5Copy, true, g);

        g.drawImage(playerTile.img, playerTile.x, playerTile.y, playerTile.width, playerTile.height, null);

        tile(tiles5Copy, false, g);
        
        g.drawImage(inventory, 64*5+20, 16, 64*3, 54, null);
        if (openInventory) {
            g.drawImage(openedInventory, 64*5+20, 16, boardWidth/2, boardHeight, null);
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    g.setColor(Color.DARK_GRAY);
                    g.fillRect((64*5+50)+ 75*i, 66 + 75*j, 75, 75);
                    g.setColor(Color.BLACK);
                    g.drawRect((64*5+50)+ 75*i, 66 + 75*j, 75, 75);
                }
            }
            g.drawImage(playerTile.img, (64*5+50)+ 75*2, 66 + 75, 75, 75, null);
        }
    } else if (!center && nextUp && nextRight) {

        
        tile(tiles6Copy, true, g);

        g.drawImage(playerTile.img, playerTile.x, playerTile.y, playerTile.width, playerTile.height, null);

        tile(tiles6Copy, false, g);
        
        g.drawImage(inventory, 64*5+20, 16, 64*3, 54, null);
        if (openInventory) {
            g.drawImage(openedInventory, 64*5+20, 16, boardWidth/2, boardHeight, null);
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    g.setColor(Color.DARK_GRAY);
                    g.fillRect((64*5+50)+ 75*i, 66 + 75*j, 75, 75);
                    g.setColor(Color.BLACK);
                    g.drawRect((64*5+50)+ 75*i, 66 + 75*j, 75, 75);
                }
            }
            g.drawImage(playerTile.img, (64*5+50)+ 75*2, 66, 75, 75, null);
        }
    } else if (!center && nextUp && !nextRight && nextLeft) {

        
        tile(tiles7Copy, true, g);

        g.drawImage(playerTile.img, playerTile.x, playerTile.y, playerTile.width, playerTile.height, null);

        tile(tiles7Copy, false, g);
        
        g.drawImage(inventory, 64*5+20, 16, 64*3, 54, null);
        if (openInventory) {
            g.drawImage(openedInventory, 64*5+20, 16, boardWidth/2, boardHeight, null);
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    g.setColor(Color.DARK_GRAY);
                    g.fillRect((64*5+50)+ 75*i, 66 + 75*j, 75, 75);
                    g.setColor(Color.BLACK);
                    g.drawRect((64*5+50)+ 75*i, 66 + 75*j, 75, 75);
                }
            }
            g.drawImage(playerTile.img, (64*5+50), 66, 75, 75, null);
        }
    } else if (!center && nextDown && nextRight && !nextLeft) {

        
        tile(tiles8Copy, true, g);

        g.drawImage(playerTile.img, playerTile.x, playerTile.y, playerTile.width, playerTile.height, null);

        tile(tiles8Copy, false, g);
        
        g.drawImage(inventory, 64*5+20, 16, 64*3, 54, null);
        if (openInventory) {
            g.drawImage(openedInventory, 64*5+20, 16, boardWidth/2, boardHeight, null);
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    g.setColor(Color.DARK_GRAY);
                    g.fillRect((64*5+50)+ 75*i, 66 + 75*j, 75, 75);
                    g.setColor(Color.BLACK);
                    g.drawRect((64*5+50)+ 75*i, 66 + 75*j, 75, 75);
                }
            }
            g.drawImage(playerTile.img, (64*5+50)+ 75*2, 66 + 75*2, 75, 75, null);
        }
    } else if (!center && nextDown && !nextRight && nextLeft) {

        
        tile(tiles9Copy, true, g);

        g.drawImage(playerTile.img, playerTile.x, playerTile.y, playerTile.width, playerTile.height, null);

        tile(tiles9Copy, false, g);
        
        g.drawImage(inventory, 64*5+20, 16, 64*3, 54, null);
        if (openInventory) {
            g.drawImage(openedInventory, 64*5+20, 16, boardWidth/2, boardHeight, null);
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    g.setColor(Color.DARK_GRAY);
                    g.fillRect((64*5+50)+ 75*i, 66 + 75*j, 75, 75);
                    g.setColor(Color.BLACK);
                    g.drawRect((64*5+50)+ 75*i, 66 + 75*j, 75, 75);
                }
            }
            g.drawImage(playerTile.img, (64*5+50), 66 + 75*2, 75, 75, null);
        }
    }
    Tile[] hearts = {heart, heart2, heart3, heart4, heart5};
    for (int i = 0; i < hearts.length; i++) {
        g.drawImage(hearts[i].img, hearts[i].x + 64 * i, hearts[i].y, hearts[i].width, hearts[i].height, null);
    }
}

public void tile(List<Tile> tileList, boolean isBackground, Graphics g) {
    for (Tile tile : tileList) {
        if (isBackground) {
            if (tile.idImg < 6) {
                g.drawImage(tile.img, tile.x, tile.y, tile.width, tile.height, null);
            }
        } else {
            if (tile.idImg >= 6) {
                g.drawImage(tile.img, tile.x, tile.y, tile.width, tile.height, null);
            }
        }
    }
}

    private boolean checkCollision() {
        Rectangle futureBounds = new Rectangle(playerTile.x+5, playerTile.y+5, playerTile.width, playerTile.height);
        
        List<Tile> tilesCopy = new ArrayList<>(tiles); // Copy the list
        List<Tile> tiles2Copy = new ArrayList<>(tiles2); // Copy the list
        List<Tile> tiles3Copy = new ArrayList<>(tiles3); // Copy the list
        List<Tile> tiles4Copy = new ArrayList<>(tiles4); // Copy the list
        List<Tile> tiles5Copy = new ArrayList<>(tiles5); // Copy the list
        List<Tile> tiles6Copy = new ArrayList<>(tiles6); // Copy the list
        List<Tile> tiles7Copy = new ArrayList<>(tiles7); // Copy the list
        List<Tile> tiles8Copy = new ArrayList<>(tiles8); // Copy the list
        List<Tile> tiles9Copy = new ArrayList<>(tiles9); // Copy the list

        if (attacking) {
            futureBounds = new Rectangle(playerTile.x+5-tileSize, playerTile.y-tileSize, playerTile.width+tileSize*2, playerTile.height+tileSize*2);
        } else {
            futureBounds = new Rectangle(playerTile.x+5, playerTile.y, playerTile.width, playerTile.height);
        }

        for (Tile tile : tilesCopy) {
            if ("collision".equals(tile.type) && futureBounds.intersects(tile.getBounds()) && center) {
                return true;
            }
            if ("tp".equals(tile.type) && futureBounds.intersects(tile.getBounds()) && center) {
                if (tile.y > 2*tileSize) {
                    System.out.println("Bottom");
                    nextRight = false;
                    nextLeft = false;
                    nextDown = true;
                    nextUp = false;
                    center = false;
                    maxY = true;
                }
                if (tile.y < 2*tileSize) {
                    System.out.println("Top");
                    nextRight = false;
                    nextLeft = false;
                    nextDown = false;
                    nextUp = true;
                    center = false;
                    zeroY = true;
                }
                if (tile.x > boardHeight-2*tileSize) {
                    System.out.println("Right");
                    nextRight = true;
                    nextLeft = false;
                    nextDown = false;
                    nextUp = false;
                    center = false;
                    zeroX = true;
                }
                
                if (tile.x < 2*tileSize) {
                    System.out.println("Left");
                    nextLeft = true;
                    nextDown = false;
                    nextUp = false;
                    center = false;
                    maxX = true;
                }
            }
        }




        for (Tile tile : tiles2Copy) {
            if ("collision".equals(tile.type) && futureBounds.intersects(tile.getBounds()) && nextUp && !nextRight && !nextLeft) {
                return true;
            }
            if ("tp".equals(tile.type) && futureBounds.intersects(tile.getBounds())  && nextUp && !nextRight && !nextLeft && beatenTile2 == 3) {
                if (tile.y < 4*tileSize) {
                    System.out.println("Top");
                    nextRight = false;
                    nextLeft = false;
                    nextDown = false;
                    nextUp = false;
                    center = true;
                    zeroY = true;
                }
                if (tile.x < 2*tileSize) {
                    System.out.println("Left");
                    nextRight = false;
                    nextLeft = true;
                    nextDown = false;
                    nextUp = true;
                    center = false;
                    maxX = true;
                }
                
                if (tile.x > boardWidth-5*tileSize) {
                    System.out.println("Right");
                    nextRight = true;
                    nextLeft = false;
                    nextDown = false;
                    nextUp = true;
                    center = false;
                    zeroX = true;
                }
            }
            if ("monster".equals(tile.type) && futureBounds.intersects(tile.getBounds()) && nextUp && !nextRight && !nextLeft) {
                if (!attacking) {
                    if (tile.x > playerTile.x) {
                        playerTile.x -= tileSize;
                    } else {
                        playerTile.x += tileSize;
                    }
                    player.health -= 1;
                } else {

                    if (tile.x < playerTile.x) {
                        tile.x -= tileSize;
                        tile.img = monster2;
                        tile.type = "beaten";
                    } else {
                        tile.x += tileSize;
                        tile.img = monster2;
                        tile.type = "beaten";
                    }
                    beatenTile2 += 1;
                    money += 1;
                    System.out.println(beatenTile2);

                }
            }
        }




        for (Tile tile : tiles3Copy) {
            if ("collision".equals(tile.type) && futureBounds.intersects(tile.getBounds())  && nextDown && !nextRight && !nextLeft) {
                return true;
            }
            if ("tp".equals(tile.type) && futureBounds.intersects(tile.getBounds())  && nextDown && !nextRight && !nextLeft && beatenTile3 == 3) {
                if (tile.y < 2*tileSize) {
                    System.out.println("Top");
                    nextRight = false;
                    nextLeft = false;
                    nextDown = false;
                    nextUp = false;
                    center = true;
                    zeroY = true;
                }
                if (tile.x > boardWidth-tileSize*2) {
                    System.out.println("Right");
                    nextRight = true;
                    nextLeft = false;
                    nextDown = true;
                    nextUp = false;
                    center = false;
                    zeroX = true;
                }
                if (tile.x < tileSize*2) {
                    System.out.println("Left");
                    nextRight = false;
                    nextLeft = true;
                    nextDown = true;
                    nextUp = false;
                    center = false;
                    maxX = true;
                }
            }
            if ("monster".equals(tile.type) && futureBounds.intersects(tile.getBounds())  && nextDown && !nextRight && !nextLeft) {
                if (!attacking) {
                    if (tile.x > playerTile.x) {
                        playerTile.x -= tileSize;
                    } else {
                        playerTile.x += tileSize;
                    }
                    player.health -= 1;
                } else {

                    if (tile.x < playerTile.x) {
                        tile.x -= tileSize;
                        tile.img = monster2;
                        tile.type = "beaten";
                    } else {
                        tile.x += tileSize;
                        tile.img = monster2;
                        tile.type = "beaten";
                    }
                    beatenTile3 += 1;
                    money += 1;
                    System.out.println(beatenTile3);

                }
            }
        }
        
        
        for (Tile tile : tiles4Copy) {
            if ("collision".equals(tile.type) && futureBounds.intersects(tile.getBounds())  && nextRight && !nextUp && !nextDown) {
                return true;
            }

            if ("tp".equals(tile.type) && futureBounds.intersects(tile.getBounds()) && nextRight && !nextUp && !nextDown && beatenTile4 == 3) {
                if (tile.y < 4*tileSize) {
                    System.out.println("Top");
                    nextRight = true;
                    nextLeft = false;
                    nextDown = false;
                    nextUp = true;
                    center = false;
                    zeroY = true;
                }
                if (tile.y > boardHeight-4*tileSize) {
                    System.out.println("Bottom");
                    nextRight = true;
                    nextLeft = false;
                    nextDown = true;
                    nextUp = false;
                    center = false;
                    maxY = true;
                }
                if (tile.x < 2*tileSize) {
                    System.out.println("Left");
                    nextRight = false;
                    nextLeft = false;
                    nextDown = false;
                    nextUp = false;
                    center = true;
                    maxX = true;
                }
            }
            if ("monster".equals(tile.type) && futureBounds.intersects(tile.getBounds())  && nextRight && !nextUp && !nextDown) {
                if (!attacking) {
                    if (tile.x > playerTile.x) {
                        playerTile.x -= tileSize;
                    } else {
                        playerTile.x += tileSize;
                    }
                    player.health -= 1;
                } else {

                    if (tile.x < playerTile.x) {
                        tile.x -= tileSize;
                        tile.img = monster2;
                        tile.type = "beaten";
                    } else {
                        tile.x += tileSize;
                        tile.img = monster2;
                        tile.type = "beaten";
                    }
                    beatenTile4 += 1;
                    money += 1;
                    System.out.println(beatenTile4);

                }
            }
        }
        
        for (Tile tile : tiles5Copy) {
            if ("collision".equals(tile.type) && futureBounds.intersects(tile.getBounds())  && nextLeft && !nextUp && !nextDown) {
                return true;
            }

            if ("tp".equals(tile.type) && futureBounds.intersects(tile.getBounds()) && nextLeft && !nextUp && !nextDown && beatenTile5 == 3) {
                if (tile.y < 4*tileSize) {
                    System.out.println("Top");
                    nextRight = false;
                    nextLeft = true;
                    nextDown = false;
                    nextUp = true;
                    center = false;
                    zeroY = true;
                }
                if (tile.y > boardHeight-4*tileSize) {
                    System.out.println("Bottom");
                    nextRight = false;
                    nextLeft = true;
                    nextDown = true;
                    nextUp = false;
                    center = false;
                    zeroY = true;
                }
                if (tile.x > boardWidth-2*tileSize) {
                    System.out.println("Right");
                    nextRight = false;
                    nextLeft = false;
                    nextDown = false;
                    nextUp = false;
                    center = true;
                    zeroX = true;
                }
            }
            
            if ("monster".equals(tile.type) && futureBounds.intersects(tile.getBounds())  && nextLeft && !nextUp && !nextDown) {
                if (!attacking) {
                    if (tile.x > playerTile.x) {
                        playerTile.x -= tileSize;
                    } else {
                        playerTile.x += tileSize;
                    }
                    player.health -= 1;
                } else {

                    if (tile.x < playerTile.x) {
                        tile.x -= tileSize;
                        tile.img = monster2;
                        tile.type = "beaten";
                    } else {
                        tile.x += tileSize;
                        tile.img = monster2;
                        tile.type = "beaten";
                    }
                    beatenTile5 += 1;
                    money += 1;
                    System.out.println(beatenTile5);

                }
            }
        }
        
        for (Tile tile : tiles6Copy) {
            if ("collision".equals(tile.type) && futureBounds.intersects(tile.getBounds()) && nextUp && nextRight && !nextLeft) {
                return true;
            }
        }
        
        for (Tile tile : tiles7Copy) {
            if ("collision".equals(tile.type) && futureBounds.intersects(tile.getBounds()) && nextUp && !nextRight && nextLeft) {
                return true;
            }
        }
        
        for (Tile tile : tiles8Copy) {
            if ("collision".equals(tile.type) && futureBounds.intersects(tile.getBounds()) && nextDown && nextRight && !nextLeft) {
                return true;
            }
        }
        
        for (Tile tile : tiles9Copy) {
            if ("collision".equals(tile.type) && futureBounds.intersects(tile.getBounds()) && nextDown && !nextRight && nextLeft) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        long currentTime = System.currentTimeMillis();
        
        if (currentTime - lastKeyPressTime < cooldownTime) {
            return;
        }
    
        lastKeyPressTime = currentTime; 

        int key = e.getKeyCode();

        if (key == KeyEvent.VK_M) {
            playerTile.img = defense;
        }
        if (key == KeyEvent.VK_X) {
            openInventory = false;
        }
        if (key == KeyEvent.VK_N) {
            attacking = true;
            playerTile.img = attFront.get(indexA);

            indexA += 1;
            if (indexA > 1) {
                indexA = 0;
            }
            repaint();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        long currentTime = System.currentTimeMillis();
        
        if (currentTime - lastKeyPressTime < cooldownTime) {
            return;
        }
    
        lastKeyPressTime = currentTime; 

        int key = e.getKeyCode();

        if (key == KeyEvent.VK_M) {
            playerTile.img = defense;
        }
        if (key == KeyEvent.VK_X) {
            openInventory = false;
        }
        if (key == KeyEvent.VK_N) {
            attacking = true;
            playerTile.img = attFront.get(indexA);

            indexA += 1;
            if (indexA > 1) {
                indexA = 0;
            }
            repaint();
        }

        if (key == KeyEvent.VK_A) {
            player.health -= 1;
        }

        if (key == KeyEvent.VK_LEFT) {
            playerVelocityX = -3;

            playerTile.img = left.get(indexL);

            indexL += 1;
            if (indexL > 3) {
                indexL = 0;
            }
            repaint();
        }
        if (key == KeyEvent.VK_RIGHT) {
            playerVelocityX = 3;

            playerTile.img = idle1_Right;playerTile.img = right.get(indexR);

            indexR += 1;
            if (indexR > 3) {
                indexR = 0;
            }
            repaint();
        }
        if (key == KeyEvent.VK_DOWN) {
            playerVelocityY = 3;

            playerTile.img = front.get(indexF);

            indexF += 1;
            if (indexF > 3) {
                indexF = 0;
            }
            repaint();
        }
        if (key == KeyEvent.VK_UP) {
            playerVelocityY = -3;

            playerTile.img = back.get(indexB);

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

        if (key == KeyEvent.VK_M || key == KeyEvent.VK_N) {
            playerTile.img = front.get(0);
            attacking = false;
        }

        if (key == KeyEvent.VK_G) {
            center = false;
            nextDown = true;
        }

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
        playerTile.x += playerVelocityX;
        playerTile.y += playerVelocityY;

        if (zeroY) {
            playerTile.y = boardHeight-4*tileSize;
            zeroY = false;
        }
        if (maxY) {
            playerTile.y = 2*tileSize;
            maxY = false;
        }
        if (zeroX) {
            playerTile.x = 2*tileSize;
            zeroX = false;
        }
        if (maxX) {
            playerTile.x = boardWidth-4*tileSize;
            maxX = false;
        }

        if (checkCollision()) {
            playerTile.x -= playerVelocityX;
            playerTile.y -= playerVelocityY;
        }

        if (player.health == 20) {
            heart.img = heart1Tile;
        }
        if (player.health == 19) {
            heart.img = heart2Tile;
        }
        if (player.health == 16) {
            heart.img = heart3Tile;
        }
        if (player.health == 17) {
            heart.img = heart4Tile;
        }
        if (player.health == 16) {
            heart.img = heart5Tile;
            heart2.img = heart1Tile;
        }
        if (player.health == 15) {
            heart2.img = heart2Tile;
        }
        if (player.health == 14) {
            heart2.img = heart3Tile;
        }
        if (player.health == 13) {
            heart2.img = heart4Tile;
        }
        if (player.health == 12) {
            heart2.img = heart5Tile;
            heart3.img = heart1Tile;
        }
        if (player.health == 11) {
            heart3.img = heart2Tile;
        }
        if (player.health == 10) {
            heart3.img = heart3Tile;
        }
        if (player.health == 9) {
            heart3.img = heart4Tile;
        }
        if (player.health == 8) {
            heart3.img = heart5Tile;
            heart4.img = heart1Tile;
        }
        if (player.health == 7) {
            heart4.img = heart2Tile;
        }
        if (player.health == 6) {
            heart4.img = heart3Tile;
        }
        if (player.health == 5) {
            heart4.img = heart4Tile;
        }
        if (player.health == 4) {
            heart4.img = heart5Tile;
            heart5.img = heart1Tile;
        }
        if (player.health == 3) {
            heart5.img = heart2Tile;
        }
        if (player.health == 2) {
            heart5.img = heart3Tile;
        }
        if (player.health == 1) {
            heart5.img = heart4Tile;
        }
        if (player.health == 0) {
            heart5.img = heart5Tile;
            gameOver = true;
        }

        repaint();
    }
    
}
