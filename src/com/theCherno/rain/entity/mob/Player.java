package com.theCherno.rain.entity.mob;

import com.theCherno.rain.Game;
import com.theCherno.rain.entity.projectile.Projectile;
import com.theCherno.rain.entity.projectile.WizardProjectile;
import com.theCherno.rain.events.Event;
import com.theCherno.rain.events.EventDispatcher;
import com.theCherno.rain.events.EventListener;
import com.theCherno.rain.events.types.MousePressedEvent;
import com.theCherno.rain.events.types.MouseReleasedEvent;
import com.theCherno.rain.graphics.AnimatedSprite;
import com.theCherno.rain.graphics.Screen;
import com.theCherno.rain.graphics.Sprite;
import com.theCherno.rain.graphics.SpriteSheet;
import com.theCherno.rain.graphics.ui.UIActionListener;
import com.theCherno.rain.graphics.ui.UIButton;
import com.theCherno.rain.graphics.ui.UIButtonListener;
import com.theCherno.rain.graphics.ui.UILabel;
import com.theCherno.rain.graphics.ui.UIManager;
import com.theCherno.rain.graphics.ui.UIPanel;
import com.theCherno.rain.graphics.ui.UIProgressBar;
import com.theCherno.rain.input.Keyboard;
import com.theCherno.rain.input.Mouse;
import com.theCherno.rain.util.ImageUtils;
import com.theCherno.rain.util.Vector2i;

import javax.imageio.ImageIO;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class Player extends Mob implements EventListener {

    private final String name;
    private final Keyboard input;
    private Sprite sprite;
    private final int anim = 0;
    private boolean walking = false;
    private final AnimatedSprite down = new AnimatedSprite(SpriteSheet.player_down, 32, 32, 3);
    private final AnimatedSprite up = new AnimatedSprite(SpriteSheet.player_up, 32, 32, 3);
    private final AnimatedSprite left = new AnimatedSprite(SpriteSheet.player_left, 32, 32, 3);
    private final AnimatedSprite right = new AnimatedSprite(SpriteSheet.player_right, 32, 32, 3);

    private AnimatedSprite animSprite = down;
    private int fireRate = 0;
    private UIProgressBar uiHealthBar;
    private BufferedImage image;
    private boolean shooting = false;

    @Deprecated
    public Player(String name, Keyboard input) {
        this.name = name;
        this.input = input;
        sprite = Sprite.player_forward;
        animSprite = down;

    }

    public Player(int x, int y, Keyboard input, String name) {
        this.x = x;
        this.y = y;
        this.input = input;
        this.name = name;
        fireRate = WizardProjectile.FIRE_RATE;

        UIManager ui = Game.getUIManager();
        UIPanel panel = (UIPanel) new UIPanel(new Vector2i((300 - 80) * 3, 0),
                new Vector2i(80 * 3, 168 * 3)).setColor(0x4f4f4f);
        ui.addPanel(panel);
        UILabel newLabel = new UILabel(new Vector2i(40, 200), name);
        newLabel.setColor(0xbbbbbb);
        newLabel.setFont(new Font("Verdana", Font.BOLD, 24));
        newLabel.dropShadow = true;
        panel.addComponent(newLabel);

        uiHealthBar = new UIProgressBar(new Vector2i(10, 210), new Vector2i(220, 20));
        uiHealthBar.setColor(0x6a6a6a);
        uiHealthBar.setForegroundColor(0xee3030);
        panel.addComponent(uiHealthBar);

        UILabel hpLabel = new UILabel(new Vector2i(uiHealthBar.position).add(new Vector2i(2, 16)), "HP");
        hpLabel.setColor(0xffffff);
        hpLabel.setFont(new Font("Verdana", Font.PLAIN, 18));
        panel.addComponent(hpLabel);
        // Player default attributes
        health = 100;

        UIButton button = new UIButton(new Vector2i(10, 260), new Vector2i(100, 30), new UIActionListener() {
            public void perform() {
                System.out.println("Button presses");
            }
        });
        button.setButtonListener(new UIButtonListener() {
            public void pressed(UIButton button) {
                button.performAction();
                button.ignoreNextPress();
            }
        });
        button.setText("Hello");
        panel.addComponent(button);

        try {
            image = ImageIO.read(new File("C:\\Users\\Shreyansh\\Desktop\\Coding_stuff\\java_files\\JavaGames\\chernoGame\\res\\textures\\home.png"));
            System.out.println("The type of image is " + image.getType());
        } catch(IOException e) {
            e.printStackTrace();
        }

        UIButton imageButton = new UIButton(new Vector2i(10, 360), image, new UIActionListener() {
            public void perform() {
                System.exit(0);
            }
        });
        imageButton.setButtonListener(new UIButtonListener() {
            public void entered(UIButton button) {
                button.setImage(ImageUtils.changeBrightness(image, -50));
            }

            public void exited(UIButton button) {
                button.setImage(image);
            }

            public void pressed(UIButton button) {
                button.setImage(ImageUtils.changeBrightness(image, -50));
            }

            public void released(UIButton button) {
                button.setImage(image);
            }
        });
        panel.addComponent(imageButton);
    }

    public String getName() {
        return name;
    }

    public void onEvent(Event event) {
        EventDispatcher dispatcher = new EventDispatcher(event);
        dispatcher.dispatch(Event.Type.MOUSE_PRESSED, (Event e) -> onMousePressed((MousePressedEvent)e));
        dispatcher.dispatch(Event.Type.MOUSE_RELEASED, (Event e) -> onMouseReleased((MouseReleasedEvent)e));
    }

    public void update() {
        if(walking) animSprite.update();
        else animSprite.setFrame(0);
        if(fireRate > 0) fireRate--;
        double xa = 0, ya = 0;
        double speed = 2;

        if(input.up) {
            animSprite = up;
            ya -= speed;
        } else if(input.down) {
            animSprite = down;
            ya += speed;
        }
        if(input.left) {
            animSprite = left;
            xa -= speed;
        }else if(input.right) {
            animSprite = right;
            xa += speed;
        }

        if(xa != 0 || ya != 0) {
            move(xa, ya);
            walking = true;
        }else{
            walking = false;
        }
        clear();
        updateShooting();

        uiHealthBar.setProgress(health / 100.0);
    }

    private void updateShooting() {
        if(!shooting || fireRate > 0) return;
        double dx = Mouse.getX() - (double)(Game.getWindowWidth() / 2) ;
        double dy = Mouse.getY() - (double)Game.getWindowHeight() / 2;
        double dir = Math.atan2(dy, dx);
        shoot(x, y, dir);
        fireRate = WizardProjectile.FIRE_RATE;
    }

    public boolean onMousePressed(MousePressedEvent e) {
        if (Mouse.getX() > 660)
            return false;

        if(e.getButton() == MouseEvent.BUTTON1) {
            shooting = true;
            return true;
        }
        return false;
    }

    public boolean onMouseReleased(MouseReleasedEvent e) {
        if(e.getButton() == MouseEvent.BUTTON1) {
            shooting = false;
            return true;
        }
        return false;
    }

    private void clear() {
        for(int i = 0; i < level.getProjectiles().size(); i++) {
            Projectile p = level.getProjectiles().get(i);
            if(p.isRemoved()) level.getProjectiles().remove(i);
        }
    }

    public void render(Screen screen) {
        sprite = animSprite.getSprite();
        screen.renderMob((int) (x - 16), (int) (y - 16), sprite);
    }

}
