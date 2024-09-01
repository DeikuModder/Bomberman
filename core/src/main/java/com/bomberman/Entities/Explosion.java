package com.bomberman.Entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Explosion extends Actor {
    private Animation<TextureRegion> explosionAnimation;
    private float stateTime;
    private boolean isFinished;

    public Explosion(float x, float y) {
        // Configurar la animación de la explosión
        TextureRegion[][] temp = TextureRegion.split(new Texture("explosion.png"), 32, 32);
        TextureRegion[] explosionFrames = new TextureRegion[temp.length * temp[0].length];
        int index = 0;
        for (int i = 0; i < temp.length; i++) {
            for (int j = 0; j < temp[i].length; j++) {
                explosionFrames[index++] = temp[i][j];
            }
        }
        explosionAnimation = new Animation<>(0.1f, explosionFrames);

        setPosition(x, y);
        stateTime = 0;
        isFinished = false;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        stateTime += delta;
        if (explosionAnimation.isAnimationFinished(stateTime)) {
            isFinished = true; // Marcar la explosión como terminada
            remove(); // Eliminar la explosión del escenario
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (!isFinished) {
            TextureRegion frame = explosionAnimation.getKeyFrame(stateTime, false);
            batch.draw(frame, getX() - frame.getRegionWidth() / 2, getY() - frame.getRegionHeight() / 2, 32, 32);
        }
    }

    public boolean isFinished() {
        return isFinished;
    }
}