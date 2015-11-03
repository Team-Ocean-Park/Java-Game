import javax.swing.*;
import java.awt.*;

//package net.scratchforfun.towerdefence;
public class Enemy {

    public static final Enemy[]enemyList = new Enemy[200];

    public static final Enemy slime = new EnemySlime(0, 5 , 2 ,10 ,3 , 4).getTextureFile("EnemySlime.png");

    public String textureFile = "";
    public Image texture = null;

    public int id;
    public int price;
    public double speed;
    public double attackSpeed;
    public  int damage;
    public int health;

    public Enemy(int id ,int price , int damage, int health ,double speed, double attackSpeed){
        if(enemyList[id] != null ){
            System.out.println("[TowerInitialization] Two enemies with same id");
        }else{
            enemyList[id]= this;

            this.id = id;
            this.price = price;
            this.damage =damage;
            this.health = health;
            this.speed = speed;
            this.attackSpeed = attackSpeed;

        }
    }

    public Enemy getTextureFile(String str){
        this.textureFile = str;
        this.texture = new ImageIcon("res\\enemy\\" + this.textureFile).getImage();

        return this;
    }

    public static  void startup(){}

}
