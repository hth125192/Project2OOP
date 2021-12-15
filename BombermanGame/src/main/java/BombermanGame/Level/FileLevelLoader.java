package BombermanGame.Level;

import BombermanGame.Board;
import BombermanGame.Entities.Characters.Bomber;
import BombermanGame.Entities.Characters.Enemies.*;
import BombermanGame.Entities.LayeredEntity;
import BombermanGame.Entities.Tiles.Destroyable.Brick;
import BombermanGame.Entities.Tiles.Grass;
import BombermanGame.Entities.Tiles.Items.BombItem;
import BombermanGame.Entities.Tiles.Items.FlameItem;
import BombermanGame.Entities.Tiles.Items.SpeedItem;
import BombermanGame.Entities.Tiles.Portal;
import BombermanGame.Entities.Tiles.Wall;
import BombermanGame.Exceptions.LoadLevelException;
import BombermanGame.Game;
import BombermanGame.Graphics.Sprite;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class FileLevelLoader extends LevelLoader {
    private static char[][] map;

    public FileLevelLoader(Board board, int level) throws LoadLevelException {
        super(board, level);
    }

    @Override
    public void loadLevel(int level) {
        try {
            Class<?> c = Class.forName("BombermanGame.Level.FileLevelLoader");
            InputStream stream = c.getResourceAsStream("/Utils/Levels/level" + level + ".txt");
            Reader r = new InputStreamReader(Objects.requireNonNull(stream), StandardCharsets.UTF_8);
            BufferedReader br = new BufferedReader(r);

            String line;
            line = br.readLine();
            String[] sizes = line.split("\\s");
            this.level = Integer.parseInt(sizes[0]);
            height = Integer.parseInt(sizes[1]);
            width = Integer.parseInt(sizes[2]);
            map = new char[width][height];

            int rowNum = 0;
            while ((line = br.readLine()) != null) {
                for (int i = 0; i < line.length(); i++) map[i][rowNum] = line.charAt(i);
                rowNum++;
            }

            stream.close();
            br.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createEntities() {
        Game.levelHeight = height;
        Game.levelWidth = width;

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                int pos = x + y * width;
                switch (map[x][y]) {
                    case 'p' -> {
                        board.addEntity(pos, new Grass(x, y, Sprite.grass));
                        board.addCharacter(new Bomber(Coordinates.tileToPixel(x), Coordinates.tileToPixel(y) + Game.TILE_SIZE, board));
                    }
                    case '#' -> board.addEntity(pos, new Wall(x, y, Sprite.wall));
                    case '*' -> board.addEntity(pos, new LayeredEntity(x, y, new Grass(x, y, Sprite.grass), new Brick(x, y, Sprite.brick)));
                    case 'x' -> board.addEntity(pos, new LayeredEntity(x, y, new Grass(x, y, Sprite.grass), new Portal(x, y, Sprite.portal), new Brick(x, y, Sprite.brick)));
                    case 'b' -> board.addEntity(pos, new LayeredEntity(x, y, new Grass(x, y, Sprite.grass), new BombItem(x, y, Sprite.powerup_bombs), new Brick(x, y, Sprite.brick)));
                    case 'f' -> board.addEntity(pos, new LayeredEntity(x, y, new Grass(x, y, Sprite.grass), new FlameItem(x, y, Sprite.powerup_flames), new Brick(x, y, Sprite.brick)));
                    case 's' -> board.addEntity(pos, new LayeredEntity(x, y, new Grass(x, y, Sprite.grass), new SpeedItem(x, y, Sprite.powerup_speed), new Brick(x, y, Sprite.brick)));
                    case '1' -> {
                        board.addEntity(pos, new Grass(x, y, Sprite.grass));
                        board.addCharacter(new Balloom(Coordinates.tileToPixel(x), Coordinates.tileToPixel(y) + Game.TILE_SIZE, board));
                    }
                    case '2' -> {
                        board.addEntity(pos, new Grass(x, y, Sprite.grass));
                        board.addCharacter(new Oneal(Coordinates.tileToPixel(x), Coordinates.tileToPixel(y) + Game.TILE_SIZE, board));
                    }
                    case '3' -> {
                        board.addEntity(pos, new Grass(x, y, Sprite.grass));
                        board.addCharacter(new Doll(Coordinates.tileToPixel(x), Coordinates.tileToPixel(y) + Game.TILE_SIZE, board));
                    }
                    case '4' -> {
                        board.addEntity(pos, new Grass(x, y, Sprite.grass));
                        board.addCharacter(new Minvo(Coordinates.tileToPixel(x), Coordinates.tileToPixel(y) + Game.TILE_SIZE, board));
                    }
                    case '5' -> {
                        board.addEntity(pos, new Grass(x, y, Sprite.grass));
                        board.addCharacter(new Ghost(Coordinates.tileToPixel(x), Coordinates.tileToPixel(y) + Game.TILE_SIZE, board));
                    }
                    case '6' -> {
                        board.addEntity(pos, new Grass(x, y, Sprite.grass));
                        board.addCharacter(new Kondoria(Coordinates.tileToPixel(x), Coordinates.tileToPixel(y) + Game.TILE_SIZE, board));
                    }
                    default -> board.addEntity(pos, new Grass(x, y, Sprite.grass));
                }
            }
        }
    }
}