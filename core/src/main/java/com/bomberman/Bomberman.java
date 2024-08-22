package com.bomberman;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.bomberman.States.MenuState;
import com.bomberman.States.PlayState;
import com.bomberman.States.statesInterface;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Bomberman extends ApplicationAdapter {
    private PlayState playState;
    private statesInterface currentState;
    private OrthographicCamera camera;
    private Viewport viewport;
    private final ConstantValues constValues = new ConstantValues();

    @Override
    public void create() {
        camera = new OrthographicCamera();
        viewport = new FitViewport(ConstantValues.WINDOW_WIDTH, ConstantValues.WINDOW_HEIGHT, camera);
        setState(new MenuState());
    }

    public void setState(statesInterface state) {
        if (currentState != null) {
            currentState.dispose();
        }
        currentState = state;
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        currentState.update(Gdx.graphics.getDeltaTime());
        currentState.render();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }


    @Override
    public void dispose() {
        playState.dispose();
    }
    
    public void startGame() {
        setState(new PlayState());
    }
}
