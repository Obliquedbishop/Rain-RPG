package com.theCherno.rain.level;

import com.theCherno.rain.entity.mob.Star;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class SpawnLevel extends Level{

    public SpawnLevel(String path) {
        super(path);
        TileCoordinate playerSpawn = new TileCoordinate(39, 39);

    }

    protected void loadLevel(String path) {
        try {
            BufferedImage image = ImageIO.read(SpawnLevel.class.getResource(path));
            int w = width = image.getWidth();
            int h = height = image.getHeight();
            tiles = new int[w * h];
            image.getRGB(0, 0, w, h, tiles, 0, w);
        } catch(IOException e) {
            e.printStackTrace();
            System.out.println("Exception! Could not load level file!");
        }
        //add(new Chaser(38, 40));
        add(new Star(33, 40));
        //add(new Shooter(38, 40));
        //add(new Shooter(38, 30));

        for(int i = 0; i < 1; i++) {
            //add(new Dummy(35, 40));
        }
    }


    protected void generateLevel() {
    }

}
