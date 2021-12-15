package BombermanGame.GUI;

import BombermanGame.GUI.Menu.Menu;
import BombermanGame.Game;

import javax.swing.*;
import java.awt.*;

public class Frame extends JFrame {
    public GamePanel gamePanel;
    private final InfoPanel infoPanel;

    private final Game game;

    public Frame() {
        setJMenuBar(new Menu(this));

        JPanel containerPanel = new JPanel(new BorderLayout());
        gamePanel = new GamePanel(this);
        infoPanel = new InfoPanel(gamePanel.getGame());

        containerPanel.add(infoPanel, BorderLayout.PAGE_START);
        containerPanel.add(gamePanel, BorderLayout.PAGE_END);

        game = gamePanel.getGame();

        add(containerPanel);

        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        game.start();
    }

    public void changeLevel(int i) {
        game.getBoard().loadLevel(i);
    }

    public void newGame() {
        game.getBoard().newGame();
    }

    public void restart() {
        game.getBoard().restart();
    }

    public void pause() {
        game.getBoard().gamePause();
    }

    public void resume() {
        game.getBoard().gameResume();
    }

    public void setTime(int time) {
        infoPanel.setTime(time);
    }

    public void setLives(int lives) {
        infoPanel.setLives(lives);
    }

    public void setPoints(int points) {
        infoPanel.setPoints(points);
    }

    public void auto() {
        game.getBoard().getBomber().setAuto(true);
    }

    public void manual() {
        game.getBoard().getBomber().setAuto(false);
    }
}