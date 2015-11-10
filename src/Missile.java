import javax.swing.*;
import java.awt.*;

public class Missile {

    public double x;
    public double y;

    public int speed;

    public double direction;

    public int damage;

    public EnemyMove target;

    Image texture = new ImageIcon("res/tower/bullets/missile.png").getImage();

    public Missile(double x, double y, int speed, int damage, EnemyMove target){
        this.x = x;
        this.y = y;

        this.damage = damage;
        this.speed = speed;

        this.target = target;

        updateDirection();
    }

    public void update() {
        updateDirection();

        this.x += speed*Math.cos(direction);
        this.y += speed*Math.sin(direction);

        checkTarget();
    }

    public void checkTarget() {
        int deltaX = (int) (this.target.xPos - this.x - 7);
        int deltaY = (int) (this.target.yPos - this.y - 15);

        int deltaRadius = 2 + 2;

        if(deltaX*deltaX + deltaY*deltaY < deltaRadius*deltaRadius) {
            this.target.health -= this.damage;

            this.target = null;
        }
    }

    public void updateDirection() {
        this.direction = Math.atan2(this.target.yPos - this.y - 15,
                                    this.target.xPos - this.x - 7);
    }
}
