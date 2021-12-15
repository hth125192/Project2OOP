package BombermanGame.Entities.Tiles;

import BombermanGame.Entities.Entity;
import BombermanGame.Graphics.Screen;
import BombermanGame.Graphics.Sprite;
import BombermanGame.Level.Coordinates;

public abstract class Tile extends Entity {
    public Tile(int x, int y, Sprite sprite) {
        this.x = x;
        this.y = y;
        this.sprite = sprite;
    }

    @Override
    public boolean collide(Entity e) {
        return false;
    }

    @Override
    public void render(Screen screen) {
        screen.renderEntity(Coordinates.tileToPixel(x), Coordinates.tileToPixel(y), this);
    }

    @Override
    public void update() {}
}