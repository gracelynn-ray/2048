import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JLabel;

public class Button extends JLabel {

    private Font gameFont;
    private String text;
    
    public Button(Font gameFont, String text, int x, int y) {
        setPreferredSize(new Dimension(250, 75));
        setBounds(x, y, 250, 75);
        this.gameFont = gameFont;
        this.text = text;
    }

    public void paint(Graphics g) {
        g.setColor(new Color(188, 172, 158));
        g.fillRoundRect(0, 0, 250, 75, 10, 10);
        g.setColor(new Color(250, 247, 241));
        g.setFont(gameFont.deriveFont(40f));
        drawCenteredText(g, text, 0, 0);
    }

    private void drawCenteredText(Graphics g, String textDisplay, int rectXValue, int rectYValue) {
        Graphics2D g2d = (Graphics2D) g;
        FontMetrics fm = g2d.getFontMetrics();
        Rectangle2D r = fm.getStringBounds(textDisplay, g2d);
        int fontXValue = (rectXValue + 250 / 2) - (int) (r.getWidth() / 2);
        int fontYValue = (rectYValue + 75 / 2) - (int) (fm.getHeight() / 2) + fm.getAscent() - 2;
        g.drawString(textDisplay, fontXValue, fontYValue);
    }
}
