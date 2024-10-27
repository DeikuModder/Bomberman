package com.bomberman.States;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.bomberman.Bomberman;

public class DeathState implements statesInterface {

    private SpriteBatch batch;
    private Stage stage;
    private Texture background;
    private Texture buttonTexture;
    private Texture buttonTexturePressed;
    private ImageButton button;

    public DeathState() {
        batch = new SpriteBatch();
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        background = new Texture("death_background.png");
        buttonTexture = new Texture("PlayButton.png");

        Image backgroundImage = new Image(background);
        backgroundImage.setPosition(0, 0);
        backgroundImage.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage.addActor(backgroundImage);

        button = new ImageButton(new TextureRegionDrawable(new TextureRegion(buttonTexture)));
        button.setPosition(Gdx.graphics.getWidth() / 2 - button.getWidth() / 2, Gdx.graphics.getHeight() / 2 - button.getHeight() / 2);
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // Volver al menú o reiniciar el juego
                Bomberman.getInstance().setState(new MenuState());
            }
        });
        stage.addActor(button);
    }

    @Override
    public void update(float deltaTime) {
        stage.act(deltaTime);
    }

    @Override
    public void render() {
        batch.begin();
        stage.draw();
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        stage.dispose();
        background.dispose();
        buttonTexture.dispose();
        buttonTexturePressed.dispose();
    }
}