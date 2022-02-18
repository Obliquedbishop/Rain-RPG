package com.theCherno.rain.graphics.ui;

import com.theCherno.rain.input.Mouse;
import com.theCherno.rain.util.Vector2i;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class UIButton extends UIComponent{

    public UILabel label;
    private UIButtonListener buttonListener;
    private final UIActionListener actionListener;

    private Image image;

    private boolean inside = false;
    private boolean pressed = false;
    private boolean ignorePressed = false;
    private boolean ignoreAction = true;

    public UIButton(Vector2i position, Vector2i size, UIActionListener actionListener) {
        super(position, size);
        Vector2i lp = new Vector2i(position);
        lp.x += 4;
        lp.y += size.y - 2;
        label = new UILabel(lp, "");
        label.setColor(0x444444);
        label.active = false;
        this.actionListener = actionListener;
        init();
    }

    public UIButton(Vector2i position, BufferedImage image, UIActionListener actionListener) {
        super(position, new Vector2i(image.getWidth(), image.getHeight()));
        this.actionListener = actionListener;
        setImage(image);
        init();
    }
    private void init() {
        color = new Color(0xAAAAAA);
        buttonListener = new UIButtonListener();
    }

    void init(UIPanel panel) {
        super.init(panel);
        if (label != null)
        panel.addComponent(label);
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public void setButtonListener(UIButtonListener buttonListener) {
        this.buttonListener = buttonListener;
    }

    public void setText(String text) {
        if(text.equals("")) {
            label.active = false;
            return;
        }
        label.text = text;
    }

    public void performAction() {
        actionListener.perform();
    }

    public void ignoreNextPress() {
        ignoreAction = true;
    }

    public void update() {
        Rectangle rect = new Rectangle(getAbsolutePosition().x, getAbsolutePosition().y, size.x, size.y);
        boolean leftMouseButtonDown = Mouse.getButton() == MouseEvent.BUTTON1;
        if (rect.contains(new Point(Mouse.getX(), Mouse.getY()))) {
            if(!inside) {
                ignorePressed = leftMouseButtonDown;
                buttonListener.entered(this);
            }
            inside = true;

            if (!pressed && !ignorePressed && leftMouseButtonDown) {
                buttonListener.pressed(this);
                pressed = true;
            } else if (Mouse.getButton() == MouseEvent.NOBUTTON) {
                if (pressed) {
                    buttonListener.released(this);
                    if(!ignoreAction) actionListener.perform();
                    else ignoreAction = false;
                    pressed = false;
                }
                ignorePressed = false;
            }
        } else {
            if(inside) {
                buttonListener.exited(this);
                pressed = false;
            }
            inside = false;
        }
    }

    public void render(Graphics g) {
        int x = position.getX() + offset.getX();
        int y = position.getY() + offset.getY();
        if (image != null) {
            g.drawImage(image, x, y, null);
        }
        //g.setColor(color);
        //g.fillRect(x, y, size.getX(), size.getY());

        if (label != null) label.render(g);
    }
}
