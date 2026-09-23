package com.bomberman.States;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.bomberman.Assets;
import com.bomberman.Bomberman;
import com.bomberman.ConstantValues;
import com.bomberman.Entities.Bomb;
import com.bomberman.Entities.ExplosionTextures;
import com.bomberman.Entities.Player;
import com.bomberman.Scenario.TileMap;

/**
 *
 * @author gabri
 */
public class PlayState implements GameState {
    private TileMap tileMap;
    private Player player;
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private BitmapFont font;
    private boolean paused = false;


    public PlayState() {
        // No dejar el InputProcessor del menú activo durante la partida
        Gdx.input.setInputProcessor(null);

        tileMap = new TileMap("background.tmx");
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        player = new Player(new com.badlogic.gdx.graphics.Texture("bomberman-sprite-player.png"), tileMap,
                ConstantValues.PLAYER_SPAWN_TILE_X * ConstantValues.BLOCK_SIZE,
                ConstantValues.PLAYER_SPAWN_TILE_Y * ConstantValues.BLOCK_SIZE);

        // La textura de bomba se comparte entre bombas y enemigos placeholder
        tileMap.generateEnemies(Assets.bombTexture(), ConstantValues.DEFAULT_ENEMY_COUNT);

        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(1.4f);
    }


    public void update(float deltaTime) {
        if (!paused) {
            player.act(deltaTime);
            tileMap.update(deltaTime);
        }
        handleInput(deltaTime);

        // Condición de victoria: sin enemigos vivos y el jugador sigue en pie
        if (player.isAlive() && tileMap.winConditionMet()) {
            Bomberman.getInstance().setState(new WinState());
        }
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

        // HUD
        batch.begin();
        font.draw(batch, "Vidas: " + player.getLifebar(), 10, tileMap.getHeight() - 10);
        font.draw(batch, "Enemigos: " + tileMap.getEnemiesLeft(), 10, tileMap.getHeight() - 35);
        if (paused) {
            font.draw(batch, "PAUSA - ESC para continuar",
                    tileMap.getWidth() / 2f - 120, tileMap.getHeight() / 2f);
        }
        batch.end();
    }

    public void dispose() {
        Gdx.input.setInputProcessor(null);
        tileMap.dispose();
        player.dispose();
        batch.dispose();
        font.dispose();
        // Liberar las texturas de explosión compartidas
        ExplosionTextures.getInstance().dispose();
    }

    public void handleInput(float dt) {
        // Colocar bomba con ESPACIO
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            Bomb bomb = player.placeBomb(Assets.bombTexture());
            if (bomb != null) {
                tileMap.addBomb(bomb);
            }
        }

        // Toggle modo debug con F3
        if (Gdx.input.isKeyJustPressed(Input.Keys.F3)) {
            tileMap.toggleDebugMode();
        }

        // Pausa con ESC
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            paused = !paused;
        }
    }
}