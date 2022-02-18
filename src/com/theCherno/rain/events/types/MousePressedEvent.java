package com.theCherno.rain.events.types;

import com.theCherno.rain.events.Event;

public class MousePressedEvent extends MouseButtonEvent{

    public MousePressedEvent(int button, int x, int y) {
        super(button, x, y, Event.Type.MOUSE_PRESSED);
    }
}
