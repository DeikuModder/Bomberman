/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bomberman.Entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.bomberman.ConstantValues;

/**
 *
 * @author gabri
 */
public class Block {
    private final Texture blockSprite;
    private final SpriteBatch batch;
    private final Rectangle block;
    private final ConstantValues constValues = new ConstantValues();
    private boolean isCollidable;
    public int x;
    public int y;
    
    public Block(String spritePath, int x, int y, boolean isCollidable) {
        this.batch = new SpriteBatch();
        this.blockSprite = new Texture(Gdx.files.internal(spritePath));
        this.block = new Rectangle();
        this.isCollidable = isCollidable;
        this.x = x;
        this.y = y;
        
        block.x = x;
        block.y = y;
        block.height = constValues.BLOCK_SIZE;
        block.width = constValues.BLOCK_SIZE;
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
