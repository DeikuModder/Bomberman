/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bomberman.Scenario;

import com.bomberman.ConstantValues;
import com.bomberman.Entities.Block;
import java.util.ArrayList;

/**
 *
 * @author gabri
 */
public class TileMap {
    private final ConstantValues constantValues = new ConstantValues();
    private final int amountOfXBlocks = constantValues.WINDOW_WIDTH / constantValues.BLOCK_SIZE;
    private final int amountOfYBlocks = constantValues.WINDOW_HEIGHT / constantValues.BLOCK_SIZE;
    private Row[] gridBlockArr;
    
    public TileMap() {
        int yOrigin = 0;
        gridBlockArr = new Row[amountOfYBlocks];
        
        for (int i = 0; i < amountOfYBlocks; i++){
            Row gridRow = new Row(amountOfXBlocks, 0, yOrigin, "grid_block.png", false);
            gridBlockArr[i] = gridRow;
            yOrigin += 64;
        }
        
        System.out.println(gridBlockArr[8].tiles[0].x);
        System.out.println(gridBlockArr[8].tiles[0].y);
    }
    
    public void render() {
       for (Row gridRow : gridBlockArr){
           gridRow.render();
       }
    }
    
    public void dispose() {
        for (Row gridRow : gridBlockArr){
           gridRow.dispose();
       }
    }
}
