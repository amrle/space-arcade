
/*
 * Name: Abhishek Nimalan, Ayaan Pathan
 * Class: ICS4U1-1A
 * Last Modified: January 27 2022
 * Class Description: This class defines enemies. Their health, their type, their dimensions,                       and any associated sounds.
 */

import java.awt.*;
import javax.swing.*;

public class Enemy extends Rectangle {

    // The reason these are public is because having a bunch of getters and setters
    // is just a big hassle.
    public final int SWOOP_SPEED = 3;  
    public static final int WIDTH = 50;
    public static final int HEIGHT = 50;
    // Velocity vectors
    public int xVelocity = 2; 
    public int yVelocity = 1;
    // Used to check if enemy is dead and should be removed
    public boolean isAlive = true;
    // Enemy properties
    public int enemyType;
    public int health;
    // sound fx
    public static Sound enemyHurt = new Sound("sound/enemyHurt.wav");
    // These vars allow us the animate the sprite of the enemy.
    public boolean firstFrame = true;
    public int delay = 0;


    /*
    Constructor
     * Desc: Spawns enemies and instantiates their properties.
             Enemy type decides what sprite to use and how much hp.
    */
    public Enemy(int spawnX, int spawnY, int enemyType) {
        super(spawnX, spawnY, WIDTH, HEIGHT);
        this.enemyType = enemyType;
        this.health = enemyType; // coincidentally, the health we want for the enemy coincides with its type.
    }


    // Setters for our velocity vectors. Responsible for dynamic movement of enemies
    public void setYVelocity(int yDirection) {
        yVelocity = yDirection;
    }
    public void setXVelocity(int xDirection) {
        xVelocity = xDirection;
    }



    // This function has an enemy ship swoop down towards the player
    public void track(int trackX) {
        xVelocity = (trackX - x) / 50;
        yVelocity = SWOOP_SPEED;
        move(); // Internally calls upon move, so we don't need to call track() and move()
        // separately in the Canvas class.
    }



    // Applies the x and y velocity to the enemy (moves the enemy)
    public void move() {
        x += xVelocity;
        y += yVelocity;
    }



    // Called when the enemy in question is hit and takes one health away from it.
    // If it runs out of health, it kills the enemy.
    public void takeDamage() {
        health--;
        enemyHurt.play(-10.0f);
        if (health == 0) {
            this.isAlive = false;
        }
    }


    // Function responsible for drawing the enemy on canvas
    public void draw(Graphics g) {
        // Cycles through the animation loop.
        if (delay == 20) {
            firstFrame = !firstFrame;
            delay = 0;
        }
        // Draw the enemy based on what frame it is at, and what type it is.
        switch (enemyType) {
            case 1:
                if (firstFrame)
                    g.drawImage(new ImageIcon("images/enemy_1_1.png").getImage(), x, y, WIDTH, HEIGHT, null);
                else
                    g.drawImage(new ImageIcon("images/enemy_1_2.png").getImage(), x, y, WIDTH, HEIGHT, null);
                break;
            case 2:
                if (firstFrame)
                    g.drawImage(new ImageIcon("images/enemy_2_1.png").getImage(), x, y, WIDTH, HEIGHT, null);
                else
                    g.drawImage(new ImageIcon("images/enemy_2_2.png").getImage(), x, y, WIDTH, HEIGHT, null);
                break;
            case 3:
                if (firstFrame)
                    g.drawImage(new ImageIcon("images/enemy_3_1.png").getImage(), x, y, WIDTH, HEIGHT, null);
                else
                    g.drawImage(new ImageIcon("images/enemy_3_2.png").getImage(), x, y, WIDTH, HEIGHT, null);
                break;
        }

        delay++;
    }
}