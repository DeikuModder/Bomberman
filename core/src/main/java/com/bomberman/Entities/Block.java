/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bomberman.Entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Block extends Actor {
    private Texture texture;
    private Rectangle bounds;

    public Block(float x, float y) {
        this.texture = new Texture("block2.png"); 
        this.bounds = new Rectangle(x, y, 32, 32); 
        setPosition(x, y);
        setSize(32, 32);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(texture, getX(), getY(), getWidth(), getHeight());
    }

    public Rectangle getBounds() {
        return bounds;
    }

    @Override
    public void act(float delta) {
        // Aquí puedes manejar la lógica de los bloques, como ser destruidos por explosiones
    }

    public void dispose() {
        texture.dispose();
    }
}