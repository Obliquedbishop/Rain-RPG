package com.theCherno.rain.entity;

import com.theCherno.rain.graphics.Screen;
import com.theCherno.rain.graphics.Sprite;
import com.theCherno.rain.level.Level;

import java.util.Random;

public class Entity {

    protected double x, y; // For the entities position, as they move, so they have their own x and y
    private boolean removed = false;
    protected Level level;
    protected Sprite sprite;
    protected final Random random = new Random();

    public Entity() {
    }

    public Entity(int x, int y, Sprite sprite) {
        this.x = x;
        this.y = y;
        this.sprite = sprite;
    }

    public void update() {
    }

    public void render(Screen screen) {
        if(sprite != null) screen.renderSprite((int) x, (int) y, sprite, true);
    }

    public void remove() {
        removed = true;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public boolean isRemoved() {
        return removed;
    }

    public void init(Level level) {
        this.level = level;
    }
}
