package com.theCherno.rain.entity.projectile;

import com.theCherno.rain.entity.Entity;
import com.theCherno.rain.graphics.Sprite;

import java.util.Random;

public abstract class Projectile extends Entity {

    protected final double xOrigin, yOrigin;
    protected double angle;
    protected Sprite sprite;
    protected double x, y;
    protected double nx, ny;
    protected double distance;
    protected double speed, range, damage;

    protected final Random random = new Random();

    public Projectile(double x, double y, double dir) {
        x -= 8;
        xOrigin = x;
        yOrigin = y;
        angle = dir;
        this.x = x;
        this.y = y;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public int getSpriteSize() {
        return sprite.SIZE;
    }
    protected void move() {

    }
}
