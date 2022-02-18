package com.theCherno.rain.level.tile;

import com.theCherno.rain.graphics.Screen;
import com.theCherno.rain.graphics.Sprite;

public class voidTile extends Tile{

    public voidTile(Sprite sprite) {
        super(sprite);
    }

    public void render(int x, int y, Screen screen) {
        screen.renderTile(x << 4, y << 4, this);
    }

}
