
/*
 * Name: Abhishek Nimalan, Ayaan Pathan
 * Class: ICS4U1-1A
 * Last Modified: January 27 2022
 * Class Description: This class defines the stars you see going downwards as you play.
*/

import java.awt.*;

public class Stars extends Rectangle {

    // Velocity of stars
    public final int FALL_VELOCITY = 7;
    public final int SLOW_FALL_VELOCITY = 3;
    // Dimensions and amount of stars at once
    public static final int WIDTH = 2;
    public static final int HEIGHT = 2;
    public final static int amount = 700;
    // Blinky blinky shiny shiny
    private int blinkSpeed = 12;
    int delay = 0;
    int color = 0;
    // 1 in 5 stars are going to be slower moving to provide a sense of depth
    boolean slowerStars = false; 


    // Constructor. No arguments.
    public Stars() {

        // Assign a star a random position
        x = (int) (Math.random() * Canvas.WIDTH) + 1;
        y = 0 - ((int) (Math.random() * Canvas.HEIGHT * 2) + 1);

        // Stars blink at different rates
        delay = (int) (Math.random() * blinkSpeed) + 1;

        // Assign a star a random colour
        color = (int) (Math.random() * 4) + 1;

        // 1/5 of stars are slower moving.
        slowerStars = ((int) (Math.random() * 5) + 1 == 1);
    }


    // Move the star down the screen.
    public void move() {
        y += (slowerStars) ? SLOW_FALL_VELOCITY : FALL_VELOCITY;
    }


    // Draw said star
    public void draw(Graphics g) {

        // Change colour
        if (delay < blinkSpeed) {
            if (color == 1)
                g.setColor(Color.WHITE);
            else if (color == 2)
                g.setColor(Color.YELLOW);
            else if (color == 3)
                g.setColor(Color.CYAN);
            else
                g.setColor(Color.PINK);
        } else if (delay == blinkSpeed * 2) {
            delay = 0;
        } else if (delay > blinkSpeed) {
            g.setColor(Color.black);
        }

        g.fillRect(x, y, WIDTH, HEIGHT);
        delay++;
    }
}