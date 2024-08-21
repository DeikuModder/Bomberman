/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bomberman.Scenario;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.bomberman.ConstantValues;
import com.bomberman.Entities.Block;

/**
 *
 * @author gabri
 */
public class TileMap extends TiledMap{
    private final ConstantValues constantValues = new ConstantValues();
    private final int amountOfXBlocks = constantValues.WINDOW_WIDTH / constantValues.BLOCK_SIZE;
    private final int amountOfYBlocks = constantValues.WINDOW_HEIGHT / constantValues.BLOCK_SIZE;
    private final Row[] gridBlockArr;
    private final String spriteBlockTexture = "blockx32.png";
    private final String spriteEmptyTexture = "groundx32.png";
    
    public TileMap() {
        int yTileOrigin = 0;
        int xTileOrigin = 0;
        gridBlockArr = new Row[amountOfYBlocks];
        
        //first fill the map with collidable blocks
        for (int i = 0; i < amountOfYBlocks; i++){
            gridBlockArr[i] = new Row(amountOfXBlocks, xTileOrigin, yTileOrigin, spriteBlockTexture, true);
            yTileOrigin += constantValues.BLOCK_SIZE;
        }
        
        //reset y and x origin back to 0 after each loop
        
        yTileOrigin = 0;
        xTileOrigin = 0;
        
        //then create the empty spaces
        
        //empty down path
        for (int i = 1; i < amountOfXBlocks - 1; i++){
            int x = gridBlockArr[1].tiles[i].x;
            int y = gridBlockArr[1].tiles[i].y;
            
            gridBlockArr[1].tiles[i] = new Block(spriteEmptyTexture, x, y, false);
            x += constantValues.BLOCK_SIZE;
        }
        
        //empty upper path
        for (int i = 1; i < amountOfXBlocks - 1; i++){
            int x = gridBlockArr[amountOfYBlocks - 2].tiles[i].x;
            int y = gridBlockArr[amountOfYBlocks - 2].tiles[i].y;
            
            gridBlockArr[amountOfYBlocks - 2].tiles[i] = new Block(spriteEmptyTexture, x, y, false);
            x += constantValues.BLOCK_SIZE;
        }
        
        //fill the inner empty space
        for (int i = 3; i < amountOfYBlocks - 3; i++){
            for (int j = 3; j < amountOfXBlocks - 3; j++){
                
                gridBlockArr[i].tiles[j] = new Block(spriteEmptyTexture, xTileOrigin + (constantValues.BLOCK_SIZE * 3), yTileOrigin + constantValues.BLOCK_SIZE * 3, false);
                xTileOrigin += constantValues.BLOCK_SIZE;
            }
            xTileOrigin = 0;
            yTileOrigin += constantValues.BLOCK_SIZE;
        }
        
        yTileOrigin = 0;
        xTileOrigin = 0;
        
        //empty left path
        for (int i = 2; i < amountOfYBlocks - 2; i++){
            int x = gridBlockArr[i].tiles[1].x;
            int y = gridBlockArr[i].tiles[1].y;
            
            gridBlockArr[i].tiles[1] = new Block(spriteEmptyTexture, x, y, false);
            y += constantValues.BLOCK_SIZE;
        }
     
        //empty right path
        for (int i = 2; i < amountOfYBlocks - 2; i++){
            int x = gridBlockArr[i].tiles[amountOfXBlocks - 2].x;
            int y = gridBlockArr[i].tiles[amountOfXBlocks - 2].y;
            
            gridBlockArr[i].tiles[amountOfXBlocks - 2] = new Block(spriteEmptyTexture, x, y, false);
            y += constantValues.BLOCK_SIZE;
        }
        /*
        Ejemplo de como acceder a un tile desde aqui:
        gridBlockArr[0].tiles[0] = El tile con origen 0,0 en coordenadas, siendo en este caso Y, X el orden.
        */
    }
    
    public void render() {
       for (Row gridRow : gridBlockArr){
           gridRow.render();
       }
    }
    
    /**
     * 
     * @param x
     * Position in X that needs to be checked
     * @param y
     * Position in Y that needs to be checked
     * @return 
     * True if the tile is Coliidable, false otherwise
     */
    public boolean isCollidable(int x, int y) {
        int tileX = x / constantValues.BLOCK_SIZE;
        int tileY = y / constantValues.BLOCK_SIZE;
    
        if (tileX < 0 || tileX >= amountOfXBlocks || tileY < 0 || tileY >= amountOfYBlocks) {
            // Fuera de los límites del tilemap
            return false; // o lanza una excepción
        }
    
        return gridBlockArr[tileY].tiles[tileX].isCollidable;
    }
    
    public void dispose() {
        for (Row gridRow : gridBlockArr){
           gridRow.dispose();
       }
    }
    
    public Block getTile(int x, int y) {
        int tileX = x / constantValues.BLOCK_SIZE;
        int tileY = y / constantValues.BLOCK_SIZE;
        
        if (tileX < 0 || tileX >= amountOfXBlocks || tileY < 0 || tileY >= amountOfYBlocks) {
            return null; // Fuera de los límites del tilemap
        }
        
        return gridBlockArr[tileY].tiles[tileX];
    }

    public int getWidth() {
        return amountOfXBlocks;
    }

    public int getHeight() {
        return amountOfYBlocks;
    }

    public int getTileWidth() {
        return constantValues.BLOCK_SIZE;
    }

    public int getTileHeight() {
        return constantValues.BLOCK_SIZE;
    }
}
