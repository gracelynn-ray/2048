import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

// Launches a game of 2048.
public class GameLauncher {
    // Initialization of contants
    public static final int SCREEN_SIZE = 750; // height and width of frame

    // Constructs and manipulates GUI objects on the event dispatch thread.
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> launchGame());
    }
    
    // Creates and sets up game window and game display.
    private static void launchGame() {
        JFrame gameWindow = new JFrame();
        GameBoard gameBoard = new GameBoard();
        setUpGame(gameWindow, gameBoard);
        moveBoard(gameWindow, gameBoard);
    }

    // Sets up window JFrame and panel game board.
    private static void setUpGame(JFrame gameWindow, GameBoard gameBoard) {
        setUpFrame(gameWindow);
        setUpPanels(gameWindow);
        gameWindow.add(gameBoard);
        gameWindow.pack();
        gameWindow.setVisible(true);
    }

    // Sets up window JFrame and centers it in middle of screen.
    private static void setUpFrame(JFrame gameWindow) {
        gameWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameWindow.setResizable(false);
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension dim = tk.getScreenSize();
        int xPos = (dim.width / 2) - (SCREEN_SIZE / 2);
        int yPos = (dim.height / 2) - (SCREEN_SIZE / 2);
        gameWindow.setLocation(xPos, yPos);
    }

    // Sets up panels that surround the game board.
    private static void setUpPanels(JFrame gameWindow) {
        final int NUM_PANELS = 4;
        JPanel[] panels = new JPanel[NUM_PANELS];
        for (int i = 0; i < NUM_PANELS; i++) {
            panels[i] = new JPanel();
            panels[i].setBackground(new Color(251, 248, 239));
            panels[i].setPreferredSize(new Dimension(125, 125));
        }
        gameWindow.add(panels[0], BorderLayout.NORTH);
        gameWindow.add(panels[1], BorderLayout.EAST);
        gameWindow.add(panels[2], BorderLayout.WEST);
        gameWindow.add(panels[3], BorderLayout.SOUTH);
    }

    public static boolean inProgress = false;

    // Adds key listener to game window. Pressed arrow keys swipe the game board in corresponding direction.
    private static void moveBoard(JFrame gameWindow, GameBoard gameBoard) {
        final int UP_ARROW = 38;
        final int DOWN_ARROW = 40;
        final int LEFT_ARROW = 37;
        final int RIGHT_ARROW = 39;
        gameWindow.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                if (!inProgress) {
                    if (e.getKeyCode() == UP_ARROW) {
                        gameBoard.swipeUp();
                    } else if (e.getKeyCode() == DOWN_ARROW) {
                        gameBoard.swipeDown();
                    } else if (e.getKeyCode() == LEFT_ARROW) {
                        gameBoard.swipeLeft();
                    } else if (e.getKeyCode() == RIGHT_ARROW) {
                        gameBoard.swipeRight();
                    }
                }
            }
        });
    }
}