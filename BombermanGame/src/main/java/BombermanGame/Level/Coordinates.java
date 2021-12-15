package BombermanGame.Level;

import BombermanGame.Game;

public class Coordinates {
    public static int pixelToTile(double i) {
        return (int) (i / Game.TILE_SIZE);
    }

    public static int tileToPixel(int i) {
        return i * Game.TILE_SIZE;
    }

    public static int tileToPixel(double i) {
        return (int) (i * Game.TILE_SIZE);
    }
}