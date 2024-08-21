/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bomberman.States;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.bomberman.Entities.Player;
import com.bomberman.Scenario.TileMap;

/**
 *
 * @author gabri
 */
public class PlayState implements statesInterface {
    private TileMap tileMap;
    private Player player;
    private SpriteBatch batch;

    public PlayState() {
        Texture texture = new Texture("bomberman-sprite-player.png"); // Carga la textura del jugador
        tileMap = new TileMap();
        batch = new SpriteBatch();
        player = new Player(texture, tileMap, 100, 100); // Inicializa el jugador con la textura y posición específica
    }

    public void update(float deltaTime) {
        player.act(deltaTime);
    }
    @Override
    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        tileMap.render();
        batch.begin();
        player.draw(batch, 1f); // Llama al método draw del jugador
        batch.end();
    }

    public void dispose() {
        tileMap.dispose();
        player.dispose();
    }

    public void handleInput() {
       
    }


}
