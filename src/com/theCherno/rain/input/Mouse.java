package com.theCherno.rain.input;

import com.theCherno.rain.events.EventListener;
import com.theCherno.rain.events.types.MouseMovedEvent;
import com.theCherno.rain.events.types.MousePressedEvent;
import com.theCherno.rain.events.types.MouseReleasedEvent;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class Mouse implements MouseListener, MouseMotionListener {

    private static int mouseX = -1;
    private static int mouseY = -1;
    private static int mouseB = -1;

    private EventListener eventListener;
    public Mouse(EventListener listener) {
        eventListener = listener;
    }
    public static int getX() {
        return mouseX;
    }

    public static int getY() {
        return mouseY;
    }

    public static int getButton() {
        return mouseB;
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
        mouseB = e.getButton();

        MousePressedEvent event = new MousePressedEvent(e.getButton(), e.getX(), e.getY());
        eventListener.onEvent(event);
    }

    public void mouseReleased(MouseEvent e) {
        mouseB = MouseEvent.NOBUTTON;

        MouseReleasedEvent event = new MouseReleasedEvent(e.getButton(), e.getX(), e.getY());
        eventListener.onEvent(event);
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseDragged(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();

        MouseMovedEvent event = new MouseMovedEvent(e.getX(), e.getY(), true);
        eventListener.onEvent(event);
    }

    public void mouseMoved(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();

        MouseMovedEvent event = new MouseMovedEvent(e.getX(), e.getY(), false);
        eventListener.onEvent(event);
    }
}
