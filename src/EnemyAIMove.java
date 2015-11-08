public class EnemyAIMove extends EnemyAI {

    //EnemyRoute route;

    public EnemyAIMove(int id) {
        super(id);
    }

    public void move(EnemyMove enemy){


        if (enemy.xPos % (int) Screen.towerSize == 0
                && enemy.yPos % (int) Screen.towerSize == 0
                && enemy.routePosX == enemy.xPos / (int) Screen.towerSize
                && enemy.routePosY == enemy.yPos / (int) Screen.towerSize){

            if (enemy.routePosX == basePosX && enemy.routePosY == basePosY){
                enemy.attack = true;
            }else {
                if (route.route[enemy.routePosX][enemy.routePosY] == route.UP){
                    enemy.routePosY--;
                }else

                if (route.route[enemy.routePosX][enemy.routePosY] == route.DOWN){
                    enemy.routePosY++;
                }else

                if (route.route[enemy.routePosX][enemy.routePosY] == route.RIGHT){
                    enemy.routePosX++;
                }else

                if (route.route[enemy.routePosX][enemy.routePosY] == route.LEFT){
                    enemy.routePosX--;
                } else {
                    cantFindRoute();
                }

            }
        }else {
            double xPos = enemy.xPos / Screen.towerSize;
            double yPos = enemy.yPos / Screen.towerSize;

            if (xPos > enemy.routePosX){
                enemy.xPos -= enemy.enemy.speed;
                  if(enemy.xPos<enemy.routePosX*(int)Screen.towerSize){
                    enemy.xPos=enemy.routePosX*(int)Screen.towerSize;
                  }
            }
            if (xPos < enemy.routePosX){
                enemy.xPos += enemy.enemy.speed;
                if(enemy.xPos>enemy.routePosX*(int)Screen.towerSize){
                   // System.out.println("true");
                    enemy.xPos=enemy.routePosX*(int)Screen.towerSize;
                }
            }
            if (yPos > enemy.routePosY){
                enemy.yPos -= enemy.enemy.speed;
                if(enemy.yPos<enemy.routePosY*(int)Screen.towerSize){
                    enemy.yPos=enemy.routePosY*(int)Screen.towerSize;
                }
            }
            if (yPos < enemy.routePosY){
                enemy.yPos += enemy.enemy.speed;
                if(enemy.yPos>enemy.routePosY*(int)Screen.towerSize){
                    enemy.yPos=enemy.routePosY*(int)Screen.towerSize;
                }
            }
        }
    }

    public void cantFindRoute(){
        System.out.println("[EnemyAIMove] can't find next move!");
    }
}
