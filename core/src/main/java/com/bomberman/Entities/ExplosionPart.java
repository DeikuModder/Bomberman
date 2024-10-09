package com.bomberman.Entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class ExplosionPart extends Actor {
    private Animation<TextureRegion> explosionAnimation;
    private float stateTime;
    private boolean isFinished;
    private String typeOrientation;  // Puede ser "center", "side", o "corner"

    public ExplosionPart(float x, float y, String type, Texture texture) {
        this.typeOrientation = type;
        // Asumiendo que el sprite está dividido en frames de 32x32
        TextureRegion[][] temp = TextureRegion.split(texture, 32, 32);
        TextureRegion[] frames = new TextureRegion[temp.length * temp[0].length];
        int index = 0;
        for (int i = 0; i < temp.length; i++) {
            for (int j = 0; j < temp[i].length; j++) {
                frames[index++] = temp[i][j];
            }
        }

     // Rotar según la orientación
    switch (typeOrientation) {
        case "side_up":
        case "corner_up":
        break;

        case "side_down":
        case "corner_down":
        break;

        case "side_right":
        case "corner_right":
            // Rotar 180 grados: flip en X
            for (TextureRegion frame : frames) {
                frame.flip(true, false);  // Girar horizontalmente (180 grados)
            }
            break;

        case "side_left":
        case "corner_left":
            // Ya están en la dirección correcta (hacia la izquierda)
            break;
    }

    // Crear la animación
    explosionAnimation = new Animation<>(0.1f, frames);
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
            batch.draw(frame, getX(), getY(), 32, 32);
        }
    }

    public boolean isFinished() {
        return isFinished;
    }
    
     public Rectangle getBounds() {
        return new Rectangle(getX(), getY(), 32, 32);
    }
}