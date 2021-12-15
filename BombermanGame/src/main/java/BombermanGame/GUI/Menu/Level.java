package BombermanGame.GUI.Menu;

import BombermanGame.GUI.Frame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class Level extends JMenu {
    Frame frame;

    public Level(Frame frame) {
        super("Level");
        this.frame = frame;

        JMenuItem level1 = new JMenuItem("Level 1");
        level1.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, KeyEvent.CTRL_DOWN_MASK));
        level1.addActionListener(new MenuActionListener(frame));
        add(level1);

        JMenuItem level2 = new JMenuItem("Level 2");
        level2.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_2, KeyEvent.CTRL_DOWN_MASK));
        level2.addActionListener(new MenuActionListener(frame));
        add(level2);

        JMenuItem level3 = new JMenuItem("Level 3");
        level3.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_3, KeyEvent.CTRL_DOWN_MASK));
        level3.addActionListener(new MenuActionListener(frame));
        add(level3);

        JMenuItem level4 = new JMenuItem("Level 4");
        level4.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_4, KeyEvent.CTRL_DOWN_MASK));
        level4.addActionListener(new MenuActionListener(frame));
        add(level4);

        JMenuItem level5 = new JMenuItem("Level 5");
        level5.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_5, KeyEvent.CTRL_DOWN_MASK));
        level5.addActionListener(new MenuActionListener(frame));
        add(level5);
    }

    static class MenuActionListener implements ActionListener {
        public Frame frame;

        public MenuActionListener(Frame frame) {
            this.frame = frame;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand().equals("Level 1")) frame.changeLevel(1);
            else if (e.getActionCommand().equals("Level 2")) frame.changeLevel(2);
            else if (e.getActionCommand().equals("Level 3")) frame.changeLevel(3);
            else if (e.getActionCommand().equals("Level 4")) frame.changeLevel(4);
            else if (e.getActionCommand().equals("Level 5")) frame.changeLevel(5);
        }
    }
}