package Zelda;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class GameEngine extends JPanel implements ActionListener, KeyListener{

    int tileSize = 32; 
    int rows = 40;
    int cols = 19;
    int boardHeight = cols*tileSize;
    int boardWidth = rows*tileSize;

    Image chosen = new ImageIcon(getClass().getResource("./assets/Map/grass.png")).getImage();
    int idImg = 1;

    class Tile {
        int x;
        int y;
        int x2;
        int y2;
        Image img;
        int idImg;
        
        Tile(int x, int y, int x2, int y2, Image img, int idImg) {
            this.x = x;
            this.y = y;
            this.x2 = x2;
            this.y2 = y2;
            this.img = img;
            this.idImg = idImg;
        }
    }

    ArrayList<Tile> tiles = new ArrayList<Tile>();
    ArrayList<Integer> datas = new ArrayList<Integer>();
    int level = 1;


    public GameEngine() {

        setBackground(Color.BLACK);
        setBounds(0, 0, boardWidth, boardHeight);
        addKeyListener(this);
        setFocusable(true);

        for (int i = 0; i< rows; i++) {
            for (int j = 0; j< cols; j++) {
                tiles.add(new Tile(i*tileSize, j*tileSize, i*tileSize*2, j*tileSize*2, chosen, idImg));
            }
        }

        addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            @Override
            public void mouseDragged(java.awt.event.MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                for (int i = 0; i < tiles.size(); i++) {
                    Tile tile = tiles.get(i);
                    if (x > tile.x && x < tile.x + tileSize && y > tile.y && y < tile.y + tileSize) {
                        Graphics g = getGraphics();
                        int index = tiles.indexOf(tile);
                        tiles.set(index, new Tile(tile.x, tile.y, tileSize, tileSize, chosen, idImg));
                        g.drawImage(chosen, tile.x, tile.y, tileSize, tileSize, null);
                        g.dispose();
                    }
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.WHITE);
        for (Tile tile : tiles) {
            g.drawImage(chosen, tile.x, tile.y, tileSize, tileSize, null);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        
        if (e.getKeyCode() == KeyEvent.VK_6) {
            idImg = 7;
            chosen = new ImageIcon(getClass().getResource("./assets/Map/home.png")).getImage();
        }
        if (e.getKeyCode() == KeyEvent.VK_G) {
            chosen = new ImageIcon(getClass().getResource("./assets/Map/grass.png")).getImage();
            idImg = 1;
        }
        if (e.getKeyCode() == KeyEvent.VK_7) {
            chosen = new ImageIcon(getClass().getResource("./assets/Map/culture.png")).getImage();
            idImg = 9;
        }
        if (e.getKeyCode() == KeyEvent.VK_8) {
            chosen = new ImageIcon(getClass().getResource("./assets/png/Movement/Front/idle1_Front.png")).getImage();
            idImg = 10;
        }
        if (e.getKeyCode() == KeyEvent.VK_9) {
            chosen = new ImageIcon(getClass().getResource("./assets/Map/rock.png")).getImage();
            idImg = 11;
        }
        if (e.getKeyCode() == KeyEvent.VK_Z) {
            chosen = new ImageIcon(getClass().getResource("./assets/Map/tp.png")).getImage();
            idImg = 12;
        }
        if (e.getKeyCode() == KeyEvent.VK_E) {
            chosen = new ImageIcon(getClass().getResource("./assets/Monsters/monster1.png")).getImage();
            idImg = 13;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_A) {
            level++;
        }
        if (e.getKeyCode() == KeyEvent.VK_B) {
            if (level > 0) {
                level--;
            } else {
                level = 0;
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_1) {
            chosen = new ImageIcon(getClass().getResource("./assets/Map/grass2.png")).getImage();
            idImg = 2;
        }
        if (e.getKeyCode() == KeyEvent.VK_2) {
            chosen = new ImageIcon(getClass().getResource("./assets/Map/buisson.png")).getImage();
            idImg = 3;
        }
        if (e.getKeyCode() == KeyEvent.VK_3) {
            chosen = new ImageIcon(getClass().getResource("./assets/Map/flaque.png")).getImage();
            idImg = 4;
        }
        if (e.getKeyCode() == KeyEvent.VK_4) {
            chosen = new ImageIcon(getClass().getResource("./assets/Map/cultureTransition.png")).getImage();
            idImg = 5;
        }
        if (e.getKeyCode() == KeyEvent.VK_5) {
            chosen = new ImageIcon(getClass().getResource("./assets/Map/fontaine1.png")).getImage();
            idImg = 6;
        }
        if (e.getKeyCode() == KeyEvent.VK_S) {
            for (Tile tile : tiles) {
                System.out.println(tile.idImg);
                datas.add(tile.idImg);
            }
            try (java.io.FileWriter writer = new java.io.FileWriter("tile_data" + level + ".csv")) {
                for (Integer data : datas) {
                    writer.write(data + ",");
                }
                writer.flush();
            } catch (java.io.IOException ex) {
                ex.printStackTrace();

            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }

}
