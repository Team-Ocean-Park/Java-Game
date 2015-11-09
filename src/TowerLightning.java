public class TowerLightning extends  Tower {

    public  TowerLightning(int id, int cost, int range, int damage, int maxAttackTime, int maxAttackDelay){
        super(id, cost, range, damage, maxAttackTime, maxAttackDelay);
    }
    public void towerAttack(int x, int y, EnemyMove enemy){
        enemy.health -= this.damage;
    }
}
