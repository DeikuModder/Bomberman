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
    private Array<ExplosionPart> explosions;
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
        // Texturas para cada parte de la explosión
        Texture centerTexture = new Texture("center.png");
        Texture sideTexture = new Texture("side2.png");
        Texture cornerTexture = new Texture("corner2.png");
        Texture sidedownTexture = new Texture("sidedown.png");
        Texture sideupTexture = new Texture("sideup.png");
        Texture cornerupTexture = new Texture("cornerup.png");
        Texture cornerdownTexture = new Texture ("cornerdown.png");
        // Añadir el centro de la explosión
        explosions.add(new ExplosionPart(getX(), getY(), "center", centerTexture));
    
        // Generar las explosiones en cada dirección
        for (int i = 1; i <= explosionRadius; i++) {
            // Arriba
            if (i == explosionRadius) {
                explosions.add(new ExplosionPart(getX(), getY() + i * 32, "corner_up", cornerupTexture));
            } else {
                explosions.add(new ExplosionPart(getX(), getY() + i * 32, "side_up", sideupTexture));
            }
    
            // Abajo
            if (i == explosionRadius) {
                explosions.add(new ExplosionPart(getX(), getY() - i * 32, "corner_down", cornerdownTexture));
            } else {
                explosions.add(new ExplosionPart(getX(), getY() - i * 32, "side_down", sidedownTexture));
            }
    
            // Derecha
            if (i == explosionRadius) {
                explosions.add(new ExplosionPart(getX() + i * 32, getY(), "corner_right", cornerTexture));
            } else {
                explosions.add(new ExplosionPart(getX() + i * 32, getY(), "side_right", sideTexture));
            }
    
            // Izquierda
            if (i == explosionRadius) {
                explosions.add(new ExplosionPart(getX() - i * 32, getY(), "corner_left", cornerTexture));
            } else {
                explosions.add(new ExplosionPart(getX() - i * 32, getY(), "side_left", sideTexture));
            }
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (isExploded) {
            System.out.println("se ha detectado que exploto");
            for (ExplosionPart explosion : explosions) {
                explosion.draw(batch, parentAlpha);
                System.out.println("se ha dibujado la explosion");
            }
        } else {
            TextureRegion frame = bombAnimation.getKeyFrame(stateTime, true);
            batch.draw(frame, x, y, width, height);
        }
    }

    public boolean isExploded() {
        return isExploded;
    }

    public Array<ExplosionPart> getExplosions() {
        return explosions;
    }
    public Rectangle getBounds() {
    return new Rectangle(x, y, width, height);
}
}