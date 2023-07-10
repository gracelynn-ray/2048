import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JLabel;

public class Logo extends JLabel {

    private Font gameFont;

    public Logo(Font gameFont) {
        setPreferredSize(new Dimension(250, 250));
        setBounds(250, 175, 250, 250);
        this.gameFont = gameFont;
    }

    public void paint(Graphics g) {
        g.setColor(new Color(238, 194, 46));
        g.fillRoundRect(0, 0, 250, 250, 10, 10);
        g.setColor(new Color(250, 247, 241));
        g.setFont(gameFont.deriveFont(85f));
        drawCenteredText(g, "2048", 0, 0);
    }

    private void drawCenteredText(Graphics g, String textDisplay, int rectXValue, int rectYValue) {
        Graphics2D g2d = (Graphics2D) g;
        FontMetrics fm = g2d.getFontMetrics();
        Rectangle2D r = fm.getStringBounds(textDisplay, g2d);
        int fontXValue = (rectXValue + 250 / 2) - (int) (r.getWidth() / 2);
        int fontYValue = (rectYValue + 250 / 2) - (int) (fm.getHeight() / 2) + fm.getAscent();
        g.drawString(textDisplay, fontXValue, fontYValue);
    }
}