package BombermanGame.Entities.Tiles;

import BombermanGame.Entities.Entity;
import BombermanGame.Graphics.Sprite;

public class Grass extends Tile {
    public Grass(int x, int y, Sprite sprite) {
        super(x, y, sprite);
    }

    @Override
    public boolean collide(Entity e) {
        return true;
    }
}