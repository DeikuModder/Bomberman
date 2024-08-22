package com.bomberman.States;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.bomberman.Bomberman;
import com.bomberman.ConstantValues;

public class MenuState implements statesInterface {
    private OrthographicCamera camera;
    private Viewport viewport;
    private Texture background;
    private SpriteBatch batch;
    private Texture Title;
    private Button playButton;
    private Stage stage;

    public MenuState() {
        camera = new OrthographicCamera();
        camera.position.set(ConstantValues.WINDOW_WIDTH / 2, ConstantValues.WINDOW_HEIGHT / 2, 0);
        camera.zoom = 1;
        viewport = new FitViewport(ConstantValues.WINDOW_WIDTH, ConstantValues.WINDOW_HEIGHT, camera);
        viewport.apply();
        background = new Texture("background.jpeg");
        Title = new Texture("BombermanTitle.png");
        batch = new SpriteBatch();
        stage = new Stage(viewport, batch);
        Gdx.input.setInputProcessor(stage);

     // Crear el botón
        Texture buttonTexture = new Texture("PlayButton.png");
        ButtonStyle buttonStyle = new ButtonStyle();
        buttonStyle.up = new TextureRegionDrawable(new TextureRegion(buttonTexture));
        playButton = new Button(buttonStyle);
        playButton.setPosition(50, 350);
        playButton.setSize(200, 50);

    // Agregar el botón al stage
        stage.addActor(playButton);
        
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Llamar al método startGame() de la clase Bomberman
                ((Bomberman) Gdx.app.getApplicationListener()).startGame();
            }
        });
        
    }
    

    @Override
    public void update(float deltaTime) {
        // Actualiza la lógica del menú principal
        stage.act(deltaTime);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(1f, 1f, 1f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(background, 0, 0, ConstantValues.WINDOW_WIDTH, ConstantValues.WINDOW_HEIGHT);
        batch.draw(Title, 30, 370, 300, 300);
        batch.end();
        stage.draw();
    }

    @Override
    public void dispose() {
        background.dispose();
        stage.dispose();
        batch.dispose();
    }
}