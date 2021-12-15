package BombermanGame.GUI;

import BombermanGame.Game;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {
    private final Game game;

    public GamePanel(Frame frame) {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(Game.WIDTH * Game.SCALE_MULTIPLE, Game.HEIGHT * Game.SCALE_MULTIPLE));

        game = new Game(frame);
        add(game);
        game.setVisible(true);

        setVisible(true);
        setFocusable(true);
    }

    public void changeSize() {
        setPreferredSize(new Dimension(Game.WIDTH * Game.SCALE_MULTIPLE, Game.HEIGHT * Game.SCALE_MULTIPLE));
        revalidate();
        repaint();
    }

    public Game getGame() {
        return game;
    }
}