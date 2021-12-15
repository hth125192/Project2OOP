package BombermanGame.Entities;

import BombermanGame.Graphics.IRender;
import BombermanGame.Graphics.Screen;
import BombermanGame.Graphics.Sprite;
import BombermanGame.Level.Coordinates;

public abstract class Entity implements IRender {
    protected double x, y;
    protected boolean removed = false;
    protected Sprite sprite;

    @Override
    public abstract void update();

    @Override
    public abstract void render(Screen screen);

    public void remove() {
        removed = true;
    }

    public boolean isRemoved() {
        return removed;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public abstract boolean collide(Entity e);

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getXTile() {
        return Coordinates.pixelToTile(x + (double) sprite.SIZE / 2);
    }

    public int getYTile() {
        return Coordinates.pixelToTile(y - (double) sprite.SIZE / 2);
    }
}