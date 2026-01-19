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
    private Texture bombTexture;
    private Texture enemyTexture;
    

    public PlayState() {
        Texture texture = new Texture("bomberman-sprite-player.png"); // Carga la textura del jugador
        tileMap = new TileMap("background.tmx");
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        player = new Player(texture, tileMap, 100, 100); // Inicializa el jugador con la textura y posición específica
        bombTexture = new Texture("Bombs.png");
        
        // Cargar textura de enemigos y generarlos
        // Usamos la textura de bomba temporalmente como placeholder para enemigos
        enemyTexture = new Texture("Bombs.png");  // TODO: Reemplazar con sprite de enemigo real
        tileMap.generateEnemies(enemyTexture, 5);  // Generar 5 enemigos
    }
    

    public void update(float deltaTime) {
        player.act(deltaTime);
        tileMap.update(deltaTime);
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
        bombTexture.dispose();
        enemyTexture.dispose();
    }
    public void handleInput(float dt) {
        // Colocar bomba con ESPACIO
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            Bomb bomb = player.placeBomb(bombTexture);  
            if (bomb != null) {
                tileMap.addBomb(bomb);
                System.out.println("Bomb placed at " + bomb.getX() + ", " + bomb.getY());
            } else {
                System.out.println("Failed to place bomb");
            }
        }
        
        // Toggle modo debug con F3
        if (Gdx.input.isKeyJustPressed(Input.Keys.F3)) {
            tileMap.toggleDebugMode();
            System.out.println("Debug mode: " + (tileMap.isDebugMode() ? "ON" : "OFF"));
        }
    }
}
