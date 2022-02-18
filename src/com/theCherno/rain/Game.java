package com.theCherno.rain;

import com.theCherno.rain.entity.mob.Player;
import com.theCherno.rain.events.Event;
import com.theCherno.rain.events.EventListener;
import com.theCherno.rain.graphics.Screen;
import com.theCherno.rain.graphics.ui.UIManager;
import com.theCherno.rain.input.Keyboard;
import com.theCherno.rain.input.Mouse;
import com.theCherno.rain.layers.Layer;
import com.theCherno.rain.level.Level;
import com.theCherno.rain.level.TileCoordinate;

import javax.swing.JFrame;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Game extends Canvas implements Runnable, EventListener {

    private static final int width = 300 - 80; // Game Screen width
    private static final int height = 168; // Game Screen height
    private static final int scale = 3; // The scale of game screen
    public static String title = "Rain";

    private Thread gameThread;
    private final JFrame frame;
    private final Keyboard key;
    private final Level level;
    private final Player player;
    private boolean running = false;

    private static UIManager uiManager;

    private final Screen screen;
    private final BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    private final int[] pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();

    private final List<Layer> layerStack = new ArrayList<Layer>();
    /**
     * The constructor for this class, where everything gets initialised
     */
    public Game() throws IOException {
        Dimension size = new Dimension(width*scale + 80 * 3, height*scale);
        setPreferredSize(size);

        screen = new Screen(width, height);
        uiManager = new UIManager();
        frame = new JFrame();
        key = new Keyboard();
        level = Level.spawn;
        addLayer(level);
        TileCoordinate playerSpawn = new TileCoordinate(35, 39);
        player = new Player(playerSpawn.x(), playerSpawn.y(), key, "Shreyansh");
        level.add(player);

        addKeyListener(key);

        Mouse mouse = new Mouse(this);
        addMouseListener(mouse);
        addMouseMotionListener(mouse);
    }

    public static int getWindowWidth() {
        return width * scale;
    }

    public static int getWindowHeight() {
        return height * scale;
    }

    public static UIManager getUIManager() {
        return uiManager;
    }

    public void addLayer(Layer layer) {
        layerStack.add(layer);
    }

    public synchronized void start(){
        running = true;
        gameThread = new Thread(this, "Display");
        gameThread.start();
    }

    /**
     * Method to end the game thread
     */
    public synchronized void stop(){
        running = false;
        try{
            gameThread.join();
        } catch(InterruptedException e){
            e.printStackTrace();
        }
    }

    public void run() {
        long lastTime = System.nanoTime();
        long timer = System.currentTimeMillis();
        final double ns = 1000000000.0 / 60.0;
        double delta = 0;
        int frames = 0;
        int updates = 0;
        requestFocus();
        while (running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            while (delta >= 1) {
                update();
                updates++;
                delta--;
            }
            render();
            frames++;

            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                frame.setTitle(title + "  |  " + updates + " ups, " + frames + " fps");
                updates = 0;
                frames = 0;
            }
        }
        stop();
    }

    public void onEvent(Event event) {
        for (int i = layerStack.size() - 1; i >= 0; i--) {
            layerStack.get(i).onEvent(event);
        }
    }

    public void update() {
        key.update();
        uiManager.update();

        // Update layers here
        level.update();
    }

    public void render() {
        BufferStrategy bs = getBufferStrategy();
        if(bs == null) {
            createBufferStrategy(3);
            return;
        }

        screen.clear();
        double xScroll = player.getX() - (double)screen.width / 2;
        double yScroll = player.getY() - (double)screen.height / 2;
        level.setScroll((int) xScroll,(int) yScroll);

        // Render layers here
        level.render(screen);
        //font.render(50, 50, -4, 0xffaaaa, "Yo\nI am Shreyansh", screen);
        for(int i = 0; i < pixels.length; i++) {
            pixels[i] = screen.pixels[i];
        }

        Graphics g = bs.getDrawGraphics();
        g.drawImage(image, 0, 0, width * scale, height * scale, null);
        g.setColor(new Color(0xff00ff));
        //g.fillRect(303, 204, 16*3, 32*3);
        uiManager.render(g);

        g.dispose();
        bs.show();
    }

    public static void main(String[] args) throws IOException {
        Game game = new Game();
        game.frame.setResizable(false);
        game.frame.setTitle(Game.title);
        game.frame.add(game);
        game.frame.pack();
        game.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        game.frame.setLocationRelativeTo(null);
        game.frame.setVisible(true);

        game.start();
    }
}
