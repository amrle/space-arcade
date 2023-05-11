
/*
 * Name: Abhishek Nimalan, Ayaan Pathan
 * Class: ICS4U1-1A
 * Last Modified: January 27 2022
 * Class Description: The container that holds all screens, and the Canvas. Responsible for                         major game events, global constants, and saving high scores.
*/

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.*;

public class Window extends JFrame {

    // Number of different levels
    public static final int LEVEL_COUNT = 8;
    public static final int BOSS_LEVEL_COUNT = 2;
    // The elements that go on the screen
    private Canvas canvas;
    private TitleScreen titleScreen;
    private GameOverScreen gameOverScreen;

    public static int lastXPosition = Canvas.WIDTH/2;
    public static int lastYPosition = Canvas.HEIGHT;
    public ArrayList<Stars> stars = new ArrayList<Stars>();

    // The level the player is currently at
    private int level = 1;
    // number of lives they've got left -- this cannot be stored in the Canvas as we
    // re-instantiate the class which causes lives to reset to 3.
    private int lives = 5;
    public static final int MAX_LIVES = 5;

    // The player's highscore (initialized in constructor)
    private int highScore;

    // Constructor function.
    public Window() {

        // Read the highscore from the file.
        try {
            this.highScore = Integer.parseInt(new BufferedReader(new FileReader("saved/highscore.txt")).readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Add our title screen to the window, but not yet the gameOverScreen.
        titleScreen = new TitleScreen(this, highScore);
        this.add(titleScreen);

        // Window settings
        this.setLayout(new FlowLayout());
        this.setTitle("GALAXIA CLASSIC");
        this.setBackground(Color.black);
        this.setSize(615, 830);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null); // middle of screen
        this.setVisible(true);

        // Start background music.
        Sound backgroundMusic = new Sound("sound/backgroundMusic.wav");
        backgroundMusic.loop();
        backgroundMusic.play(-20.0f);
    }

    // To start game - whether that be for the first time or after a game over.
    public void startGame() {
        this.remove(titleScreen);
        if (gameOverScreen != null) {
            this.remove(gameOverScreen);
        }

        canvas = new Canvas(this, level, lives, 0); // Re-instantiate the canvas, effectively creating a new game.
        canvas.setBounds(0, 0, 600, 800);
        this.add(canvas);
    }

    // To advance the player one level - Reset the Canvas and increment level variable.
    public void nextLevel(int score, int lives) {
        this.remove(canvas);
        level++;

        canvas = new Canvas(this, level, lives, score);
        canvas.setBounds(0, 0, 600, 800);
        this.add(canvas);
    }

    // Terminate the game and display gameover screen when user dies.
    public void gameOver(int score) {
        // Reset level to 1
        level = 1;

        // Show game over screen
        this.remove(canvas);
        gameOverScreen = new GameOverScreen(this, score);
        this.add(gameOverScreen);
        this.revalidate();
        this.repaint();

        // Update highscore if there is a new highscore
        if (score > highScore) {
            try {
                BufferedWriter bw = new BufferedWriter(new FileWriter("saved/highscore.txt", false));
                bw.write(Integer.toString(score));
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}