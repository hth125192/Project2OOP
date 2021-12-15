package BombermanGame.GUI.Menu;

import BombermanGame.GUI.Frame;

import javax.swing.*;

public class Menu extends JMenuBar {
    public Menu(Frame frame) {
        add(new Game(frame));
        add(new Player(frame));
        add(new Level(frame));
        add(new Options(frame));
    }
}