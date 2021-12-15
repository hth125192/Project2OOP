package BombermanGame.Entities.Characters.AI;

import BombermanGame.Board;
import BombermanGame.Entities.Characters.Bomber;
import BombermanGame.Game;
import BombermanGame.Library.Pair;
import BombermanGame.Library.Queue;

import java.util.ArrayList;

public class AIBomber extends AI {
    protected Bomber bomber;

    private static int preDirection;
    private static int count;

    public AIBomber(Board board, Bomber bomber) {
        this.board = board;
        this.bomber = bomber;
        ArrayList<Character> canGoThrought = new ArrayList<>()
        {{
            add(' ');
            add('x');
            add('p');
            add('7');
            add('8');
            add('b');
            add('f');
            add('s');
        }};

        for (char c : canGoThrought) canGo.replace(c, true);
    }

    @Override
    public void calcDangerDistance() {
        dangerDistance = new int[m][n];
        Queue<Pair> queue = new Queue<>();
        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++)
                if (map[i][j] != '#' && map[i][j] != '*' && map[i][j] != '1' && map[i][j] != '2' && map[i][j] != '3' && map[i][j] != '4' && map[i][j] != '5' && map[i][j] != '6' && !inDanger[i][j]) {
                    queue.add(new Pair(i, j));
                    dangerDistance[i][j] = 0;
                } else dangerDistance[i][j] = -1;

        while (!queue.isEmpty()) {
            Pair top = queue.remove();
            for (int i = 0; i < 4; i++) {
                int u = top.getX() + hX[i];
                int v = top.getY() + hY[i];
                if (!validate(u, v)) continue;
                if (map[u][v] == '1' || map[u][v] == '2' || map[u][v] == '3' || map[u][v] == '4' || map[u][v] == '5' || map[u][v] == '6' || map[u][v] == '7' || map[u][v] == '#' || map[u][v] == '*') continue;
                if (dangerDistance[u][v] >= 0) continue;
                dangerDistance[u][v] = dangerDistance[top.getX()][top.getY()] + 1;
                queue.add(new Pair(u, v));
            }
        }
    }

    public int calculateDirection() {
        calcCurrentMap();
        initDistance();
        int temp = bestDirection();
        if (temp + preDirection == 3) {
            count++;
            if (count == 4) {
                count = 0;
                preDirection = 4;
                return preDirection;
            }
        }
        preDirection = temp;
        return temp;
    }

    private int[][] bfs() {
        int[][] d = new int[m][n];
        Queue <Pair> queue = new Queue<>();
        char c = '2';
        int countSmart = 0;
        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++)
                if (map[i][j] == '2' || map[i][j] == '4' || map[i][j] == '5' || map[i][j] == '6') countSmart++;

        if (countSmart == 0) c = '1';
        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++)
                if (map[i][j] == '2' || map[i][j] == '4' || map[i][j] == '5' || map[i][j] == '6' || map[i][j] == c) {
                    queue.add(new Pair(i, j));
                    d[i][j] = 0;
                } else d[i][j] = -1;

        while (!queue.isEmpty()) {
            Pair top = queue.remove();
            for (int i = 0; i < 4; i++) {
                int u = top.getX() + hX[i];
                int v = top.getY() + hY[i];
                if (!validate(u, v)) continue;
                if (d[u][v] != -1) continue;
                if (!canGo.get(map[u][v])) continue;
                d[u][v] = d[top.getX()][top.getY()] + 1;
                if (map[u][v] == '8' || map[u][v] == 'p') continue;
                queue.add(new Pair(u, v));
            }
        }
        return d;
    }

    private int bestDirection() {
        int countSmart = 0;
        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++)
                if (map[i][j] == '2' || map[i][j] == '4' || map[i][j] == '5' || map[i][j] == '6') countSmart++;

        int x = -1, y = -1;
        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++)
                if (map[i][j] == 'p' || map[i][j] == '8') {
                    x = i;
                    y = j;
                    break;
                }

        if (inDanger[x][y]) {
            int direction = -1;
            int curAns = m * n;
            if (countSmart == 0) distanceFromEnemy = bfs();
            int curDistance = dangerDistance[x][y];
            if (curDistance == -1) return random.nextInt(4);
            boolean ok = distanceFromEnemy[x][y] > Game.getBombRadius() + 3;

            for (int i = 0; i < 4; i++) {
                int u = x + hX[i];
                int v = y + hY[i];
                if (!validate(u, v)) continue;
                if (dangerDistance[u][v] == -1) continue;
                if (dangerDistance[u][v] < curDistance) {
                    curDistance = dangerDistance[u][v];
                    curAns = distanceFromEnemy[u][v];
                    direction = i;
                } else if (dangerDistance[u][v] == curDistance) {
                    if (ok) {
                        if (curAns > distanceFromEnemy[u][v] || curAns == -1) {
                            curAns = distanceFromEnemy[u][v];
                            direction = i;
                        }
                    } else {
                        if (curAns < distanceFromEnemy[u][v] || distanceFromEnemy[u][v] == 1) {
                            curAns = distanceFromEnemy[u][v];
                            direction = i;
                        }
                    }
                }
            }
            return direction;
        } else {
            int[][] d = bfs();
            int curDistance = d[x][y];
            System.out.println(curDistance);
            if (curDistance <= Game.getBombRadius() + 1 && bomber.canPlaceBomb()) return 4;
            else if (curDistance <= Game.getBombRadius() + 1) {
                int direction = -1;
                curDistance = distanceFromEnemy[x][y];
                for (int i = 0; i < 4; i++) {
                    int u = x + hX[i];
                    int v = y + hY[i];
                    if (!validate(u, v)) continue;
                    if (inDanger[u][v]) continue;
                    if (distanceFromEnemy[u][v] == -1) continue;
                    if (distanceFromEnemy[u][v] > curDistance) {
                        curDistance = distanceFromEnemy[u][v];
                        direction = i;
                    }
                }
                if (direction != -1) return direction;
                return random.nextInt(4);
            }
            int direction = -1;
            boolean boom = false;

            for (int i = 0; i < 4; i++) {
                int u = x + hX[i];
                int v = y + hY[i];
                if (!validate(u, v)) continue;
                if (inDanger[u][v]) continue;
                if (d[u][v] == -1) continue;
                if (d[u][v] < curDistance || curDistance == -1) {
                    curDistance = d[u][v];
                    direction = i;
                    boom = map[u][v] == '*';
                } else if (d[u][v] == curDistance) {
                    int temp = random.nextInt(4);
                    if (temp == 1) direction = i;
                    boom = map[u][v] == '*';
                }
            }
            if (boom) return 4;

            curDistance = d[x][y];
            for (int i = 0; i < 4; i++) {
                int u = x + hX[i];
                int v = y + hY[i];
                if (!validate(u, v)) continue;
                if (inDanger[u][v]) continue;
                if (d[u][v] == -1) continue;
                if (d[u][v] > curDistance) {
                    curDistance = d[u][v];
                    direction = i;
                }
            }
            return direction;
        }
    }
}