package com.bomberman;

import com.badlogic.gdx.graphics.Texture;

/**
 * Texturas compartidas por varias entidades del juego.
 * Las texturas se cargan una sola vez y se reutilizan,
 * evitando cargas duplicadas y fugas de memoria.
 */
public final class Assets {

    private static Texture block;
    private static Texture bomb;

    private Assets() {}

    public static Texture blockTexture() {
        if (block == null) {
            block = new Texture("block2.png");
        }
        return block;
    }

    public static Texture bombTexture() {
        if (bomb == null) {
            bomb = new Texture("Bombs.png");
        }
        return bomb;
    }

    /**
     * Libera todas las texturas compartidas.
     * Debe llamarse cuando el juego se cierra.
     */
    public static void disposeAll() {
        if (block != null) {
            block.dispose();
            block = null;
        }
        if (bomb != null) {
            bomb.dispose();
            bomb = null;
        }
    }
}