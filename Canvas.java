/*
 * Name: Abhishek Nimalan, Ayaan Pathan
 * Class: ICS4U1-1A
 * Last Modified: January 27 2022
 * Class Description: The Canvas class is the panel where all the game's elements are drawn.
 */

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import java.io.FileReader;
import java.util.*;

public class Canvas extends JPanel implements Runnable, KeyListener {
    // An instance of the Window class that is passed to this class so we can access
    // methods in there like gameOver()
    Window window;

    // Canvas resolution
    public static final int WIDTH = 600;
    public static final int HEIGHT = 800;

    // Sound effects
    public static Sound powerupGet = new Sound("sound/powerupGet.wav");

    // Timer is used to represent "ticks" in the game.
    public int timer = 0;

    // We want this class running in a separate thread.
    private Thread t;

    // Player ship, and enemy ships
    public Ship ship;
    ArrayList<Enemy> enemies = new ArrayList<Enemy>();
    public Enemy tracker; // There will be one enemy at a time that is actively pursuing the
                          // player ship

    // There will only ever be one powerup on the stage at once
    public Powerup powerup;

    // there will only be one boss per level, if any at all.
    public Boss boss;

    // There will only ever be one meteor raining down at any time
    Meteor meteor;

    // The player's bullets, the stars, and the enemies' bullets.
    ArrayList<Bullet> bullets = new ArrayList<Bullet>();
    ArrayList<Bullet> enemyBullets = new ArrayList<Bullet>();
    public static ArrayList<Stars> stars;

    // These are used to decide the enemy ships' group movement
    int enemyVelocityX = 1;
    int enemyVelocityY = 1;

    // At the beginning of a new level, the enemy ships come down from the top of
    // the screen.
    // This variable is responsible for that functionality
    int entranceOffset = 0;

    // The player's score
    int score;

    // Pre: An instance of the Window class (which is its parent element) so that
    // it can notify its parent of major happenings (like gameovers, moving on to
    // next level, etc.), and a score which is necessary for continuity when moving
    // to new levels.
    // Post: Create player, parse the level from our saved level files, add
    // keylisteners,
    // and begin execution of this class in a fresh Thread.
    // Desc: The Canvas is created with all necessary sub-elements instantiated
    public Canvas(Window window, int level, int lives, int score) {
        // Initialize global variables
        this.window = window;
        this.score = score;

        this.stars = window.stars;

        // Make new playership
        ship = new Ship(lives);

        // Spawn enemies based on level -- we have this info stored in files
        parseLevel(level);
		
        // Canvas dimensions
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setFocusable(true);

        // Listen for keyboard input
        this.addKeyListener(this);

        // Listen for mouse input
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                ship.mousePressed(e);
            }
        });

        // Class run in a thread
        t = new Thread(this);
        t.start();
    }

    /*
     * Pre: The level number that we'd like to parse
     * Post: The function spawns enemies of appropriate types in their appropriate
     * places
     * Desc:
     */
    private void parseLevel(int level) {
        // Spawn enemies based on level -- we have this info stored in files
        // It works as follows:
        // This game is endless. Levels are chosen at random
        // However, every 3 levels, you get a boss fight.
        FileReader reader;
        try {
            // Determine whether it's going to be a random level or a bossfight (every third
            // level)
            if (level % 3 == 0) {
                reader = new FileReader("levels/boss" + (int) (Math.random() * Window.BOSS_LEVEL_COUNT + 1) + ".txt");
            } else {
                reader = new FileReader("levels/level" + (int) (Math.random() * Window.LEVEL_COUNT + 1) + ".txt");
            }
            char character = '_';
            int spawnX = 0;
            int spawnY = 0;
            entranceOffset = 200;
            // A guide: (mess around with the .txt files and create your own levels! Also,
            // no more than 5 enemies in a row.)
            // _ = empty space
            // x = enemy_1
            // v = enemy_2
            // o = enemy_3
            // X = boss
            // new line (\n) = new row of enemies
            while (character != '!') {
                character = (char) reader.read();

                switch (character) {
                    case 'x': {
                        enemies.add(new Enemy(spawnX * 100 + 50, spawnY * 100 + 100 - entranceOffset, 1));
                        spawnX++;
                        break;
                    }
                    case 'v': {
                        enemies.add(new Enemy(spawnX * 100 + 50, spawnY * 100 + 100 - entranceOffset, 2));
                        spawnX++;
                        break;
                    }
                    case 'o': {
                        enemies.add(new Enemy(spawnX * 100 + 50, spawnY * 100 + 100 - entranceOffset, 3));
                        spawnX++;
                        break;
                    }
                    case '_': {
                        spawnX++; // skip over this space since it's _
                        break;
                    }
                    case '\n': {
                        spawnX = 0;
                        spawnY++;
                        break;
                    }
                    case 'X': {
                        boss = new Boss(spawnX * 100 + 50, spawnY * 100 + 100, 20);
                        spawnX++;
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /*
     * Pre: a Graphics context is passed into the function automatically by
     * javax.swing
     * Post: Internally calls upon the draw function, below this.
     * Desc. All elements are refreshed with new positions on screen
     */
    public void paint(Graphics g) {
        Image image = createImage(WIDTH, HEIGHT);
        Graphics graphics = image.getGraphics();
        draw(graphics);
        g.drawImage(image, 0, 0, this);
    }

    /*
     * Pre: The Graphics context in which to draw is passed in to this function by
     * the
     * paint() function above.
     * Post: E V E R Y T H I N G ' S is updated
     * Desc: draw() draws sprites to the screen.
     */
    public void draw(Graphics g) {
        score(g);
        lives(g);

        for (int i = 0; i < stars.size(); i++) {
            stars.get(i).draw(g);
        }
        if (ship.lives > 0)
            ship.draw(g, ship.lives);
        else
            ship.drawExplosion(g);

        if (ship.hasFired) {
            // Spawn Bullet
            bullets.add(new Bullet(1, ship.x + Ship.WIDTH / 2, ship.y + Ship.HEIGHT / 2, 5));
            ship.hasFired = false;
        }

        for (int i = 0; i < bullets.size(); i++) {
            bullets.get(i).draw(g);
        }
        for (int i = 0; i < enemyBullets.size(); i++) {
            enemyBullets.get(i).draw(g);
        }
        for (int i = 0; i < enemies.size(); i++) {
            enemies.get(i).draw(g);
        }
        if (tracker != null) {
            tracker.draw(g);
        }
        if (boss != null) {
            if (boss.isAlive)
                boss.draw(g);
            else
                boss.drawExplosion(g);
        }
        // 1/3000 chance of a meteor strike
        if (meteor == null && (int) (Math.random() * 300) + 1 == 2) {
            meteor = new Meteor(-1, ship.x, 0, 10);
        }
        if (meteor != null) {
            if (meteor.showedWarning)
                meteor.draw(g);
            else
                meteor.drawWarning(g);
        }
        if (powerup != null) {
            powerup.draw(g);
        }
    }

    /*
     * Pre: Needs a Graphics context to draw in -- this is provided to the function
     * by the
     * draw() function above.
     * Post: The score is updated on screen
     * Desc: ^
     */
    private void score(Graphics g) {
        g.setColor(new Color(255, 255, 255));
        g.setFont(new Font("Karmatic Arcade", Font.PLAIN, 19));
        String text = Integer.toString(score);
        int x = Canvas.WIDTH / 2 - g.getFontMetrics().stringWidth(text) / 2;
        int y = 50;
        g.drawString(text, x, y);
    }

    /*
     * Pre: Needs a Graphics context to draw in -- this is provided to the function
     * by the
     * draw() function above.
     * Post: The life count of the player is updated on screen.
     * Desc: ^
     */
    private void lives(Graphics g) {
        int x = 25;
        int y = Canvas.HEIGHT - 60;
        for (int i = 0; i < ship.lives; i++) {
            g.drawImage(new ImageIcon("images/heart.png").getImage(), x, y, 25, 25, null);
            x += 40;
        }
    }

    /*
     * Pre: None
     * Post: Checks for collisions between objects:
     * - Kills enemies that get hit by bullets
     * - Decrements player lives if hit
     * - Allows player to collect powerups, if present
     * - Deals damage to boss, if present
     * Desc: Checks for collisions between objects on screen.
     */
    public void checkCollisions() {
        // Check if player hitbox and powerup (if it exists) collide
        if (powerup != null) {
            if (ship.x + Ship.WIDTH > powerup.x && ship.x < powerup.x + Powerup.WIDTH
                    && ship.y + Ship.HEIGHT > powerup.y && ship.y < powerup.y + Powerup.HEIGHT) {
                ship.lives = Window.MAX_LIVES;
                powerupGet.play(-20.0f);
                powerup = null;
            }
        }

        // Check if enemy hitbox and player bullet hitbox intersect
        for (int i = 0; i < bullets.size(); i++) {
            int bx = bullets.get(i).x; // bullet pos
            int by = bullets.get(i).y;
            for (int j = 0; j < enemies.size(); j++) {
                int ex = enemies.get(j).x; // enemy pos
                int ey = enemies.get(j).y;
                // check if positions intersect
                if (by >= ey && by < ey + Enemy.HEIGHT
                        && bx >= ex && bx < ex + Enemy.WIDTH) {
                    enemies.get(j).takeDamage();
                    bullets.remove(i);
                    score += 100;
                }
            }
            // Since the tracker is not included in the enemies list, we need
            // to check it separately
            if (tracker != null) {
                if (by >= tracker.y && by < tracker.y + Enemy.HEIGHT
                        && bx >= tracker.x && bx < tracker.x + Enemy.WIDTH) {
                    tracker.takeDamage();
                    bullets.remove(i);
                    score += 100;
                }
            }
        }

        // Check if player hitbox and enemy bullet hitbox intersect
        for (int i = 0; i < enemyBullets.size(); i++) {
            int bx = enemyBullets.get(i).x; // bullet pos
            int by = enemyBullets.get(i).y;
            int sx = ship.x; // ship pos
            int sy = ship.y;
            // check if positions intersect
            if (by >= sy - Bullet.HEIGHT && by < sy + Ship.HEIGHT
                    && bx >= sx - Bullet.WIDTH && bx < sx + Ship.WIDTH) {
                    ship.lives--;
                    enemyBullets.remove(i);

                    Ship.shipHurt.play(-15f);
                 
            }
        }

        // Check if player hitbox and enemy hitbox intersect
        for (int i = 0; i < enemies.size(); i++) {
            int ex = enemies.get(i).x; // enemy pos
            int ey = enemies.get(i).y;
            int sx = ship.x; // ship pos
            int sy = ship.y;
            // check if positions intersect
            if (ey >= sy - Enemy.HEIGHT && ey < sy + Ship.HEIGHT
                    && ex >= sx - Enemy.WIDTH && ex < sx + Ship.WIDTH) {
                    ship.lives--;
                    enemies.get(i).takeDamage();
                    Ship.shipHurt.play(-15f);
            }
        }

        // Check if player hitbox and boss hitbox intersect
        if (boss != null) {
            int bx = boss.x; // boss pos
            int by = boss.y;
            int sx = ship.x; // ship pos
            int sy = ship.y;
            // check if positions intersect
            if (by >= sy - Boss.HEIGHT && by < sy + Ship.HEIGHT
                    && bx >= sx - Boss.WIDTH && bx < sx + Ship.WIDTH) {
                // Boss is a one hit kill, so we don't need to check if the player has more than
                // 1 life
                window.gameOver(score);
                t.interrupt(); // Stop the thread, or else the "window.gameOver()" function will be repeatedly
                // called.
            }
        }

        // Check if player bullet hitbox and boss hitbox intersect
        if (boss != null) {
            for (int i = 0; i < bullets.size(); i++) {
                int bx = bullets.get(i).x; // bullet pos
                int by = bullets.get(i).y;
                int sx = boss.x; // boss pos
                int sy = boss.y;
                // check if positions intersect
                if (by >= sy - Boss.HEIGHT && by < sy + Boss.HEIGHT
                        && bx >= sx - Boss.WIDTH && bx < sx + Boss.WIDTH) {
                    bullets.remove(i); // otherwise, the bullet will continue to damage the boss while passing through
                    // it.
                    boss.takeDamage();
                }
            }
        }

        // Check if player hitbox and meteor hitbox intersect
        if (meteor != null) {

            int bx = meteor.x; // bullet pos
            int by = meteor.y;
            int sx = ship.x; // ship pos
            int sy = ship.y;
            // check if positions intersect
            if (by >= sy - Meteor.HEIGHT && by < sy + Ship.HEIGHT
                    && bx >= sx - Meteor.WIDTH && bx < sx + Ship.WIDTH) {
                meteor = null;
                    ship.lives--;
                    Ship.shipHurt.play(-15f);
            }

        }

        // Prevent player ship from leaving screen
        if (ship.x < 0) {
            ship.x = 0;
        } else if ((ship.x + 75) > WIDTH) {
            ship.x = (WIDTH - 80);
        }
        if (ship.y < 0) {
            ship.y = (0);
        } else if ((ship.y + 75) > HEIGHT) {
            ship.y = (HEIGHT - 80);
        }

        if (tracker != null) {
            // We want the actively tracking enemy to respawn at top of screen instead of
            // dying when leaving the screen
            if (tracker.y > HEIGHT) {
                tracker.y = 0;
            }
            // Also, if the tracking enemy collides with the player, the player dies.
            int ex = tracker.x; // enemy pos
            int ey = tracker.y;
            int sx = ship.x; // ship pos
            int sy = ship.y;
            // check if positions intersect
            if (ey >= sy - Enemy.HEIGHT && ey < sy + Ship.HEIGHT
                    && ex >= sx - Enemy.WIDTH && ex < sx + Ship.WIDTH) {
                    ship.lives--;
                    Ship.shipHurt.play(-15f);
                    tracker = null;
               
            }
        }
        // Bring boss back to the top if it leaves the screen.
        if (boss != null) {
            if (boss.y > HEIGHT) {
                boss.y = 0;
            }
        }

        if (ship.lives <= 0 && ship.explosionFinished) {
            window.gameOver(score);
            t.interrupt();
        }

        // Remove boss and tracker if they are dead.
        if (boss != null) {
            if (!boss.isAlive && boss.explosionFinished) {
                boss = null;
                score += 1000;
            }
        }
        if (tracker != null) {
            if (!tracker.isAlive) {
                tracker = null;
            }
        }

        // Remove bullets, enemy bullets, and stars from their respective lists if they
        // leave the screen.
        bullets.removeIf(temp -> (temp.y < 0));
        enemyBullets.removeIf(temp -> (temp.y > Canvas.HEIGHT));
        stars.removeIf(temp -> (temp.y > Canvas.HEIGHT));

        if (stars.size() < Stars.amount)
            stars.add(new Stars());

        // Remove enemies from the list if they are dead.
        enemies.removeIf(temp -> (!temp.isAlive));
    }

    /*
     * Pre: None
     * Post: Update position of all objects on screen.
     * - Move enemies, move player, move boss if present
     * - Responsible for movement patterns of enemy swarm
     * - Also responsible for having one enemy track you at a time
     * - Finally, is responsible for spawning bullets from enemies and player.
     */
    public void move() {

        for (int i = 0; i < stars.size(); i++) {
            stars.get(i).move();
        }

        // move player ship
        if (ship.lives > 0)
            ship.move();

        // Spawn a powerup every now and then, but make it rare
        if (timer % 50 == 0 && Math.random() > 0.95 && powerup == null) {
            int x = (int) (Math.random() * WIDTH) - Powerup.WIDTH;
            int y = (int) (Math.random() * HEIGHT) - Powerup.HEIGHT;
            powerup = new Powerup(x, y);
        }

        // Enemies enter the screen from the top
        if (entranceOffset > 0) {
            for (int i = 0; i < enemies.size(); i++) {
                enemies.get(i).y += 3;
            }
            entranceOffset -= 2;
        }

        // This code deals with the specific enemy ship swooping down at the player.
        int randnum = (int) (Math.random() * (enemies.size() - 1)); // This decides what enemy will swoop down at the
        // player
        if (tracker == null) {
            if (enemies.size() > 0) {
                tracker = enemies.get(randnum);
                enemies.remove(randnum);
            } else if (boss == null) {
                Window.lastXPosition = ship.x;
                Window.lastYPosition = ship.y;
                window.nextLevel(score, ship.lives);
                t.interrupt(); // Stop the thread, or else the "window.nextLevel()" function will be repeatedly
                // called.
            }
        } else {
            tracker.track(ship.x);
        }

        // Move bullets, enemies, and the stars.
        for (int i = 0; i < bullets.size(); i++) {
            bullets.get(i).move();
        }
        for (int i = 0; i < enemyBullets.size(); i++) {
            enemyBullets.get(i).move();
        }
        for (int i = 0; i < enemies.size(); i++) {
            enemies.get(i).move();
        }

        // All this happens every 50 game ticks:
        if (timer % 50 == 0) {
            // Causes ships to zig-zag and dodge stuff.
            enemyVelocityX = -enemyVelocityX;
            // The ships will move up and down at random
            if (Math.random() > 0.5) {
                enemyVelocityY = -enemyVelocityY;
            }
            // However, if the enemy ships move too far down or too far up, get them to move
            // upwards/downwards again
            if (enemies.size() > 0) {
                if (enemies.get(0).y > HEIGHT / 5) {
                    enemyVelocityY = -Math.abs(enemyVelocityY);
                } else if (enemies.get(0).y < 0) {
                    enemyVelocityY = Math.abs(enemyVelocityY);
                }
            }
            for (int i = 0; i < enemies.size(); i++) {
                enemies.get(i).setXVelocity(enemyVelocityX);
                enemies.get(i).setYVelocity(enemyVelocityY);
            }
            // Choose a random ship that will shoot
            randnum = (int) (Math.random() * (enemies.size()));
            // Spawn a bullet at the location of the ship, so long as there are ships left
            if (enemies.size() > 0) {
                enemyBullets.add(new Bullet(-1, enemies.get(randnum).x, enemies.get(randnum).y, 5));
            }
            // Also have the currently tracking ship shoot a bullet if it is roughly in line
            // with the player, if it is alive
            if (tracker != null) {
                if (Math.abs(tracker.x - ship.x) < 50) {
                    enemyBullets.add(new Bullet(-1, tracker.x, tracker.y, 5));
                }
            }
        }

        // This happens every 300 game ticks (Only applies to the boss, if it is present
        // in the level)
        if (boss != null) {
            // This is the period in which the boss is either vibrating, or swooping down at
            // the player
            if (timer >= 200 && boss.isAlive) {
                // This means that for 25 game ticks, the boss will vibrate around a little, as
                // a warning to the player that it is about to swoop downwards
                if (timer < 225 && timer % 5 == 0) {
                    boss.setXVelocity(boss.VIBRATE_SPEED);
                    boss.VIBRATE_SPEED = -boss.VIBRATE_SPEED;
                }
                if (timer == 225) {
                    boss.dash();
                }
                if (timer == 299) {
                    boss.y = 50; // To reset the boss to its original position.
                }
                boss.move();
            }

            // Boss tracks ship and shoots bullets.
            if (timer < 200) {
                if (timer % 50 == 0) {
                    enemyBullets.add(new Bullet(-1, (boss.x + (boss.width / 2)), boss.y, 8));
                }
                boss.track(ship.x);
            }
        }

        // Reset timer.
        if (timer == 300) {
            timer = 0;
        }

        timer++;

        // Update meteor position after warning animation has played.
        // Remove meteor if it leaves the screen.
        if (meteor != null && meteor.showedWarning) {
            if (meteor.y < Canvas.HEIGHT) {
                meteor.move();
            } else {
                meteor = null;
            }
        }
    }

    // Key listening.
    @Override
    public void keyPressed(KeyEvent e) {
        ship.keyPressed(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        ship.keyReleased(e);
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    // Game loop.
    public void run() {

        long lastTime = System.nanoTime();
        double amountOfTicks = 60;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        long now;

        this.requestFocus();

        while (!Thread.interrupted()) {
            now = System.nanoTime();
            delta = delta + (now - lastTime) / ns;
            lastTime = now;

            if (delta >= 1) {
                move();
                repaint();
                checkCollisions();
                delta--;
            }
        }
    }
}
