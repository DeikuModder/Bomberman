/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bomberman;

/**
 * Constantes globales del juego.
 * Todas las constantes son static final para evitar instanciación innecesaria.
 * 
 * @author gabri
 */
public final class ConstantValues {
    
    // Constructor privado para evitar instanciación
    private ConstantValues() {}
    
    // Dimensiones de la ventana
    public static final int WINDOW_WIDTH = 800;
    public static final int WINDOW_HEIGHT = 480;
    
    // Tamaño de los bloques/tiles
    public static final int BLOCK_SIZE = 32;
    public static final int GRID_SIZE = BLOCK_SIZE;
    
    // Configuración del jugador
    public static final int DEFAULT_PLAYER_LIVES = 25;
    public static final int DEFAULT_MAX_BOMBS = 2;
    public static final float DEFAULT_BOMB_TIMER = 5f;
    public static final float DEFAULT_PLAYER_SPEED = 300f;
    public static final float INVULNERABILITY_DURATION = 1.0f;
    
    // Configuración del mapa
    public static final int DEFAULT_BLOCK_COUNT = 35;
    public static final int DEFAULT_ENEMY_COUNT = 5;
    
    // Spawn del jugador (en tiles de la cuadrícula)
    public static final int PLAYER_SPAWN_TILE_X = 3;
    public static final int PLAYER_SPAWN_TILE_Y = 3;
    
    // Zonas seguras de generación (en tiles)
    public static final int SPAWN_SAFE_RADIUS = 2; // Cajas no se generan cerca del jugador
    public static final int ENEMY_SPAWN_MIN_DISTANCE = 6; // Enemigos no se generan tan cerca del jugador
}
