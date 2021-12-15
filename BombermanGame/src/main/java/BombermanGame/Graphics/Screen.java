package BombermanGame.Graphics;

import BombermanGame.Board;
import BombermanGame.Entities.Characters.Bomber;
import BombermanGame.Entities.Entity;
import BombermanGame.Game;

import java.awt.*;
import java.util.Arrays;

public class Screen {
    private final int TRANSPARENT_COLOR = 0xffff00ff;
    protected int width, height;
    public int[] pixels;

    public static int xOffset = 0, yOffset = 0;

    public Screen(int width, int height) {
        this.width = width;
        this.height = height;
        pixels = new int[width * height];
    }

    public void clear() {
        Arrays.fill(pixels, 0);
    }

    public void renderEntity(int xp, int yp, Entity entity) {
        xp -= xOffset;
        yp -= yOffset;
        for (int y = 0; y < entity.getSprite().getSize(); y++) {
            int ya = y + yp;
            for (int x = 0; x < entity.getSprite().getSize(); x++) {
                int xa = x + xp;
                if (xa < - entity.getSprite().getSize() || xa >= width || ya < 0 || ya >= height) break;
                if (xa < 0) xa = 0;
                int color = entity.getSprite().getPixel(x + y * entity.getSprite().getSize());
                if (color != TRANSPARENT_COLOR) pixels[xa + ya * width] = color;
            }
        }
    }

    public void renderEntityWithBelowSprite(int xp, int yp, Entity entity, Sprite below) {
        xp -= xOffset;
        yp -= yOffset;
        for (int y = 0; y < entity.getSprite().getSize(); y++) {
            int ya = y + yp;
            for (int x = 0; x < entity.getSprite().getSize(); x++) {
                int xa = x + xp;
                if (xa < - entity.getSprite().getSize() || xa >= width || ya < 0 || ya >= height) break;
                if (xa < 0) xa = 0;
                int color = entity.getSprite().getPixel(x + y * entity.getSprite().getSize());
                if(color != TRANSPARENT_COLOR) pixels[xa + ya * width] = color;
                else pixels[xa + ya * width] = below.getPixel(x + y * below.getSize());
            }
        }
    }

    public static void setOffset(int xO, int yO) {
        xOffset = xO;
        yOffset = yO;
    }

    public static int calculateXOffset(Board board, Bomber bomber) {
        if (bomber == null) return 0;
        int temp = xOffset;
        double bomberX = bomber.getX() / 16;
        double complement = 0.5;
        int firstBreakpoint = board.getWidth() / 4;
        int lastBreakpoint = board.getWidth() - firstBreakpoint;
        if (bomberX > firstBreakpoint + complement && bomberX < lastBreakpoint - complement) temp = (int) bomber.getX()  - (Game.WIDTH / 2);
        return temp;
    }

    /* Vẽ các màn hình game */
    public void drawEndGame(Graphics g, int points) {
        g.setColor(Color.black);
        g.fillRect(0, 0, getRealWidth(), getRealHeight());

        Font font = new Font("Arial", Font.PLAIN, 20 * Game.SCALE_MULTIPLE);
        g.setFont(font);
        g.setColor(Color.white);
        drawCenteredString("GAME OVER", getRealWidth(), getRealHeight(), g);

        font = new Font("Arial", Font.PLAIN, 10 * Game.SCALE_MULTIPLE);
        g.setFont(font);
        g.setColor(Color.yellow);
        drawCenteredString("SCORE: " + points, getRealWidth(), getRealHeight() + (Game.TILE_SIZE * 2) * Game.SCALE_MULTIPLE, g);
    }

    public void drawChangeLevel(Graphics g, int level) {
        g.setColor(Color.black);
        g.fillRect(0, 0, getRealWidth(), getRealHeight());

        Font font = new Font("Arial", Font.PLAIN, 20 * Game.SCALE_MULTIPLE);
        g.setFont(font);
        g.setColor(Color.white);
        drawCenteredString("LEVEL " + level, getRealWidth(), getRealHeight(), g);
    }

    public void drawPaused(Graphics g) {
        Font font = new Font("Arial", Font.PLAIN, 20 * Game.SCALE_MULTIPLE);
        g.setFont(font);
        g.setColor(Color.white);
        drawCenteredString("PAUSED", getRealWidth(), getRealHeight(), g);
    }

    public void drawFinishGame(Graphics g, int points) {
        g.setColor(Color.black);
        g.fillRect(0, 0, getRealWidth(), getRealHeight());

        Font font = new Font("Arial", Font.PLAIN, 20 * Game.SCALE_MULTIPLE);
        g.setFont(font);
        g.setColor(Color.white);
        drawCenteredString("VICTORY", getRealWidth(), getRealHeight(), g);

        font = new Font("Arial", Font.PLAIN, 10 * Game.SCALE_MULTIPLE);
        g.setFont(font);
        g.setColor(Color.yellow);
        drawCenteredString("SCORE: " + points, getRealWidth(), getRealHeight() + (Game.TILE_SIZE * 2) * Game.SCALE_MULTIPLE, g);
    }

    public void drawCenteredString(String s, int w, int h, Graphics g) {
        FontMetrics fm = g.getFontMetrics();
        g.drawString(s, (w - fm.stringWidth(s)) / 2, (fm.getAscent() + (h - (fm.getAscent() + fm.getDescent())) / 2));
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getRealWidth() {
        return width * Game.SCALE_MULTIPLE;
    }

    public int getRealHeight() {
        return height * Game.SCALE_MULTIPLE;
    }
}