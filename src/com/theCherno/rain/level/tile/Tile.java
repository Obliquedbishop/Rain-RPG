package com.theCherno.rain.level.tile;

import com.theCherno.rain.graphics.Screen;
import com.theCherno.rain.graphics.Sprite;
import com.theCherno.rain.level.tile.spawn_level.*;

public class Tile {

    public Sprite sprite;

    public static Tile grass = new GrassTile(Sprite.grass);
    public static Tile flower = new FlowerTile(Sprite.flower);
    public static Tile rock = new RockTile(Sprite.rock);
    public static Tile voidTile = new voidTile(Sprite.voidSprite);

    public static Tile spawn_grass = new SpawnGrassTile(Sprite.spawn_grass);
    public static Tile spawn_hedge = new SpawnHedgeTile(Sprite.spawn_hedge);
    public static Tile spawn_water = new SpawnWaterTile(Sprite.spawn_water);
    public static Tile spawn_wall1 = new SpawnWallTile(Sprite.spawn_wall1);
    public static Tile spawn_wall2 = new SpawnWallTile(Sprite.spawn_wall2);
    public static Tile spawn_floor = new SpawnFloorTile(Sprite.spawn_floor);

    // colour codes
    public final static int col_spawn_grass= 0xff4cff00;
    public final static int col_spawn_hedge= 0xff267f00;
    public final static int col_spawn_water= 0xff00ffff;
    public final static int col_spawn_wall1= 0xff000000;
    public final static int col_spawn_wall2= 0xff404040;
    public final static int col_spawn_floor= 0xff5b0008;



    // Each tile we must crate must have a sprite attached to it
    public Tile(Sprite sprite) {
        this.sprite = sprite;
    }

    public void render(int x, int y, Screen screen) {
    }

    public boolean solid() {
        return false;
    }
}
