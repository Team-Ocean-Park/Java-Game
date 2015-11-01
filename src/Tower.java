import javax.swing.*;
import java.awt.*;

public class Tower {
    public String textureFile = "";
    public Image texture;

    public static final Tower[] towerList = new Tower[200];

    public static final  Tower lightningTower = new TowerLightning(0, 0).getTextureFile("res\\tower\\lightningTower.jpg");

    public  int id;
    public int cost;

    public Tower(int id, int cost){
        if (towerList[id] != null){
            System.out.println("[TowerInitialization] Two towers with same id: id=" + id);
        } else {
            towerList[id]  = this;

            this.id = id;
            this.cost = cost;
        }
    }

    public  Tower getTextureFile(String str){
        this.textureFile = str;

        this.texture = new ImageIcon(this.textureFile).getImage();

        return null;
    }
}
