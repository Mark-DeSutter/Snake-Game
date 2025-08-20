import java.awt.*;
import javax.swing.*;

/**
 * GamePanel handles the rendering and UI for the Snake game.
 * It draws the snake, apple, score, and game over screen.
 * Also manages the "New Game" button for restarting the game.
 */
public class GamePanel extends JPanel {

    // Constants for screen and game sizing
    static final int SCREEN_WIDTH = 1300;
    static final int SCREEN_HEIGHT = 750;
    static final int UNIT_SIZE = 50;
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / (UNIT_SIZE * UNIT_SIZE);

    // Reference to the game controller for game state
    GameController controller;

    // JFrame to display the game
    JFrame frame;

    /**
     * Constructor sets up the game window and panel.
     * @param controller Reference to the GameController managing game logic
     */
    GamePanel(GameController controller) {
        frame = new JFrame();
        frame.add(this);
        frame.setTitle("Snake");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setVisible(true);

        this.controller = controller;

        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);

        frame.pack(); // Sets to minimum possible size
        frame.setLocationRelativeTo(null); // Places window in the center of the screen
    }

    /**
     * Overridden paintComponent to handle custom drawing.
     * @param g Graphics object for drawing
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    /**
     * Draws the game elements: snake, apple, score, and game over screen.
     * @param g Graphics object for drawing
     */
    public void draw(Graphics g) {
        if (controller.running) {
            // Draw apple
            g.setColor(Color.red);
            g.fillOval(controller.appleX, controller.appleY, UNIT_SIZE, UNIT_SIZE);

            // Draw snake
            for (int i = 0; i < controller.bodyParts; i++) {
                if (i == 0) {
                    g.setColor(Color.green); // head
                    g.fillRect(controller.x[i], controller.y[i], UNIT_SIZE, UNIT_SIZE);
                } else {
                    g.setColor(new Color(45, 180, 0)); // body
                    g.fillRect(controller.x[i], controller.y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }

            // Draw score
            g.setColor(Color.red);
            g.setFont(new Font("Ink Free", Font.BOLD, 40));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score: " + controller.applesEaten,
                    (SCREEN_WIDTH - metrics.stringWidth("Score: " + controller.applesEaten)) / 2,
                    g.getFont().getSize());
        } else {
            // Draw game over screen
            gameOver(g);
        }
    }

    /**
     * Draws the game over screen and adds a "New Game" button if not present.
     * @param g Graphics object for drawing
     */
    public void gameOver(Graphics g) {
        // Draw score
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 40));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Score: " + controller.applesEaten,
                (SCREEN_WIDTH - metrics1.stringWidth("Score: " + controller.applesEaten)) / 2,
                g.getFont().getSize());

        // Draw "Game Over" text
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 75));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Game Over",
                (SCREEN_WIDTH - metrics2.stringWidth("Game Over")) / 2,
                SCREEN_HEIGHT / 2);

        // Only add the button if it doesn't already exist
        if (newGameButton == null) {
            newGameButton = new JButton("New Game");
            newGameButton.setBounds(SCREEN_WIDTH / 2 - 150, SCREEN_HEIGHT / 2 + 25, 300, 100);
            newGameButton.setBackground(Color.black);
            newGameButton.setForeground(Color.red);
            newGameButton.setFont(new Font("Ink Free", Font.BOLD, 55));

            this.setLayout(null); // Needed for absolute positioning
            this.add(newGameButton);
            newGameButton.setVisible(true);

            // Action listener to restart the game and remove the button
            newGameButton.addActionListener(e -> {
                this.remove(newGameButton);
                newGameButton = null;
                controller.startGame();
            });
        }
    }

    // Field to track if the New Game button is already added
    private JButton newGameButton = null;

}

