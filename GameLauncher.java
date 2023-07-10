import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

// Launches a game of 2048.
public class GameLauncher {

    // Initialization of contants
    public static final int SCREEN_SIZE = 750; // height and width of frame

    public static final Color BACKGROUND_COLOR = new Color(251, 248, 239);

    // Constructs and manipulates GUI objects on the event dispatch thread.
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> launchGame());
    }
    
    // Creates and sets up game window and game display.
    private static void launchGame() {
        JFrame gameWindow = new JFrame();
        Font gameFont = createGameFont();
        ScoreBoard scoreBoard = new ScoreBoard(gameFont);
        GameBoard gameBoard = new GameBoard(scoreBoard, gameFont);
        setUpFrame(gameWindow);
        setUpHomeScreen(gameWindow, gameBoard, scoreBoard, gameFont);        
    }

    // Creates game font from font file.
    private static Font createGameFont() {
        try {
            return Font.createFont(Font.TRUETYPE_FONT, new File("GameFont.ttf"));
        } catch (FontFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
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

    // Sets up starting screen. Pressing play button begins game.
    private static void setUpHomeScreen(JFrame gameWindow, GameBoard gameBoard, ScoreBoard scoreBoard, Font gameFont) {
        final int TILE_X_VALUE_BUTTON = 250;
        final int TILE_Y_VALUE_BUTTON = 475;

        // Resets the frame in case coming from game over screen.
        gameWindow.getContentPane().removeAll();
        gameWindow.revalidate();
        gameWindow.repaint();

        JPanel homeScreen = new JPanel();
        setUpHomeScreen(homeScreen);

        homeScreen.add(new Logo(gameFont));
        homeScreen.add(new Button(gameFont, "PLAY", TILE_X_VALUE_BUTTON, TILE_Y_VALUE_BUTTON));

        // GameBoard appears once button is pressed.
        JButton start = new JButton();
        setUpStartButton(start, TILE_X_VALUE_BUTTON, TILE_Y_VALUE_BUTTON);
        start.addActionListener(e -> {
            gameWindow.remove(homeScreen);
            setUpGame(gameWindow, gameBoard, scoreBoard, gameFont);
        });

        homeScreen.add(start);
        gameWindow.add(homeScreen);
        gameWindow.pack();
        gameWindow.setVisible(true);
    }

    // Sets up panel that represents the home screen.
    private static void setUpHomeScreen(JPanel homeScreen) {
        homeScreen.setPreferredSize(new Dimension(SCREEN_SIZE, SCREEN_SIZE));
        homeScreen.setBackground(BACKGROUND_COLOR);
        homeScreen.setLayout(null);
    }

    // Makes button unseeable and puts it at proper position.
    private static void setUpStartButton(JButton start, int x, int y) {
        start.setBounds(x, y, 250, 75);
        start.setOpaque(false);
        start.setBorderPainted(false);
    }

    // Sets up window JFrame and panel game board.
    private static void setUpGame(JFrame gameWindow, GameBoard gameBoard, ScoreBoard scoreBoard, Font gameFont) {
        setUpPanels(gameWindow);
        gameWindow.add(gameBoard);
        gameWindow.add(scoreBoard, BorderLayout.NORTH);
        gameWindow.pack();
        gameWindow.revalidate();
        gameWindow.repaint();
        moveBoard(gameWindow, gameBoard, scoreBoard, gameFont);
    }

    // Sets up panels that surround the game board.
    private static void setUpPanels(JFrame gameWindow) {
        final int NUM_PANELS = 3;
        JPanel[] panels = new JPanel[NUM_PANELS];
        for (int i = 0; i < NUM_PANELS; i++) {
            panels[i] = new JPanel();
            panels[i].setBackground(new Color(251, 248, 239));
            panels[i].setPreferredSize(new Dimension(125, 125));
        }
        gameWindow.add(panels[0], BorderLayout.EAST);
        gameWindow.add(panels[1], BorderLayout.WEST);
        panels[2].setPreferredSize(new Dimension(125, 50));
        gameWindow.add(panels[2], BorderLayout.SOUTH);
    }

    // Adds key listener to game window. Pressed arrow keys swipe the game board in corresponding direction.
    private static void moveBoard(JFrame gameWindow, GameBoard gameBoard, ScoreBoard scoreBoard, Font gameFont) {
        gameWindow.setFocusable(true);
        gameWindow.requestFocus();
        final int UP_ARROW = 38;
        final int DOWN_ARROW = 40;
        final int LEFT_ARROW = 37;
        final int RIGHT_ARROW = 39;
        gameWindow.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                if (!gameBoard.moveInProgress()) {
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
                if (gameBoard.gameOver()) {
                    ((JFrame) e.getSource()).removeKeyListener(this);
                    scoreBoard.gameOver();
                    setUpGameOverScreen(gameWindow, scoreBoard, gameFont);
                }
            }
        });
    }

    // Sets up game over screen. Appears once player has lost.
    private static void setUpGameOverScreen(JFrame gameWindow, ScoreBoard scoreBoard, Font gameFont) {
        final int TILE_X_VALUE_BUTTON = 250;
        final int TILE_Y_VALUE_BUTTON = 400;

        // Removes game board and score board from window.
        gameWindow.getContentPane().removeAll();
        gameWindow.revalidate();
        gameWindow.repaint();

        JPanel gameOverScreen = new JPanel();
        setUpGameOverScreen(gameOverScreen);

        JLabel losingMessage = new JLabel("You lost!");
        setUpLosingMessage(losingMessage, gameFont);

        gameOverScreen.add(losingMessage);
        gameOverScreen.add(new Button(gameFont, "TRY AGAIN", TILE_X_VALUE_BUTTON, TILE_Y_VALUE_BUTTON));

        // Goes back to home screen once button is pressed.
        JButton start = new JButton();
        setUpStartButton(start, TILE_X_VALUE_BUTTON, TILE_Y_VALUE_BUTTON);
        start.addActionListener(e -> {
            gameWindow.remove(gameOverScreen);
            setUpHomeScreen(gameWindow, new GameBoard(scoreBoard, gameFont), scoreBoard, gameFont);
        });

        gameOverScreen.add(start);
        gameWindow.add(gameOverScreen);
        gameWindow.pack();
        gameWindow.setVisible(true);
    }

    // Sets up panel that represents game over screen.
    private static void setUpGameOverScreen(JPanel gameOverScreen) {
        gameOverScreen.setPreferredSize(new Dimension(SCREEN_SIZE, SCREEN_SIZE));
        gameOverScreen.setBackground(BACKGROUND_COLOR);
        gameOverScreen.setLayout(null);
    }

    // Sets up label that displays the losing message.
    private static void setUpLosingMessage(JLabel losingMessage, Font gameFont) {
        losingMessage.setFont(gameFont.deriveFont(125f));
        losingMessage.setForeground(new Color(188, 172, 158));
        losingMessage.setBounds(120, 100, 525, 400);
    }

}