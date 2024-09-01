package com.bomberman.Entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;

public class Bomb extends Actor {
    private Animation<TextureRegion> bombAnimation;
    private float stateTime;
    private boolean isExploded;
    private float explosionDelay;  // Tiempo antes de explotar
    private int explosionRadius;   // Radio de la explosión en número de tiles
    private Array<Explosion> explosions;
    private float width;
    private float height;
    private float x;
    private float y;

    public Bomb(Texture bombTexture, float x, float y, float explosionDelay, int explosionRadius) {
        this.explosionDelay = explosionDelay;
        this.explosionRadius = explosionRadius;
        this.isExploded = false;
        this.x = x;
        this.y = y;
        this.width = 32; // Establece el ancho de la textura
        this.height = 32; // Establece el alto de la textura
        // Configurar animación de la bomba
        TextureRegion[][] temp = TextureRegion.split(bombTexture, bombTexture.getWidth() / 3, bombTexture.getHeight());
        TextureRegion[] frames = new TextureRegion[temp.length * temp[0].length];
        int index = 0;
        for (int i = 0; i < temp.length; i++) {
            for (int j = 0; j < temp[i].length; j++) {
                frames[index++] = temp[i][j];
            }
        }
        bombAnimation = new Animation<>(0.2f, frames);

        setPosition(x, y);
        stateTime = 0;
        explosions = new Array<>();
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        stateTime += delta;
        if (!isExploded) {
            explosionDelay -= delta;
            if (explosionDelay <= 0) {
                explode();
            }
        } 
    }

    private void explode() {
        isExploded = true;
        generateExplosions();
    }

    private void generateExplosions() {
        // Generar la explosión principal y en las direcciones cardinales
        explosions.add(new Explosion(getX(), getY())); // Explosión en la posición de la bomba

        for (int i = 1; i <= explosionRadius; i++) {
            explosions.add(new Explosion(getX() + i * 32, getY())); // Derecha
            explosions.add(new Explosion(getX() - i * 32, getY())); // Izquierda
            explosions.add(new Explosion(getX(), getY() + i * 32)); // Arriba
            explosions.add(new Explosion(getX(), getY() - i * 32)); // Abajo
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (isExploded) {
            for (Explosion explosion : explosions) {
                explosion.draw(batch, parentAlpha);
            }
        } else {
            TextureRegion frame = bombAnimation.getKeyFrame(stateTime, true);
            batch.draw(frame, x, y, width, height);
        }
    }

    public boolean isExploded() {
        return isExploded;
    }

    public Array<Explosion> getExplosions() {
        return explosions;
    }
    public Rectangle getBounds() {
    return new Rectangle(x, y, width, height);
}
}