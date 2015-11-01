import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;

public class Screen extends JPanel implements Runnable{

    Thread thread = new Thread(this);

    Frame frame;
    Levels level;
    LevelFile levelFile;

    User user; //account

    private int fps = 0;
    public int scene = 0;

    public int hand = 0;
    public int handXPos = 0;
    public int handYPos = 0;

    public  boolean running = false;

    public double towerWidth = 1;
    public double towerHeight = 1;

    public  int[][] map = new int[22][14];
    public  Tower[][] towerMap = new Tower[22][14];
    public  Image[] terrain = new Image[100];

    //TODO: add terrain.png
    private String packageName = "res";


    public Screen(Frame frame){
        this.frame = frame;

        this.frame.addKeyListener(new KeyHandler(this));
        this.frame.addMouseListener(new MouseHandler(this));

        towerWidth = this.frame.getWidth() / 28.8;
        towerHeight = this.frame.getHeight()  / 18;

        thread.start();
    }

    public void  paintComponent(Graphics g){
        g.clearRect(0, 0, this.frame.getWidth(), this.frame.getHeight());

        if (scene == 0){
            g.setColor(Color.BLUE);
            g.fillRect(0, 0, this.frame.getWidth(), this.frame.getHeight());
        } else if(scene == 1){
            //background
            g.setColor(Color.GREEN);
            g.fillRect(0, 0, this.frame.getWidth(), this.frame.getHeight());

            //grid
            g.setColor(Color.GRAY);
            for (int x = 0; x < 22; x++) {
                for (int y = 0; y <  14; y++) {
                    g.drawImage(terrain[map[x][y]], (int) towerWidth + (x *(int)towerWidth),(int) towerHeight + (x *(int)towerHeight), (int) towerWidth, (int) towerHeight, null);
                    g.drawRect((int) (50 + (x * towerWidth)), (int) (50 + ( y * towerHeight)), (int) towerWidth, (int) towerHeight);
                }
            }
        //health and money
            g.drawRect(12, (15*50) + 12, 125, (900 - (15*50) - 12 - 12)/3);
            g.drawString("Health: " + user.player.health, 12 + 25, (15*50) + 12 + 25);

            g.drawRect(12, (15*50) + 12 + ((900 - (15*50) - 12 - 12)/3), 125, (900 - (15*50) - 12 - 12)/3);
            g.drawString("Money: " + user.player.money, 12 + 25, (15*50) + 12 + 25 + 50);

            g.drawRect(12, (15*50) + 12 + ((900 - (15*50) - 12 - 12)/3) * 2, 125, (900 - (15*50) - 12 - 12)/3);

        //tower scroll list buttons
            g.drawRect(12 + 12 + 125, (15*50) + 12, 40, 900 - (15*50) - 12 - 12 );
            //other button on the other side
            
        //tower list
        for (int x = 0; x < 20 ; x++) {
            for (int y = 0; y < 2; y++) {
                if (Tower.towerList[x * 2 + y] != null){
                    g.drawImage(Tower.towerList[x * 2 + y].texture,(int) (12 + 12 + 125 + 40 + 12 + (x * towerWidth)), (int) ((15*50) + 12 + (y * towerHeight)), (int) towerWidth, (int) towerHeight, null);

                    if (Tower.towerList[x * 2 + y].cost > this.user.player.money){
                        g.setColor(new Color(255, 0, 0, 100));
                        g.fillRect((int) (12 + 12 + 125 + 40 + 12 + (x * towerWidth)), (int) ((15*50) + 12 + (y * towerHeight)), (int) towerWidth, (int) towerHeight);
                    }
                }

                g.setColor(Color.GRAY);
                g.drawRect((int) (12 + 12 + 125 + 40 + 12 + (x * towerWidth)), (int) ((15*50) + 12 + (y * towerHeight)),(int) towerWidth, (int) towerHeight);
            }
        }


        //Hand
        if (hand != 0 && Tower.towerList[hand - 1] != null){
            g.drawImage(Tower.towerList[hand - 1].texture, this.handXPos - (int)this.towerWidth / 2,this.handYPos - (int)towerHeight / 2, (int) this.towerWidth, (int) this.towerHeight, null);
        }

        } else {
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, this.frame.getWidth(), this.frame.getHeight());
        }

        //FPS at the bottom!
        g.drawString(fps + "", 10, 10);
    }

    //only first time
    public void loadGame(){
        user = new User(this);
        levelFile = new LevelFile();

        ClassLoader cl = this.getClass().getClassLoader();

        for (int y = 0; y < 10 ; y++) {
            for (int x = 0; x < 10; x++) {
                terrain[x + (y * 10)] = new ImageIcon(cl.getResource(packageName + "\\terrain.png")).getImage();
                terrain[x + (y * 10)] = createImage(new FilteredImageSource(terrain[x + (y * 10)].getSource(), new CropImageFilter(x*25, y*25, 25, 25)));
            }
        }
        running = true;
    }

    public void startGame(User user, String level){
        user.createPlayer();

        this.level = levelFile.getLevel(level);
        this.level.findSpawnPoint();
        this.map = this.level.map;

        this.scene = 1; //level 1
    }

    public void run(){

        long lastFrame = System.currentTimeMillis();
        int frames = 0;

        loadGame();

        while (running){
            repaint();

            frames++;

            if (System.currentTimeMillis() - 1000 >= lastFrame){
                fps = frames;
                frames = 0;
                lastFrame = System.currentTimeMillis();
            }

            try {
                Thread.sleep(1);
            } catch (InterruptedException e){
                e.printStackTrace();
            }
        }
        System.exit(0);
    }

    public void placeTower(int x, int y){
        int xPos = (x - (int) towerWidth) / (int) towerWidth;
        int yPos = (y - (int) towerHeight) / (int) towerHeight;

        if (xPos > 22 || yPos > 14){

        } else if(towerMap[xPos][yPos] == null && map[xPos][yPos] == 0){
            user.player.money -= Tower.towerList[hand - 1].cost;

            towerMap[xPos][yPos] = Tower.towerList[hand - 1];
        }
    }


    public class MouseHeld{
        boolean mouseDown = false;

        public void mouseMoved(MouseEvent e) {
            handXPos = e.getXOnScreen();
            handYPos = e.getYOnScreen();
        }

        public  void  updateMouse(MouseEvent e){
            if (scene == 1){
                if(mouseDown && hand == 0){
                    if (e.getXOnScreen() >= ((int) (12 + 12 + (int)(frame.getWidth() / 11.52) + frame.getWidth()/40 + 12))
                            && e.getXOnScreen() <= ((int)12 + 12 + (int) (frame.getWidth() / 11.52)+ frame.getWidth() / 40 + 12 + (18* towerWidth))){
                        if (e.getYOnScreen() >= (15 * (int) towerHeight) + 12
                                && e.getYOnScreen() <= (15 * (int) towerHeight) + 12 + (int) towerHeight * 2){
                            //Tower 1
                            if (e.getXOnScreen() >= ((int) (12 + 12 + (int)(frame.getWidth() / 11.52) + frame.getWidth()/40 + 12))
                                    && e.getXOnScreen() <= ((int)12 + 12 + (int) (frame.getWidth() / 11.52)+ frame.getWidth() / 40 + 12 + towerWidth)
                                    && e.getYOnScreen() >= (15 * (int) towerHeight) + 12
                                    && e.getYOnScreen() <= (15 * (int) towerHeight) + 12 + (int) towerHeight){
                                if (user.player.money >= Tower.towerList[0].cost){
                                    System.out.println("[SHOP] You bought a tower for " + Tower.towerList[0].cost + "!");
                                    hand = 1;
                                }



                            }
                        }
                    }
                }
            }
        }
        public void mouseDown(MouseEvent e) {
             mouseDown = true;

            if (hand != 0){
                placeTower(e.getXOnScreen(), e.getYOnScreen());
                hand = 0;
            }

            updateMouse(e);
        }
    }

    public class KeyTyped{
        public  void keyESC(){
            running = false;
        }

        public void keySPACE() {
           startGame(user, "Level1");
        }
    }

}
