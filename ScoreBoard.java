import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JPanel;

// Displays the title and current score of 2048 game.
public class ScoreBoard extends JPanel {

    private int score;
    private int bestScore;
    private Font gameFont;
    private ArrayList<Integer> previousScores;

    // Constructs ScoreBoard using given Font.
    public ScoreBoard(Font gameFont) {
        this.gameFont = gameFont;
        setPreferredSize(new Dimension(125, 200));
        Scanner previousScoresInput = null;
        try {
            previousScoresInput = new Scanner(new File("Scores.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        previousScores = new ArrayList<>();
        while (previousScoresInput.hasNextInt()) {
            previousScores.add(previousScoresInput.nextInt());
        }
        bestScore = previousScores.size() == 0 ? 0 : previousScores.get(0);
        for (Integer previousScore : previousScores) {
            if (previousScore > bestScore) {
                bestScore = previousScore;
            }
        }
    }

    // Increases score by given amount and repaints board.
    public void updateScore(int scoreUpdate) {
        score += scoreUpdate;
        repaint();
    }

    // Resets score for next round and changes best score if this score beat it.
    public void gameOver() {
        if (score > bestScore) {
            bestScore = score;
        }
        PrintStream previousScoresOutput = null;
        try {
            previousScoresOutput = new PrintStream(new File("Scores.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        for (Integer previousScore : previousScores) {
            previousScoresOutput.println(previousScore);
        }
        previousScoresOutput.println(score);
        score = 0;
    }

    // Draws the score board design and updates new score upon repaint.
    public void paint(Graphics g) {
        super.paint(g);

        // Initialization of constants.
        // Colors that are used to paint score board.
        final Color BACKGROUND_COLOR = new Color(251, 248, 239);
        final Color BACKGROUND_COLOR_TITLE = new Color(238, 194, 46);
        final Color BACKGROUND_COLOR_SCORE = new Color(188, 172, 158);
        final Color FONT_COLOR_TAN = new Color(237, 226, 213);
        final Color FONT_COLOR_WHITE = new Color(250, 247, 241);

        // Used for positioning of score board shapes.
        final int ARC = 10;
        final int FONT_Y_VALUE_SCORE = 132;
        final int FONT_Y_VALUE_SCORE_TITLE = 92;
        final int TILE_X_VALUE_BEST_SCORE = 485;
        final int TILE_X_VALUE_CURRENT_SCORE = 325;
        final int TILE_X_VALUE_TITLE = 125;
        final int TILE_Y_VALUE_SCORE = 30;
        final int TILE_Y_VALUE_TITLE = 30;
        final int TILE_SIZE = 140;

        final float FONT_SIZE_TITLE = 49;
        final float FONT_SIZE_SCORE = 35;

        // Draws background of panel.
        g.setColor(BACKGROUND_COLOR);
        g.fillRect(0, 0, 750, 200);

        // Draws tile with title name.
        g.setColor(BACKGROUND_COLOR_TITLE);
        g.fillRoundRect(TILE_X_VALUE_TITLE, TILE_Y_VALUE_TITLE, TILE_SIZE, TILE_SIZE, ARC, ARC);
        g.setFont(gameFont.deriveFont(FONT_SIZE_TITLE));
        g.setColor(FONT_COLOR_WHITE);
        drawCenteredText(g, "2048", TILE_X_VALUE_TITLE, TILE_SIZE, 115);

        // Draws tiles with score and best score.
        g.setColor(BACKGROUND_COLOR_SCORE);
        g.fillRoundRect(TILE_X_VALUE_CURRENT_SCORE, TILE_Y_VALUE_SCORE, TILE_SIZE, TILE_SIZE, ARC, ARC);
        g.fillRoundRect(TILE_X_VALUE_BEST_SCORE, TILE_Y_VALUE_SCORE, TILE_SIZE, TILE_SIZE, ARC, ARC);
        g.setColor(FONT_COLOR_TAN);
        g.setFont(gameFont.deriveFont(FONT_SIZE_SCORE));
        drawCenteredText(g, "SCORE", TILE_X_VALUE_CURRENT_SCORE, TILE_SIZE, FONT_Y_VALUE_SCORE_TITLE);
        drawCenteredText(g, "BEST", TILE_X_VALUE_BEST_SCORE, TILE_SIZE, FONT_Y_VALUE_SCORE_TITLE);
        g.setColor(FONT_COLOR_WHITE);
        drawCenteredText(g, score + "", TILE_X_VALUE_CURRENT_SCORE, TILE_SIZE, FONT_Y_VALUE_SCORE);
        drawCenteredText(g, bestScore + "", TILE_X_VALUE_BEST_SCORE, TILE_SIZE, FONT_Y_VALUE_SCORE);
    }

    // Draws text centered in a rectangle with the given positioning.
    private void drawCenteredText(Graphics g, String textDisplay, int rectXValue, int rectSize, int fontYValue) {
        Graphics2D g2d = (Graphics2D) g;
        FontMetrics fm = g2d.getFontMetrics();
        Rectangle2D r = fm.getStringBounds(textDisplay, g2d);
        int fontXValue = (rectXValue + rectSize / 2) - (int) (r.getWidth() / 2);
        g.drawString(textDisplay, fontXValue, fontYValue);
    }
}