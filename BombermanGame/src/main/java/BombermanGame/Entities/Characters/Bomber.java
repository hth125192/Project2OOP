package BombermanGame.Entities.Characters;

import BombermanGame.Board;
import BombermanGame.Entities.Bomb.Bomb;
import BombermanGame.Entities.Bomb.FlameSegment;
import BombermanGame.Entities.Characters.AI.AI;
import BombermanGame.Entities.Characters.AI.AIBomber;
import BombermanGame.Entities.Characters.Enemies.Enemy;
import BombermanGame.Entities.Entity;
import BombermanGame.Entities.LayeredEntity;
import BombermanGame.Entities.Message;
import BombermanGame.Entities.Tiles.Destroyable.Brick;
import BombermanGame.Entities.Tiles.Items.Item;
import BombermanGame.Entities.Tiles.Portal;
import BombermanGame.Entities.Tiles.Wall;
import BombermanGame.Game;
import BombermanGame.Graphics.Screen;
import BombermanGame.Graphics.Sprite;
import BombermanGame.Input.Keyboard;
import BombermanGame.Level.Coordinates;

import java.awt.*;
import java.util.Iterator;
import java.util.List;

public class Bomber extends Character {
    private final int time = 15;
    protected Keyboard input;
    protected AI ai;
    protected int finalAnimation = 30;

    protected int timeBetweenPutBombs = 0;
    private final List<Bomb> bombs;
    private boolean auto = false;
    private boolean isPlaceBomb = false;
    private int countTime = 0;
    private boolean render = true;
    private int direction;
    private final int maxSteps;
    private int steps;

    public Bomber(int x, int y, Board board) {
        super(x, y, board);
        timeAfter = 250;
        bombs = this.board.getBombs();
        input = this.board.getInput();
        sprite = Sprite.player_right;
        ai = new AIBomber(this.board, this);
        maxSteps = (int) Math.round(Game.TILE_SIZE / Game.getBomberSpeed());
        steps = maxSteps + 1;
    }

    @Override
    public void update() {
        animate();
        clearBombs();
        if (!alive) {
            afterKill();
            return;
        }
        if (timeBetweenPutBombs < -7500) timeBetweenPutBombs = 0;
        else timeBetweenPutBombs--;
        calculateMove();
        detectPlaceBomb();
    }

    @Override
    public void render(Screen screen) {
        calculateXOffset();
        if (alive) chooseSprite();
        else sprite = Sprite.movingSprite(Sprite.player_dead1, Sprite.player_dead2, Sprite.player_dead3, animate, 60);
        if (render) screen.renderEntity((int) x, (int) y - sprite.SIZE, this);
    }

    public void calculateXOffset() {
        int xScroll = Screen.calculateXOffset(board, this);
        Screen.setOffset(xScroll, 0);
    }

    private void detectPlaceBomb() {
        if (auto) {
            if (isPlaceBomb && timeBetweenPutBombs < 0 && Game.getBombRate() > 0) {
                timeBetweenPutBombs = 30;
                placeBomb(Coordinates.pixelToTile(x + (double) Game.TILE_SIZE / 2), Coordinates.pixelToTile(y - (double) Game.TILE_SIZE / 2));
                isPlaceBomb = false;
            }
            return;
        }
        if (input.space && timeBetweenPutBombs < 0 && Game.getBombRate() > 0) {
            timeBetweenPutBombs = 30;
            placeBomb(Coordinates.pixelToTile(x + (double) Game.TILE_SIZE / 2), Coordinates.pixelToTile(y - (double) Game.TILE_SIZE / 2));
        }
    }

    protected void placeBomb(int x, int y) {
        Entity downThere = board.getEntity(x, y, this);
        if (downThere instanceof LayeredEntity)
            if (((LayeredEntity) downThere).getTopEntity() instanceof Portal) return;
        if (downThere instanceof Bomb || downThere instanceof Enemy) return;
        board.addBomb(new Bomb(x, y, board));
        Game.playSE(1);
        Game.addBombRate(-1);
    }

    public boolean canPlaceBomb() {
        Entity downThere = board.getEntity(getXTile(), getYTile(), this);
        if(downThere instanceof LayeredEntity)
            if (((LayeredEntity) downThere).getTopEntity() instanceof Portal) return false;
        if (downThere instanceof Bomb || downThere instanceof Enemy) return false;
        return timeBetweenPutBombs < 0 && Game.getBombRate() > 0;
    }

    private void clearBombs() {
        Iterator<Bomb> bs = bombs.iterator();
        Bomb b;
        while (bs.hasNext()) {
            b = bs.next();
            if (b.isRemoved()) {
                bs.remove();
                Game.addBombRate(1);
            }
        }
    }

    @Override
    public void kill() {
        if (!alive) return;
        Game.playSE(4);
        alive = false;
        board.addLives(-1);

        Message msg = new Message("-1 LIVE", getXMessage(), getYMessage(), 2, Color.white, 14);
        board.addMessage(msg);
    }

    @Override
    protected void afterKill() {
        if (timeAfter > 0) {
            --timeAfter;
            if (finalAnimation > 0) finalAnimation--;
            else render = false;
        } else {
            if (board.getLives() > 0) board.restart();
            else board.endGame();
        }
    }

    @Override
    protected void calculateMove() {
        if (auto) processAuto();
        else processManual();
    }

    private void processAuto() {
        if (steps == maxSteps + 1) direction = ai.calculateDirection();
        steps--;
        if (steps == 0) {
            steps = maxSteps;
            direction = ai.calculateDirection();
            System.out.println(direction);
        }

        switch (direction) {
            case 0 -> {
                moving = true;
                move(x, y + Game.getBomberSpeed());
                countTime--;
                if (countTime < 0) countTime = time;
            }
            case 1 -> {
                moving = true;
                move(x + Game.getBomberSpeed(), y);
                countTime--;
                if (countTime < 0) countTime = time;
            }
            case 2 -> {
                moving = true;
                move(x - Game.getBomberSpeed(), y);
                countTime--;
                if (countTime < 0) countTime = time;
            }
            case 3 -> {
                moving = true;
                move(x, y - Game.getBomberSpeed());
                countTime--;
                if (countTime < 0) countTime = time;
            }
            case 4 -> {
                moving = false;
                countTime = 0;
                isPlaceBomb = true;
            }
            default -> {
                moving = false;
                countTime = 0;
            }
        }
    }

    private void processManual() {
        if (input.down) {
            moving = true;
            move(x, y + Game.getBomberSpeed());
            countTime--;
            if (countTime < 0) countTime = time;
        } else if (input.left) {
            moving = true;
            move(x - Game.getBomberSpeed(), y);
            countTime--;
            if (countTime < 0) countTime = time;
        } else if (input.right) {
            moving = true;
            move(x + Game.getBomberSpeed(), y);
            countTime--;
            if (countTime < 0) countTime = time;
        } else if (input.up) {
            moving = true;
            move(x, y - Game.getBomberSpeed());
            countTime--;
            if (countTime < 0) countTime = time;
        } else {
            countTime = 0;
            moving = false;
        }
    }

    @Override
    public boolean canMove(double x, double y) {
        double loLy = y - 1;
        double loRy = y - 1;
        double upLy = y + 1 - Game.TILE_SIZE;
        double upRy = y + 1 - Game.TILE_SIZE;
        double upLx = x + 1;
        double loLx = x + 1;
        double upRx = x - 1 + (double) Game.TILE_SIZE * 3 / 4;
        double loRx = x - 1 + (double) Game.TILE_SIZE * 3 / 4;

        int tile_UpLx = Coordinates.pixelToTile(upLx);
        int tile_UpLy = Coordinates.pixelToTile(upLy);

        int tile_UpRx = Coordinates.pixelToTile(upRx);
        int tile_UpRy = Coordinates.pixelToTile(upRy);

        int tile_LoLx = Coordinates.pixelToTile(loLx);
        int tile_LoLy = Coordinates.pixelToTile(loLy);

        int tile_LoRx = Coordinates.pixelToTile(loRx);
        int tile_LoRy = Coordinates.pixelToTile(loRy);

        Entity entity_UpLeft = board.getEntity(tile_UpLx, tile_UpLy, this);
        Entity entity_UpRight = board.getEntity(tile_UpRx, tile_UpRy, this);
        Entity entity_LoLeft = board.getEntity(tile_LoLx, tile_LoLy, this);
        Entity entity_LoRight = board.getEntity(tile_LoRx, tile_LoRy, this);
        if (entity_LoLeft instanceof Wall || entity_LoRight instanceof Wall || entity_UpLeft instanceof Wall || entity_UpRight instanceof Wall) return false;
        if (entity_LoLeft instanceof LayeredEntity || entity_LoRight instanceof LayeredEntity || entity_UpLeft instanceof LayeredEntity || entity_UpRight instanceof LayeredEntity) {
            if (entity_LoLeft instanceof LayeredEntity) {
                Entity top = ((LayeredEntity) entity_LoLeft).getTopEntity();
                if (top instanceof Brick) return false;
                if (top instanceof Item) {
                    top.collide(this);
                    return true;
                }
                if (top instanceof Portal && board.detectNoEnemies()) {
                    if (top.collide(this)) board.nextLevel();
                    return true;
                }
            }
            if (entity_LoRight instanceof LayeredEntity) {
                Entity top = ((LayeredEntity) entity_LoRight).getTopEntity();
                if (top instanceof Brick) return false;
                if (top instanceof Item) {
                    top.collide(this);
                    return true;
                }
                if (top instanceof Portal && board.detectNoEnemies()) {
                    if (top.collide(this)) board.nextLevel();
                    return true;
                }
            }
            if (entity_UpLeft instanceof LayeredEntity) {
                Entity top = ((LayeredEntity) entity_UpLeft).getTopEntity();
                if (top instanceof Brick) return false;
                if (top instanceof Item) {
                    top.collide(this);
                    return true;
                }
                if (top instanceof Portal && board.detectNoEnemies()) {
                    if (top.collide(this)) board.nextLevel();
                    return true;
                }
            }
            if (entity_UpRight instanceof LayeredEntity) {
                Entity top = ((LayeredEntity) entity_UpRight).getTopEntity();
                if (top instanceof Brick) return false;
                if (top instanceof Item) {
                    top.collide(this);
                    return true;
                }
                if (top instanceof Portal && board.detectNoEnemies()) {
                    if (top.collide(this)) board.nextLevel();
                    return true;
                }
            }
        }
        return !collide(entity_LoLeft) && !collide(entity_LoRight) && !collide(entity_UpLeft) && !collide(entity_UpRight);
    }

    private void soften(double xa, double ya) {
        if (xa != x && y == ya) {
            double near1 = ((int) ya / Game.TILE_SIZE) * Game.TILE_SIZE;
            double near2 = ((int) ya / Game.TILE_SIZE + 1) * Game.TILE_SIZE;
            if (ya - near1 <= 8) {
                if (canMove(xa, near1)) {
                    y--;
                    soften(xa, ya--);
                    if (xa > x) direct = 4;
                    else direct = 3;
                }
            }
            if (near2 - ya <= 8) {
                if (canMove(xa, near2)) {
                    y++;
                    move(xa, ya++);
                    if (xa > x) direct = 4;
                    else direct = 3;
                }
            }
        } else if (xa == x && y != ya) {
            double near1 = ((int) xa / Game.TILE_SIZE) * Game.TILE_SIZE;
            double near2 = ((int) xa / Game.TILE_SIZE + 1) * Game.TILE_SIZE;
            if (xa - near1 <= 8) {
                if (canMove(near1, ya)) {
                    x--;
                    soften(xa--, ya);
                }
            }
            if (near2 - xa <= 8) {
                if (canMove(near2, ya)) {
                    x++;
                    soften(xa++, ya);
                    direct = 1;
                }
            }
        }
    }

    @Override
    public void move(double xa, double ya) {
        if (y < ya) direct = 2;
        if (y > ya) direct = 0;
        if (x > xa) direct = 3;
        if (x < xa) direct = 4;
        if (canMove(xa, ya)) {
            x = xa;
            y = ya;
        } else soften(xa, ya);
    }

    @Override
    public boolean collide(Entity e) {
        if (e instanceof FlameSegment) e.collide(this);
        if (e instanceof Enemy)
            if (getXTile() == e.getXTile() && getYTile() == e.getYTile()) kill();
        if (e instanceof Bomb) return e.collide(this);
        return false;
    }

    private void chooseSprite() {
        switch (direct) {
            case 0 -> {
                sprite = Sprite.player_up;
                if (moving) sprite = Sprite.movingSprite(Sprite.player_up_1, Sprite.player_up_2, animate, 20);
            }
            case 2 -> {
                sprite = Sprite.player_down;
                if (moving) sprite = Sprite.movingSprite(Sprite.player_down_1, Sprite.player_down_2, animate, 20);
            }
            case 3 -> {
                sprite = Sprite.player_left;
                if (moving) sprite = Sprite.movingSprite(Sprite.player_left_1, Sprite.player_left_2, animate, 20);
            }
            default -> {
                sprite = Sprite.player_right;
                if (moving) sprite = Sprite.movingSprite(Sprite.player_right_1, Sprite.player_right_2, animate, 20);
            }
        }
    }

    public void setAuto(boolean q) {
        auto = q;
    }
}
