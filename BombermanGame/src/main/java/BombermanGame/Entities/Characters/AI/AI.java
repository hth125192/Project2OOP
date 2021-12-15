package BombermanGame.Entities.Characters.AI;

import BombermanGame.Board;
import BombermanGame.Game;
import BombermanGame.Library.Pair;
import BombermanGame.Library.Queue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public abstract class AI {
    protected Random random = new Random();
    protected Board board;
    protected HashMap<Character, Boolean> canGo;
    protected char[][] map;
    protected int[] hX = {1, 0, 0, -1};
    protected int[] hY = {0, 1, -1, 0};
    protected int m, n;
    protected boolean[][] inDanger;
    protected int[][] dangerDistance, distanceFromEnemy;

    public AI() {
        m = Game.levelHeight;
        n = Game.levelWidth;
        canGo = new HashMap<>();
        ArrayList<Character> items = new ArrayList<>()
        {{
            add('#');
            add(' ');
            add('*');
            add('x');
            add('p');
            add('1');
            add('2');
            add('3');
            add('4');
            add('5');
            add('6');
            add('7');
            add('8');
            add('b');
            add('f');
            add('s');
        }};
        for (char c : items) canGo.put(c, false);
    }

    protected boolean validate(int u, int v) {
        return (0 <= u && u < Game.levelHeight && 0 <= v && v < Game.levelWidth);
    }

    private void calcInDanger() {
        inDanger = new boolean[m][n];
        int[][] d = new int[m][n];
        Queue<Pair> queue = new Queue<>();
        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++)
                if (map[i][j] == '7' || map[i][j] == '8') {
                    queue.add(new Pair(i, j));
                    inDanger[i][j] = true;
                    d[i][j] = 0;
                } else inDanger[i][j] = false;

        while (!queue.isEmpty()) {
            Pair top = queue.remove();
            int u = top.getX();
            int v = top.getY();
            for (int j = 0; j < 4; j++) {
                for (int i = 1; i <= Game.getBombRadius(); i++) {
                    int x = u + hX[j] * i;
                    int y = v + hY[j] * i;
                    if (!validate(x, y)) break;
                    if (map[x][y] == '#') break;
                    if (map[x][y] == '*' || map[x][y] == 'p' || map[x][y] == 'x' || map[x][y] == '2' || map[x][y] == '4' || map[x][y] == '5' || map[x][y] == '6' || map[x][y] == '7' || map[x][y] == '8') {
                        inDanger[x][y] = true;
                        break;
                    }
                    inDanger[x][y] = true;
                }
            }
        }
    }

    abstract public void calcDangerDistance();

    private void calcDistanceFromEnemy() {
        distanceFromEnemy = new int[m][n];
        Queue<Pair> queue = new Queue<>();
        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++)
                if (map[i][j] == '2' || map[i][j] == '4' || map[i][j] == '5' || map[i][j] == '6') {
                    queue.add(new Pair(i, j));
                    distanceFromEnemy[i][j] = 0;
                } else distanceFromEnemy[i][j] = -1;

        while (!queue.isEmpty()) {
            Pair top = queue.remove();
            for (int i = 0; i < 4; i++) {
                int u = top.getX() + hX[i];
                int v = top.getY() + hY[i];
                if (!validate(u, v)) continue;
                if (map[u][v] == '2' || map[u][v] == '4' || map[u][v] == '5' || map[u][v] == '6' || map[u][v] == '7' || map[u][v] == '#' || map[u][v] == '*') continue;
                if (distanceFromEnemy[u][v] >= 0) continue;
                distanceFromEnemy[u][v] = distanceFromEnemy[top.getX()][top.getY()] + 1;
                queue.add(new Pair(u, v));
            }
        }

        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++)
                if (map[i][j] == '4') {
                    queue.add(new Pair(i, j));
                    distanceFromEnemy[i][j] = 0;
                }

        while (!queue.isEmpty()) {
            Pair top = queue.remove();
            for (int i = 0; i < 4; i++) {
                int u = top.getX() + hX[i];
                int v = top.getY() + hY[i];
                if (!validate(u, v)) continue;
                if (map[u][v] == '2' || map[u][v] == '4' || map[u][v] == '5' || map[u][v] == '6' || map[u][v] == '7' || map[u][v] == '#') continue;
                if (distanceFromEnemy[u][v] != -1 && distanceFromEnemy[u][v] <= distanceFromEnemy[top.getX()][top.getY()] + 1) continue;
                distanceFromEnemy[u][v] = distanceFromEnemy[top.getX()][top.getY()] + 1;
                queue.add(new Pair(u, v));
            }
        }
    }

    public void initDistance() {
        calcInDanger();
        calcDangerDistance();
        calcDistanceFromEnemy();
    }

    public void calcCurrentMap() throws NullPointerException {
        if (this.board == null) throw new NullPointerException();
        char[][] temp = this.board.reviveMap();
        if (temp == null) {
            map = null;
            return;
        }
        map = new char[m][n];
        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++) map[i][j] = temp[j][i];
    }

    public abstract int calculateDirection();
}