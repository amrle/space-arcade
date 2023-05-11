
/*
 * Name: Abhishek Nimalan, Ayaan Pathan
 * Class: ICS4U1-1A
 * Last Modified: January 27 2022
 * Class Description: Responsible for the Warp effect on Title screen and Game Over screen
*/

import java.awt.*;

public class WarpEffect extends Rectangle {

    // velocity of lines
    public int fallVelocity = 5;
    // spawn location
    public int startX = Canvas.WIDTH / 2;
    public int startY = Canvas.HEIGHT / 2;
    // amount we want at once
    public static final int amount = 200;
    // Influencing where the lines are going
    private double angle = 0;
    private double length = 0;
    private double speed = 0;
    private double offset = 0;
    // positions
    public double x1, x2, y1, y2, z1, z2;
    // color
    int color = 0;


    // Constructor. No arguments.
    public WarpEffect() {
        // Line properties (angle relative to center, speed and offset from center)
        angle = (int) (Math.random() * 360) + 1;
        speed = (int) (Math.random() * 2) + 1;
        offset = (int) (Math.random() * 500) + 1;
        
        // First point of line.
        x1 = startX + offset * Math.cos(Math.toRadians(angle));
        y1 = startY + offset * Math.sin(Math.toRadians(angle));
        
        // Use first point to determine second point of line.
        x2 = x1 + (length * Math.cos(Math.toRadians(angle)));
        y2 = y1 + (length * Math.sin(Math.toRadians(angle)));

        // give it a random colour
        color = (int) (Math.random() * 4) + 1;
    }


    // update positions of line
    public void move() {
        angle += 0.5; // rotate lines.
        length += 5; // increase line length
        speed += 1; // speed up

        // Make line longer as it moves (foreshortening).
        x2 = x1 + (length * Math.cos(Math.toRadians(angle)));
        y2 = y1 + (length * Math.sin(Math.toRadians(angle)));
       
        // Move line diagonally .
        x1 += (speed * Math.cos(Math.toRadians(angle)));
        y1 += (speed * Math.sin(Math.toRadians(angle)));
      
        x2 += (speed * Math.cos(Math.toRadians(angle)));
        y2 += (speed * Math.sin(Math.toRadians(angle)));
    }


    // Draw line
    public void draw(Graphics g) {
        // Change colour.
        switch (color) {
            case 1:
                g.setColor(Color.white);
                break;
            case 2:
                g.setColor(Color.yellow);
                break;
            case 3:
                g.setColor(Color.cyan);
                break;
            default:
                g.setColor(Color.pink);
                break;
        }
        // Draw line.
        g.drawLine((int) x1, (int) y1, (int) x2, (int) y2);
    }
}
