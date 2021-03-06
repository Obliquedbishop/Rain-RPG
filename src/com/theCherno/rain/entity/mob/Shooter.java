package com.theCherno.rain.entity.mob;

import com.theCherno.rain.entity.Entity;
import com.theCherno.rain.graphics.AnimatedSprite;
import com.theCherno.rain.graphics.Screen;
import com.theCherno.rain.graphics.Sprite;
import com.theCherno.rain.graphics.SpriteSheet;
import com.theCherno.rain.util.Vector2i;

import java.util.List;

public class Shooter extends Mob{

    private final AnimatedSprite down = new AnimatedSprite(SpriteSheet.dummy_down, 32, 32, 3);
    private final AnimatedSprite up = new AnimatedSprite(SpriteSheet.dummy_up, 32, 32, 3);
    private final AnimatedSprite left = new AnimatedSprite(SpriteSheet.dummy_left, 32, 32, 3);
    private final AnimatedSprite right = new AnimatedSprite(SpriteSheet.dummy_right, 32, 32, 3);

    private AnimatedSprite animSprite = down;
    private double time = 0;
    private int xa = 0;
    private int ya = 0;
    private Entity rand;

    private int fireRate = 0;

    public Shooter(int x, int y) {
        this.x = x << 4;
        this.y = y << 4;
        sprite = Sprite.dummy;
        fireRate = 10 + random.nextInt(10);
    }

    public void update() {
        time++;
        if(fireRate > 0) fireRate--;
        if(time % (random.nextInt(50) + 30) == 0) {
            xa = random.nextInt(3) - 1;
            ya = random.nextInt(3) - 1;
            if(random.nextInt(5) == 0){
                xa = 0;
                ya = 0;
            }
        }
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
        } else if(xa > 0) {
            animSprite = right;
            dir = Direction.RIGHT;
        }
        if(xa != 0 || ya != 0) {
            //move(xa, ya);
            walking = true;
        }else{
            walking = false;
        }
        if(fireRate <= 0) {
            shootRandom();
            fireRate = 10 + random.nextInt(10);
        }
    }

    private void shootRandom() {
        if(time % 60 == 0) {
            List<Entity> entities = level.getEntities(this, 500);
            entities.add(level.getClientPlayer());
            int index = random.nextInt(entities.size());
            rand = entities.get(index);
        }

        if(rand != null) {
            double px = rand.getX();
            double py = rand.getY();
            double dx = px - x;
            double dy = py - y;
            double dir = Math.atan2(dy, dx);
            System.out.println("yo");
            shoot(x, y, dir);
        }
    }

    private void shootClosest() {
        List<Entity> entities = level.getEntities(this, 500);
        entities.add(level.getClientPlayer());

        double min = 0;
        Entity closest = null;
        for(int i = 0; i < entities.size(); i++) {
            Entity e = entities.get(i);
            double distance = Vector2i.getDistance(new Vector2i((int)x, (int)y),
                    new Vector2i((int)e.getX(), (int)e.getY()));
            if(i == 0 || distance < min) {
                min = distance;
                closest = e;
            }
        }
        if(closest != null) {
            double px = closest.getX();
            double py = closest.getY();
            double dx = px - x;
            double dy = py - y;
            double dir = Math.atan2(dy, dx);
            shoot(x, y, dir);
        }

    }

    public void render(Screen screen) {
        sprite = animSprite.getSprite();
        //Debug.drawRect(screen, 50, 50, 16, 16,false);
        screen.renderMob((int) (x - 16), (int) (y - 16), this);
    }
}
