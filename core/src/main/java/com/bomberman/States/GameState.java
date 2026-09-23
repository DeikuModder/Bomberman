package com.bomberman.States;

/**
 * Interfaz común de los estados del juego.
 */
public interface GameState {
    void update(float deltaTime);
    void render();
    void dispose();
}