package BombermanGame.Entities.Tiles.Items;

import BombermanGame.Entities.Tiles.Tile;
import BombermanGame.Graphics.Sprite;

public abstract class Item extends Tile {
    public Item(int x, int y, Sprite sprite) {
        super(x, y, sprite);
    }
}