import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/*
 * Launches a game of 2048.
 */
public class GameLauncher {
    // Initialization of contants
    public static final int NUM_ROWS = 4; // number of rows on gameBoard
    public static final int NUM_COLS = 4; // number of columns on gameBoard
    public static final int EMPTY = 1; // represents a spot with no tile
    // array of colors; array position corresponds to power of 2 that tile value is
    public static final Color[] TILE_COLORS = { new Color(204, 192, 178), new Color(236, 224, 215),
        new Color(237, 223, 199), new Color(241, 177, 120), new Color(245, 149, 98),
        new Color(245, 124, 94), new Color(246, 94, 60) , new Color(238, 208, 113),
        new Color(238, 205, 99), new Color(236, 199, 79), new Color(239, 197, 63),
        new Color(238, 194, 46), new Color(62, 57, 51) } ;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> launchGame());
    }
    
    private static void launchGame() {
        JFrame gameWindow = new JFrame();
        JPanel gameBoardDisplay = new JPanel();
        Board gameBoard = new Board();
        setUpGame(gameWindow, gameBoardDisplay, gameBoard);
        moveBoard(gameWindow, gameBoardDisplay, gameBoard);
    }

    private static void setUpGame(JFrame f, JPanel gameBoard, Board board) {
        setUpFrame(f);
        setUpPanels(f);
        setUpBoardDisplay(gameBoard);
        mirrorBoard(gameBoard, board);
        f.add(gameBoard);
        f.pack();
        f.setVisible(true);
    }

    private static void setUpFrame(JFrame f) {
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setResizable(false);
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension dim = tk.getScreenSize();
        int xPos = (dim.width / 2) - (375);
        int yPos = (dim.height / 2) - (375);
        f.setLocation(xPos, yPos);
    }

    private static void setUpPanels(JFrame f) {
        JPanel top = new JPanel();
        top.setBackground(new Color(251, 248, 239));
        top.setPreferredSize(new Dimension(125, 125));

        JPanel right = new JPanel();
        right.setBackground(new Color(251, 248, 239));
        right.setPreferredSize(new Dimension(125, 125));

        JPanel left = new JPanel();
        left.setBackground(new Color(251, 248, 239));
        left.setPreferredSize(new Dimension(125, 125));

        JPanel bottom = new JPanel();
        bottom.setBackground(new Color(251, 248, 239));
        bottom.setPreferredSize(new Dimension(125, 125));

        f.add(top, BorderLayout.NORTH);
        f.add(right, BorderLayout.EAST);
        f.add(left, BorderLayout.WEST);
        f.add(bottom, BorderLayout.SOUTH);
    }

    private static void setUpBoardDisplay(JPanel gameBoard) {
        gameBoard.setBorder(BorderFactory.createLineBorder(new Color(188, 172, 158), 10));
        gameBoard.setLayout(new GridLayout(NUM_ROWS, NUM_COLS, 10, 10));
        gameBoard.setBackground(new Color(188, 172, 158));
        gameBoard.setPreferredSize(new Dimension(500, 500));
    }

    private static void mirrorBoard(JPanel gameBoardDisplay, Board gameBoard) {
        gameBoardDisplay.removeAll();
        int[][] boardRepresentation = gameBoard.getBoardRepresentation();
        for (int row = 0; row < NUM_ROWS; row++) {
            for (int col = 0; col < NUM_COLS; col++) {
                int tileValue = boardRepresentation[row][col];
                JLabel tile = new JLabel("", JLabel.CENTER);
                tile.setBackground(TILE_COLORS[(int) (Math.log10(tileValue) / Math.log10(2))]);
                tile.setOpaque(true);
                if (tileValue != EMPTY) {
                    tile.setText("" + tileValue);
                }
                if (tileValue < 128) {
                    tile.setFont(new Font(null, Font.BOLD, 50));
                } else if (tileValue < 1024) {
                    tile.setFont(new Font(null, Font.BOLD, 45));
                } else {
                    tile.setFont(new Font(null, Font.BOLD, 30));
                }
                if (tileValue < 8) {
                    tile.setForeground(new Color(118, 111, 100));
                } else {
                    tile.setForeground(new Color(250, 247, 241));
                }
                gameBoardDisplay.add(tile);
            }
        }
        gameBoardDisplay.revalidate();
    }

    private static void moveBoard(JFrame gameWindow, JPanel gameBoardDisplay, Board gameBoard) {
        gameWindow.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == 38) {
                    gameBoard.swipeUp();
                } else if (e.getKeyCode() == 40) {
                    gameBoard.swipeDown();
                } else if (e.getKeyCode() == 37) {
                    gameBoard.swipeLeft();
                } else if (e.getKeyCode() == 39) {
                    gameBoard.swipeRight();
                }
                mirrorBoard(gameBoardDisplay, gameBoard);
            }
        });
    }
}
