package com.theCherno.rain.graphics;

import com.theCherno.rain.entity.mob.Chaser;
import com.theCherno.rain.entity.mob.Mob;
import com.theCherno.rain.entity.mob.Star;
import com.theCherno.rain.entity.projectile.Projectile;
import com.theCherno.rain.level.tile.Tile;

import java.util.Arrays;
import java.util.Random;

public class Screen {

    public final int width;
    public final int height;
    public int[] pixels;
    public final int MAP_SIZE = 64;
    public final int MAP_SIZE_MASK = MAP_SIZE - 1;

    public int xOffset, yOffset;
    public int[] tiles = new int[MAP_SIZE * MAP_SIZE];
    private static final Random random = new Random();

    private final int ALPHA_COl = 0xffff00ff;

    public Screen(int width, int height) {
        this.width = width;
        this.height = height;
        pixels = new int[width * height];

        for(int i = 0; i < MAP_SIZE*MAP_SIZE; i++){
            tiles[i] = random.nextInt(0xffffff);
        }
    }

    public void clear() {
        Arrays.fill(pixels, 0);
    }

    public void renderSheet(int xp, int yp, SpriteSheet sheet, boolean fixed) {
        if(fixed) {
            xp -= xOffset;
            yp -= yOffset;
        }
        for (int y = 0; y < sheet.SPRITE_HEIGHT; y++) {
            int ya = y + yp;
            for (int x = 0; x < sheet.SPRITE_WIDTH; x++) {
                int xa = x + xp;
                if(xa < 0 || xa >= width || ya < 0 || ya >= height) continue;
                pixels[xa + ya * width] = sheet.pixels[x + y * sheet.SPRITE_WIDTH];
            }
        }
    }
    public void renderTextCharacter(int xp, int yp, Sprite sprite, int color, boolean fixed) {
        if(fixed) {
            xp -= xOffset;
            yp -= yOffset;
        }
        for (int y = 0; y < sprite.getHeight(); y++) {
            int ya = y + yp;
            for (int x = 0; x < sprite.getWidth(); x++) {
                int xa = x + xp;
                if(xa < 0 || xa >= width || ya < 0 || ya >= height) continue;
                int col = sprite.pixels[x + y * sprite.getWidth()];
                if (col != ALPHA_COl) pixels[xa + ya * width] = color;
            }
        }
    }
    public void renderSprite(int xp, int yp, Sprite sprite, boolean fixed) {
        if(fixed) {
            xp -= xOffset;
            yp -= yOffset;
        }
        for (int y = 0; y < sprite.getHeight(); y++) {
            int ya = y + yp;
            for (int x = 0; x < sprite.getWidth(); x++) {
                int xa = x + xp;
                if(xa < 0 || xa >= width || ya < 0 || ya >= height) continue;
                int col = sprite.pixels[x + y * sprite.getWidth()];
                if (col != ALPHA_COl) pixels[xa + ya * width] = col;
            }
        }
    }

    public void renderTile(int xp, int yp, Tile tile) {
        xp -= xOffset;
        yp -= yOffset;
        for(int y = 0; y < tile.sprite.SIZE; y++) {
            int ya = y + yp;
            for (int x = 0; x < tile.sprite.SIZE; x++) {
                int xa = x + xp;
                if(xa < -tile.sprite.SIZE || xa >= width || ya < 0 || ya >= height) break;
                if(xa < 0) xa = 0;
                pixels[xa + ya * width] = tile.sprite.pixels[x + y * tile.sprite.SIZE];
            }
        }
    }

    public void renderProjectile(int xp, int yp, Projectile p) {
        xp -= xOffset;
        yp -= yOffset;
        for(int y = 0; y < p.getSpriteSize(); y++) {
            int ya = y + yp;
            for (int x = 0; x < p.getSpriteSize(); x++) {
                int xa = x + xp;
                if(xa < -p.getSpriteSize() || xa >= width || ya < 0 || ya >= height) break;
                if(xa < 0) xa = 0;
                int col = p.getSprite().pixels[x + y * p.getSpriteSize()];
                if(col != ALPHA_COl) pixels[xa + ya * width] = col;
            }
        }
    }

    public void renderMob(int xp, int yp, Sprite sprite) {
        xp -= xOffset;
        yp -= yOffset;
        for(int y = 0; y < 32; y++) {
            int ya = y + yp;
            for (int x = 0; x < 32; x++) {
                int xa = x + xp;
                if(xa < -32 || xa >= width || ya < 0 || ya >= height) break;
                if(xa < 0) xa = 0;
                int col = sprite.pixels[x + y * 32];
                if(col != ALPHA_COl) pixels[xa + ya * width] = col;
            }
        }
    }

    public void renderMob(int xp, int yp, Mob mob) {
        xp -= xOffset;
        yp -= yOffset;
        for(int y = 0; y < 32; y++) {
            int ya = y + yp;
            for (int x = 0; x < 32; x++) {
                int xa = x + xp;
                if(xa < -32 || xa >= width || ya < 0 || ya >= height) break;
                if(xa < 0) xa = 0;
                int col = mob.getSprite().pixels[x + y * 32];
                if((mob instanceof Chaser) && col == 0xffFFD9AD) col = 0xff321DD3;
                if((mob instanceof Star) && col == 0xffFFD9AD) col = 0xffE8E83A;
                if(col != ALPHA_COl) pixels[xa + ya * width] = col;
            }
        }
    }

    public void setOffset(int xOffset, int yOffset) {
        this.xOffset = xOffset;
        this.yOffset = yOffset;
    }

    public void drawRect(int xp, int yp, int width, int height, int colour, boolean fixed) {
        if(fixed) {
            xp -= xOffset;
            yp -= yOffset;
        }
        for(int x = xp; x < xp + width; x++) {
            if(x < 0 || x >= this.width || yp >= this.height) continue;
            if(yp > 0) pixels[x + yp * this.width] = colour;
            if(yp + height >= this.height) continue;
            if(yp + height> 0) pixels[x + (yp + height) * this.width] = colour;
        }
        for(int y = yp; y <= yp + height; y++) {
            if(xp >= this.width || y < 0 || y >= this.height) continue;
            if(xp > 0) pixels[xp + y * this.width] = colour;
            if(xp + width >= this.width) continue;
            if(xp + width > 0) pixels[(xp + width) + y * this.width] = colour;
        }
    }

    public void fillRect(int xp, int yp, int width, int height, int color, boolean fixed) {
        if (fixed) {
            xp -= xOffset;
            yp -= yOffset;
        }

        for(int y = 0; y < height; y++) {
            int y0 = yp + y;
            if(y0 < 0 || y0 >= this.height) continue;
            for(int x=  0; x < width ; x++) {
                int x0 = xp + x;
                if(x0 < 0 || x0 >= this.width) continue;
                pixels[x0 + y0 * this.width] = color;
            }
        }
    }
}
