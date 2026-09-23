package com.bomberman.Entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.bomberman.ConstantValues;

public class Bomb extends Actor {
    private Animation<TextureRegion> bombAnimation;
    private float stateTime;
    private boolean isExploded;
    private boolean hasNotifiedExplosion;  // Para notificar solo una vez cuando explota
    private float explosionDelay;  // Tiempo antes de explotar
    private int explosionRadius;   // Radio de la explosión en número de tiles
    private Array<ExplosionPart> explosions;
    private BlockCheck blockCheck;  // Permite que la explosión se detenga en muros/bloques
    private BombExplodedListener explosionListener;  // Listener para notificar cuando explota

    // Interfaz para notificar cuando la bomba explota
    public interface BombExplodedListener {
        void onBombExploded();
    }

    /**
     * Verifica si una celda del mundo bloquea el paso de la explosión.
     */
    public interface BlockCheck {
        boolean isBlocked(float worldX, float worldY);
    }

    public Bomb(Texture bombTexture, float x, float y, float explosionDelay, int explosionRadius, BlockCheck blockCheck) {
        this.explosionDelay = explosionDelay;
        this.explosionRadius = explosionRadius;
        this.blockCheck = blockCheck;
        this.isExploded = false;
        this.hasNotifiedExplosion = false;
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
        setSize(ConstantValues.BLOCK_SIZE, ConstantValues.BLOCK_SIZE);
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
        // Notificar al listener que la bomba ha explotado
        if (explosionListener != null && !hasNotifiedExplosion) {
            hasNotifiedExplosion = true;
            explosionListener.onBombExploded();
        }
    }

    // Método para establecer el listener de explosión
    public void setExplosionListener(BombExplodedListener listener) {
        this.explosionListener = listener;
    }

    private boolean isBlockedAt(float worldX, float worldY) {
        return blockCheck != null && blockCheck.isBlocked(worldX, worldY);
    }

    private void generateExplosions() {
        // Obtener texturas del gestor (Singleton - evita memory leak)
        ExplosionTextures textures = ExplosionTextures.getInstance();
        if (!textures.isLoaded()) {
            textures.load();
        }

        int tileSize = ConstantValues.BLOCK_SIZE;

        // Añadir el centro de la explosión
        explosions.add(new ExplosionPart(getX(), getY(), "center", textures.getCenterTexture()));

        // Arriba
        for (int i = 1; i <= explosionRadius; i++) {
            float cx = getX();
            float cy = getY() + i * tileSize;
            if (isBlockedAt(cx, cy)) {
                explosions.add(new ExplosionPart(cx, cy, "corner_up", textures.getCornerUpTexture()));
                break;
            }
            if (i == explosionRadius) {
                explosions.add(new ExplosionPart(cx, cy, "corner_up", textures.getCornerUpTexture()));
            } else {
                explosions.add(new ExplosionPart(cx, cy, "side_up", textures.getSideUpTexture()));
            }
        }

        // Abajo
        for (int i = 1; i <= explosionRadius; i++) {
            float cx = getX();
            float cy = getY() - i * tileSize;
            if (isBlockedAt(cx, cy)) {
                explosions.add(new ExplosionPart(cx, cy, "corner_down", textures.getCornerDownTexture()));
                break;
            }
            if (i == explosionRadius) {
                explosions.add(new ExplosionPart(cx, cy, "corner_down", textures.getCornerDownTexture()));
            } else {
                explosions.add(new ExplosionPart(cx, cy, "side_down", textures.getSideDownTexture()));
            }
        }

        // Derecha
        for (int i = 1; i <= explosionRadius; i++) {
            float cx = getX() + i * tileSize;
            float cy = getY();
            if (isBlockedAt(cx, cy)) {
                explosions.add(new ExplosionPart(cx, cy, "corner_right", textures.getCornerTexture()));
                break;
            }
            if (i == explosionRadius) {
                explosions.add(new ExplosionPart(cx, cy, "corner_right", textures.getCornerTexture()));
            } else {
                explosions.add(new ExplosionPart(cx, cy, "side_right", textures.getSideTexture()));
            }
        }

        // Izquierda
        for (int i = 1; i <= explosionRadius; i++) {
            float cx = getX() - i * tileSize;
            float cy = getY();
            if (isBlockedAt(cx, cy)) {
                explosions.add(new ExplosionPart(cx, cy, "corner_left", textures.getCornerTexture()));
                break;
            }
            if (i == explosionRadius) {
                explosions.add(new ExplosionPart(cx, cy, "corner_left", textures.getCornerTexture()));
            } else {
                explosions.add(new ExplosionPart(cx, cy, "side_left", textures.getSideTexture()));
            }
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (isExploded) {
            for (ExplosionPart explosion : explosions) {
                explosion.draw(batch, parentAlpha);
            }
        } else {
            TextureRegion frame = bombAnimation.getKeyFrame(stateTime, true);
            batch.draw(frame, getX(), getY(), ConstantValues.BLOCK_SIZE, ConstantValues.BLOCK_SIZE);
        }
    }

    public boolean isExploded() {
        return isExploded;
    }

    public Array<ExplosionPart> getExplosions() {
        return explosions;
    }

    public Rectangle getBounds() {
        return new Rectangle(getX(), getY(), ConstantValues.BLOCK_SIZE, ConstantValues.BLOCK_SIZE);
    }
}