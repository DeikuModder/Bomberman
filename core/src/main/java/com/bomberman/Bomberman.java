package com.bomberman;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.bomberman.States.PlayState;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Bomberman extends ApplicationAdapter {
    private PlayState playState;
    private Camera camera;
    private Viewport viewport;

    @Override
    public void create() {
        camera = new PerspectiveCamera();
        viewport = new FitViewport(800, 600, camera);
        playState = new PlayState();
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0.2f, 0.7f, 0.2f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        playState.render();
    }
    
    public void resize(int width, int height) {
        viewport.update(width, height);
    }


    @Override
    public void dispose() {
        playState.dispose();
    }
}
