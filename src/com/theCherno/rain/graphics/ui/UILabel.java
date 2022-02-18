package com.theCherno.rain.graphics.ui;

import com.theCherno.rain.util.Vector2i;

import java.awt.Graphics;
import java.awt.Font;
import java.awt.Color;

public class UILabel extends UIComponent{

    public String text;
    private Font font;
    public boolean dropShadow = false;
    public int dropShadowOffset = 2;

    public UILabel(Vector2i position, String text) {
        super(position);
        this.text = text;
        font = new Font("Helvetica", Font.PLAIN, 32);
        color = new Color(0xff00ff);
    }

    public UILabel setFont(Font font) {
        this.font = font;
        return this;
    }

    public void render(Graphics g) {
        if(dropShadow) {
            g.setColor(Color.BLACK);
            g.setFont(font);
            g.drawString(text, position.getX() + offset.getX() + dropShadowOffset, position.getY() + offset.getY() + dropShadowOffset);
        }
        g.setFont(font);
        g.setColor(color);
        g.drawString(text, position.getX() + offset.getX(), position.getY() + offset.getY());
    }

}
