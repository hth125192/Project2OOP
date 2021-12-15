package BombermanGame.GUI.Menu;

import BombermanGame.GUI.Frame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class Game extends JMenu {
    public Frame frame;

    public Game(Frame frame) {
        super("Game");
        this.frame = frame;

        JMenuItem newGame = new JMenuItem("New");
        newGame.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK));
        newGame.addActionListener(new MenuActionListener(frame));
        add(newGame);

        JMenuItem restart = new JMenuItem("Restart");
        restart.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.SHIFT_DOWN_MASK));
        restart.addActionListener(new MenuActionListener(frame));
        add(restart);

        JMenuItem pause = new JMenuItem("Pause");
        pause.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, KeyEvent.CTRL_DOWN_MASK));
        pause.addActionListener(new MenuActionListener(frame));
        add(pause);

        JMenuItem resume = new JMenuItem("Resume");
        resume.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, KeyEvent.CTRL_DOWN_MASK));
        resume.addActionListener(new MenuActionListener(frame));
        add(resume);
    }

    static class MenuActionListener implements ActionListener {
        public Frame frame;

        public MenuActionListener(Frame frame) {
            this.frame = frame;
        }

        @Override
        public void actionPerformed(ActionEvent event) {
            if (event.getActionCommand().equals("New")) frame.newGame();
            else if (event.getActionCommand().equals("Restart")) frame.restart();
            else if (event.getActionCommand().equals("Pause")) frame.pause();
            else if (event.getActionCommand().equals("Resume")) frame.resume();
        }
    }
}