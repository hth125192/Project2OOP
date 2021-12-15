package BombermanGame.Entities.Characters.AI;

import BombermanGame.Library.Pair;
import BombermanGame.Library.Queue;

import java.util.ArrayList;

public abstract class AIEnemy extends AI {
    protected boolean speed;
    protected boolean allowSpeedUp = false;
    public AIEnemy(boolean speed) {
        this.speed = speed;
        ArrayList<Character> canGoThrought = new ArrayList<>()
        {{
            add(' ');
            add('x');
            add('p');
            add('8');
            add('b');
            add('f');
            add('s');
        }};
        for (char c : canGoThrought) canGo.replace(c, true);
    }

    protected int bestDirection(int yy, int xx) {
        int sX = -1, sY = -1;
        for (int i = 0; i < m; i++) {
            boolean breakable = false;
            for (int j = 0; j < n; j++) {
                if (map[i][j] == 'p' || map[i][j] == '8') {
                    sX = i;
                    sY = j;
                    breakable = true;
                    break;
                }
            }
            if (breakable) break;
            sX =0; sY = 0;
        }
        Pair s = new Pair(sX, sY);
        Queue<Pair> queue = new Queue<>();
        int[][] distance = new int[m][n];
        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++) distance[i][j] = -1;
        distance[sX][sY] = 0;
        queue.add(s);
        while (!queue.isEmpty()) {
            Pair u = queue.remove();
            for (int i = 0; i < 4; i++) {
                int x = u.getX() + hX[i];
                int y = u.getY() + hY[i];
                if (!validate(x, y)) continue;
                if (distance[x][y] != -1) continue;
                if (!canGo.get(map[x][y])) continue;
                distance[x][y] = distance[u.getX()][u.getY()] + 1;
                queue.add(new Pair(x, y));
            }
        }

        int direction = -1;
        if (inDanger[xx][yy]) {
            int curDistance = dangerDistance[xx][yy];
            int distanceToBomber = m * n;
            if (curDistance == -1) return 0;
            for (int i = 0; i < 4; i++) {
                int x = xx + hX[i];
                int y = yy + hY[i];
                if (!validate(x, y)) continue;
                if (dangerDistance[x][y] == -1) continue;
                if (dangerDistance[x][y] < curDistance) {
                    curDistance = dangerDistance[x][y];
                    direction = i;
                    distanceToBomber = distance[x][y];
                } else if (dangerDistance[x][y] == curDistance) {
                    if (distanceToBomber == -1 || distanceToBomber > distance[x][y]) {
                        direction = i;
                        distanceToBomber = distance[x][y];
                    }
                }
            }
            if (direction == -1) direction = random.nextInt(4);
            allowSpeedUp = true;
            return direction;
        } else {
            int[] die = new int[4];
            for (int i = 0; i < 4; i++) die[i] = 0;
            int curDistance = distance[xx][yy];
            for (int i = 0; i < 4; i++) {
                int x = xx + hX[i];
                int y = yy + hY[i];
                if (!validate(x, y)) {
                    die[i] = 1;
                    continue;
                }
                if (inDanger[x][y]) {
                    die[i] = 2;
                    continue;
                }
                if (distance[x][y] == -1) continue;
                if (distance[x][y] < curDistance) {
                    curDistance = distance[x][y];
                    direction = i;
                }
            }
            allowSpeedUp = curDistance < 4;
            if (direction == -1) {
                for (int i = 0; i < 4; i++)
                    if (die[i] == 0) return i;
                for (int i = 0; i < 4; i++)
                    if (die[i] == 1) return i;
                return 0;
            } else return direction;
        }
    }
}