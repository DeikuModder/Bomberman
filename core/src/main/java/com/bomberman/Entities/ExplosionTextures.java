package com.bomberman.Entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Disposable;

/**
 * Gestor de texturas de explosiones.
 * Implementa el patrón Singleton para cargar las texturas una sola vez
 * y reutilizarlas en todas las explosiones, evitando memory leaks.
 */
public class ExplosionTextures implements Disposable {
    
    private static ExplosionTextures instance;
    
    // Texturas de explosión (cargadas una sola vez)
    private Texture centerTexture;
    private Texture sideTexture;
    private Texture cornerTexture;
    private Texture sideDownTexture;
    private Texture sideUpTexture;
    private Texture cornerUpTexture;
    private Texture cornerDownTexture;
    
    private boolean isLoaded = false;
    
    // Constructor privado (Singleton)
    private ExplosionTextures() {}
    
    /**
     * Obtiene la instancia única del gestor de texturas.
     */
    public static ExplosionTextures getInstance() {
        if (instance == null) {
            instance = new ExplosionTextures();
        }
        return instance;
    }
    
    /**
     * Carga todas las texturas de explosión.
     * Solo se cargan una vez, llamadas posteriores son ignoradas.
     */
    public void load() {
        if (!isLoaded) {
            centerTexture = new Texture("center.png");
            sideTexture = new Texture("side2.png");
            cornerTexture = new Texture("corner2.png");
            sideDownTexture = new Texture("sidedown.png");
            sideUpTexture = new Texture("sideup.png");
            cornerUpTexture = new Texture("cornerup.png");
            cornerDownTexture = new Texture("cornerdown.png");
            isLoaded = true;
        }
    }
    
    /**
     * Verifica si las texturas están cargadas.
     */
    public boolean isLoaded() {
        return isLoaded;
    }
    
    // Getters para cada textura
    public Texture getCenterTexture() {
        return centerTexture;
    }
    
    public Texture getSideTexture() {
        return sideTexture;
    }
    
    public Texture getCornerTexture() {
        return cornerTexture;
    }
    
    public Texture getSideDownTexture() {
        return sideDownTexture;
    }
    
    public Texture getSideUpTexture() {
        return sideUpTexture;
    }
    
    public Texture getCornerUpTexture() {
        return cornerUpTexture;
    }
    
    public Texture getCornerDownTexture() {
        return cornerDownTexture;
    }
    
    /**
     * Libera todas las texturas de memoria.
     * Debe llamarse cuando el juego se cierra.
     */
    @Override
    public void dispose() {
        if (isLoaded) {
            centerTexture.dispose();
            sideTexture.dispose();
            cornerTexture.dispose();
            sideDownTexture.dispose();
            sideUpTexture.dispose();
            cornerUpTexture.dispose();
            cornerDownTexture.dispose();
            isLoaded = false;
        }
        instance = null;
    }
}
