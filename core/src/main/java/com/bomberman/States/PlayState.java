/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bomberman.States;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.bomberman.Entities.Bomb;
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
    private OrthographicCamera camera;
    

    public PlayState() {
        Texture texture = new Texture("bomberman-sprite-player.png"); // Carga la textura del jugador
        tileMap = new TileMap("background.tmx");
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        player = new Player(texture, tileMap, 100, 100); // Inicializa el jugador con la textura y posición específica
       
    }
    

    public void update(float deltaTime) {
        player.act(deltaTime);
      //  tileMap.update(deltaTime);
        handleInput(deltaTime);
    }

    @Override
    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.setToOrtho(false, tileMap.getWidth(), tileMap.getHeight());
        batch.setProjectionMatrix(camera.combined);
        tileMap.render(batch, camera);
        batch.begin();
        player.draw(batch, 1f);
        batch.end();
    }

    public void dispose() {
        tileMap.dispose();
        player.dispose();
    }

    public void handleInput(float dt) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            Bomb bomb = player.placeBomb();
            if (bomb != null) {
               // tileMap.addBomb(bomb);
                System.out.println("Bomb placed at " + bomb.getX() + ", " + bomb.getY());
            } else {
                System.out.println("Failed to place bomb");
            }
        }
    }
}
