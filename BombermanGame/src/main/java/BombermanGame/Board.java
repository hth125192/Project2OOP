package BombermanGame;

import BombermanGame.Entities.Bomb.Bomb;
import BombermanGame.Entities.Bomb.FlameSegment;
import BombermanGame.Entities.Characters.Bomber;
import BombermanGame.Entities.Characters.Character;
import BombermanGame.Entities.Characters.Enemies.*;
import BombermanGame.Entities.Entity;
import BombermanGame.Entities.LayeredEntity;
import BombermanGame.Entities.Message;
import BombermanGame.Entities.Tiles.Destroyable.Brick;
import BombermanGame.Entities.Tiles.Grass;
import BombermanGame.Entities.Tiles.Items.BombItem;
import BombermanGame.Entities.Tiles.Items.FlameItem;
import BombermanGame.Entities.Tiles.Items.SpeedItem;
import BombermanGame.Entities.Tiles.Portal;
import BombermanGame.Entities.Tiles.Wall;
import BombermanGame.Exceptions.LoadLevelException;
import BombermanGame.Graphics.IRender;
import BombermanGame.Graphics.Screen;
import BombermanGame.Input.Keyboard;
import BombermanGame.Level.FileLevelLoader;
import BombermanGame.Level.LevelLoader;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Board implements IRender {
    protected LevelLoader levelLoader;
    protected Game game;
    protected Keyboard input;
    protected Screen screen;

    public Entity[] entities;
    public List<Character> characters = new ArrayList<>();
    protected List<Bomb> bombs = new ArrayList<>();
    private final List<Message> messages = new ArrayList<>();

    private int screenToShow = -1;

    private int time = Game.TIME;
    private int points = Game.POINTS;
    private int lives = Game.LIVES;

    private final char[][] map;

    public Board(Game game, Keyboard input, Screen screen) {
        this.game = game;
        this.input = input;
        this.screen = screen;
        loadLevel(1);
        map = new char[levelLoader.getWidth()][levelLoader.getHeight()];
    }

    @Override
    public void update() {
        if (game.isPaused()) return;
        updateMap();
        updateEntities();
        updateCharacters();
        updateBombs();
        updateMessages();
        detectEndGame();

        for (int i = 0; i < characters.size(); i++) {
            Character a = characters.get(i);
            if (a.isRemoved()) characters.remove(i);
        }
    }

    @Override
    public void render(Screen screen) {
        if (game.isPaused()) return;

        int x0 = Screen.xOffset >> 4;
        int x1 = (Screen.xOffset + screen.getWidth() + Game.TILE_SIZE) / Game.TILE_SIZE;
        int y0 = Screen.yOffset >> 4;
        int y1 = (Screen.yOffset + screen.getHeight()) / Game.TILE_SIZE;

        for (int y = y0; y < y1; y++)
            for (int x = x0; x < x1; x++) entities[x + y * levelLoader.getWidth()].render(screen);

        renderBombs(screen);
        renderCharacter(screen);
    }

    public void nextLevel() {
        loadLevel(levelLoader.getLevel() + 1);
    }

    public void loadLevel(int level) {
        time = Game.TIME;
        screenToShow = 2;
        game.resetScreenDelay();
        game.pause();
        characters.clear();
        bombs.clear();
        messages.clear();

        resetPropertiesButKeepScore();
        Screen.setOffset(0, 0);
        Game.playSE(7);
        try {
            levelLoader = new FileLevelLoader(this, level);
            entities = new Entity[levelLoader.getHeight() * levelLoader.getWidth()];
            levelLoader.createEntities();
        } catch (LoadLevelException e) {
            endGame();
        } catch (NullPointerException e) {
            finishGame();
        }
    }

    protected void detectEndGame() {
        if (time <= 0) endGame();
    }

    public void newGame() {
        loadLevel(1);
        resetProperties();
        lives = Game.LIVES;
    }

    public void restart() {
        if (lives > 0) {
            loadLevel(levelLoader.getLevel());
            resetProperties();
        }
    }

    public void endGame() {
        screenToShow = 1;
        game.resetScreenDelay();
        Game.playSE(9);
        game.pause();
    }

    public void finishGame() {
        screenToShow = 4;
        game.resetScreenDelay();
        Game.playSE(8);
        game.pause();
    }

    public void gamePause() {
        game.resetScreenDelay();
        if (screenToShow <= 0) screenToShow = 3;
        game.pause();
    }

    public void gameResume() {
        game.resetScreenDelay();
        screenToShow = -1;
        game.resume();
    }

    private void resetProperties() {
        points = Game.POINTS;
        Game.bomberSpeed = 1.0;
        Game.bombRadius = 1;
        Game.bombRate = 1;
    }

    private void resetPropertiesButKeepScore() {
        Game.bomberSpeed = 1.0;
        Game.bombRadius = 1;
        Game.bombRate = 1;
    }

    public boolean detectNoEnemies() {
        int total = 0;
        for (Character character : characters)
            if (!(character instanceof Bomber)) total++;
        return total == 0;
    }

    public void drawScreen(Graphics g) {
        switch (screenToShow) {
            case 1 -> screen.drawEndGame(g, points);
            case 2 -> screen.drawChangeLevel(g, levelLoader.getLevel());
            case 3 -> screen.drawPaused(g);
            case 4 -> screen.drawFinishGame(g, points);
        }
    }

    public Entity getEntity(double x, double y, Character m) {
        Entity res;

        res = getFlameSegmentAt((int) x, (int) y);
        if (res != null) return res;

        res = getBombAt(x, y);
        if (res != null) return res;

        res = getCharacterAtExcluding((int) x, (int) y, m);
        if (res != null) return res;

        res = getEntityAt((int) x, (int) y);

        return res;
    }

    public List<Bomb> getBombs() {
        return bombs;
    }

    public Bomb getBombAt(double x, double y) {
        Iterator<Bomb> bs = bombs.iterator();
        Bomb b;
        while (bs.hasNext()) {
            b = bs.next();
            if (b.getX() == (int) x && b.getY() == (int) y) return b;
        }
        return null;
    }

    public Bomber getBomber() {
        Iterator<Character> itr = characters.iterator();
        Character cur;
        while (itr.hasNext()) {
            cur = itr.next();
            if (cur instanceof Bomber) return (Bomber) cur;
        }
        return null;
    }

    public Character getCharacterAtExcluding(int x, int y, Character a) {
        Iterator<Character> itr = characters.iterator();
        Character cur;
        while (itr.hasNext()) {
            cur = itr.next();
            if (cur == a) continue;
            if (cur.getXTile() == x && cur.getYTile() == y) return cur;
        }
        return null;
    }

    public FlameSegment getFlameSegmentAt(int x, int y) {
        Iterator<Bomb> bs = bombs.iterator();
        Bomb b;
        while (bs.hasNext()) {
            b = bs.next();
            FlameSegment e = b.flameAt(x, y);
            if (e != null) return e;
        }
        return null;
    }

    public Entity getEntityAt(double x, double y) {
        return entities[(int) x + (int) y * levelLoader.getWidth()];
    }

    public void addEntity(int pos, Entity e) {
        entities[pos] = e;
    }

    public void addCharacter(Character e) {
        characters.add(e);
    }

    public void addBomb(Bomb e) {
        bombs.add(e);
    }

    public void addMessage(Message e) {
        messages.add(e);
    }

    protected void renderCharacter(Screen screen) {
        for (Character character : characters) character.render(screen);
    }

    protected void renderBombs(Screen screen) {
        for (Bomb bomb : bombs) bomb.render(screen);
    }

    public void renderMessages(Graphics g) {
        Message m;
        for (Message message : messages) {
            m = message;
            g.setFont(new Font("Arial", Font.PLAIN, m.getSize()));
            g.setColor(m.getColor());
            g.drawString(m.getMessage(), (int) m.getX() - Screen.xOffset * Game.SCALE_MULTIPLE, (int) m.getY());
        }
    }

    protected void updateEntities() {
        if (game.isPaused()) return;
        for (Entity entity : entities) entity.update();
    }

    protected void updateCharacters() {
        if (game.isPaused()) return;
        Iterator<Character> itr = characters.iterator();
        while (itr.hasNext() && !game.isPaused()) itr.next().update();
    }

    protected void updateBombs() {
        if (game.isPaused()) return;
        for (Bomb bomb : bombs) bomb.update();
    }

    protected void updateMessages() {
        if (game.isPaused()) return;
        Message m;
        int left;
        for (int i = 0; i < messages.size(); i++) {
            m = messages.get(i);
            left = m.getDuration();
            if (left > 0) m.setDuration(--left);
            else messages.remove(i);
        }
    }

    public int subtractTime() {
        if (game.isPaused()) return this.time;
        else return this.time--;
    }

    public Keyboard getInput() {
        return input;
    }

    public LevelLoader getLevel() {
        return levelLoader;
    }

    public Game getGame() {
        return game;
    }

    public int getShow() {
        return screenToShow;
    }

    public void setShow(int i) {
        screenToShow = i;
    }

    public int getTime() {
        return time;
    }

    public int getPoints() {
        return points;
    }

    public int getLives() {
        return lives;
    }

    public void addPoints(int points) {
        this.points += points;
    }

    public void addLives(int lives) {
        this.lives += lives;
    }

    public int getWidth() {
        return levelLoader.getWidth();
    }


    private char revive(Entity e) {
        if (e instanceof Wall) return '#';
        else if (e instanceof Grass) return ' ';
        else if (e instanceof LayeredEntity) {
            Entity top = ((LayeredEntity) e).getTopEntity();
            if (top instanceof Portal) return 'x';
            else if (top instanceof SpeedItem) return 's';
            else if (top instanceof BombItem) return 'b';
            else if (top instanceof FlameItem) return 'f';
            else if (top instanceof Brick) return '*';
            else return ' ';
        } else if (e instanceof Character) {
            if (e instanceof Bomber) {
                if (getEntity(e.getXTile(), e.getYTile(), (Bomber) e) instanceof Bomb) return '8';
                return 'p';
            } else if (e instanceof Balloom) return '1';
            else if (e instanceof Oneal) return '2';
            else if (e instanceof Doll) return '3';
            else if (e instanceof Minvo) return '4';
            else if (e instanceof Ghost) return '5';
            else if (e instanceof Kondoria) return '6';
            else return 'p';
        } else if (e instanceof Bomb) {
            Bomber b = getBomber();
            if (b.getXTile() == e.getX() && b.getYTile() == e.getY()) return '8';
            return '7';
        } else return ' ';
    }

    private void updateMap() {
        for (int h = 0; h < levelLoader.getHeight(); h++)
            for (int w = 0; w < levelLoader.getWidth(); w++) map[w][h] = revive(getEntity(w, h, null));

    }

    public char[][] reviveMap() {
        if (map != null) updateMap();
        return map;
    }
}