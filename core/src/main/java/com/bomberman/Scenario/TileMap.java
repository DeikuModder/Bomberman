/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bomberman.Scenario;

import com.bomberman.Entities.Block;
import com.bomberman.Entities.Bomb;
import com.bomberman.Entities.Enemy;
import com.bomberman.Entities.ExplosionPart;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.bomberman.ConstantValues;

public class TileMap implements Bomb.BlockCheck {
    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;
    private Array<Rectangle> collisionRectangles;
    private ShapeRenderer shapeRenderer; // Para depuración visual
    private Array<Bomb> bombs = new Array<>();
    private Array<Block> blocks = new Array<>();
    private Array<Enemy> enemies = new Array<>();
    private int totalblocks = ConstantValues.DEFAULT_BLOCK_COUNT;
    private int totalEnemies = ConstantValues.DEFAULT_ENEMY_COUNT;  // Número de enemigos a generar
    private List<CollisionListener> collisionListeners = new ArrayList<>();
    
    // Flag para activar/desactivar modo debug (visualización de hitboxes)
    private boolean debugMode = false;

    public TileMap(String mapPath) {
        map = new TmxMapLoader().load(mapPath);
        mapRenderer = new OrthogonalTiledMapRenderer(map);
        collisionRectangles = new Array<>();
        shapeRenderer = new ShapeRenderer(); // Inicializar el ShapeRenderer para depuración
        loadCollisions();
        generateRandomBlocks(totalblocks);
        // Los enemigos se generan después de cargar la textura en PlayState
    }
    
    /**
     * Genera enemigos en posiciones aleatorias válidas del mapa.
     * 
     * @param enemyTexture Textura para los enemigos
     * @param count Número de enemigos a generar
     */
    public void generateEnemies(com.badlogic.gdx.graphics.Texture enemyTexture, int count) {
        TiledMapTileLayer groundLayer = (TiledMapTileLayer) map.getLayers().get("Capa de patrones 1");
        TiledMapTileLayer blockLayer = (TiledMapTileLayer) map.getLayers().get("blocks");
        
        int mapWidth = groundLayer.getWidth();
        int mapHeight = groundLayer.getHeight();
        int tileWidth = (int) groundLayer.getTileWidth();
        int tileHeight = (int) groundLayer.getTileHeight();
        
        totalEnemies = count;
        
        for (int i = 0; i < count; i++) {
            int x, y;
            int attempts = 0;
            int maxAttempts = 200;
            
            // Encontrar una posición válida que no esté ocupada y alejada del jugador
            do {
                x = MathUtils.random(mapWidth - 1);
                y = MathUtils.random(mapHeight - 1);
                attempts++;
                
                boolean blocked = blockLayer.getCell(x, y) != null || isBlockAt(x * tileWidth, y * tileHeight);
                boolean tooCloseToPlayer = Math.abs(x - ConstantValues.PLAYER_SPAWN_TILE_X)
                        + Math.abs(y - ConstantValues.PLAYER_SPAWN_TILE_Y) < ConstantValues.ENEMY_SPAWN_MIN_DISTANCE;
                
                if (!blocked && !tooCloseToPlayer) {
                    break;
                }
            } while (attempts < maxAttempts);
            
            if (attempts < maxAttempts) {
                // Crear el enemigo con referencia al checker de colisiones
                Enemy enemy = new Enemy(enemyTexture, x * tileWidth, y * tileHeight, 
                    bounds -> checkCollisionForEnemy(bounds));
                enemies.add(enemy);
                System.out.println("Enemy spawned at: (" + x + ", " + y + ")");
            } else {
                System.out.println("No se encontró posición válida para un enemigo");
            }
        }
    }
    
    /**
     * Verifica si hay un bloque destructible en la posición dada.
     */
    private boolean isBlockAt(float x, float y) {
        for (Block block : blocks) {
            if (block.getBounds().contains(x, y)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Versión de checkCollision para enemigos (no colisionan con otros enemigos).
     */
    private boolean checkCollisionForEnemy(Rectangle objectBounds) {
        // Colisión con tiles del mapa
        for (Rectangle rect : collisionRectangles) {
            if (objectBounds.overlaps(rect)) {
                return true;
            }
        }
        // Colisión con bloques destructibles
        for (Block block : blocks) {
            if (objectBounds.overlaps(block.getBounds())) {
                return true;
            }
        }
        // Colisión con bombas
        for (Bomb bomb : bombs) {
            if (objectBounds.overlaps(bomb.getBounds())) {
                return true;
            }
        }
        return false;
    }
    private void loadCollisions() {
        // Obtén la capa de bloques con colisiones
        TiledMapTileLayer collisionLayer = (TiledMapTileLayer) map.getLayers().get("blocks");
    
        // Verifica que la capa exista
        if (collisionLayer != null) {
            int tileWidth = (int) collisionLayer.getTileWidth();
            int tileHeight = (int) collisionLayer.getTileHeight();
    
            // Recorre toda la capa de colisiones
            for (int x = 0; x < collisionLayer.getWidth(); x++) {
                for (int y = 0; y < collisionLayer.getHeight(); y++) {
                    TiledMapTileLayer.Cell cell = collisionLayer.getCell(x, y);
    
                    // Si la celda no es nula y contiene un tile con valor colisionable (ID = 2)
                    if (cell != null && cell.getTile() != null) {
                        TiledMapTile tile = cell.getTile();
                        int tileId = tile.getId();
    
                        // Los tiles con ID 2 son los colisionables
                        if (tileId == 2) {
                            Rectangle rectangle = new Rectangle(x * tileWidth, y * tileHeight, tileWidth, tileHeight);
                            collisionRectangles.add(rectangle);
                        }
                    }
                }
            }
        }
    }

    public void generateRandomBlocks(int blockCount) {
        TiledMapTileLayer groundLayer = (TiledMapTileLayer) map.getLayers().get("Capa de patrones 1");
        TiledMapTileLayer blockLayer = (TiledMapTileLayer) map.getLayers().get("blocks");
    
        int mapWidth = groundLayer.getWidth();
        int mapHeight = groundLayer.getHeight();
        int tileWidth = (int) groundLayer.getTileWidth();
        int tileHeight = (int) groundLayer.getTileHeight();
    
        for (int i = 0; i < blockCount; i++) {
            int x = 0, y = 0;
            int attempts = 0;
            int maxAttempts = 200;
    
            // Encontrar una posición aleatoria libre, lejos del spawn del jugador
            do {
                x = MathUtils.random(mapWidth - 1);
                y = MathUtils.random(mapHeight - 1);
                attempts++;
    
                boolean occupied = blockLayer.getCell(x, y) != null || isBlockAt(x * tileWidth, y * tileHeight);
                boolean inSafeZone = Math.abs(x - ConstantValues.PLAYER_SPAWN_TILE_X) <= ConstantValues.SPAWN_SAFE_RADIUS
                        && Math.abs(y - ConstantValues.PLAYER_SPAWN_TILE_Y) <= ConstantValues.SPAWN_SAFE_RADIUS;
    
                if (!occupied && !inSafeZone) {
                    break;
                }
            } while (attempts < maxAttempts);
    
            if (attempts < maxAttempts) {
                // Crear y agregar el bloque
                Block block = new Block(x * tileWidth, y * tileHeight);
                blocks.add(block);
                System.out.println("Block added at: (" + x + ", " + y + ")");
            }
        }
    }

   public void render(SpriteBatch batch, OrthographicCamera camera) {
    batch.setProjectionMatrix(camera.combined);
    mapRenderer.setView(camera);
   
    // Renderiza el mapa
    batch.begin();
    mapRenderer.render();
   // Dibujar bombas y sus explosiones
   for (Bomb bomb : bombs) {
    if (bomb.isExploded()) {
        for (ExplosionPart explosion : bomb.getExplosions()) {
            explosion.draw(batch, 1);
         //   System.out.println("se ha dibujado la explosion");
        }
    } else {
        bomb.draw(batch, 1);
        }
    }
    for (Block block : blocks) {
        block.draw(batch, 1);
    }
    
    // Dibujar enemigos
    for (Enemy enemy : enemies) {
        enemy.draw(batch, 1);
    }

    batch.end();

    // Solo dibujar hitboxes si está activo el modo debug
    if (debugMode) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        // Dibujar los rectángulos de colisión de las explosiones
        for (Bomb bomb : bombs) {
            Array<ExplosionPart> explosions = bomb.getExplosions();
            for (ExplosionPart explosion : explosions) {
                Rectangle explosionBounds = explosion.getBounds();
                shapeRenderer.setColor(Color.RED); // Rectángulos de explosiones en rojo
                shapeRenderer.rect(explosionBounds.x, explosionBounds.y, explosionBounds.width, explosionBounds.height);
            }
        }

        // Dibujar los rectángulos de colisión del jugador
        for (CollisionListener listener : collisionListeners) {
            Rectangle playerBounds = listener.getCollisionBounds();
            shapeRenderer.setColor(Color.GREEN); // Rectángulos del jugador en verde
            shapeRenderer.rect(playerBounds.x, playerBounds.y, playerBounds.width, playerBounds.height);
        }
        
        // Dibujar los rectángulos de colisión de los enemigos
        for (Enemy enemy : enemies) {
            if (enemy.isAlive()) {
                Rectangle enemyBounds = enemy.getBounds();
                shapeRenderer.setColor(Color.MAGENTA); // Rectángulos de enemigos en magenta
                shapeRenderer.rect(enemyBounds.x, enemyBounds.y, enemyBounds.width, enemyBounds.height);
            }
        }

        shapeRenderer.end();
    }
}
    

public boolean checkCollision(Rectangle objectBounds) {
    for (Rectangle rect : collisionRectangles) {
        if (objectBounds.overlaps(rect)) {
            return true;
        }
    }
     // Verificar colisiones con bombas
     for (Bomb bomb : bombs) {
        Rectangle bombBounds = bomb.getBounds();
        if (objectBounds.overlaps(bombBounds)) {
            return true;
        }
    }
    // Verificar colisiones con bloques
    for (Block block : blocks) {
        if (objectBounds.overlaps(block.getBounds())) {
            return true;
        }
    }
    return false;
}

    /**
     * Indica si una celda del mundo bloquea el paso de las explosiones
     * (muros del mapa o bloques destructibles).
     */
    @Override
    public boolean isBlocked(float worldX, float worldY) {
        for (Rectangle rect : collisionRectangles) {
            if (rect.contains(worldX, worldY)) {
                return true;
            }
        }
        for (Block block : blocks) {
            if (block.getBounds().contains(worldX, worldY)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Indica si ya existe una bomba en la celda indicada.
     */
    public boolean isBombAt(float worldX, float worldY) {
        for (Bomb bomb : bombs) {
            if (bomb.getBounds().overlaps(new Rectangle(worldX, worldY, ConstantValues.BLOCK_SIZE, ConstantValues.BLOCK_SIZE))) {
                return true;
            }
        }
        return false;
    }

    /**
     * Cantidad de enemigos vivos en el mapa.
     */
    public int getEnemiesLeft() {
        return enemies.size;
    }

    /**
     * Condición de victoria: no queda ningún enemigo vivo.
     */
    public boolean winConditionMet() {
        return totalEnemies > 0 && enemies.size == 0;
    }

    public void dispose() {
        map.dispose();
        mapRenderer.dispose();
        shapeRenderer.dispose(); // Liberar recursos del ShapeRenderer
    }

    public int getWidth() {
        int tileSize = (int) map.getProperties().get("tilewidth");
        int mapWidthInTiles = (int) map.getProperties().get("width");
        return mapWidthInTiles * tileSize;
    }
    public int getHeight() {
        int tileSize = (int) map.getProperties().get("tileheight");
        int mapHeightInTiles = (int) map.getProperties().get("height");
        return mapHeightInTiles * tileSize;
    }

      // Método para agregar bombas
      public void addBomb(Bomb bomb) {
        bombs.add(bomb);
    }

    public void update(float dt) {
        // Actualizar enemigos
        for (int i = enemies.size - 1; i >= 0; i--) {
            Enemy enemy = enemies.get(i);
            enemy.act(dt);
            
            // Verificar colisión con el jugador
            for (CollisionListener listener : collisionListeners) {
                if (enemy.collidesWith(listener.getCollisionBounds())) {
                    System.out.println("¡Enemigo tocó al jugador!");
                    listener.onCollision();
                }
            }
        }
        
        // Actualizar bombas
        for (int i = bombs.size - 1; i >= 0; i--) {
            Bomb bomb = bombs.get(i);
            bomb.act(dt);  // Actualiza la bomba
    
            // Verifica si la bomba ha explotado
            if (bomb.isExploded()) {
                // Maneja las explosiones de esta bomba
                Array<ExplosionPart> explosions = bomb.getExplosions();
    
                // Actualiza las explosiones
                for (ExplosionPart explosion : explosions) {
                    explosion.act(dt); // Actualizar la animación de la explosión
                    
                    // Verifica colisiones con bloques normales
                    for (int bi = blocks.size - 1; bi >= 0; bi--) {
                        Block block = blocks.get(bi);
                        if (explosion.getBounds().overlaps(block.getBounds())) {
                            // Destruir el bloque normal
                            blocks.removeIndex(bi);
                            break;
                        }
                    }
                    
                    // Verifica colisiones con enemigos
                    for (int j = enemies.size - 1; j >= 0; j--) {
                        Enemy enemy = enemies.get(j);
                        if (enemy.isAlive() && explosion.getBounds().overlaps(enemy.getBounds())) {
                            enemy.kill();
                            enemies.removeIndex(j);
                            System.out.println("¡Enemigo eliminado por explosión!");
                        }
                    }
                     
                    // Verifica colisiones con el jugador 
                    for (CollisionListener listener : collisionListeners) {
                        if (explosion.getBounds().overlaps(listener.getCollisionBounds())) {
                            System.out.println("Colisión detectada con el jugador");
                            listener.onCollision();
                        }
                    }
                }
    
                // Si todas las explosiones han terminado, elimina la bomba
                boolean allExplosionsFinished = true;
                for (ExplosionPart explosion : explosions) {
                    if (!explosion.isFinished()) {
                        allExplosionsFinished = false;
                        break;
                    }
                }
    
                if (allExplosionsFinished) {
                    bombs.removeIndex(i);  // Eliminar la bomba después de que todas sus explosiones han terminado
                }
            }
        }
    }

    public interface CollisionListener {
        void onCollision();
        Rectangle getCollisionBounds(); // Agregué este método
    }

    public void addCollisionListener(CollisionListener listener) {
        collisionListeners.add(listener);
    }

    /**
     * Activa o desactiva el modo debug.
     * Cuando está activo, se dibujan los hitboxes de colisión.
     * @param enabled true para activar, false para desactivar
     */
    public void setDebugMode(boolean enabled) {
        this.debugMode = enabled;
    }
    
    /**
     * Indica si el modo debug está activo.
     * @return true si está activo, false si no
     */
    public boolean isDebugMode() {
        return debugMode;
    }
    
    /**
     * Alterna el modo debug (toggle).
     */
    public void toggleDebugMode() {
        this.debugMode = !this.debugMode;
    }

    public void renderCollisionBounds() {
    shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

    // Dibujar los rectángulos de colisión de las explosiones
    for (Bomb bomb : bombs) {
        Array<ExplosionPart> explosions = bomb.getExplosions();
        for (ExplosionPart explosion : explosions) {
            Rectangle explosionBounds = explosion.getBounds();
            shapeRenderer.setColor(Color.BLUE); // Rectángulos de explosiones en rojo
            shapeRenderer.rect(explosionBounds.x, explosionBounds.y, explosionBounds.width, explosionBounds.height);
        }
    }

    // Dibujar los rectángulos de colisión del jugador
    for (CollisionListener listener : collisionListeners) {
        Rectangle playerBounds = listener.getCollisionBounds();
        shapeRenderer.setColor(Color.ORANGE); // Rectángulos del jugador en verde
        shapeRenderer.rect(playerBounds.x, playerBounds.y, playerBounds.width, playerBounds.height);
    }

    shapeRenderer.end();
}

}