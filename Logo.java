import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JLabel;

// Creates title logo for 2048 game.
public class Logo extends JLabel {

    // Initialization of constants.
    public static final int LOGO_LENGTH = 250;

    private Font gameFont;

    // Constructs a new logo with given font.
    public Logo(Font gameFont) {
        setPreferredSize(new Dimension(LOGO_LENGTH, LOGO_LENGTH));
        setBounds(250, 175, LOGO_LENGTH, LOGO_LENGTH);
        this.gameFont = gameFont;
    }

    // Draws the logo with its tile and text.
    public void paint(Graphics g) {
        final Color BACKGROUND_COLOR = new Color(238, 194, 46);
        final Color FONT_COLOR = new Color(250, 247, 241);

        g.setColor(BACKGROUND_COLOR);
        g.fillRoundRect(0, 0, LOGO_LENGTH, LOGO_LENGTH, 10, 10);
        g.setColor(FONT_COLOR);
        g.setFont(gameFont.deriveFont(85f));
        drawCenteredText(g, "2048", 0, 0);
    }

    // Draws text in center of logo tile.
    private void drawCenteredText(Graphics g, String textDisplay, int rectXValue, int rectYValue) {
        Graphics2D g2d = (Graphics2D) g;
        FontMetrics fm = g2d.getFontMetrics();
        Rectangle2D r = fm.getStringBounds(textDisplay, g2d);
        int fontXValue = (rectXValue + LOGO_LENGTH / 2) - (int) (r.getWidth() / 2);
        int fontYValue = (rectYValue + LOGO_LENGTH / 2) - (int) (fm.getHeight() / 2) + fm.getAscent();
        g.drawString(textDisplay, fontXValue, fontYValue);
    }
}