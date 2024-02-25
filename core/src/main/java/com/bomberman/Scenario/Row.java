/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bomberman.Scenario;

import com.bomberman.Entities.Block;

/**
 *
 * @author gabri
 */
public class Row {
    Block[] tiles;
    
    public Row(int length, int x, int y, String sprite, boolean isCollidable) {
        tiles = new Block[length];
        
        for (int i = 0; i < length; i++){
            tiles[i] = new Block(sprite, x, y, isCollidable);
            x += 64;
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
