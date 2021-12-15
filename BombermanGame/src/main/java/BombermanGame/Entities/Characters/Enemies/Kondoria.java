package BombermanGame.Entities.Characters.Enemies;

import BombermanGame.Board;
import BombermanGame.Entities.Characters.AI.AIHigh;
import BombermanGame.Entities.Entity;
import BombermanGame.Entities.Tiles.Wall;
import BombermanGame.Game;
import BombermanGame.Graphics.Sprite;
import BombermanGame.Level.Coordinates;

public class Kondoria extends Enemy {
    public Kondoria(int x, int y, Board board) {
        super(x, y, board, Sprite.kondoria_dead, Game.getBomberSpeed() / 2, 1000);
        sprite = Sprite.kondoria_right1;
        ai = new AIHigh(board, this);
    }

    @Override
    public boolean canMove(double x, double y) {
        double loLy = y - 1;
        double loRy = y - 1;
        double upLy = y - Game.TILE_SIZE;
        double upRy = y - Game.TILE_SIZE;
        double upRx = x - 1 + Game.TILE_SIZE;
        double loRx = x - 1 + Game.TILE_SIZE;
        int tile_UpLx = Coordinates.pixelToTile(x);
        int tile_UpLy = Coordinates.pixelToTile(upLy);
        int tile_UpRx = Coordinates.pixelToTile(upRx);
        int tile_UpRy = Coordinates.pixelToTile(upRy);
        int tile_LoLx = Coordinates.pixelToTile(x);
        int tile_LoLy = Coordinates.pixelToTile(loLy);
        int tile_LoRx = Coordinates.pixelToTile(loRx);
        int tile_LoRy = Coordinates.pixelToTile(loRy);
        Entity entity_UpLeft = board.getEntity(tile_UpLx, tile_UpLy, this);
        Entity entity_UpRight = board.getEntity(tile_UpRx, tile_UpRy, this);
        Entity entity_LoLeft = board.getEntity(tile_LoLx, tile_LoLy, this);
        Entity entity_LoRight = board.getEntity(tile_LoRx, tile_LoRy, this);
        if (entity_LoLeft instanceof Wall || entity_LoRight instanceof Wall || entity_UpLeft instanceof Wall || entity_UpRight instanceof Wall) return false;
        return !collide(entity_LoLeft) && !collide(entity_LoRight) && !collide(entity_UpLeft) && !collide(entity_UpRight);
    }

    @Override
    protected void chooseSprite() {
        switch (direct) {
            case 0, 1:
                if (moving) sprite = Sprite.movingSprite(Sprite.kondoria_right1, Sprite.kondoria_right2, Sprite.kondoria_right3, animate, 60);
                else sprite = Sprite.kondoria_left1;
                break;
            case 2, 3:
                if (moving) sprite = Sprite.movingSprite(Sprite.kondoria_left1, Sprite.kondoria_left2, Sprite.kondoria_left3, animate, 60);
                else sprite = Sprite.kondoria_left1;
                break;
        }
    }
}