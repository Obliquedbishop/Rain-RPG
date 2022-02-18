package com.theCherno.rain.entity.projectile;

import com.theCherno.rain.entity.spawner.ParticleSpawner;
import com.theCherno.rain.graphics.Screen;
import com.theCherno.rain.graphics.Sprite;

public class WizardProjectile extends Projectile{

    public static final int FIRE_RATE = 8; // Higher is slower

    // Projectile and it's particle's properties
    private final int ProjectileXOffset = 4;
    private final int ProjectileYOffset = 4;
    private final int projectileSize = 8;
    private final int particleSize = 3;

    public WizardProjectile(double x, double y, double dir) {
        super(x, y, dir);
        range = 100;
        speed = 1;
        damage = 20;
        sprite = Sprite.rotate(Sprite.projectile_arrow, angle);

        nx = Math.cos(angle) * speed;
        ny = Math.sin(angle) * speed;
    }

    private int xOffsetParticle() {
        int c = level.CollisionPoint();
        if(c == 0 || c == 2) return ProjectileXOffset;
        else return ProjectileXOffset + projectileSize - particleSize;
    }

    private int yOffsetParticle() {
        int c = level.CollisionPoint();
        if(c == 0 || c == 1) return ProjectileYOffset;
        else return ProjectileYOffset + projectileSize - particleSize;
    }

    private int time =0;

    public void update() {
        if(level.tileCollision((int)(x + nx), (int)(y + ny), projectileSize, ProjectileXOffset, ProjectileYOffset)) {
            level.add(new ParticleSpawner((int)x + xOffsetParticle(), (int)y + yOffsetParticle(), 90, 30, level));
            remove();
        }
        /*time++;
        if(time % 10 == 0) {
            sprite = Sprite.rotate(sprite, Math.PI / 20.0);
        }
         */
        move();
    }

    protected void move() {
            x += nx;
            y += ny;
        if(distance() > range) remove();
    }

    private double distance() {
        double dist = 0;
        dist = Math.sqrt(Math.abs((xOrigin - x) * (xOrigin - x) + (yOrigin - y) * (yOrigin - y)));
        return dist;
    }

    public void render(Screen screen) {
        screen.renderProjectile((int) x , (int)y, this);
    }
}
