/*
 * Name: Abhishek Nimalan, Ayaan Pathan
 * Class: ICS4U1-1A
 * Last Modified: January 27 2022
 * Class Description: This class is the Game Over screen, displayed when you die. It gives you
                      the option to retry, quit, and it displays your score.
 */


import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.io.File;;

public class GameOverScreen extends JPanel implements Runnable {

    // Class is to be run in its own thread
    private final Thread t;
    // These are used to determine what button the user is clicking, and if they have clicked
    private String screen = "";
    String selected = "";
    private boolean clicked = false;
    // The fancy warp effect 
    ArrayList<WarpEffect> warpLines = new ArrayList<WarpEffect>();
    // An instance of its parent (the window) so it can inform it of when a new game starts
    private Window window;
    // Self explanatory
    private int playerFinalScore;

    

    // An instance of the window is passed to this class so it can mount itself to
    // the window
    public GameOverScreen(Window window, int playerFinalScore) {
        this.window = window;
        this.playerFinalScore = playerFinalScore;
        this.setBackground(Color.black);
        this.setPreferredSize(new Dimension(Canvas.WIDTH, Canvas.HEIGHT));
        // Load our fonts and start the class off in a new thread
        loadFonts();
        t = new Thread(this);
        t.start();
    }


    // Internally calls upon the draw method below
    public void paint(Graphics g) {
        Image image = createImage(Canvas.WIDTH, Canvas.HEIGHT);
        Graphics graphics = image.getGraphics();
        draw(graphics);
        g.drawImage(image, 0, 0, this);
    }


    // This is where the magic of drawing all those warp lines and buttons takes place
    public void draw(Graphics g) {
        for (int i = 0; i < warpLines.size(); i++) {
            warpLines.get(i).draw(g);
        }
        String text = "";
        int x, y;
        int spacing = 100;
        g.setColor(new Color(255, 0, 0)); // RED

        g.setFont(new Font("Karmatic Arcade", Font.PLAIN, 70));
        text = "GAME OVER";
        x = Canvas.WIDTH / 2 - getWidth(text, g) / 2;
        y = Canvas.HEIGHT / 2 + getHeight(text, g) / 2 - spacing;
        g.drawString(text, x, y);

        g.setColor(new Color(255, 255, 255)); // WHITE

        g.setFont(g.getFont().deriveFont(30f));
        text = "RETRY?";
        x = Canvas.WIDTH / 2 - getWidth(text, g) / 2;
        y = Canvas.HEIGHT / 2 + getHeight(text, g) / 2;

        button(text, x, y, 30, g);

        text = "QUIT";
        x = Canvas.WIDTH / 2 - getWidth(text, g) / 2;
        y = Canvas.HEIGHT / 2 + getHeight(text, g) / 2 + (spacing - getHeight(text, g));

        button(text, x, y, 30, g);

        g.setColor(new Color(255, 207, 77)); // YELLOW
        g.setFont(g.getFont().deriveFont(20f));
        text = "FINAL SCORE - " + playerFinalScore;
        x = Canvas.WIDTH / 2 - getWidth(text, g) / 2;
        y = Canvas.HEIGHT / 2 + getHeight(text, g) / 2 + (spacing - getHeight(text, g)) * 2;
        g.drawString(text, x, y);
    }


    // Updates position of the WarpLines
    public void move() {
        for (int i = 0; i < warpLines.size(); i++) {
            warpLines.get(i).move();
        }
    }


    // Load in the fonts we use for our screen
    private void loadFonts() {
        try {
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("fonts/ka1.ttf")));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // Used to get the dimensions for our buttons.
    private int getHeight(String text, Graphics g) {
        return (g.getFontMetrics().getHeight());
    }
    private int getWidth(String text, Graphics g) {
        return (g.getFontMetrics().stringWidth(text));
    }


    // Draw the buttons and check for any activity on them
    private void button(String text, int x, int y, int textSize, Graphics g) {
        g.drawString(text, x, y);
        checkClick(x, y - getHeight(text, g) / 2 - textSize / 3, x + getWidth(text, g), y + getHeight(text, g), text);
    }

    
    // If a button is clicked, check which one and perfrom according actions in the run() 
    // method below
    private void checkClick(int x1, int y1, int x2, int y2, String text) {
        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
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



    // Our main loop where we call upon all the functions above this.
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

            warpLines.removeIf(
                    temp -> (temp.y1 > Canvas.HEIGHT || temp.y1 < 0 || temp.x1 < 0 || temp.x1 > Canvas.WIDTH));

            if (warpLines.size() < WarpEffect.amount)
                warpLines.add(new WarpEffect());

            if (delta >= 1) {
                move();
                repaint();
                delta--;
            }
        }

        switch (screen) {
            case "RETRY?": {
                window.startGame();
                break;
            }
            case "QUIT": {
                System.out.println("Game has ended. Thanks for Playing!");
                System.exit(0);
                break;
            }
        }
    }

}
