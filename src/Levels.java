public class Levels {

    int[][] map;

    SpawnPoint spawnPoint;

    public  void findSpawnPoint(){
        for (int x = 0; x < map.length; x++) {
            for (int y = 0; y < map[0].length; y++) {
                if (map[x][y] == 2){
                    spawnPoint = new SpawnPoint(x, y);
                }
            }
        }
    }
}
