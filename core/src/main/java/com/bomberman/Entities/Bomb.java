package com.bomberman.Entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

// Clase Bomba
public class Bomb extends Actor {
    private Texture bombTexture;
    private float explosionTime = 0f; // Tiempo de explosión en segundos
    boolean isExploded = false;
    private Animation<TextureRegion> explosionAnimation;
    private float stateTime;
    
    public Bomb(Texture bombTexture, float x, float y) {
        this.bombTexture = bombTexture;
        TextureRegion[][] temp = new TextureRegion(bombTexture).split(bombTexture.getWidth() / 3, bombTexture.getHeight());
        TextureRegion[] frames = new TextureRegion[temp.length * temp[0].length];
        int index = 0;
        for (int i = 0; i < temp.length; i++) {
            for (int j = 0; j < temp[i].length; j++) {
                frames[index++] = temp[i][j];
            }
        }
        explosionAnimation = new Animation<>(0.1f, frames);
        setPosition(x, y); // Set the position of the bomb
        stateTime = 0; // Reset the state time
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        if (!isExploded) {
            explosionTime -= delta;
            if (explosionTime <= 0) {
                explode();
            }
        }
        else {
            stateTime += delta; // Actualiza stateTime cuando isExploded es true
        }
    }

    private void explode() {
        isExploded = true;
        // Animación de explosión aquí
        // ...
    }
    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (isExploded) {
            TextureRegion frame = explosionAnimation.getKeyFrame(stateTime, true);
            batch.draw(frame, getX() - frame.getRegionWidth() / 2, getY() - frame.getRegionHeight() / 2, 32, 32);
        } else {
            batch.draw(bombTexture, getX() - bombTexture.getWidth() / 2, getY() - bombTexture.getHeight() / 2, bombTexture.getWidth(), bombTexture.getHeight());
        }
    }


    public void update(float dt) {
        act(dt);
    }
    
}
