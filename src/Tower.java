import javax.swing.*;
import java.awt.*;

public class Tower {
    public String textureFile = "";
    public Image texture;

    public static final Tower[] towerList = new Tower[200];

    public static final  Tower lightningTower = new TowerLightning(0, 10, 2).getTextureFile("res\\tower\\lightningTower.jpg");

    public int id;
    public int cost;
    public int range;

    public Tower(int id, int cost, int range){
        if (towerList[id] != null){
            System.out.println("[TowerInitialization] Two towers with same id: id=" + id);
        } else {
            towerList[id]  = this;

            this.id = id;
            this.cost = cost;
            this.range = range;
        }
    }

    public  Tower getTextureFile(String str){
        this.textureFile = str;

        this.texture = new ImageIcon(this.textureFile).getImage();

        return null;
    }
}
