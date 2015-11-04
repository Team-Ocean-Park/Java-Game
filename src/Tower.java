import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class Tower implements Cloneable{
    public String textureFile = "";
    public Image texture;

    public static final Tower[] towerList = new Tower[200];

    public static final  Tower lightningTower = new TowerLightning(0, 10, 2, 4, 3, 3).getTextureFile("res\\tower\\lightningTower.jpg");

    public int id;
    public int cost;
    public int range;

    public int damage;

    public int attackTime; //(timer)how long do we want the laser/attack to stay on
    public int attackDelay; //(timer) Pause between each attack

    public int maxAttackTime;
    public int maxAttackDelay;

    public int FIRST = 1; //attack enemy nearest based
    public int RANDOM = 2; //attack random enemy

    public int attackStrategy = RANDOM;

    public EnemyMove target;

    public Tower(int id, int cost, int range, int damage, int maxAttackTime, int maxAttackDelay){
        if (towerList[id] != null){
            System.out.println("[TowerInitialization] Two towers with same id: id=" + id);
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
                
                if ((dx * dx) + (dy + dy) < dradius * dradius){
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
