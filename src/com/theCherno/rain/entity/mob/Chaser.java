package com.theCherno.rain.entity.mob;

import com.theCherno.rain.graphics.AnimatedSprite;
import com.theCherno.rain.graphics.Screen;
import com.theCherno.rain.graphics.SpriteSheet;

import java.util.List;

public class Chaser extends Mob{

    private final AnimatedSprite down = new AnimatedSprite(SpriteSheet.dummy_down, 32, 32, 3);
    private final AnimatedSprite up = new AnimatedSprite(SpriteSheet.dummy_up, 32, 32, 3);
    private final AnimatedSprite left = new AnimatedSprite(SpriteSheet.dummy_left, 32, 32, 3);
    private final AnimatedSprite right = new AnimatedSprite(SpriteSheet.dummy_right, 32, 32, 3);

    private AnimatedSprite animSprite = down;

    private double xa = 0;
    private double ya = 0;
    private double speed = 1.2;

    public Chaser(int x, int y) {
        this.x = x << 4;
        this.y = y << 4;
        sprite = animSprite.getSprite();
    }

    public void update() {
        move();
        if(walking) animSprite.update();
        else animSprite.setFrame(0);

        if(ya < 0) {
            animSprite = up;
            dir = Direction.UP;
        } else if(ya > 0) {
            animSprite = down;
            dir = Direction.DOWN;
        }
        if(xa < 0) {
            animSprite = left;
            dir = Direction.LEFT;
        }else if(xa > 0) {
            animSprite = right;
            dir = Direction.RIGHT;
        }

    }

    private void move() {
        xa = 0;
        ya = 0;
        List<Player> players = level.getPlayers(this, 5000);
        if(players.size() > 0) {
            Player player = players.get(0);
            if (x < player.getX()) xa += speed;
            if (x > player.getX()) xa -= speed;
            if (y < player.getY()) ya += speed;
            if (y > player.getY()) ya -= speed;
        }
        if(xa != 0 || ya != 0) {
            move(xa, ya);
            walking = true;
        }else{
            walking = false;
        }
    }

    public void render(Screen screen) {
        sprite = animSprite.getSprite();
        screen.renderMob((int) (x - 16), (int) (y - 16), this);
    }
}
