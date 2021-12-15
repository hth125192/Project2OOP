package BombermanGame.Entities.Bomb;

import BombermanGame.Board;
import BombermanGame.Entities.AnimatedEntity;
import BombermanGame.Entities.Characters.Bomber;
import BombermanGame.Entities.Characters.Character;
import BombermanGame.Entities.Entity;
import BombermanGame.Game;
import BombermanGame.Graphics.Screen;
import BombermanGame.Graphics.Sprite;
import BombermanGame.Level.Coordinates;

public class Bomb extends AnimatedEntity {
    protected double timeToExplode = 120;
    public int timeAfter = 20;
    protected Board board;
    protected Flame[] flames;
    protected boolean exploded = false;

    public Bomb(int x, int y, Board board) {
        this.x = x;
        this.y = y;
        this.board = board;
        sprite = Sprite.bomb;
    }

    @Override
    public void update() {
        if (timeToExplode > 0) timeToExplode--;
        else {
            if (!exploded) explode();
            else updateFlames();
            if (timeAfter > 0) timeAfter--;
            else remove();
        }
        animate();
    }

    @Override
    public void render(Screen screen) {
        if (exploded) {
            sprite = Sprite.movingSprite(Sprite.bomb_exploded, Sprite.bomb_exploded1, Sprite.bomb_exploded2, animate, 20);
            renderFlames(screen);
        } else sprite = Sprite.movingSprite(Sprite.bomb, Sprite.bomb_1, Sprite.bomb_2, animate, 40);
        screen.renderEntity((int) x << 4, (int) y << 4, this);
    }

    public void renderFlames(Screen screen) {
        for (Flame flame : flames) flame.render(screen);
    }

    public void updateFlames() {
        for (Flame flame : flames) flame.update();
    }

    protected void explode() {
        Game.playSE(2);
        timeToExplode = 0;
        exploded = true;

        flames = new Flame[4];
        flames[0] = new Flame((int) x, (int) y - 1, 0, Game.getBombRadius(), board);
        flames[1] = new Flame((int) x + 1, (int) y, 1, Game.getBombRadius(), board);
        flames[2] = new Flame((int) x, (int) y + 1, 2, Game.getBombRadius(), board);
        flames[3] = new Flame((int) x - 1, (int) y, 3, Game.getBombRadius(), board);

        Character ch = board.getCharacterAtExcluding((int) x, (int) y, null);
        if (ch != null) ch.kill();
    }

    public FlameSegment flameAt(int x, int y) {
        if (!exploded) return null;
        for (Flame flame : flames) {
            if (flame == null) return null;
            FlameSegment e = flame.flameSegmentAt(x, y);
            if (e != null) return e;
        }
        return null;
    }

    @Override
    public boolean collide(Entity e) {
        if (e instanceof FlameSegment && !exploded) explode();

        if (e instanceof Bomber) {
            double loLy = e.getY() - 1;
            double loRy = e.getY() - 1;
            double upLy = e.getY() + 1 - Game.TILE_SIZE;
            double upRy = e.getY() + 1 - Game.TILE_SIZE;
            double upLx = e.getX() + 1;
            double loLx = e.getX() + 1;
            double upRx = e.getX() - 1 + (double) Game.TILE_SIZE * 3 / 4;
            double loRx = e.getX() - 1 + (double) Game.TILE_SIZE * 3 / 4;
            int tile_UpLx = Coordinates.pixelToTile(upLx);
            int tile_UpLy = Coordinates.pixelToTile(upLy);
            int tile_UpRx = Coordinates.pixelToTile(upRx);
            int tile_UpRy = Coordinates.pixelToTile(upRy);
            int tile_LoLx = Coordinates.pixelToTile(loLx);
            int tile_LoLy = Coordinates.pixelToTile(loLy);
            int tile_LoRx = Coordinates.pixelToTile(loRx);
            int tile_LoRy = Coordinates.pixelToTile(loRy);
            return (tile_LoLx != x || tile_LoLy != y) && (tile_LoRx != x || tile_LoRy != y) && (tile_UpLx != x || tile_UpLy != y) && (tile_UpRx != x || tile_UpRy != y);
        }
        return true;
    }
}