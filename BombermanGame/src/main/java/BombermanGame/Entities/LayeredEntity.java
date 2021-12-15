package BombermanGame.Entities;

import BombermanGame.Entities.Tiles.Destroyable.DestroyableTile;
import BombermanGame.Graphics.Screen;

import java.util.LinkedList;

public class LayeredEntity extends Entity {
    protected LinkedList<Entity> entities = new LinkedList<>();

    public LayeredEntity(int x, int y, Entity ... entities) {
        this.x = x;
        this.y = y;

        for (int i = 0; i < entities.length; i++) {
            this.entities.add(entities[i]);
            if (i > 1)
                if (entities[i] instanceof DestroyableTile) ((DestroyableTile) entities[i]).addBelowSprite(entities[i - 1].getSprite());
        }
    }

    @Override
    public void update() {
        clearRemoved();
        getTopEntity().update();
    }

    @Override
    public void render(Screen screen) {
        getTopEntity().render(screen);
    }

    public Entity getTopEntity() {
        return entities.getLast();
    }

    private void clearRemoved() {
        if (getTopEntity().isRemoved()) entities.removeLast();
    }

    @Override
    public boolean collide(Entity e) {
        return false;
    }
}