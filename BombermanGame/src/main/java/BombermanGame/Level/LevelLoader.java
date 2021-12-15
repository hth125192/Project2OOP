package BombermanGame.Level;

import BombermanGame.Board;
import BombermanGame.Exceptions.LoadLevelException;

public abstract class LevelLoader {
    protected int width, height, level;
    protected Board board;

    public LevelLoader(Board board, int level) throws LoadLevelException {
        loadLevel(level);
        this.board = board;
    }

    public abstract void loadLevel(int level) throws LoadLevelException;

    public abstract void createEntities();

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getLevel() {
        return level;
    }
}
