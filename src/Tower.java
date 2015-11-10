import javax.swing.*;
import java.awt.*;
import java.util.Random;
import java.util.Scanner;

public abstract class Tower implements Cloneable{
    public String textureFile = "";
    public Image texture = null;

    public static final Tower[] towerList = new Tower[36];

    //public static Tower lightningTower = new TowerLightning(0, 10, 2, 4D, 1D).getTextureFile("lightningTower.png");
    public static final Tower lightningTowerYellow = new TowerLightning(0, 10, 2, 4, 6, 9).getTextureFile("res\\tower\\lightningTower1.jpg");
    public static final Tower lightningTowerGreen = new TowerLightning(1, 25, 3, 6, 6, 15).getTextureFile("res\\tower\\lightningTower2.jpg");
    public static final Tower lightningTowerBlue = new TowerLightning(2, 50, 3, 6, 6, 12).getTextureFile("res\\tower\\lightningTower3.jpg");
    public static final Tower lightningTowerPink = new TowerLightning(3, 75, 4, 10, 9, 21).getTextureFile("res\\tower\\lightningTower4.jpg");
    public static final Tower missileTower = new TowerMissile(4, 10, 2, 3, 0, 17).getTextureFile("res\\tower\\missileTower.jpg");

    public int id;
    public int cost;
    public int range;

    public int damage;

    public int attackTime; //(timer)how long do we want the laser/attack to stay on
    public int attackDelay; //(timer) Pause between each attack;

    public EnemyMove target;

    public int FIRST = 1; //attack enemy nearest based
    public int RANDOM = 2; //attack random enemy

    public int attackStrategy = RANDOM;

    public int maxAttackTime;
    public int maxAttackDelay;



    public static Screen screen;

    public Tower(int id, int cost, int range, int damage, int maxAttackTime, int maxAttackDelay){
        if (towerList[id] != null){
            System.out.println("[TowerInitialization] Two towers with same id! id=" + id);
        } else {
            towerList[id]  = this;

            this.id = id;
            this.cost = cost;
            this.range = range;
            this.damage = damage;

            this.maxAttackTime = maxAttackTime;
            this.maxAttackDelay = maxAttackDelay;

            this.attackTime = 0;
            this.attackDelay =0;
        }
    }

    public EnemyMove calculateEnemy(EnemyMove[] enemies, int x, int y){
    //which of the enemies given are in our range?
        EnemyMove[] enemiesInRange = new EnemyMove[enemies.length];

        int towerX = x;
        int towerY = y;

        int towerRadius = this.range;
        int enemyRadius = 1;

        int enemyX;
        int enemyY;

        for (int i = 0; i < enemies.length; i++) {
            if (enemies[i] != null){
                enemyX = (int) (enemies[i].xPos / Screen.towerSize);
                enemyY = (int) (enemies[i].yPos / Screen.towerSize);

                int dx = enemyX - towerX;
                int dy = enemyY - towerY;
                
                int dradius = towerRadius + enemyRadius;
                
                if ((dx * dx) + (dy * dy) < dradius * dradius){
                    enemiesInRange[i] = enemies[i];
                }
            }
        }
        
        if (this.attackStrategy == RANDOM){
            int totalEnemies = 0;

            for (int i = 0; i < enemiesInRange.length; i++) {
                if (enemiesInRange[i] != null){
                    totalEnemies++;
                }
            }

            if (totalEnemies > 0){
                int enemy = new Random().nextInt(totalEnemies);
                int enemiesTaken = 0;
                int i = 0;

                while (true){
                    if (enemiesTaken == enemy && enemiesInRange[i] != null){
                        return enemiesInRange[i];
                    }

                    if (enemiesInRange[i] != null) {
                        enemiesTaken++;
                    }

                    i++;
                }
            }
        }

        return null;
    }
    public abstract void towerAttack(int x, int y, EnemyMove enemy);

    protected Object clone(){
        try {
            return  super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        return null;
    }

    public  Tower getTextureFile(String str){
        this.textureFile = str;

        this.texture = new ImageIcon(this.textureFile).getImage();

        return null;
    }
}
