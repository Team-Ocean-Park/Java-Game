import java.util.Random;

public class Wave {

    Screen screen;
    int waveNumber=0;
   // int enemiesThisRound=0;
    int pointThisRound;
    int currentPoints;
    //int maxPoints;

    int currentDalay=0;
    int spawnRate=25;

    boolean waveSpawning;

    public Wave(Screen screen){
        this.screen=screen;
    }

    public void nextWave(){
        this.waveNumber++;
        this.pointThisRound=this.waveNumber*25;
        this.currentPoints=0;
        this.waveSpawning=true;

        System.out.println("[Wave]Wave" + this.waveNumber + "Incomming!");

        for(int i=0;i<this.screen.enemyMap.length;i++){
            this.screen.enemyMap[i]=null;

        }
    }

    public void spawnEnemies(){
        if(this.currentPoints<this.pointThisRound){
          if(currentDalay<spawnRate){
              currentDalay+=Screen.speed;
          }else{
              currentDalay=0;

              System.out.println("[Wave]Enamy Spawned");

              int[] enemiesSpawnableID= new int[Enemy.enemyList.length] ;
              int enemiesSpawnableSoFar=0;

              for(int i=0;i<Enemy.enemyList.length;i++){
                  if(Enemy.enemyList[i]!=null){
                 if(Enemy.enemyList[i].points+currentPoints<=this.pointThisRound &&Enemy.enemyList[i].points<=this.waveNumber){
                     enemiesSpawnableID[enemiesSpawnableSoFar]=Enemy.enemyList[i].id;
                     enemiesSpawnableSoFar++;

                    }
                  }
              }
            int enemyID=new Random().nextInt(enemiesSpawnableSoFar);

              this.currentPoints+=Enemy.enemyList[enemyID].points;
            this.screen.spawnEnemy(enemiesSpawnableID[enemyID]);

          }
        } else { this.waveSpawning=false;}
    }


}
