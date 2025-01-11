
package SpaceShip;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;


public class SpaceInvaders extends JPanel implements ActionListener, KeyListener {
    //board
    int tileSize = 32;
    int rows = 16;
    int columns = 16;

    int boardWidth = tileSize * columns; // 32 * 16
    int boardHeight = tileSize * rows; // 32 * 16

    Image shipImg;
    Image alienImg;
    Image alienCyanImg;
    Image alienMagentaImg;
    Image alienYellowImg;
    Image backgroundImg ;
    ArrayList<Image> alienImgArray;

    class Block {
        int x;
        int y;
        int width;
        int height;
        Image img;
        boolean alive = true; //used for aliens
        boolean used = false; //used for bullets
        
        Block(int x, int y, int width, int height, Image img) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.img = img;
        }
    }

    //ship
    int shipWidth = tileSize*2;
    int shipHeight = tileSize;
    int shipX = tileSize * columns/2 - tileSize;
    int shipY = tileSize * rows - tileSize*2-10;
    int shipVelocityX = 0; //ship moving speed
    int shipVelocityY = 0;
    Block ship;

    Block bg;

    //aliens
    ArrayList<Block> alienArray;
    int alienWidth = tileSize*2;
    int alienHeight = tileSize*2;
    int alienX = tileSize;
    int alienY = tileSize;

    int alienRows = 2;
    int alienColumns = 3;
    int alienCount = 0; 
    int alienVelocityX = 1; 

    //bullets
    ArrayList<Block> bulletArray;
    int bulletWidth = tileSize/8;
    int bulletHeight = tileSize/2;
    int bulletVelocityY = -10;

    // game 
    Timer gameLoop;
    boolean gameOver = false;
    boolean win = false;
    int score = 0;
    String level = "1";
    int round = 1;

    SpaceInvaders() {
        // for the panel
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setFocusable(true);
        addKeyListener(this);

        //load images
        backgroundImg = new ImageIcon(getClass().getResource("./assets/space.jpg")).getImage();
        shipImg = new ImageIcon(getClass().getResource("./assets/ship.png")).getImage();
        alienImg = new ImageIcon(getClass().getResource("./assets/sprinky.png")).getImage();
        alienCyanImg = new ImageIcon(getClass().getResource("./assets/oceany.png")).getImage();
        alienMagentaImg = new ImageIcon(getClass().getResource("./assets/goldeny.png")).getImage();
        alienYellowImg = new ImageIcon(getClass().getResource("./assets/crupty.png")).getImage();

        alienImgArray = new ArrayList<Image>();
        alienImgArray.add(alienImg);
        alienImgArray.add(alienCyanImg);
        alienImgArray.add(alienMagentaImg);
        alienImgArray.add(alienYellowImg);

        ship = new Block(shipX, shipY, shipWidth, shipHeight, shipImg);
        bg = new Block(0, 0, 512, 512, backgroundImg);
        alienArray = new ArrayList<Block>();
        bulletArray = new ArrayList<Block>();

        //game timer
        gameLoop = new Timer(1000/60, this); //1000/60 = 16.6
        createAliens();
        gameLoop.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {

        //ship
        g.drawImage(bg.img, bg.x, bg.y, bg.width, bg.height, null);

        g.drawImage(ship.img, ship.x, ship.y, ship.width, ship.height, null);

        //aliens
        for (int i = 0; i < alienArray.size(); i++) {
            Block alien = alienArray.get(i);
            if (alien.alive) {
                g.drawImage(alien.img, alien.x, alien.y, alien.width, alien.height, null);
            }
        }

        //bullets
        g.setColor(Color.white);
        for (int i = 0; i < bulletArray.size(); i++) {
            Block bullet = bulletArray.get(i);
            if (!bullet.used) {
                g.drawRect(bullet.x, bullet.y, bullet.width, bullet.height);
                // g.fillRect(bullet.x, bullet.y, bullet.width, bullet.height);
            }
        }
        g.setColor(Color.white);

        //score
        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.PLAIN, 56));
        if (gameOver) {
            g.setColor(Color.RED);
            g.drawString("Game Over: " + String.valueOf((int) score), 0, boardHeight/2);
            g.setFont(new Font("Arial", Font.PLAIN, 38));
            g.drawString("Press CONTROL to restart ", 0, boardHeight/2+46);
        }
        else if (win) {
            g.setColor(Color.YELLOW);
            g.drawString("You WON : " + String.valueOf((int) score), 0, boardHeight/2);
            g.setFont(new Font("Arial", Font.PLAIN, 38));
            g.drawString("Press CONTROL to restart ", 0, boardHeight/2+46);
        }
        else {
            g.setFont(new Font("Arial", Font.PLAIN, 32));
            g.setColor(Color.WHITE);
            g.drawRect(5, 5, 100, 40);
            g.drawString(String.valueOf((int) score), 10, 35);
            g.setColor(Color.YELLOW);
            g.setFont(new Font("Arial", Font.PLAIN, 32));
            g.setColor(Color.WHITE);
            g.drawRect(boardWidth - 360, 5, 350, 40);
            g.drawString("Level : " + level + "| Round : " + round, boardWidth-350, 35);
        }
    }

    public void move() {

        
        // player's move
        if (ship.y + shipVelocityY >= 438 || ship.x + shipVelocityX <= 4 || ship.x + shipVelocityX >= 434) {
            ship.x += 0;
            ship.y += 0;
        } else {
            ship.x += shipVelocityX;
            ship.y += shipVelocityY;
        }
        
        //alien
        for (int i = 0; i < alienArray.size(); i++) {
            Block alien = alienArray.get(i);
            if (alien.alive) {
                alien.x += alienVelocityX;

                //if alien touches the borders
                if (alien.x + alien.width >= boardWidth || alien.x <= 0) {
                    alienVelocityX *= -1;
                    alien.x += alienVelocityX*2;
                }
                for (int j = 0; j < alienArray.size(); j++) {
                    if (alienArray.get(j).y+tileSize*2 >= ship.y) {
                        gameOver = true;
                    }
                }
            }
        }

        //bullets
        for (int i = 0; i < bulletArray.size(); i++) {
            Block bullet = bulletArray.get(i);
            bullet.y += bulletVelocityY;

            //bullet collision with aliens
            for (int j = 0; j < alienArray.size(); j++) {
                Block alien = alienArray.get(j);
                if (!bullet.used && alien.alive && detectCollision(bullet, alien)) {
                    bullet.used = true;
                    alien.alive = false;
                    alienCount--;
                    score += 100;
                }
            }
        }

        //clear bullets
        while (bulletArray.size() > 0 && (bulletArray.get(0).used || bulletArray.get(0).y < 0)) {
            bulletArray.remove(0); //removes the first element of the array
        }

        //next level
        if (alienCount == 0) {
            //increase the number of aliens in columns and rows by 1
            score += alienColumns * alienRows * 100; //bonus points :)
            alienColumns = Math.min(alienColumns + 1, columns/2 -2); //cap at 16/2 -2 = 6
            alienRows = Math.min(alienRows + 1, rows-6);  //cap at 16-6 = 10
            alienArray.clear();
            bulletArray.clear();
            createAliens();
            round += 1;
        }

        if (round == 4) {
            win = true;
        }
    }

    public void createAliens() {
        Random random = new Random();
        for (int c = 0; c < alienColumns; c++) {
            for (int r = 0; r < alienRows; r++) {
                int randomImgIndex = random.nextInt(alienImgArray.size());
                Block alien = new Block(
                    alienX + c*alienWidth, 
                    alienY + r*alienHeight, 
                    alienWidth, 
                    alienHeight,
                    alienImgArray.get(randomImgIndex)
                );
                alienArray.add(alien);
            }
        }
        alienCount = alienArray.size();
    }
    

    public boolean detectCollision(Block a, Block b) {
        return  a.x < b.x + b.width &&  //a's top left corner doesn't reach b's top right corner
                a.x + a.width > b.x &&  //a's top right corner passes b's top left corner
                a.y < b.y + b.height && //a's top left corner doesn't reach b's bottom left corner
                a.y + a.height > b.y;   //a's bottom left corner passes b's top left corner
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if (gameOver) {
            gameLoop.stop();
            ship.x = tileSize * columns/2 - tileSize;
            ship.y = tileSize * rows - tileSize*2-10;
        }
        if (win) {
            gameLoop.stop();
            ship.x = tileSize * columns/2 - tileSize;
            ship.y = tileSize * rows - tileSize*2-10;
        }
    }
    

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
            if (gameOver) { //any key to restart
                ship.x = shipX;
                bulletArray.clear();
                alienArray.clear();
                gameOver = false;
                round = 1;
                score = 0;
                alienColumns = 3;
                alienRows = 2;
                alienVelocityX = 1;
                createAliens();
                gameLoop.start();
            }
            else if (win) { //any key to restart
                ship.x = shipX;
                bulletArray.clear();
                alienArray.clear();
                win = false;
                round = 1;
                score = 0;
                alienColumns = 3;
                alienRows = 2;
                alienVelocityX = 1;
                createAliens();
                gameLoop.start();

            }
        }
        
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            shipVelocityX = -10; //move left one tile
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            shipVelocityX = 10;//move right one tile
        }
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            shipVelocityY = -10; //move left one tile
        }
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            shipVelocityY = 10; //move right one tile
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {
        
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            //shoot bullet
            Block bullet = new Block(ship.x + shipWidth*15/32, ship.y, bulletWidth, bulletHeight, null);
            bulletArray.add(bullet);
            
        }
        
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            shipVelocityX = 0; //move left one tile
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            shipVelocityX = 0; //move right one tile
        }
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            shipVelocityY = 0; //move left one tile
        }
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            shipVelocityY = 0; //move right one tile
        }
    }
}
