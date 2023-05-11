/*
 * Name: Abhishek Nimalan, Ayaan Pathan
 * Class: ICS4U1-1A
 * Last Modified: January 27 2022
 * Class Description: This class defines the powerup which restores the player's lives.
*/

import javax.swing.*;
import java.awt.*;

public class Powerup {

    // Dimensions
    public static final int WIDTH = 50;
    public static final int HEIGHT = 45;
    // Spawn loc
    public int x;
    public int y;

    // Constructor. Simply pass in a place to spawn the powerup
    public Powerup(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // This is used to display the powerup to the player.
    public void draw(Graphics g) {
        g.drawImage(new ImageIcon("images/hp_powerup.png").getImage(), x, y, WIDTH, HEIGHT, null);
    }

}
