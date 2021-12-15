package BombermanGame.Entities.Bomb;

import BombermanGame.Board;
import BombermanGame.Entities.Characters.Character;
import BombermanGame.Entities.Entity;
import BombermanGame.Entities.LayeredEntity;
import BombermanGame.Entities.Tiles.Destroyable.DestroyableTile;
import BombermanGame.Entities.Tiles.Wall;
import BombermanGame.Graphics.Screen;

public class Flame extends Entity {
    protected Board board;
    protected int direction;
    protected int xOrigin, yOrigin;
    protected FlameSegment[] flameSegments = new FlameSegment[0];
    private final int radius;

    public Flame(int x, int y, int direction, int radius, Board board) {
        xOrigin = x;
        yOrigin = y;
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.radius = radius;
        this.board = board;
        createFlameSegments();
    }

    private void createFlameSegments() {
        boolean last = false;
        int length = calculatePermitedDistance();
        flameSegments = new FlameSegment[length];
        for (int i = 0; i < length; i++) {
            if (i == calculatePermitedDistance() - 1) last = true;
            if (direction == 0) {
                flameSegments[i] = new FlameSegment(xOrigin, yOrigin - i, direction, last, board);
                flameSegments[i].collide(board.getCharacterAtExcluding(xOrigin, yOrigin - i, null));
                flameSegments[i].collide(board.getBombAt(xOrigin, yOrigin - i));
            } else if (direction == 1) {
                flameSegments[i] = new FlameSegment(xOrigin + i, yOrigin, direction, last, board);
                flameSegments[i].collide(board.getCharacterAtExcluding(xOrigin + i, yOrigin, null));
                flameSegments[i].collide(board.getBombAt(xOrigin + i, yOrigin));
            } else if (direction == 2) {
                flameSegments[i] = new FlameSegment(xOrigin, yOrigin + i, direction, last, board);
                flameSegments[i].collide(board.getCharacterAtExcluding(xOrigin, yOrigin + i, null));
                flameSegments[i].collide(board.getBombAt(xOrigin, yOrigin + i));
            } else {
                flameSegments[i] = new FlameSegment(xOrigin - i, yOrigin, direction, last, board);
                flameSegments[i].collide(board.getCharacterAtExcluding(xOrigin - i, yOrigin, null));
                flameSegments[i].collide(board.getBombAt(xOrigin - i, yOrigin));
            }
        }
    }

    private int calculatePermitedDistance() {
        if (direction == 0) {
            for (int i = 0; i < radius; i++) {
                Entity barrier = board.getEntityAt(xOrigin, yOrigin - i);
                if (barrier instanceof Wall) return i;
                if (barrier instanceof LayeredEntity) {
                    Entity top = ((LayeredEntity) barrier).getTopEntity();
                    if (top instanceof DestroyableTile) {
                        if (!((DestroyableTile) top).isDestroyed()) {
                            ((DestroyableTile) top).destroy();
                            return i;
                        }
                    }
                }
            }
        }
        if (direction == 1) {
            for (int i = 0; i < radius; i++) {
                Entity barrier = board.getEntityAt(xOrigin + i, yOrigin);
                if (barrier instanceof Wall) return i;
                if (barrier instanceof LayeredEntity) {
                    Entity top = ((LayeredEntity) barrier).getTopEntity();
                    if (top instanceof DestroyableTile) {
                        if (!((DestroyableTile) top).isDestroyed()) {
                            ((DestroyableTile) top).destroy();
                            return i;
                        }
                    }
                }
            }
        }
        if (direction == 2) {
            for (int i = 0; i < radius; i++) {
                Entity barrier = board.getEntityAt(xOrigin, yOrigin + i);
                if (barrier instanceof Wall) return i;
                if (barrier instanceof LayeredEntity) {
                    Entity top = ((LayeredEntity) barrier).getTopEntity();
                    if (top instanceof DestroyableTile) {
                        if (!((DestroyableTile) top).isDestroyed()) {
                            ((DestroyableTile) top).destroy();
                            return i;
                        }
                    }
                }
            }
        }
        if (direction == 3) {
            for (int i = 0; i < radius; i++) {
                Entity barrier = board.getEntityAt(xOrigin - i, yOrigin);
                if (barrier instanceof Wall) return i;
                if (barrier instanceof LayeredEntity) {
                    Entity top = ((LayeredEntity) barrier).getTopEntity();
                    if (top instanceof DestroyableTile) {
                        if (!((DestroyableTile) top).isDestroyed()) {
                            ((DestroyableTile) top).destroy();
                            return i;
                        }
                    }
                }
            }
        }
        return radius;
    }

    public FlameSegment flameSegmentAt(int x, int y) {
        for (FlameSegment flameSegment : flameSegments)
            if (flameSegment.getX() == x && flameSegment.getY() == y) return flameSegment;
        return null;
    }

    @Override
    public void update() {
        for (FlameSegment flameSegment : flameSegments) flameSegment.update();
    }

    @Override
    public void render(Screen screen) {
        for (FlameSegment flameSegment : flameSegments) flameSegment.render(screen);
    }

    @Override
    public boolean collide(Entity e) {
        if (e instanceof Character) ((Character) e).kill();
        return false;
    }
}