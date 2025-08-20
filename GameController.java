import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

/**
 * GameController class manages the logic and state of the Snake game.
 * Handles movement, apple spawning, collision detection, and user input.
 */
public class GameController implements ActionListener {
    // Delay between game updates (ms)
    int delay;
    // Arrays to store x and y coordinates of snake body parts
    int x[];
    int y[];
    // Number of body parts in the snake
    int bodyParts;
    // Number of apples eaten
    int applesEaten;
    // Current apple's x and y position
    int appleX;
    int appleY;
    // Current direction of the snake ('U', 'D', 'L', 'R')
    char direction;
    // Game running state
    boolean running = false;

    // Reference to the game panel for rendering
    GamePanel gamePanel;
    // Random number generator for apple placement
    Random random;
    // Timer for game loop
    Timer timer;

    /**
     * Constructor initializes game state and starts the game.
     */
    public GameController() {
        bodyParts = 6;
        applesEaten = 0;
        direction = 'R';
        delay = 175;

        gamePanel = new GamePanel(this);
        random = new Random();

        x = new int[gamePanel.GAME_UNITS];
        y = new int[gamePanel.GAME_UNITS];

        running = false;

        // Add key listener for user input
        gamePanel.addKeyListener(new MyKeyAdapter());

        startGame();
    }

    /**
     * Initializes or restarts the game state.
     */
    public void startGame() {
        // Set initial snake position (centered)
        x[0] = gamePanel.SCREEN_WIDTH / 2;
        y[0] = gamePanel.SCREEN_HEIGHT / 2 + 25;

        direction = 'R';
        bodyParts = 6;
        applesEaten = 0;

        newApple();
        running = true;

        // Start the game timer
        timer = new Timer(delay, this);
        timer.start();
    }

    /**
     * Spawns a new apple at a random position not overlapping the snake.
     */
    public void newApple() {
        do {
            appleX = random.nextInt((int) (gamePanel.SCREEN_WIDTH / gamePanel.UNIT_SIZE)) * gamePanel.UNIT_SIZE;
            appleY = random.nextInt((int) (gamePanel.SCREEN_HEIGHT / gamePanel.UNIT_SIZE)) * gamePanel.UNIT_SIZE;
        } while (!noOverlap());
    }

    /**
     * Moves the snake in the current direction.
     */
    public void move() {
        // Move each body part to the position of the previous one
        for (int i = bodyParts; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        // Update head position based on direction
        switch (direction) {
            case 'U':
                y[0] = y[0] - gamePanel.UNIT_SIZE;
                break;
            case 'D':
                y[0] = y[0] + gamePanel.UNIT_SIZE;
                break;
            case 'L':
                x[0] = x[0] - gamePanel.UNIT_SIZE;
                break;
            case 'R':
                x[0] = x[0] + gamePanel.UNIT_SIZE;
                break;
        }
    }

    /**
     * Checks if the snake's head has eaten the apple.
     * If so, increases body size and spawns a new apple.
     */
    public void checkApple() {
        if ((x[0] == appleX) && (y[0] == appleY)) {
            bodyParts++;
            applesEaten++;
            newApple();
        }
    }

    /**
     * Checks for collisions with the snake's body or the game borders.
     * Stops the game if a collision is detected.
     */
    public void checkCollisions() {
        // Check if head collides with body
        for (int i = bodyParts; i > 0; i--) {
            if ((x[0] == x[i]) && (y[0] == y[i])) {
                running = false;
            }
        }
        // Check if head touches left border
        if (x[0] < 0) {
            running = false;
        }
        // Check if head touches right border
        if (x[0] > gamePanel.SCREEN_WIDTH) {
            running = false;
        }
        // Check if head touches top border
        if (y[0] < 0) {
            running = false;
        }
        // Check if head touches bottom border
        if (y[0] > gamePanel.SCREEN_HEIGHT) {
            running = false;
        }

        // Stop the timer if the game is over
        if (!running) {
            timer.stop();
        }
    }

    /**
     * Main game loop, called by the timer.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkApple();
            checkCollisions();
        } else {
            // Show game over screen
            gamePanel.gameOver(gamePanel.getGraphics());
        }
        // Repaint the game panel
        gamePanel.repaint();
    }

    /**
     * Handles keyboard input for controlling the snake.
     */
    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if (direction != 'R') {
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (direction != 'L') {
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if (direction != 'D') {
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (direction != 'U') {
                        direction = 'D';
                    }
                    break;
            }
        }
    }

    /**
     * Checks if the new apple position overlaps with the snake's body.
     *
     * @return true if no overlap, false otherwise
     */
    public boolean noOverlap() {
        for (int i = 0; i < bodyParts; i++) {
            for (int j = 0; j < bodyParts; j++) {
                if (x[i] == appleX && y[j] == appleY) {
                    return false;
                }
            }
        }
        return true;
    }
}

