package BombermanGame.Entities.Tiles;

import BombermanGame.Entities.Characters.Bomber;
import BombermanGame.Entities.Entity;
import BombermanGame.Game;
import BombermanGame.Graphics.Sprite;

public class Portal extends Tile {
    public Portal(int x, int y, Sprite sprite) {
        super(x, y, sprite);
    }

    @Override
    public boolean collide(Entity e) {
        if (e instanceof Bomber)
            if (getX() == e.getXTile() && getY() == e.getYTile()) {
                Game.playSE(7);
                return true;
            }
        return false;
    }
}
