/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bomberman.Scenario;

import com.bomberman.Entities.Block;
import com.bomberman.ConstantValues;

/**
 *
 * @author gabri
 */
public class Row {
    Block[] tiles;
    private final ConstantValues constantValues = new ConstantValues();
    
    public Row(int length, int x, int y, String sprite, boolean isCollidable) {
        tiles = new Block[length];
        
        for (int i = 0; i < length; i++){
            tiles[i] = new Block(sprite, x, y, isCollidable);
            //add the size of the block each loop to move the origin in x of the block
            x += constantValues.BLOCK_SIZE;
        }
    }
    
    
    public void render() {
        for (Block block : tiles){
            block.render();
        }
    }
    
    public void dispose() {
        for (Block block : tiles){
            block.dispose();
        }
    }
}
