package com.bomberman;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.bomberman.States.PlayState;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Bomberman extends ApplicationAdapter {
    private PlayState playState;
    private OrthographicCamera camera;
    private Viewport viewport;
    private final ConstantValues constValues = new ConstantValues();

    @Override
    public void create() {
        camera = new OrthographicCamera();
        viewport = new FitViewport(constValues.WINDOW_WIDTH, constValues.WINDOW_HEIGHT, camera);
        playState = new PlayState();
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0.2f, 0.7f, 0.2f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        playState.render();
    }
    
    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }


    @Override
    public void dispose() {
        playState.dispose();
    }
}
