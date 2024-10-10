/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bomberman.Scenario;

import com.bomberman.Entities.Block;
import com.bomberman.Entities.Bomb;
import com.bomberman.Entities.ExplosionPart;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class TileMap {
    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;
    private Array<Rectangle> collisionRectangles;
    private ShapeRenderer shapeRenderer; // Para depuración visual
    private Array<Bomb> bombs = new Array<>();
    private Array<Block> blocks = new Array<>();
    private int totalblocks = 35;
    private List<CollisionListener> collisionListeners = new ArrayList<>();

    public TileMap(String mapPath) {
        map = new TmxMapLoader().load(mapPath);
        mapRenderer = new OrthogonalTiledMapRenderer(map);
        collisionRectangles = new Array<>();
       // shapeRenderer = new ShapeRenderer(); // Inicializar el ShapeRenderer para depuración
        loadCollisions();
        generateRandomBlocks(totalblocks);
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
                            System.out.println("Collision rectangle added at: " + rectangle);
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
    
        for (int i = 0; i < blockCount; i++) {
            int x, y;
    
            // Encontrar una posición aleatoria que no esté ocupada por un bloque en la capa "blocks"
            do {
                x = MathUtils.random(mapWidth - 1);
                y = MathUtils.random(mapHeight - 1);
            } while (blockLayer.getCell(x, y) != null);
    
            // Crear y agregar el bloque
            Block block = new Block(x * groundLayer.getTileWidth(), y * groundLayer.getTileHeight());
            blocks.add(block);
            System.out.println("Block added at: (" + x + ", " + y + ")");
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
            System.out.println("se ha dibujado la explosion");
        }
    } else {
        bomb.draw(batch, 1);
        }
    }
    for (Block block : blocks) {
        block.draw(batch, 1);
    }

    batch.end();

    // Dibujar los rectángulos de colisión
   // shapeRenderer.setProjectionMatrix(camera.combined);
    //shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
    //shapeRenderer.setColor(Color.RED); // Rojo para colisiones
    //for (Rectangle rect : collisionRectangles) {
     //   shapeRenderer.rect(rect.x, rect.y, rect.width, rect.height);
    //}
    //shapeRenderer.end();
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
                    for (Block block : blocks) {
                        if (explosion.getBounds().overlaps(block.getBounds())) {
                        // Destruir el bloque normal
                        blocks.removeValue(block, true);
                        break;
                        }
                    }
                      // Verifica colisiones con el jugador (nuevo código)
                  for (CollisionListener listener : collisionListeners) {
                      if (explosion.getBounds().overlaps(listener.getCollisionBounds())) {
                        listener.onCollision(explosion.getBounds());
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
        void onCollision(Rectangle collisionArea);
        Rectangle getCollisionBounds(); // Agregué este método
    }

    public void addCollisionListener(CollisionListener listener) {
        collisionListeners.add(listener);
    }

}