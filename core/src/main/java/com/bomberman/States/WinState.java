package com.bomberman.States;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.bomberman.Bomberman;

public class WinState implements GameState {

    private SpriteBatch batch;
    private Stage stage;
    private Texture background;
    private Texture buttonTexture;
    private ImageButton button;
    private BitmapFont font;

    public WinState() {
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
        button.setPosition(Gdx.graphics.getWidth() / 2 - button.getWidth() / 2, Gdx.graphics.getHeight() / 2 - button.getHeight() / 2 - 60);
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Bomberman.getInstance().setState(new MenuState());
            }
        });
        stage.addActor(button);

        font = new BitmapFont();
        font.setColor(Color.GOLD);
        font.getData().setScale(2.5f);
    }

    @Override
    public void update(float deltaTime) {
        stage.act(deltaTime);
    }

    @Override
    public void render() {
        batch.begin();
        font.draw(batch, "VICTORIA", Gdx.graphics.getWidth() / 2f - 100, Gdx.graphics.getHeight() / 2f + 60);
        batch.end();
        stage.draw();
    }

    @Override
    public void dispose() {
        batch.dispose();
        stage.dispose();
        background.dispose();
        buttonTexture.dispose();
        font.dispose();
    }
}