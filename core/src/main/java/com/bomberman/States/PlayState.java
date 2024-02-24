/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bomberman.States;

import com.bomberman.Entities.Block;

/**
 *
 * @author gabri
 */
public class PlayState implements statesInterface {
    private Block block;
    
    public PlayState() {
        block = new Block("block.png");
    }
    
    public void render() {
        block.render();
    }
    
    public void dispose() {
        block.dispose();
    }
}
