package BombermanGame.Entities.Tiles.Items;

import BombermanGame.Entities.Characters.Bomber;
import BombermanGame.Entities.Entity;
import BombermanGame.Game;
import BombermanGame.Graphics.Sprite;

public class FlameItem extends Item {
    public FlameItem(int x, int y, Sprite sprite) {
        super(x, y, sprite);
    }

    @Override
    public boolean collide(Entity e) {
        if (e instanceof Bomber)
            if (getX() == e.getXTile() && getY() == e.getYTile()) {
                Game.playSE(5);
                remove();
                Game.addBombRadius(1);
            }
        return false;
    }
}