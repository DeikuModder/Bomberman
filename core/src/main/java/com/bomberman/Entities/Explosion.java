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
    private Texture explosion;
    private TextureRegion[] explosionFrames;

    public Explosion(float x, float y) {
        explosion = new Texture("Explosion.png");
        // Configurar la animación de la explosión
        TextureRegion [][] temp = TextureRegion.split(explosion, explosion.getWidth() / 2 , explosion.getHeight() / 2);
        explosionFrames = new TextureRegion[2 * 2];
        int indice = 0;
        for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 2; j++) {
				explosionFrames[indice++] = temp[i][j];
			}
		}
        
        explosionAnimation = new Animation<>(0.2f, explosionFrames);
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
            batch.draw(frame, getX() - 80 , getY() - 70, 200, 200);
        }
    }

    public boolean isFinished() {
        return isFinished;
    }
}