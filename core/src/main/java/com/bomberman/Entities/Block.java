/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bomberman.Entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;


/**
 *
 * @author gabri
 */
public class Block {
    private final Texture blockSprite;
    private final SpriteBatch batch;
    private final Rectangle block;
    
    public Block(String spritePath) {
        this.batch = new SpriteBatch();
        this.blockSprite = new Texture(Gdx.files.internal(spritePath));
        this.block = new Rectangle();
        
        block.x = 800 /2 - 64 -2;
        block.y = 20;
        block.height = 64;
        block.width = 64;
    }
    
    public void render() {
        this.batch.begin();
        this.batch.draw(this.blockSprite, this.block.x, this.block.y);
        this.batch.end();
    }
    
    public void dispose() {
        this.batch.dispose();
        this.blockSprite.dispose();
    }
}
