import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JLabel;

// Creates button for 2048 game.
public class Button extends JLabel {

    // Initialization of constants.
    public static final int BUTTON_WIDTH = 250;
    public static final int BUTTON_HEIGHT = 75;

    private Font gameFont;
    private String text;
    
    // Constructs a button in given position with given font and text.
    public Button(Font gameFont, String text, int x, int y) {
        setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        setBounds(x, y, BUTTON_WIDTH, BUTTON_HEIGHT);
        this.gameFont = gameFont;
        this.text = text;
    }

    // Draws the button with correct colors and at correct position.
    public void paint(Graphics g) {
        final Color BACKGROUND_COLOR = new Color(188, 172, 158);
        final Color FONT_COLOR = new Color(250, 247, 241);
        final float FONT_SIZE = 40;

        g.setColor(BACKGROUND_COLOR);
        g.fillRoundRect(0, 0, BUTTON_WIDTH, BUTTON_HEIGHT, 10, 10);
        g.setColor(FONT_COLOR);
        g.setFont(gameFont.deriveFont(FONT_SIZE));
        drawCenteredText(g, text, 0, 0);
    }

    // Draws text in center of button.
    private void drawCenteredText(Graphics g, String textDisplay, int rectXValue, int rectYValue) {
        Graphics2D g2d = (Graphics2D) g;
        FontMetrics fm = g2d.getFontMetrics();
        Rectangle2D r = fm.getStringBounds(textDisplay, g2d);
        int fontXValue = (rectXValue + BUTTON_WIDTH / 2) - (int) (r.getWidth() / 2);
        int fontYValue = (rectYValue + BUTTON_HEIGHT / 2) - (int) (fm.getHeight() / 2) + fm.getAscent() - 2;
        g.drawString(textDisplay, fontXValue, fontYValue);
    }
}
