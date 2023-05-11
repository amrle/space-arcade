/*
    This class handles bullets of any kind.
*/

import java.awt.*;
import javax.swing.*;

public class Bullet extends Rectangle {

    // Variables
    public int fallVelocity;            // Bullet speed
    public static final int WIDTH = 20; // Dimensions
    public static final int HEIGHT = 45;
    public int x = 0;                   // Current positioning on Canvas
    public int y = 0;

    private int direction = 0;
    private int delay = 0;     // Determines what frame of the sprite to show
    String image = "";         // This String stores the relative path to  image of the bullet


    // Pre: Direction is either 1, or -1, and serves as a multiplier for our fallVelocity.
    //      startX and startY is the spawn location of the bullet, and the fallVelocity
    //      is the absolute speed of the bullet, irrespective of direction.
    public Bullet(int direction, int startX, int startY, int fallVelocity) {
        this.direction = direction;
        this.x = startX;
        this.y = startY;
        this.fallVelocity = fallVelocity;
    }


    // Pre: None
    // Post: Bullet y position is changed according to velocity and direction
    // Desc: Used to move bullets across the screen
    public void move() {
        y -= fallVelocity * direction;
    }


    // Pre: None
    // Post: Draws bullets
	// Desc: Draw the player or enemy bullets.

    public void draw(Graphics g) {

		// Enemy bullet animation
        if (direction == -1) {
            if (delay == 0) {
                image = "images/eb_1.png";
            } else if (delay == 2) {
                image = "images/eb_2.png";
            } else if (delay == 4) {
                image = "images/eb_3.png";
            } else if (delay == 6) {
                delay = -2;
            }
            g.drawImage(new ImageIcon(image).getImage(), x, y, WIDTH, HEIGHT, null);
       
	    } 
		// Player bullet animation
		else {
            if (delay == 0) {
                image = "images/b_1.png";
            } else if (delay == 2) {
                image = "images/b_2.png";
            } else if (delay == 4) {
                image = "images/b_3.png";
            } else if (delay == 6) {
                delay = -2;
            }
           
		    g.drawImage(new ImageIcon(image).getImage(), x, y, WIDTH, HEIGHT, null);
        }

        delay++; // add a delay so the frames don't change too quickly
    }
}