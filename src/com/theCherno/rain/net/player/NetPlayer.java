package com.theCherno.rain.net.player;

import com.theCherno.rain.entity.mob.Mob;
import com.theCherno.rain.graphics.Screen;

public class NetPlayer extends Mob {


    public void update() {

    }

    public void render(Screen screen) {
        screen.fillRect((int) x, (int) y, 32, 32, 0x2030cc, true);
    }
}
