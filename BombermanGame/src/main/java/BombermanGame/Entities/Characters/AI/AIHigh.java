package BombermanGame.Entities.Characters.AI;

import BombermanGame.Board;
import BombermanGame.Entities.Characters.Enemies.Enemy;
import BombermanGame.Library.Pair;
import BombermanGame.Library.Queue;

public class AIHigh extends AIEnemy {
    Enemy enemy;

    public AIHigh(Board board, Enemy enemy) {
        super(false);
        canGo.replace('*', true);
        canGo.replace('6', true);
        this.board = board;
        this.enemy = enemy;
    }

    @Override
    public int calculateDirection() {
        calcCurrentMap();
        if (map == null) return 1;
        initDistance();
        return bestDirection(enemy.getXTile(), enemy.getYTile());
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
                if (map[u][v] == '1' || map[u][v] == '2' || map[u][v] == '3' || map[u][v] == '4' || map[u][v] == '5' || map[u][v] == '7' || map[u][v] == '8' || map[u][v] == '#') continue;
                if (dangerDistance[u][v] >= 0) continue;
                dangerDistance[u][v] = dangerDistance[top.getX()][top.getY()] + 1;
                queue.add(new Pair(u, v));
            }
        }
    }
}