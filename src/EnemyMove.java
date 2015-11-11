public class EnemyMove {

    Enemy enemy;

    double xPos;
    double yPos;

    int routePosX;
    int routePosY;

    int finishedEnemyPosX;
    int finishedEnemyPosY;

    int health;

    int id;

    boolean attack;
    boolean isEnemyFinished;

    public EnemyMove( Enemy enemy,SpawnPoint spawnPoint, EndPoint endPoint ){
        this.enemy = enemy;

        this.routePosX = spawnPoint.getX();
        this.routePosY = spawnPoint.getY();

        this.finishedEnemyPosX = endPoint.getX();
        this.finishedEnemyPosY = endPoint.getY();


        this.xPos = spawnPoint.getX() * (int)Screen.towerSize;
        this.yPos = spawnPoint.getY() * (int)Screen.towerSize;

        this.id = enemy.id;

        this.attack = false;
        this.isEnemyFinished = false;
        this.health = enemy.health;
    }

    public EnemyMove update(){
        EnemyMove currentEnemy = this;

        if(currentEnemy.routePosX == currentEnemy.finishedEnemyPosX  - 2 &&
                currentEnemy.routePosY == currentEnemy.finishedEnemyPosY) {
            currentEnemy.isEnemyFinished = true;
        } else {
            currentEnemy.isEnemyFinished = false;
        }

        if((currentEnemy.health<=0) || (currentEnemy.routePosX == currentEnemy.finishedEnemyPosX - 1&&
                currentEnemy.routePosY == currentEnemy.finishedEnemyPosY)) {
            return null;
        }

        return currentEnemy;
    }


}
