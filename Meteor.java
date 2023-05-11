/*
 * Name: Abhishek Nimalan, Ayaan Pathan
 * Class: ICS4U1-1A
 * Last Modified: January 27 2022
 * Class Description: This class defines the Meteor, which is rained down upon the player every
                      now and then.
*/

import javax.swing.*;
import java.awt.*;

public class Meteor extends Bullet {
    
	// Meteor dimensions
	public static final int WIDTH = 45;
    public static final int HEIGHT = 75;
    
	// To check if we have shoed a warning to the player yet or not.
    public boolean showedWarning = false;
	
	// Warning sign 
	private int delay = 0;
    private int loops = 0;

    // Initialize meteor object with a spawn position, the velocity which it will fall.
    public Meteor(int direction, int startX, int startY, int fallVelocity) {
        super(direction, startX, startY, fallVelocity);
    }


	// Draw the warning sign animation (5 frames)
    public void drawWarning(Graphics g) {
        int interval = 10;
        if (delay == interval * 0) {
            image = "images/danger_1.png";
        } else if (delay == interval * 1) {
            image = "images/danger_2.png";
        } else if (delay == interval * 2) {
            image = "images/danger_3.png";
        } else if (delay == interval * 3) {
            image = "images/danger_4.png";
        } else if (delay == interval * 4) {
            image = "images/danger_5.png";
        } else if (delay == interval * 5) {
            delay = -2;
            loops++;
        }
		// Play the warning animation 5 times.
        if (loops == 5)
            showedWarning = true;

        g.drawImage(new ImageIcon(image).getImage(), super.x, super.y, 75, 75, null);

        delay++;
    }

	// Draw the meteor sprite.
    @Override
    public void draw(Graphics g) {
        g.drawImage(new ImageIcon("images/meteor.png").getImage(), super.x, super.y, WIDTH, HEIGHT, null);
    }
}
