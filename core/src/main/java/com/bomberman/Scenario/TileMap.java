/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bomberman.Scenario;

import com.bomberman.ConstantValues;
import com.bomberman.Entities.Block;
import com.bomberman.Entities.Bomb;
import com.bomberman.Entities.Explosion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class TileMap {
    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;
    private Array<Rectangle> collisionRectangles;
    private ShapeRenderer shapeRenderer; // Para depuración visual
    private Array<Bomb> bombs = new Array<>();

    public TileMap(String mapPath) {
        map = new TmxMapLoader().load(mapPath);
        mapRenderer = new OrthogonalTiledMapRenderer(map);
        collisionRectangles = new Array<>();
        shapeRenderer = new ShapeRenderer(); // Inicializar el ShapeRenderer para depuración
        loadCollisions();
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

   public void render(SpriteBatch batch, OrthographicCamera camera) {
    batch.setProjectionMatrix(camera.combined);
    mapRenderer.setView(camera);
    
    // Renderiza el mapa
    batch.begin();
    mapRenderer.render();
    for (Bomb bomb : bombs) {
        bomb.draw(batch, 1);
    }

    batch.end();

    // Dibujar los rectángulos de colisión
    shapeRenderer.setProjectionMatrix(camera.combined);
    shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
    shapeRenderer.setColor(Color.RED); // Rojo para colisiones
    for (Rectangle rect : collisionRectangles) {
        shapeRenderer.rect(rect.x, rect.y, rect.width, rect.height);
    }
    shapeRenderer.end();
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

            // Verifica si la bomba ha explotado y maneja las explosiones
            if (bomb.isExploded()) {
                handleExplosions(bomb.getExplosions());
                bombs.removeIndex(i);  // Eliminar la bomba después de que explota
            }
        }
    }

     private void handleExplosions(Array<Explosion> explosions) {
        for (Explosion explosion : explosions) {
            // Verifica colisiones y otros efectos de las explosiones aquí
            // Por ejemplo, destruir bloques, eliminar enemigos, etc.
        }
    }

}