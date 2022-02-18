package com.theCherno.rain.graphics.ui;

import com.theCherno.rain.util.Vector2i;

import java.awt.Color;
import java.awt.Graphics;

public class UIComponent {

    public Vector2i position, size;
    protected Vector2i offset;
    public Color color;
    protected UIPanel panel;

    protected boolean active = true;

    public UIComponent(Vector2i position){
        this.position = position;
        offset = new Vector2i();
    }

    public UIComponent(Vector2i position, Vector2i size){
        this.position = position;
        this.size = size;
        offset = new Vector2i();
    }

    void init(UIPanel uiPanel) {
        this.panel = panel;
    }

    public UIComponent setColor(int color) {
        this.color = new Color(color);
        return this;
    }

    public void update() {
    }

    public void render(Graphics g) {

    }

    public Vector2i getAbsolutePosition() {
        return new Vector2i(position).add(offset);
    }

    void setOffset(Vector2i offset) {
        this.offset = offset;
    }

}
