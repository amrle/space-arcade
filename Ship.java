/*
 * Name: Abhishek Nimalan, Ayaan Pathan
 * Class: ICS4U1-1A
 * Last Modified: January 27 2022
 * Class Description: This class defines the player's ship. Its dimensions, sounds, lives,                          velocity vectors, sprite animations, etc.
*/

import java.awt.*;
import java.awt.event.*;
import java.util.Date;
import javax.swing.*;

public class Ship extends Rectangle {

    public final int SPEED = 5; // movement speed of ship
    // Hitbox Dimensions
    public static final int WIDTH = 60;
    public static final int HEIGHT = 60;

    // Variables relating to ship's shooting functionality
    public long lastFired = new Date().getTime();
    public boolean hasFired = false;

    // Sounds
    public static Sound laserFire = new Sound("sound/laser.wav");
    public static Sound shipHurt = new Sound("sound/playerHurt.wav");
    public static Sound shipDeath = new Sound("sound/playerDeath.wav");
    public boolean deathSoundPlayed = false;
    // Velocity vectors
    public int yVelocity;
    public int xVelocity;
	// Ship lives/health
    public int lives;
    // For animating the ship sprite
    private boolean firstFrame = true;
    private int delay = 0;
    // For animating the ship's explosion when it dies.
    int frameNumber = -1;
    int explosionDelay = 0;
    boolean explosionFinished = false;
    final String[] SHIP_EXPLOSION_FRAMES = new String[] 
    {
        "images/ship_explosion_1.png",
        "images/ship_explosion_2.png",
        "images/ship_explosion_3.png",
        "images/ship_explosion_4.png",
        "images/ship_explosion_5.png",
        "images/ship_explosion_6.png",
        "images/ship_explosion_7.png",
        "images/ship_explosion_8.png",
        "images/ship_explosion_9.png",
        "images/ship_explosion_10.png",
    };


    // Constructor. Pass in number of lives, and you're good to go.
    public Ship(int lives) {
        super(Window.lastXPosition, Window.lastYPosition, WIDTH, HEIGHT);
        this.lives = lives;
        
    }


    // Keylisteners - Arrow keys
    public void keyPressed(KeyEvent e) {
        if (e.getKeyChar() == 'd' || e.getKeyCode() == KeyEvent.VK_RIGHT) {
            setXDirection(SPEED);
            move();
        }

        if (e.getKeyChar() == 'a' || e.getKeyCode() == KeyEvent.VK_LEFT) {
            setXDirection(SPEED * -1);
            move();
        }

        if (e.getKeyChar() == 'w' || e.getKeyCode() == KeyEvent.VK_UP) {
            setYDirection(SPEED * -1);
            move();
        }

        if (e.getKeyChar() == 's' || e.getKeyCode() == KeyEvent.VK_DOWN) {
            setYDirection(SPEED);
            move();
        }
    }
    // Keylisteners - WASD
    public void keyReleased(KeyEvent e) {
        if (e.getKeyChar() == 'd' || e.getKeyCode() == KeyEvent.VK_RIGHT) {
            setXDirection(0);
            move();
        }

        if (e.getKeyChar() == 'a' || e.getKeyCode() == KeyEvent.VK_LEFT) {
            setXDirection(0);
            move();
        }

        if (e.getKeyChar() == 'w' || e.getKeyCode() == KeyEvent.VK_UP) {
            setYDirection(0);
            move();
        }

        if (e.getKeyChar() == 's' || e.getKeyCode() == KeyEvent.VK_DOWN) {
            setYDirection(0);
            move();
        }

        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            if (lastFired + 200 < new Date().getTime()) {
                lastFired = new Date().getTime();
                hasFired = true;
                laserFire.play(-15.0f);
            }

        }
    }
    // Mouse listener 
    public void mousePressed(MouseEvent e) {
        if (lastFired + 200 < new Date().getTime()) {
            lastFired = new Date().getTime();
            hasFired = true;
            laserFire.play(-15.0f);
        }
    }


    // Setters for our velocities
    public void setYDirection(int yDirection) {
        yVelocity = yDirection;
    }
    public void setXDirection(int xDirection) {
        xVelocity = xDirection;
    }


    // update ship's position according to velocity.
    public void move() {
        y = y + yVelocity;
        x = x + xVelocity;
    }



    // Draw ship
    public void draw(Graphics graphics, int currentLives) {
        Image shipImage = null;
        Graphics2D g = (Graphics2D) graphics;
        if (delay == 20) {
            firstFrame = !firstFrame;
            delay = 0;
        }
        if (firstFrame) {
            
            // If ship is on its last life, change the ship's opacity to create a blinking effect.
            if (currentLives == 1)
                g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));

            g.drawImage(new ImageIcon("images/ship_1.png").getImage(), x, y, WIDTH, HEIGHT, null);
        } else {
            g.drawImage(new ImageIcon("images/ship_2.png").getImage(), x, y, WIDTH, HEIGHT, null);
        }

        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        delay++;
        g.drawImage(shipImage, x, y, WIDTH, HEIGHT, null);
    }



    // Draws ship's death animation
    public void drawExplosion(Graphics g) 
    {
        // We want the explosion sound effect to be played at the same time as the animation.
        // run it asynchronously in a thread, but only once.
        if(!deathSoundPlayed) {
            new Thread() {
                public void run() {
                    shipDeath.play(-5.0f);
                }
            }.start();
            deathSoundPlayed = true;
        }
        int interval = 5;

        if (explosionDelay % interval == 0)           
            frameNumber++;
        
        if (frameNumber >= SHIP_EXPLOSION_FRAMES.length)
        {
            frameNumber = SHIP_EXPLOSION_FRAMES.length;
            explosionFinished = true;
            explosionDelay = 0;
        }
        else
        {
            g.drawImage(new ImageIcon(SHIP_EXPLOSION_FRAMES[frameNumber]).getImage(), x - WIDTH , y - HEIGHT, 200, 200, null);
        }
        
        explosionDelay++;
    }

    
}
