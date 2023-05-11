/*
This class is for the Boss that you encounter every 3 levels.
It extends the enemy class, since most of its functionality is the same.
*/

import javax.print.event.PrintJobListener;
import javax.swing.*;
import java.awt.*;

public class Boss extends Enemy {

    public int maxHealth;

    // All the other variables are inherited from the parent class. Quite
    // convenient. We override whatever we want to change.
    public static final int WIDTH = 100;
    public static final int HEIGHT = 100;

    public final int SWOOP_SPEED = 12; // The speed at which the boss dashes downwards
    public int VIBRATE_SPEED = 5; // This is speed at which it vibrates before dashing

    int frameNumber = 0;
    int explosionDelay = 0;
    boolean explosionFinished = false;

    final String[] BOSS_EXPLOSION_FRAMES = new String[] 
    {
        "images/explosion_1.png",
        "images/explosion_2.png",
        "images/explosion_3.png",
        "images/explosion_4.png",
        "images/explosion_5.png",
        "images/explosion_6.png",
        "images/explosion_7.png",
        "images/explosion_8.png",
        "images/explosion_9.png",
        "images/explosion_10.png"
    };

    // Pre: the x and y coordinates to spawn the boss at, and how much health it
    // should have.
    // Post: Boss is created at said coords, with said health.
    // Desc: Used to spawn a boss at a given location with a certain amount of hp
    public Boss(int x, int y, int health) {
        super(x, y, 1);
        super.health = health; // Override the health var in the Enemy class
        maxHealth = health;
    }

    // Pre: none
    // Post: Boss swoops downward
    // Desc: This causes the boss to swwop downwards to the player in an attempt to
    // attack it.
    // It moves strictly downwards, and cannot move side to side while dashing.
    public void dash() {
        setXVelocity(0);
        setYVelocity(SWOOP_SPEED);
    }

    // Pre: the x-coordinate of the point the boss is to track
    // Post: The velocity is set to be in the direction of the point it is tracking.
    // The
    // velocity is greater the farther away the Boss is from the point it is
    // tracking
    // Desc: This is what allows the boss to move to the player's locatoin and fire
    // off shots.
    // We override the default track function so boss only moves side to side and
    // not downwards towards the player.
    @Override
    public void track(int trackX) {
        xVelocity = (trackX - x) / 50;
        yVelocity = 0; // We don't want this ship to swoop downwards while tracking
        move();
    }

    public void drawExplosion(Graphics g) {
        int interval = 8;

        if (explosionDelay % interval == 0) 
        {
            frameNumber++;
        }
        
        if (frameNumber >= BOSS_EXPLOSION_FRAMES.length) {
            frameNumber = BOSS_EXPLOSION_FRAMES.length;
            explosionFinished = true;
        }
        else
        {
            g.drawImage(new ImageIcon(BOSS_EXPLOSION_FRAMES[frameNumber]).getImage(), super.x  - WIDTH, super.y - HEIGHT, 300, 300, null);
        }
        
        explosionDelay++;
    }

    // Pre: none
    // Post: The Boss is drawn on the screen
    // Desc: We override the Enemy class's draw function because the boss has
    // different dimensions, and a different sprite
    @Override
    public void draw(Graphics g) {
        // The sprite has two frames, switching every 20 game ticks
        if (delay == 20) {
            firstFrame = !firstFrame;
            delay = 0;
        }
        delay++;
        // Draw the corresponding frame of the boss
        if (firstFrame)
            g.drawImage(new ImageIcon("images/boss_1.png").getImage(), x, y, WIDTH, HEIGHT, null);
        else
            g.drawImage(new ImageIcon("images/boss_2.png").getImage(), x, y, WIDTH, HEIGHT, null);

        // Draw the boss's health bar (changes colour depending on the amount of health
        // the boss has)
        for (int i = 0; i < super.health; i++) {
            if (super.health < maxHealth / 4)
                g.drawImage(new ImageIcon("images/boss_low_hp.png").getImage(), i * 20 + 100, 10, 20, 5, null); // RED
            else if (super.health < maxHealth / 2)
                g.drawImage(new ImageIcon("images/boss_medium_hp.png").getImage(), i * 20 + 100, 10, 20, 5, null); // YELLOW
            else
                g.drawImage(new ImageIcon("images/boss_high_hp.png").getImage(), i * 20 + 100, 10, 20, 5, null); // GREEN
        }
    }
}
