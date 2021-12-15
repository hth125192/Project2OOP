package BombermanGame;

import BombermanGame.GUI.Frame;
import BombermanGame.Graphics.Screen;
import BombermanGame.Input.Keyboard;
import BombermanGame.Sound.Sound;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class Game extends Canvas {
    //Thông số game
    public static final String TITLE = "Bomberman";
    public static final int TILE_SIZE = 16;
    public static final int WIDTH = TILE_SIZE * (31 / 2), HEIGHT = 13 * TILE_SIZE;
    public static int SCALE_MULTIPLE = 3;

    //Thông số mặc định của người chơi
    private static final int BOMB_RATE = 1;
    private static final int BOMB_RADIUS = 1;
    private static final double BOMBER_SPEED = 1.0;

    //Thông số mặc định của game
    public static final int TIME = 200;
    public static final int POINTS = 0;
    public static final int LIVES = 3;

    protected static int SCREEN_DELAY = 3;

    //Thông số riêng của người chơi, có thể thay đổi khi ăn được các item
    protected static int bombRate = BOMB_RATE;
    protected static int bombRadius = BOMB_RADIUS;
    protected static double bomberSpeed = BOMBER_SPEED;

    //Thời gian trong một level
    protected int screenDelay = SCREEN_DELAY;

    public static int levelWidth;
    public static int levelHeight;

    private final Keyboard input;
    static Sound sound = new Sound();

    //Trạng thái game
    private boolean running = false;
    private boolean paused = true;

    //Sử dụng để render game
    private final BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
    private final int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();

    private final Board board;
    private final Screen screen;
    private final Frame frame;

    public Game(Frame frame) {
        this.frame = frame;
        this.frame.setTitle(TITLE);

        input = new Keyboard();
        screen = new Screen(WIDTH, HEIGHT);

        board = new Board(this, input, screen);
        addKeyListener(input);
    }

    //Render game, cài đặt để tạo ra FPS lớn nhất có thể tương thích với máy
    private void renderGame() {
        BufferStrategy bs = getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(3);
            return;
        }

        screen.clear();
        board.render(screen);

        System.arraycopy(screen.pixels, 0, pixels, 0, pixels.length);

        Graphics g = bs.getDrawGraphics();
        g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
        board.renderMessages(g);

        g.dispose();
        bs.show();
    }

    private void renderScreen() {
        BufferStrategy bs = getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(3);
            return;
        }

        screen.clear();

        Graphics g = bs.getDrawGraphics();
        board.drawScreen(g);

        g.dispose();
        bs.show();
    }

    //Update các thông số game
    private void update() {
        input.update();
        board.update();
    }

    //Start game
    public void start() {
        playMusic(0);

        running = true;

        long lastTime = System.nanoTime();
        long timer = System.currentTimeMillis();
        final double nps = 1000000000.0 / 60.0;
        double delta = 0;
        int frames = 0, updates = 0;

        requestFocus();

        while (running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / nps;
            lastTime = now;
            while (delta > 1) {
                update();
                updates++;
                delta--;
            }

            if (paused) {
                if (screenDelay <= 0) {
                    board.setShow(-1);
                    paused = false;
                }
                renderScreen();
            } else renderGame();

            frames++;

            if (System.currentTimeMillis() - timer > 1000) {
                frame.setTime(board.subtractTime());
                frame.setPoints(board.getPoints());
                frame.setLives(board.getLives());

                timer += 1000;

                this.frame.setTitle("Bomberman" + " | " + updates + " rate, " + frames + " fps");
                updates = 0;
                frames = 0;

                if (this.board.getShow() == 2) screenDelay--;
            }
        }
    }

    public static int getBombRate() {
        return bombRate;
    }

    public static int getBombRadius() {
        return bombRadius;
    }

    public static double getBomberSpeed() {
        return bomberSpeed;
    }

    public static void addBomberSpeed(double i) {
        bomberSpeed += i;
    }

    public static void addBombRadius(int i) {
        bombRadius += i;
    }

    public static void addBombRate(int i) {
        bombRate += i;
    }

    public void resetScreenDelay() {
        screenDelay = SCREEN_DELAY;
    }

    public Board getBoard() {
        return board;
    }

    public void resume() {
        this.paused = false;
        screenDelay = -1;
    }

    public boolean isPaused() {
        return paused;
    }

    public void pause() {
        paused = true;
    }

    public void playMusic(int i) {
        sound.setFile(i);
        sound.play();
        sound.loop();
    }

    public static void playSE(int i) {
        sound.setFile(i);
        sound.play();
    }
}