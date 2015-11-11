public class EnemyRoute {

    Levels level;

    int[][] route = new int[22][14];
    int[][] route_points_worth = new int[22][14];

    int RIGHT = 1;
    int DOWN = 2;
    int LEFT = 3;
    int UP = 4;

    int lastPos= -1;

    int xPos;
    int yPos;

    int baseBlock = 3;
    Base base;

    public EnemyRoute(Levels level){
        this.level = level;

        this.xPos = this.level.spawnPoint.getX();
        this.yPos = this.level.spawnPoint.getY();

        calculateRoute();
    }

    private void calculateRoute() {
        while (base == null){
            calculateNextPos();
        }
    }

    private void calculateNextPos() {
        for (int i = 0; i < 5; i++) {
            if (i != lastPos){
                if (yPos > 0 && i == UP){
                    if (level.map[xPos][yPos - 1] == 1){
                        setPointsWorth();

                        lastPos = DOWN;
                        route[xPos][yPos] = UP;

                        yPos--;

                        break;
                    } else if(level.map[xPos][yPos - 1] == baseBlock){
                        base = new Base(xPos, yPos);
                        break;
                    }
                }

                if (xPos < 21 && i == RIGHT){
                    if (level.map[xPos + 1][yPos] == 1){
                        setPointsWorth();

                        lastPos = LEFT;
                        route[xPos][yPos] = RIGHT;

                        xPos++;

                        break;
                    }else if(level.map[xPos + 1][yPos] == baseBlock){
                        base = new Base(xPos, yPos);
                        break;
                    }
                }

                if (xPos > 0 && i == LEFT){
                    if (level.map[xPos - 1][yPos] == 1){
                        setPointsWorth();

                        lastPos = RIGHT;
                        route[xPos][yPos] = LEFT;

                        xPos++;

                        break;
                    } else if(level.map[xPos - 1][yPos] == baseBlock){
                        base = new Base(xPos, yPos);
                        break;
                    }
                }


                if (yPos < 13 && i == DOWN){
                    if (level.map[xPos][yPos + 1] == 1){
                        setPointsWorth();

                        lastPos = UP;
                        route[xPos][yPos] = DOWN;

                        yPos++;

                        break;
                    } else if(level.map[xPos][yPos + 1] == baseBlock){
                        base = new Base(xPos, yPos);
                        break;
                    }
                }
            }
        }
    }

    private void setPointsWorth(){
        if (lastPos == UP){
            route_points_worth[xPos][yPos] = route_points_worth[xPos][yPos - 1] + 1;
        }

        if (lastPos == DOWN){
            route_points_worth[xPos][yPos] = route_points_worth[xPos][yPos + 1] + 1;
        }

        if (lastPos == RIGHT){
            route_points_worth[xPos][yPos] = route_points_worth[xPos + 1][yPos] + 1;
        }

        if (lastPos == LEFT){
            route_points_worth[xPos][yPos] = route_points_worth[xPos - 1][yPos - 1] + 1;
        }

        if (lastPos == -1){
            route_points_worth[xPos][yPos] = 1;
        }
    }

    public int getPointsWorth(int x, int y){
        return route_points_worth[x][y];
    }
}
