/*
 * Name: Abhishek Nimalan, Ayaan Pathan
 * Class: ICS4U1-1A
 * Last Modified: January 27 2022
 * Class Description: This class defines the Title screen, which allows the user to start the                       game, quit, and displays the user's all-time high score.
*/


import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.io.File;

public class TitleScreen extends JPanel implements Runnable {
    // run class in it's own thread
    private Thread t;
    // Used for determining what button user has clicked on
    private String selected = "";
    private String screen = "";
    private boolean clicked = false;
    // Used for our fancy warp WarpEffect
    private ArrayList<WarpEffect> warpLines = new ArrayList<WarpEffect>();
    // Needs an instance of its parent element so it can notify it when game starts.
    private Window window;
    // Self-explanatory
    private int playerHighScore;


    // The constructor needs its container to be passed into it so that it can
    // attach itself to that container, and notify it when the game starts 'n stuff.
    public TitleScreen(Window window, int playerHighScore) {
        this.window = window;
        this.playerHighScore = playerHighScore;
        this.setBackground(Color.black);
        this.setPreferredSize(new Dimension(Canvas.WIDTH, Canvas.HEIGHT));
        // create the warplines
        for (int i = 0; i < WarpEffect.amount; i++) {
            warpLines.add(new WarpEffect());
        }
        // Load fonts, and start in fresh thread.
        loadFonts();
        t = new Thread(this);
        t.start();
    }


    // Draw the buttons and warp lines 'n stuff
    public void paint(Graphics g) {
        Image image = createImage(Canvas.WIDTH, Canvas.HEIGHT);
        Graphics graphics = image.getGraphics();
        draw(graphics);
        g.drawImage(image, 0, 0, this);
    }


    // Move the warp lines
    public void move() {
        for (int i = 0; i < warpLines.size(); i++) {
            warpLines.get(i).move();
        }
    }


    // self-explanatory
    private void loadFonts() {
        try {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("fonts/ka1.ttf")));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // Allows us to get dimensions of buttons and other text on the screen
    private int getHeight(String text, Graphics g) {
        return (g.getFontMetrics().getHeight());
    }
    private int getWidth(String text, Graphics g) {
        return (g.getFontMetrics().stringWidth(text));
    }


    // Draw the button and check for clicks
    private void button(String text, int x, int y, int textSize, Graphics g) {
        g.drawString(text, x, y);
        checkClick(x, y - getHeight(text, g) / 2 - textSize / 3, x + getWidth(text, g), y + getHeight(text, g), text);
    }


    // check for clicks
    private void checkClick(int x1, int y1, int x2, int y2, String text) {
        addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent e) {
                if (e.getX() > x1 && e.getX() < x2 && e.getY() > y1 && e.getY() < y2) {
                    if (!clicked)
                        new Sound("sound/select.wav").play(-20f);
                    clicked = true;

                    screen = text;
                    t.interrupt();
                }
            }

        });
    }


    // update everything on screen
    public void draw(Graphics g) {
        for (int i = 0; i < warpLines.size(); i++) {
            warpLines.get(i).draw(g);
        }
        String text = "";
        int x, y;
        int spacing = 150;
        g.setColor(new Color(0, 255, 255)); // CYAN

        g.setFont(new Font("Karmatic Arcade", Font.PLAIN, 70));
        text = "GALAXIA";
        x = Canvas.WIDTH / 2 - getWidth(text, g) / 2;
        y = Canvas.HEIGHT / 2 + getHeight(text, g) / 2 - spacing;
        g.drawString(text, x, y);

        g.setFont(g.getFont().deriveFont(40f));
        text = "CLASSIC";
        x = Canvas.WIDTH / 2 - getWidth(text, g) / 2;
        y = Canvas.HEIGHT / 2 + getHeight(text, g) / 2 - spacing + 80;
        g.drawString(text, x, y);

        g.setColor(new Color(255, 255, 255)); // WHITE

        g.setFont(g.getFont().deriveFont(30f));
        text = "START";
        x = Canvas.WIDTH / 2 - getWidth(text, g) / 2;
        y = Canvas.HEIGHT / 2 + getHeight(text, g) / 2;

        button(text, x, y, 30, g);

        text = "QUIT";
        x = Canvas.WIDTH / 2 - getWidth(text, g) / 2;
        y = Canvas.HEIGHT / 2 + getHeight(text, g) / 2 + (spacing - getHeight(text, g) - 40);

        button(text, x, y, 30, g);

        g.setColor(new Color(255, 207, 77)); // YELLOW
        g.setFont(g.getFont().deriveFont(20f));
        text = "HIGH SCORE - " + playerHighScore;
        x = Canvas.WIDTH / 2 - getWidth(text, g) / 2;
        y = Canvas.HEIGHT / 2 + getHeight(text, g) / 2 + (spacing - getHeight(text, g)) * 2 - 70;
        g.drawString(text, x, y);
    }

    

    // Our main loop that calls upon all the functions above
    public void run() {
        long lastTime = System.nanoTime();
        double amountOfTicks = 60;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        long now;

        while (!Thread.interrupted()) {
            now = System.nanoTime();
            delta = delta + (now - lastTime) / ns;
            lastTime = now;

            if (warpLines.size() < WarpEffect.amount) {
                warpLines.add(new WarpEffect());
            }
            warpLines.removeIf(
                    temp -> (temp.y1 > Canvas.HEIGHT || temp.y1 < 0 || temp.x1 < 0 || temp.x1 > Canvas.WIDTH));

            if (delta >= 1) {
                move();
                repaint();
                delta--;
            }
        }
        // If buttons have been clicked, this chooses what to do about it.
        switch (screen) {
            case "START": {
                window.startGame();
                break;
            }
            case "QUIT": {
                System.out.println("Game has ended. Thanks for playing.");
                System.exit(0);
                break;
            }
        }
    }
}
