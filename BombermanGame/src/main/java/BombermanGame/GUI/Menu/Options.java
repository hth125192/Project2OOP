package BombermanGame.GUI.Menu;

import BombermanGame.GUI.Frame;
import BombermanGame.Game;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Options extends JMenu implements ChangeListener {
    Frame frame;

    public Options(Frame frame) {
        super("Options");
        this.frame = frame;

        add(new JLabel("Size"));
        JSlider sizeRange = new JSlider(JSlider.HORIZONTAL, 1, 5, 3);
        sizeRange.setMajorTickSpacing(1);
        sizeRange.setMinorTickSpacing(1);
        sizeRange.setPaintTicks(true);
        sizeRange.setPaintLabels(true);
        sizeRange.addChangeListener(this);
        add(sizeRange);
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        JSlider source = (JSlider)e.getSource();
        if (!source.getValueIsAdjusting()) {
            Game.SCALE_MULTIPLE = source.getValue();
            frame.gamePanel.changeSize();
            frame.revalidate();
            frame.pack();
        }
    }
}