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
    private final int cooldownTime = 200;
    boolean nextUp = false;
    boolean nextDown = false;
    boolean nextRight = false;
    boolean nextLeft = false;
    boolean center = true;
    boolean gameOver = false;
    
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
    Image idle1_Left = new ImageIcon(getClass().getResource("./assets/png/Movement/Left/idle1_Left.png")).getImage();
    Image idle2_Left = new ImageIcon(getClass().getResource("./assets/png/Movement/Left/idle2_Left.png")).getImage();
    Image idle3_Left = new ImageIcon(getClass().getResource("./assets/png/Movement/Left/idle3_Left.png")).getImage();
    Image idle4_Left = new ImageIcon(getClass().getResource("./assets/png/Movement/Left/idle4_Left.png")).getImage();

    int indexR = 0;
    Image idle1_Right = new ImageIcon(getClass().getResource("./assets/png/Movement/Right/idle1_Right.png")).getImage();
    Image idle2_Right = new ImageIcon(getClass().getResource("./assets/png/Movement/Right/idle2_Right.png")).getImage();
    Image idle3_Right = new ImageIcon(getClass().getResource("./assets/png/Movement/Right/idle3_Right.png")).getImage();
    Image idle4_Right = new ImageIcon(getClass().getResource("./assets/png/Movement/Right/idle4_Right.png")).getImage();

    int indexF = 0;
    Image idle1_Front = new ImageIcon(getClass().getResource("./assets/png/Movement/Front/idle1_Front.png")).getImage();
    Image idle2_Front = new ImageIcon(getClass().getResource("./assets/png/Movement/Front/idle2_Front.png")).getImage();
    Image idle3_Front = new ImageIcon(getClass().getResource("./assets/png/Movement/Front/idle3_Front.png")).getImage();
    Image idle4_Front = new ImageIcon(getClass().getResource("./assets/png/Movement/Front/idle4_Front.png")).getImage();
    
    int indexB = 0;
    Image idle1_Back = new ImageIcon(getClass().getResource("./assets/png/Movement/Back/idle1_Back.png")).getImage();
    Image idle2_Back = new ImageIcon(getClass().getResource("./assets/png/Movement/Back/idle2_Back.png")).getImage();
    Image idle3_Back = new ImageIcon(getClass().getResource("./assets/png/Movement/Back/idle3_Back.png")).getImage();
    Image idle4_Back = new ImageIcon(getClass().getResource("./assets/png/Movement/Back/idle4_Back.png")).getImage();

    ArrayList<Image> front = new ArrayList<Image>();
    ArrayList<Image> right = new ArrayList<Image>();
    ArrayList<Image> left = new ArrayList<Image>();
    ArrayList<Image> back = new ArrayList<Image>();

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

    Tile playerTile = new Tile((int)(boardWidth/2), (int)(boardHeight/2), tileSize, 48, idle1_Front, 0, "character");
    Character player = new Character(playerTile, 20, 20, 5);
    int playerVelocityX = 0;
    int playerVelocityY = 0;

    int indexA = 0;
    Image attack1 = new ImageIcon(getClass().getResource("./assets/Link/Attack/att1.png")).getImage();

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
        
        try (BufferedReader br = new BufferedReader(new FileReader("Zelda\\tile_data1.csv"))) {
            String line;
            
            p = 0;
            b = 0;
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
                            tiles.add(new Tile(p * tileSize, tiles.size() / rows * tileSize+tileSize*b, tileSize, tileSize, grass, value, "bg"));
                        }
                        if (value == 2 ) {
                            tiles.add(new Tile(p * tileSize, tiles.size() / rows * tileSize+tileSize*b, tileSize, tileSize, grass2, value, "bg"));
                        }
                        if (value == 3 ) {
                            tiles.add(new Tile(p * tileSize, tiles.size() / rows * tileSize+tileSize*b, tileSize, tileSize, buisson, value, "collision"));
                        } 
                        if (value == 4 ) {
                            tiles.add(new Tile(p * tileSize, tiles.size() / rows * tileSize+tileSize*b, tileSize, tileSize, flaque, value, "collision"));
                        }
                        if (value == 5 ) {
                            tiles.add(new Tile(p * tileSize, tiles.size() / rows * tileSize+tileSize*b, tileSize, tileSize, cultureTransition, value, "bg"));
                        } 
                        if (value == 6 ) {
                            tiles.add(new Tile(p * tileSize, tiles.size() / rows * tileSize+tileSize*b, tileSize*2, tileSize*2, fontaine1, value, "bg"));
                        } 
                        if (value == 7 ) {
                            tiles.add(new Tile(p * tileSize, tiles.size() / rows * tileSize+tileSize*b, tileSize*4, tileSize*4, home, value, "collision"));
                        } 
                        if (value == 9 ) {
                            tiles.add(new Tile(p * tileSize, tiles.size() / rows * tileSize+tileSize*b, tileSize, tileSize, culture, value, "bg"));
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
        try (BufferedReader br = new BufferedReader(new FileReader("Zelda\\tile_data2.csv"))) {
            String line;
            
            p = 0;
            b = 0;
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
                            tiles2.add(new Tile(p * tileSize, tiles2.size() / rows * tileSize+tileSize*b, tileSize, tileSize, grass, value, "bg"));
                        }
                        if (value == 2 ) {
                            tiles2.add(new Tile(p * tileSize, tiles2.size() / rows * tileSize+tileSize*b, tileSize, tileSize, grass2, value, "bg"));
                        }
                        if (value == 3 ) {
                            tiles2.add(new Tile(p * tileSize, tiles2.size() / rows * tileSize+tileSize*b, tileSize, tileSize, buisson, value, "collision"));
                        } 
                        if (value == 4 ) {
                            tiles2.add(new Tile(p * tileSize, tiles2.size() / rows * tileSize+tileSize*b, tileSize, tileSize, flaque, value, "collision"));
                        }
                        if (value == 5 ) {
                            tiles2.add(new Tile(p * tileSize, tiles2.size() / rows * tileSize+tileSize*b, tileSize, tileSize, cultureTransition, value, "bg"));
                        } 
                        if (value == 6 ) {
                            tiles2.add(new Tile(p * tileSize, tiles2.size() / rows * tileSize+tileSize*b, tileSize*2, tileSize*2, fontaine1, value, "bg"));
                        } 
                        if (value == 7 ) {
                            tiles2.add(new Tile(p * tileSize, tiles2.size() / rows * tileSize+tileSize*b, tileSize*4, tileSize*4, home, value, "collision"));
                        } 
                        if (value == 9 ) {
                            tiles2.add(new Tile(p * tileSize, tiles2.size() / rows * tileSize+tileSize*b, tileSize, tileSize, culture, value, "bg"));
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
        try (BufferedReader br = new BufferedReader(new FileReader("Zelda\\tile_data3.csv"))) {
            String line;
            
            p = 0;
            b = 0;
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
                            tiles3.add(new Tile(p * tileSize, tiles3.size() / rows * tileSize+tileSize*b, tileSize, tileSize, grass, value, "bg"));
                        }
                        if (value == 2 ) {
                            tiles3.add(new Tile(p * tileSize, tiles3.size() / rows * tileSize+tileSize*b, tileSize, tileSize, grass2, value, "bg"));
                        }
                        if (value == 3 ) {
                            tiles3.add(new Tile(p * tileSize, tiles3.size() / rows * tileSize+tileSize*b, tileSize, tileSize, buisson, value, "collision"));
                        } 
                        if (value == 4 ) {
                            tiles3.add(new Tile(p * tileSize, tiles3.size() / rows * tileSize+tileSize*b, tileSize, tileSize, flaque, value, "collision"));
                        }
                        if (value == 5 ) {
                            tiles3.add(new Tile(p * tileSize, tiles3.size() / rows * tileSize+tileSize*b, tileSize, tileSize, cultureTransition, value, "bg"));
                        } 
                        if (value == 6 ) {
                            tiles3.add(new Tile(p * tileSize, tiles3.size() / rows * tileSize+tileSize*b, tileSize*2, tileSize*2, fontaine1, value, "bg"));
                        } 
                        if (value == 7 ) {
                            tiles3.add(new Tile(p * tileSize, tiles3.size() / rows * tileSize+tileSize*b, tileSize*4, tileSize*4, home, value, "collision"));
                        } 
                        if (value == 9 ) {
                            tiles3.add(new Tile(p * tileSize, tiles3.size() / rows * tileSize+tileSize*b, tileSize, tileSize, culture, value, "bg"));
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
        try (BufferedReader br = new BufferedReader(new FileReader("Zelda\\tile_data4.csv"))) {
            String line;
            
            p = 0;
            b = 0;
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
                            tiles4.add(new Tile(p * tileSize, tiles4.size() / rows * tileSize+tileSize*b, tileSize, tileSize, grass, value, "bg"));
                        }
                        if (value == 2 ) {
                            tiles4.add(new Tile(p * tileSize, tiles4.size() / rows * tileSize+tileSize*b, tileSize, tileSize, grass2, value, "bg"));
                        }
                        if (value == 3 ) {
                            tiles4.add(new Tile(p * tileSize, tiles4.size() / rows * tileSize+tileSize*b, tileSize, tileSize, buisson, value, "collision"));
                        } 
                        if (value == 4 ) {
                            tiles4.add(new Tile(p * tileSize, tiles4.size() / rows * tileSize+tileSize*b, tileSize, tileSize, flaque, value, "collision"));
                        }
                        if (value == 5 ) {
                            tiles4.add(new Tile(p * tileSize, tiles4.size() / rows * tileSize+tileSize*b, tileSize, tileSize, cultureTransition, value, "bg"));
                        } 
                        if (value == 6 ) {
                            tiles4.add(new Tile(p * tileSize, tiles4.size() / rows * tileSize+tileSize*b, tileSize*2, tileSize*2, fontaine1, value, "bg"));
                        } 
                        if (value == 7 ) {
                            tiles4.add(new Tile(p * tileSize, tiles4.size() / rows * tileSize+tileSize*b, tileSize*4, tileSize*4, home, value, "collision"));
                        } 
                        if (value == 9 ) {
                            tiles4.add(new Tile(p * tileSize, tiles4.size() / rows * tileSize+tileSize*b, tileSize, tileSize, culture, value, "bg"));
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
        try (BufferedReader br = new BufferedReader(new FileReader("Zelda\\tile_data5.csv"))) {
            String line;
            
            p = 0;
            b = 0;
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
                            tiles5.add(new Tile(p * tileSize, tiles5.size() / rows * tileSize+tileSize*b, tileSize, tileSize, grass, value, "bg"));
                        }
                        if (value == 2 ) {
                            tiles5.add(new Tile(p * tileSize, tiles5.size() / rows * tileSize+tileSize*b, tileSize, tileSize, grass2, value, "bg"));
                        }
                        if (value == 3 ) {
                            tiles5.add(new Tile(p * tileSize, tiles5.size() / rows * tileSize+tileSize*b, tileSize, tileSize, buisson, value, "collision"));
                        } 
                        if (value == 4 ) {
                            tiles5.add(new Tile(p * tileSize, tiles5.size() / rows * tileSize+tileSize*b, tileSize, tileSize, flaque, value, "collision"));
                        }
                        if (value == 5 ) {
                            tiles5.add(new Tile(p * tileSize, tiles5.size() / rows * tileSize+tileSize*b, tileSize, tileSize, cultureTransition, value, "bg"));
                        } 
                        if (value == 6 ) {
                            tiles5.add(new Tile(p * tileSize, tiles5.size() / rows * tileSize+tileSize*b, tileSize*2, tileSize*2, fontaine1, value, "bg"));
                        } 
                        if (value == 7 ) {
                            tiles5.add(new Tile(p * tileSize, tiles5.size() / rows * tileSize+tileSize*b, tileSize*4, tileSize*4, home, value, "collision"));
                        } 
                        if (value == 9 ) {
                            tiles5.add(new Tile(p * tileSize, tiles5.size() / rows * tileSize+tileSize*b, tileSize, tileSize, culture, value, "bg"));
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
        try (BufferedReader br = new BufferedReader(new FileReader("Zelda\\tile_data6.csv"))) {
            String line;
            
            p = 0;
            b = 0;
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
                            tiles6.add(new Tile(p * tileSize, tiles6.size() / rows * tileSize+tileSize*b, tileSize, tileSize, grass, value, "bg"));
                        }
                        if (value == 2 ) {
                            tiles6.add(new Tile(p * tileSize, tiles6.size() / rows * tileSize+tileSize*b, tileSize, tileSize, grass2, value, "bg"));
                        }
                        if (value == 3 ) {
                            tiles6.add(new Tile(p * tileSize, tiles6.size() / rows * tileSize+tileSize*b, tileSize, tileSize, buisson, value, "collision"));
                        } 
                        if (value == 4 ) {
                            tiles6.add(new Tile(p * tileSize, tiles6.size() / rows * tileSize+tileSize*b, tileSize, tileSize, flaque, value, "collision"));
                        }
                        if (value == 5 ) {
                            tiles6.add(new Tile(p * tileSize, tiles6.size() / rows * tileSize+tileSize*b, tileSize, tileSize, cultureTransition, value, "bg"));
                        } 
                        if (value == 6 ) {
                            tiles6.add(new Tile(p * tileSize, tiles6.size() / rows * tileSize+tileSize*b, tileSize*2, tileSize*2, fontaine1, value, "bg"));
                        } 
                        if (value == 7 ) {
                            tiles6.add(new Tile(p * tileSize, tiles6.size() / rows * tileSize+tileSize*b, tileSize*4, tileSize*4, home, value, "collision"));
                        } 
                        if (value == 9 ) {
                            tiles6.add(new Tile(p * tileSize, tiles6.size() / rows * tileSize+tileSize*b, tileSize, tileSize, culture, value, "bg"));
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
        try (BufferedReader br = new BufferedReader(new FileReader("Zelda\\tile_data7.csv"))) {
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
                            tiles7.add(new Tile(p * tileSize, tiles7.size() / rows * tileSize+tileSize*b, tileSize, tileSize, grass, value, "bg"));
                        }
                        if (value == 2 ) {
                            tiles7.add(new Tile(p * tileSize, tiles7.size() / rows * tileSize+tileSize*b, tileSize, tileSize, grass2, value, "bg"));
                        }
                        if (value == 3 ) {
                            tiles7.add(new Tile(p * tileSize, tiles7.size() / rows * tileSize+tileSize*b, tileSize, tileSize, buisson, value, "collision"));
                        } 
                        if (value == 4 ) {
                            tiles7.add(new Tile(p * tileSize, tiles7.size() / rows * tileSize+tileSize*b, tileSize, tileSize, flaque, value, "collision"));
                        }
                        if (value == 5 ) {
                            tiles7.add(new Tile(p * tileSize, tiles7.size() / rows * tileSize+tileSize*b, tileSize, tileSize, cultureTransition, value, "bg"));
                        } 
                        if (value == 6 ) {
                            tiles7.add(new Tile(p * tileSize, tiles7.size() / rows * tileSize+tileSize*b, tileSize*2, tileSize*2, fontaine1, value, "bg"));
                        } 
                        if (value == 7 ) {
                            tiles7.add(new Tile(p * tileSize, tiles7.size() / rows * tileSize+tileSize*b, tileSize*4, tileSize*4, home, value, "collision"));
                        } 
                        if (value == 9 ) {
                            tiles7.add(new Tile(p * tileSize, tiles7.size() / rows * tileSize+tileSize*b, tileSize, tileSize, culture, value, "bg"));
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
        try (BufferedReader br = new BufferedReader(new FileReader("Zelda\\tile_data8.csv"))) {
            String line;
            
            p = 0;
            b = 0;
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
                            tiles8.add(new Tile(p * tileSize, tiles8.size() / rows * tileSize+tileSize*b, tileSize, tileSize, grass, value, "bg"));
                        }
                        if (value == 2 ) {
                            tiles8.add(new Tile(p * tileSize, tiles8.size() / rows * tileSize+tileSize*b, tileSize, tileSize, grass2, value, "bg"));
                        }
                        if (value == 3 ) {
                            tiles8.add(new Tile(p * tileSize, tiles8.size() / rows * tileSize+tileSize*b, tileSize, tileSize, buisson, value, "collision"));
                        } 
                        if (value == 4 ) {
                            tiles8.add(new Tile(p * tileSize, tiles8.size() / rows * tileSize+tileSize*b, tileSize, tileSize, flaque, value, "collision"));
                        }
                        if (value == 5 ) {
                            tiles8.add(new Tile(p * tileSize, tiles8.size() / rows * tileSize+tileSize*b, tileSize, tileSize, cultureTransition, value, "bg"));
                        } 
                        if (value == 6 ) {
                            tiles8.add(new Tile(p * tileSize, tiles8.size() / rows * tileSize+tileSize*b, tileSize*2, tileSize*2, fontaine1, value, "bg"));
                        } 
                        if (value == 7 ) {
                            tiles8.add(new Tile(p * tileSize, tiles8.size() / rows * tileSize+tileSize*b, tileSize*4, tileSize*4, home, value, "collision"));
                        } 
                        if (value == 9 ) {
                            tiles8.add(new Tile(p * tileSize, tiles8.size() / rows * tileSize+tileSize*b, tileSize, tileSize, culture, value, "bg"));
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
        try (BufferedReader br = new BufferedReader(new FileReader("Zelda\\tile_data9.csv"))) {
            String line;
            
            p = 0;
            b = 0;
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
                            tiles9.add(new Tile(p * tileSize, tiles9.size() / rows * tileSize+tileSize*b, tileSize, tileSize, grass, value, "bg"));
                        }
                        if (value == 2 ) {
                            tiles9.add(new Tile(p * tileSize, tiles9.size() / rows * tileSize+tileSize*b, tileSize, tileSize, grass2, value, "bg"));
                        }
                        if (value == 3 ) {
                            tiles9.add(new Tile(p * tileSize, tiles9.size() / rows * tileSize+tileSize*b, tileSize, tileSize, buisson, value, "collision"));
                        } 
                        if (value == 4 ) {
                            tiles9.add(new Tile(p * tileSize, tiles9.size() / rows * tileSize+tileSize*b, tileSize, tileSize, flaque, value, "collision"));
                        }
                        if (value == 5 ) {
                            tiles9.add(new Tile(p * tileSize, tiles9.size() / rows * tileSize+tileSize*b, tileSize, tileSize, cultureTransition, value, "bg"));
                        } 
                        if (value == 6 ) {
                            tiles9.add(new Tile(p * tileSize, tiles9.size() / rows * tileSize+tileSize*b, tileSize*2, tileSize*2, fontaine1, value, "bg"));
                        } 
                        if (value == 7 ) {
                            tiles9.add(new Tile(p * tileSize, tiles9.size() / rows * tileSize+tileSize*b, tileSize*4, tileSize*4, home, value, "collision"));
                        } 
                        if (value == 9 ) {
                            tiles9.add(new Tile(p * tileSize, tiles9.size() / rows * tileSize+tileSize*b, tileSize, tileSize, culture, value, "bg"));
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

    List<Tile> currentTiles = getCurrentTileList();

    drawTiles(g, currentTiles, true);

    g.drawImage(playerTile.img, playerTile.x, playerTile.y, playerTile.width, playerTile.height, null);

    drawTiles(g, currentTiles, false);

    drawHearts(g);
}

private List<Tile> getCurrentTileList() {
    if (center) return tiles;
    if (nextDown && !nextRight && !nextLeft) return tiles3;
    if (nextUp && !nextRight && !nextLeft) return tiles2;
    if (nextRight && !nextUp && !nextDown) return tiles4;
    if (nextLeft && !nextUp && !nextDown) return tiles5;
    if (nextUp && nextRight && !nextLeft) return tiles6;
    if (nextUp && !nextRight && nextLeft) return tiles7;
    if (nextDown && nextRight && !nextLeft) return tiles8;
    if (nextDown && !nextRight && nextLeft) return tiles9;
    
    return tiles; 
}

private void drawTiles(Graphics g, List<Tile> tileList, boolean isBackground) {
    for (Tile tile : tileList) {
        if ((isBackground && tile.idImg < 6) || (!isBackground && tile.idImg > 6)) {
            if (!"player".equals(tile.type)) { 
                g.drawImage(tile.img, tile.x, tile.y, tile.width, tile.height, null);
            }
        }
    }
}

private void drawHearts(Graphics g) {
    Tile[] hearts = {heart, heart2, heart3, heart4, heart5};
    for (int i = 0; i < hearts.length; i++) {
        g.drawImage(hearts[i].img, hearts[i].x + 64 * i, hearts[i].y, hearts[i].width, hearts[i].height, null);
    }
}



    private boolean checkCollision() {
        Rectangle futureBounds = new Rectangle(playerTile.x+5, playerTile.y+5, playerTile.width, playerTile.height);
        for (Tile tile : tiles) {
            if ("collision".equals(tile.type) && futureBounds.intersects(tile.getBounds()) && center) {
                return true;
            }
        }
        for (Tile tile : tiles2) {
            if ("collision".equals(tile.type) && futureBounds.intersects(tile.getBounds()) && nextUp && !nextRight && !nextLeft) {
                return true;
            }
        }
        for (Tile tile : tiles3) {
            if ("collision".equals(tile.type) && futureBounds.intersects(tile.getBounds())  && nextDown && !nextRight && !nextLeft) {
                return true;
            }
        }
        
        for (Tile tile : tiles4) {
            if ("collision".equals(tile.type) && futureBounds.intersects(tile.getBounds())  && nextRight && !nextUp && !nextDown) {
                return true;
            }
        }
        
        for (Tile tile : tiles5) {
            if ("collision".equals(tile.type) && futureBounds.intersects(tile.getBounds())  && nextLeft && !nextUp && !nextDown) {
                return true;
            }
        }
        
        for (Tile tile : tiles6) {
            if ("collision".equals(tile.type) && futureBounds.intersects(tile.getBounds()) && nextUp && nextRight && !nextLeft) {
                return true;
            }
        }
        
        for (Tile tile : tiles7) {
            if ("collision".equals(tile.type) && futureBounds.intersects(tile.getBounds()) && nextUp && !nextRight && nextLeft) {
                return true;
            }
        }
        
        for (Tile tile : tiles8) {
            if ("collision".equals(tile.type) && futureBounds.intersects(tile.getBounds()) && nextDown && nextRight && !nextLeft) {
                return true;
            }
        }
        
        for (Tile tile : tiles9) {
            if ("collision".equals(tile.type) && futureBounds.intersects(tile.getBounds()) && nextDown && !nextRight && nextLeft) {
                return true;
            }
        }
        return false;
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

        if (key == KeyEvent.VK_M) {
            playerTile.img = attack1;
        }

        if (key == KeyEvent.VK_A) {
            player.health -= 1;
        }

        if (key == KeyEvent.VK_LEFT) {
            playerVelocityX = -2;

            playerTile.img = left.get(indexL);

            indexL += 1;
            if (indexL > 3) {
                indexL = 0;
            }
            repaint();
        }
        if (key == KeyEvent.VK_RIGHT) {
            playerVelocityX = 2;

            playerTile.img = idle1_Right;playerTile.img = right.get(indexR);

            indexR += 1;
            if (indexR > 3) {
                indexR = 0;
            }
            repaint();
        }
        if (key == KeyEvent.VK_DOWN) {
            playerVelocityY = 2;

            playerTile.img = front.get(indexF);

            indexF += 1;
            if (indexF > 3) {
                indexF = 0;
            }
            repaint();
        }
        if (key == KeyEvent.VK_UP) {
            playerVelocityY = -2;

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

        if (key == KeyEvent.VK_M) {
            playerTile.img = front.get(0);
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
        playerTile.x += playerVelocityX*2;
        playerTile.y += playerVelocityY*2;

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

        System.out.println("Center: "+center+ " Up: " + nextUp + " Down: " + nextDown + " Right: " + nextRight + " Left: " + nextLeft);

        repaint();
    }
    
}
