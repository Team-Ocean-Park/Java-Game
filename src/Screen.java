import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;
import java.io.Console;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Screen extends JPanel implements Runnable{

    Thread thread = new Thread(this);

    Frame frame;
    Levels level;
    LevelFile levelFile;
    public static EnemyAI enemyAI;
    Wave wave;

    User user; //account

    private int fps = 0;

    public int scene;

    public int hand = 0;
    public int handXPos = 0;
    public int handYPos = 0;

    public static int speed;

    public boolean running = false;

    public static double towerSize;

    public int frameWidth;
    public int frameHeight;

    public static int frameHeightBorder;

    public Tower selectedTower;

    public  int[][] map = new int[22][14];
    public  Tower[][] towerMap = new Tower[22][14];
    public  Image[] terrain = new Image[100];

    private Image buttonStartGame = new ImageIcon("res\\buttons\\startButton.png").getImage();
    private Image buttonSpeedUpGame = new ImageIcon("res\\buttons\\speedButton.png").getImage();
    private Image buttonSpeedUpGame2x = new ImageIcon("res\\buttons\\speedButtonx2.png").getImage();
    private Image openscreen = new ImageIcon("res\\openscreen.jpg").getImage();
    private Image gameover = new ImageIcon("res\\gameover.jpg").getImage();
    private Image pauseScreen = new ImageIcon("res\\pauseScreen.jpg").getImage();
    private Image exit = new ImageIcon("res\\exit.jpg").getImage();

    public EnemyMove[] enemyMap = new EnemyMove[50];
    public static Missile[] missiles = new  Missile[10];

    public Screen(Frame frame){
        this.frame = frame;

        this.frame.addKeyListener(new KeyHandler(this));
        this.frame.addMouseListener(new MouseHandler(this));
        this.frame.addMouseMotionListener(new MouseHandler(this));

        frameWidth = this.frame.getWidth();
        frameHeight = this.frame.getWidth() / 16 * 9;

        frameHeightBorder = (this.frame.getHeight() - frameHeight) / 2;

        towerSize = this.frameHeight  / 18;

        thread.start();
    }

    public void  paintComponent(Graphics g) {
        g.clearRect(0, 0, this.frame.getWidth(), this.frame.getHeight());

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, this.frame.getWidth(), this.frame.getHeight());

        if (scene == 0) {
            g.setColor(Color.BLUE);
            g.fillRect(0, 0 + frameHeightBorder, this.frameWidth, this.frameHeight);
            g.drawImage(openscreen, 0, 0, getWidth(), getHeight(), this);
        } else if (scene == 1) {
            //background
            g.setColor(Color.GREEN);
            g.fillRect(0, 0 + frameHeightBorder, this.frameWidth, this.frameHeight);

            //grid
            g.setColor(Color.GRAY);
            for (int x = 0; x < 22; x++) {
                for (int y = 0; y < 14; y++) {
                    g.drawImage(terrain[map[x][y]], (int) towerSize + (x * (int) towerSize), (int) towerSize + (y * (int) towerSize) + frameHeightBorder, (int) towerSize, (int) towerSize, null);
                    g.drawRect((int) ((int) towerSize + (x * towerSize)), (int) ((int) towerSize + (y * towerSize)) + frameHeightBorder, (int) towerSize, (int) towerSize);
                }
            }
            //Upgrade Menu
            g.setColor(Color.GRAY);
            g.drawRect((int) towerSize * 24, (int) towerSize + frameHeightBorder, (int) towerSize * 7, (int) towerSize * 14);
            g.drawRect((int) towerSize * 24 + (int) towerSize / 2 - 1, (int) towerSize + frameHeightBorder + (int) towerSize / 2 - 1, (int) towerSize * 3 + 2, (int) towerSize * 3 + 2);
            if (selectedTower != null) {
                g.drawImage(selectedTower.texture, (int) towerSize * 24 + (int) towerSize / 2, (int) towerSize + frameHeightBorder + (int) towerSize / 2, (int) towerSize * 3, (int) towerSize * 3, null);
            }

            //Tower Description
            g.drawRect((int) towerSize * 28, (int) towerSize + (((int) towerSize * 5 / 2 + 2) / 3) * 0 + frameHeightBorder + (int) towerSize * 2 / 4 - 1, frame.getWidth() - (int) towerSize * 29 - (int) towerSize / 2, ((int) towerSize * 5 / 2 + 2) / 3);
            g.drawRect((int) towerSize * 28, (int) towerSize + (((int) towerSize * 5 / 2 + 2) / 3) * 1 + frameHeightBorder + (int) towerSize * 3 / 4 - 1, frame.getWidth() - (int) towerSize * 29 - (int) towerSize / 2, ((int) towerSize * 5 / 2 + 2) / 3);
            g.drawRect((int) towerSize * 28, (int) towerSize + (((int) towerSize * 5 / 2 + 2) / 3) * 2 + frameHeightBorder + (int) towerSize * 4 / 4 - 1, frame.getWidth() - (int) towerSize * 29 - (int) towerSize / 2, ((int) towerSize * 5 / 2 + 2) / 3);

            //Tower Strategy
            g.drawRect((int) towerSize * 24 + (int) towerSize / 2 - 1 + 0 * (((int) towerSize * 9 / 2) / 4 + (int) towerSize / 2), (int) towerSize * 5 + frameHeightBorder, ((int) towerSize * 9 / 2) / 4, ((int) towerSize * 5 / 2 + 2) / 3);
            g.drawRect((int) towerSize * 24 + (int) towerSize / 2 - 1 + 1 * (((int) towerSize * 9 / 2) / 4 + (int) towerSize / 2), (int) towerSize * 5 + frameHeightBorder, ((int) towerSize * 9 / 2) / 4, ((int) towerSize * 5 / 2 + 2) / 3);
            g.drawRect((int) towerSize * 24 + (int) towerSize / 2 - 1 + 2 * (((int) towerSize * 9 / 2) / 4 + (int) towerSize / 2), (int) towerSize * 5 + frameHeightBorder, ((int) towerSize * 9 / 2) / 4, ((int) towerSize * 5 / 2 + 2) / 3);
            g.drawRect((int) towerSize * 24 + (int) towerSize / 2 - 1 + 3 * (((int) towerSize * 9 / 2) / 4 + (int) towerSize / 2), (int) towerSize * 5 + frameHeightBorder, ((int) towerSize * 9 / 2) / 4, ((int) towerSize * 5 / 2 + 2) / 3);


            //Options menu
            //g.drawRect((int)towerSize * 24, (int)towerSize*31/2, (int)towerSize * 7, (int)towerSize*2);

            //three buttons
            g.drawRect((int) towerSize * 24, (int) towerSize * 31 / 2 + (int) towerSize * 2 / 3 * 0, (int) towerSize * 13 / 4, (int) towerSize * 2 / 3);
            g.drawRect((int) towerSize * 24, (int) towerSize * 31 / 2 + (int) towerSize * 2 / 3 * 1, (int) towerSize * 13 / 4, (int) towerSize * 2 / 3);
            g.drawRect((int) towerSize * 24, (int) towerSize * 31 / 2 + (int) towerSize * 2 / 3 * 2, (int) towerSize * 13 / 4, (int) towerSize * 2 / 3);

            //start round/ speed up round
            boolean flag = false;
            for (int i = 0; i < enemyMap.length; i++) {
                if (enemyMap[i] != null) {
                    flag = true;
                }
            }
            if (!flag) {
                g.drawImage(buttonStartGame, (int) towerSize * 111 / 4, (int) towerSize * 31 / 2, (int) towerSize * 13 / 4, (int) towerSize * 2, null);
            } else {
                if (speed == 1) {
                    g.drawImage(buttonSpeedUpGame, (int) towerSize * 111 / 4, (int) towerSize * 31 / 2, (int) towerSize * 13 / 4, (int) towerSize * 2, null);
                } else {
                    g.drawImage(buttonSpeedUpGame2x, (int) towerSize * 111 / 4, (int) towerSize * 31 / 2, (int) towerSize * 13 / 4, (int) towerSize * 2, null);
                }
            }

            //Enemies
            for (int i = 0; i < enemyMap.length; i++) {
                if (enemyMap[i] != null) {
                    g.drawImage(enemyMap[i].enemy.texture, (int) enemyMap[i].xPos + (int) towerSize, (int) enemyMap[i].yPos + (int) towerSize + frameHeightBorder, (int) towerSize, (int) towerSize, null);
                }
            }

            //health and money + levelNumber
            g.drawRect((int) (12 * towerSize / 25), (15 * (int) towerSize) + (int) (12 * towerSize / 25) + frameHeightBorder, (int) (frameWidth / 11.52), (this.frameHeight - (15 * (int) towerSize) - (int) (12 * towerSize / 25) - (int) (12 * towerSize / 25)) / 3);
            g.drawString("Health: " + (Math.round(user.player.health)), (int) (12 * towerSize / 25) + 25, (15 * (int) towerSize) + (int) (12 * towerSize / 25) + 25 + frameHeightBorder);

            g.drawRect((int) (12 * towerSize / 25), (15 * (int) towerSize) + (int) (12 * towerSize / 25) + ((this.frameHeight - (15 * (int) towerSize) - (int) (12 * towerSize / 25) - (int) (12 * towerSize / 25)) / 3) + frameHeightBorder, (int) (frameWidth / 11.52), (this.frameHeight - (15 * (int) towerSize) - (int) (12 * towerSize / 25) - (int) (12 * towerSize / 25)) / 3);
            g.drawString("Money: " + (Math.round(user.player.money)), (int) (12 * towerSize / 25) + 25, (15 * (int) towerSize) + (int) (12 * towerSize / 25) + 25 / 2 + (int) towerSize + frameHeightBorder);

            g.drawRect((int) (12 * towerSize / 25), (15 * (int) towerSize) + (int) (12 * towerSize / 25) + (((this.frameHeight - (15 * (int) towerSize) - (int) (12 * towerSize / 25) - (int) (12 * towerSize / 25)) / 3) * 2) + frameHeightBorder, (int) (frameWidth / 11.52), (this.frameHeight - (15 * (int) towerSize) - (int) (12 * towerSize / 25) - (int) (12 * towerSize / 25)) / 3);

            //tower scroll list buttons
            g.drawRect((int) (12 * towerSize / 25) + (int) (12 * towerSize / 25) + (int) (frameWidth / 11.52), (15 * (int) towerSize) + (int) (12 * towerSize / 25) + frameHeightBorder, this.frameWidth / 40, this.frameHeight - (15 * (int) towerSize) - (int) (12 * towerSize / 25) - (int) (12 * towerSize / 25));
            //other button on the other side

            //tower list
            for (int x = 0; x < 18; x++) {
                for (int y = 0; y < 2; y++) {
                    if (Tower.towerList[x * 2 + y] != null) {
                        g.drawImage(Tower.towerList[x * 2 + y].texture, (int) ((int) (12 * towerSize / 25) + (int) (12 * towerSize / 25) + (frameWidth / 11.52) + this.frameWidth / 40 + (int) (12 * towerSize / 25) + (x * towerSize)), (int) ((15 * towerSize) + (int) (12 * towerSize / 25) + (y * towerSize)) + frameHeightBorder, (int) towerSize, (int) towerSize, null);

                        if (Tower.towerList[x * 2 + y].cost > this.user.player.money) {
                            g.setColor(new Color(255, 0, 0, 100));
                            g.fillRect((int) ((int) (12 * towerSize / 25) + (int) (12 * towerSize / 25) + (frameWidth / 11.52) + this.frameWidth / 40 + (int) (12 * towerSize / 25) + (x * towerSize)), (int) ((15 * towerSize) + (int) (12 * towerSize / 25) + (y * towerSize)) + frameHeightBorder, (int) towerSize, (int) towerSize);
                        }
                    }

                    g.setColor(Color.GRAY);
                    g.drawRect((int) ((int) (12 * towerSize / 25) + (int) (12 * towerSize / 25) + (int) (frameWidth / 11.52) + this.frameWidth / 40 + (int) (12 * towerSize / 25) + (x * towerSize)), (int) ((15 * towerSize) + (int) (12 * towerSize / 25) + (y * towerSize)) + frameHeightBorder, (int) towerSize, (int) towerSize);
                }
            }


            //Towers on grid
            for (int x = 0; x < 22; x++) {
                for (int y = 0; y < 14; y++) {
                    if (towerMap[x][y] != null) {
                        g.drawImage(Tower.towerList[towerMap[x][y].id].texture, (int) towerSize + (x * (int) towerSize), (int) towerSize + (y * (int) towerSize) + frameHeightBorder, (int) towerSize, (int) towerSize, null);
                    }
                }
            }

            //Attacking Towers on grid
            for (int x = 0; x < 22; x++) {
                for (int y = 0; y < 14; y++) {
                    if (towerMap[x][y] != null) {
                        //Attack Enemy
                        if (towerMap[x][y].target != null) {
                            if (towerMap[x][y] instanceof TowerLightning) {
                                g.setColor(Color.RED);
                                g.drawLine((int) towerSize + (x * (int) towerSize) + (int) towerSize / 2,
                                        (int) towerSize + (y * (int) towerSize) + frameHeightBorder + (int) towerSize / 2,
                                        (int) towerSize + (int) towerMap[x][y].target.xPos + (int) towerSize / 2,
                                        (int) towerSize + (int) towerMap[x][y].target.yPos + frameHeightBorder + (int) towerSize / 2);
                            }
                        }
                        if (selectedTower == towerMap[x][y]) {
                            g.setColor(Color.GRAY);
                            g.drawOval((int) towerSize + (x * (int) towerSize) - (towerMap[x][y].range * 2 * (int) towerSize + (int) towerSize) / 2 + (int) towerSize / 2,
                                    (int) towerSize + (y * (int) towerSize) - (towerMap[x][y].range * 2 * (int) towerSize + (int) towerSize) / 2 + (int) towerSize / 2 + frameHeightBorder,
                                    towerMap[x][y].range * 2 * (int) towerSize + (int) towerSize,
                                    towerMap[x][y].range * 2 * (int) towerSize + (int) towerSize);
                            g.setColor(new Color(64, 64, 64, 64));
                            g.fillOval((int) towerSize + (x * (int) towerSize) - (towerMap[x][y].range * 2 * (int) towerSize + (int) towerSize) / 2 + (int) towerSize / 2,
                                    (int) towerSize + (y * (int) towerSize) - (towerMap[x][y].range * 2 * (int) towerSize + (int) towerSize) / 2 + (int) towerSize / 2 + frameHeightBorder,
                                    towerMap[x][y].range * 2 * (int) towerSize + (int) towerSize,
                                    towerMap[x][y].range * 2 * (int) towerSize + (int) towerSize);
                        }
                    }
                }
            }

            //Missiles
            Graphics2D g2d = (Graphics2D) g;

            for (int i = 0; i < missiles.length; i++) {
                if (missiles[i] != null) {
                    g2d.rotate(missiles[i].direction + Math.toRadians(90), (int) missiles[i].x + (int) towerSize + (int) towerSize / 2, (int) missiles[i].y + (int) towerSize + (int) towerSize / 2 + frameHeightBorder);
                    g.drawImage(missiles[i].texture, (int) missiles[i].x + (int) towerSize + (int) towerSize / 2, (int) missiles[i].y + (int) towerSize + (int) towerSize / 2 + frameHeightBorder, 14, 30, null);
                    g2d.rotate(-missiles[i].direction + Math.toRadians(-90), (int) missiles[i].x + (int) towerSize + (int) towerSize / 2, (int) missiles[i].y + (int) towerSize + (int) towerSize / 2 + frameHeightBorder);
                }
            }

            //Hand
            if (hand != 0 && Tower.towerList[hand - 1] != null) {
                g.drawImage(Tower.towerList[hand - 1].texture, this.handXPos - (int) this.towerSize / 2, this.handYPos - (int) towerSize / 2, (int) this.towerSize, (int) this.towerSize, null);
            }

        } else if (scene == 2) {
            g.drawImage(exit, ((this.frameWidth / 2) - 300), ((this.frameHeight / 2) - 287), null);

        } else if (scene == 3){
            g.drawImage(gameover, ((this.frameWidth / 2) - 300), ((this.frameHeight / 2) - 287), null);
        }else {
            g.setColor(Color.WHITE);
            g.fillRect(0, 0+ frameHeightBorder, this.frameWidth, this.frameHeight);
        }

        //FPS at the bottom!
        g.setColor(Color.BLACK);
        g.drawString(fps + "", 10, 10 + frameHeightBorder);

        frames++;
    }

    //only first time
    public void loadGame(){
        user = new User(this);
        levelFile = new LevelFile();
        wave = new Wave(this);


        for (int y = 0; y < 10 ; y++) {
            for (int x = 0; x < 10; x++) {
                terrain[x + (y * 10)] = new ImageIcon("res/terrain/terrain.png").getImage();
                terrain[x + (y * 10)] = createImage(new FilteredImageSource(terrain[x + (y * 10)].getSource(), new CropImageFilter(x*25, y*25, 25, 25)));
            }
        }

        running = true;
    }

    public void endGame(){
        if (isGameOver){
            this.scene = 3;
        } else {
            this.scene = 2;
        }

        //new Reminder(5);
    }


    //each time you start a level
    public void startGame(User user, String level){
        user.createPlayer();

        this.speed = 1;

        this.level = levelFile.getLevel(level);
        this.level.findSpawnPoint();
        this.level.findEndPoint();
        this.map = this.level.map;

        this.enemyAI = new EnemyAI(this.level);

        this.scene = 1; //level 1
        this.wave.waveNumber = 0;
    }

    int frames = 0;

    public void run(){
        long lastFrame = System.currentTimeMillis();
        int synchronised_fps = 0;

        loadGame();

        while (running){
            repaint();

            if (System.currentTimeMillis() - 1000 >= lastFrame){
                fps = frames;
                frames = 0;
                lastFrame = System.currentTimeMillis();
            }

            //if (System.currentTimeMillis() / 30 > 1000 / 30)

            double time = (double)System.currentTimeMillis() / 1000;
            int timeMilliSec = (int) Math.round((time - (int)time) * 1000);

            if (timeMilliSec > synchronised_fps * 1000 / 25 ){
                synchronised_fps++;
                update();
                if (isGameOver){
                    endGame();
                    running = false;
                }

                if (synchronised_fps == 1000/25){
                    synchronised_fps = 0;
                }
            }

            if (timeMilliSec + 1000/25 < synchronised_fps * 1000 / 25){
                synchronised_fps = 0;
            }

            try {
                Thread.sleep(1);
            } catch (InterruptedException e){
                e.printStackTrace();
            }
        }
        new Reminder(3);
       // System.exit(0);
    }

    private boolean gamePaused = false;
    private void pauseGame(boolean gamePaused) {
        if (gamePaused){
            Graphics g = null;
            g.drawImage(pauseScreen, ((this.frameWidth / 2) - 300), ((this.frameHeight / 2) - 287), null);
        }
    }

    private boolean isGameOver = false;
    public void enemyUpdate(){
        for (int i = 0; i < enemyMap.length; i++) {
            if (enemyMap[i] != null){
                if (!enemyMap[i].attack){
                    EnemyAI.moveAI.move(enemyMap[i]); //currently only to check if
                }

                int moneyEarnedOfDead = Enemy.enemyList[enemyMap[i].id].health;

                enemyMap[i] = enemyMap[i].update();

                if (enemyMap[i] != null) {
                    if (enemyMap[i].isEnemyFinished) {
                        user.player.health -= 0.2;
                    } else if (enemyMap[i].isKilled) {
                        user.player.money += moneyEarnedOfDead;
                        enemyMap[i] = null;
                    }

                    if (user.player.health == 0 || user.player.health < 0){
                        isGameOver = true;
                    }
                }

            }
        }
    }

    public void towerUpdate(){
        for (int x = 0; x < 22; x++) {
            for (int y = 0; y < 14; y++) {
                if (towerMap[x][y] != null){
                    towerAttack(x, y);
                }
            }
        }
    }

    public void towerAttack(int x, int y){
        if (this.towerMap[x][y].target == null){
            //find a target for our tower
            if (this.towerMap[x][y].attackDelay > this.towerMap[x][y].maxAttackDelay){
                EnemyMove currentEnemy = this.towerMap[x][y].calculateEnemy(enemyMap, x, y);

                if (currentEnemy != null){
                    this.towerMap[x][y].towerAttack(x, y, currentEnemy);

                    this.towerMap[x][y].target = currentEnemy;
                    this.towerMap[x][y].attackTime = 0;
                    this.towerMap[x][y].attackDelay = 0;

                    System.out.println("[Tower] enemy attacked! health: " + currentEnemy.health);
                    System.out.println(" X: " + x + " Y: " + y);
                }
            }else {
                this.towerMap[x][y].attackDelay += speed;
            }
        }else {
            if (this.towerMap[x][y].attackTime < this.towerMap[x][y].maxAttackTime){
                this.towerMap[x][y].attackTime += speed;
            }else   {
                this.towerMap[x][y].target = null;
            }
        }
    }

    public void missileUpdate() {
        for (int i = 0; i < missiles.length; i++) {
            if(missiles[i] != null) {
                missiles[i].update();

                if(missiles[i].target == null) {
                    missiles[i] = null;
                }
            }
        }
    }

    public void update(){
        enemyUpdate();
        towerUpdate();
        missileUpdate();


        if (wave.waveSpawning){
            wave.spawnEnemies();
        }
    }

    public  void spawnEnemy(int enemyID){
        for (int i = 0; i < enemyMap.length; i++) {
            if(enemyMap[i] == null){
                enemyMap[i] = new EnemyMove(Enemy.enemyList[enemyID], level.spawnPoint, level.endPoint);
                break;
            }
        }
    }


    public void placeTower(int x, int y) {
        int xPos = x / (int) towerSize;
        int yPos = y / (int) towerSize;

        if (xPos > 0 && xPos <= 22 && yPos <= 14 && yPos > 0) {
            xPos -= 1;
            yPos -= 1;

            if (towerMap[xPos][yPos] == null && map[xPos][yPos] == 0) {
                user.player.money -= Tower.towerList[hand - 1].cost;

                towerMap[xPos][yPos] = (Tower) Tower.towerList[hand - 1].clone();
                //towerMap[xPos][yPos].range = new Random().nextInt(2) +2;
                selectedTower = towerMap[xPos][yPos];
            }
        }
    }

    public void  selectTower(int x, int y){
        int xPos = x / (int) towerSize;
        int yPos = y / (int) towerSize;

        if (xPos > 0 && xPos <= 22 && yPos <= 14 && yPos > 0) {
            xPos -= 1;
            yPos -= 1;

            selectedTower = towerMap[xPos][yPos];
        }else{
            if (xPos <= 24 && xPos >= 30 && yPos >= 1 && yPos <= 14){
                selectedTower = null;
            }
        }
    }

    public void hitStartRoundButton(){
        boolean flag = false;
        for (int i = 0; i < enemyMap.length; i++) {
            if (enemyMap[i] != null){
                flag = true;
            }
        }

        if (!flag){
            wave.nextWave();
        } else {
            if(speed == 1){
                speed = 4;
            }else {
                speed = 1;
            }
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
                    if (e.getXOnScreen() >= ((int) ((int)(12* towerSize / 25) + (int)(12* towerSize / 25) + (int)(frameWidth / 11.52) + frameWidth) / 40 + (int)(12* towerSize / 25))
                            && e.getXOnScreen() <= ((int)(12* towerSize / 25) + (int)(12* towerSize / 25) + (int) (frameWidth / 11.52)+ frameWidth / 40 + (int)(12* towerSize / 25) + (18* towerSize))){
                        if (e.getYOnScreen() >= (15 * (int) towerSize) + (int)(12* towerSize / 25) + frameHeightBorder
                                && e.getYOnScreen() <= (15 * (int) towerSize) + 12 + (int) towerSize * 2 + frameHeightBorder){
                            for(int i=0;i<Tower.towerList.length;i++) {
                                if (e.getXOnScreen() >= ((int) ((int)(12* towerSize / 25) + (int)(12* towerSize / 25) + (int) (frameWidth / 11.52) + frameWidth / 40 + (int)(12* towerSize / 25)))+(int)(i/2)*(int)towerSize
                                        && e.getXOnScreen() <= ((int)( (int)(12* towerSize / 25) + (int)(12* towerSize / 25) + (int) (frameWidth / 11.52) + frameWidth / 40 + (int)(12* towerSize / 25) + towerSize))+(int)(i/2)*(int)towerSize
                                        && e.getYOnScreen() >= (15 * (int) towerSize) + (int)(12* towerSize / 25) + frameHeightBorder+(int)(i%2)*(int)towerSize
                                        && e.getYOnScreen() <= (15 * (int) towerSize) + (int)(12* towerSize / 25) + (int) towerSize + frameHeightBorder+(int)(i%2)*(int)towerSize) {
                                    if (user.player.money >= Tower.towerList[i].cost) {
                                        System.out.println("[SHOP] You bought a tower for " + Tower.towerList[i].cost + "!");
                                        hand = i+1;

                                        return;
                                    }

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
                placeTower(e.getX(), e.getY()- frameHeightBorder);

                hand = 0;
            }else{
                selectTower(e.getX(), e.getY() - frameHeightBorder);

                if (e.getX() > (int)towerSize*111/4 && e.getX()< (int)towerSize*124/4){
                    if (e.getY() > (int)towerSize*31/2 && e.getY() < (int)towerSize*35/2) {
                        hitStartRoundButton();
                    }
                }
            }

            updateMouse(e);
        }
    }

    public class KeyTyped{
        public  void keyESC(){
            endGame();
            running = false;
        }

        public  void keyEnter(){
            hitStartRoundButton();
        }

        public void keySPACE() {
            startGame(user, "level1");
        }

        public void keyPAUSE()
        {
            gamePaused = true;
            //pauseGame(gamePaused);
        }
    }
}