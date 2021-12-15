package BombermanGame.Entities.Characters;

import BombermanGame.Board;
import BombermanGame.Entities.AnimatedEntity;
import BombermanGame.Game;
import BombermanGame.Graphics.Screen;

public abstract class Character extends AnimatedEntity {
    protected Board board;
    protected int direct = -1;
    protected boolean alive = true;
    protected boolean moving = false;
    public int timeAfter = 70;

    public Character(int x, int y, Board board) {
        this.x = x;
        this.y = y;
        this.board = board;
    }

    @Override
    public abstract void update();

    @Override
    public abstract void render(Screen screen);

    protected abstract void calculateMove();

    protected abstract void move(double xa, double ya);

    public abstract void kill();

    protected abstract void afterKill();

    protected abstract boolean canMove(double x, double y);

    protected double getXMessage() {
        return (x * Game.SCALE_MULTIPLE) + ((double) sprite.SIZE / 2 * Game.SCALE_MULTIPLE);
    }

    protected double getYMessage() {
        return (y * Game.SCALE_MULTIPLE) - ((double) sprite.SIZE / 2 * Game.SCALE_MULTIPLE);
    }
}