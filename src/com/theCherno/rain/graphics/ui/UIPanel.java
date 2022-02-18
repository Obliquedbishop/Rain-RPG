package com.theCherno.rain.graphics.ui;

import com.theCherno.rain.util.Vector2i;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

public class UIPanel extends UIComponent{

    private final List<UIComponent> components = new ArrayList<UIComponent>();

    public UIPanel(Vector2i position, Vector2i size) {
        super(position);
        this.position = position;
        this.size = size;
        color = new Color(0xcacaca);
    }

    public void addComponent(UIComponent component) {
        component.init(this);
        components.add(component);
    }

    public void update() {
        for(UIComponent component : components) {
            component.setOffset(position);
            component.update();
        }
    }

    public void render(Graphics g) {
        g.setColor(color);
        g.fillRect(position.getX(), position.getY(), size.getX(), size.getY());
        for(UIComponent component : components) {
            component.render(g);
        }
    }
}
