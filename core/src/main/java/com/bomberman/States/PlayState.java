/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bomberman.States;

import com.bomberman.Scenario.TileMap;

/**
 *
 * @author gabri
 */
public class PlayState implements statesInterface {
    TileMap tileMap;
            
    public PlayState() {
        tileMap = new TileMap();
    }
    
    public void render() {
        tileMap.render();
    }
    
    public void dispose() {
        tileMap.dispose();
    }
}
