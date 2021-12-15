package BombermanGame.GUI;

import BombermanGame.Game;

import javax.swing.*;
import java.awt.*;

public class InfoPanel extends JPanel {
    private final JLabel timeLabel;
    private final JLabel pointsLabel;
    private final JLabel livesLabel;

    public InfoPanel(Game game) {
        setLayout(new GridLayout());

        timeLabel = new JLabel("Time: " + game.getBoard().getTime());
        timeLabel.setForeground(Color.white);
        timeLabel.setHorizontalAlignment(JLabel.CENTER);

        pointsLabel = new JLabel("Score: " + game.getBoard().getPoints());
        pointsLabel.setForeground(Color.white);
        pointsLabel.setHorizontalAlignment(JLabel.CENTER);

        livesLabel = new JLabel("Lives: " + game.getBoard().getLives());
        livesLabel.setForeground(Color.white);
        livesLabel.setHorizontalAlignment(JLabel.CENTER);

        add(timeLabel);
        add(pointsLabel);
        add(livesLabel);

        setBackground(Color.black);
        setPreferredSize(new Dimension(0, 40));
    }

    public void setTime(int t) {
        timeLabel.setText("Time: " + t);
    }

    public void setPoints(int t) {
        pointsLabel.setText("Score: " + t);
    }

    public void setLives(int t) {
        livesLabel.setText("Lives: " + t);
    }
}