package com.bomberman.Entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Disposable;
import com.bomberman.ConstantValues;

/**
 * Clase base para enemigos.
 * Los enemigos se mueven por el mapa y dañan al jugador al contacto.
 */
public class Enemy extends Actor implements Disposable {
    
    // Propiedades básicas
    private Vector2 position;
    private float width;
    private float height;
    private Rectangle bounds;
    private boolean isAlive;
    
    // Movimiento
    private float speed;
    private Vector2 direction;
    private float moveTimer;
    private float changeDirectionTime;
    private boolean isMoving;
    private float targetX, targetY;
    private int gridX, gridY;
    
    // Gráficos
    private Texture texture;
    private Animation<TextureRegion> animation;
    private float stateTime;
    
    // Referencia para verificar colisiones
    private CollisionChecker collisionChecker;
    
    // Constantes
    private static final float GRID_SIZE = ConstantValues.GRID_SIZE;
    private static final float DEFAULT_SPEED = 100f;
    private static final float MIN_DIRECTION_CHANGE_TIME = 1.0f;
    private static final float MAX_DIRECTION_CHANGE_TIME = 3.0f;
    
    /**
     * Interfaz para verificar colisiones con el mapa.
     */
    public interface CollisionChecker {
        boolean checkCollision(Rectangle bounds);
    }
    
    /**
     * Constructor del enemigo.
     * 
     * @param texture Textura del enemigo
     * @param x Posición X inicial (en píxeles)
     * @param y Posición Y inicial (en píxeles)
     * @param collisionChecker Referencia para verificar colisiones
     */
    public Enemy(Texture texture, float x, float y, CollisionChecker collisionChecker) {
        this.texture = texture;
        this.collisionChecker = collisionChecker;
        this.width = GRID_SIZE;
        this.height = GRID_SIZE;
        
        // Alinear posición a la cuadrícula
        this.gridX = (int) (x / GRID_SIZE);
        this.gridY = (int) (y / GRID_SIZE);
        this.position = new Vector2(gridX * GRID_SIZE, gridY * GRID_SIZE);
        
        this.targetX = position.x;
        this.targetY = position.y;
        
        this.bounds = new Rectangle(position.x, position.y, width, height);
        this.isAlive = true;
        this.speed = DEFAULT_SPEED;
        this.direction = new Vector2(0, 0);
        this.moveTimer = 0;
        this.changeDirectionTime = MathUtils.random(MIN_DIRECTION_CHANGE_TIME, MAX_DIRECTION_CHANGE_TIME);
        this.isMoving = false;
        this.stateTime = 0;
        
        // Crear animación simple (si la textura tiene múltiples frames)
        createAnimation();
        
        // Elegir dirección inicial aleatoria
        chooseRandomDirection();
    }
    
    /**
     * Crea la animación del enemigo.
     */
    private void createAnimation() {
        // Intentar dividir la textura en 3 frames horizontales
        int frameCount = 3;
        int frameWidth = texture.getWidth() / frameCount;
        int frameHeight = texture.getHeight();
        
        // Si la textura es muy pequeña, usar un solo frame
        if (frameWidth < 16) {
            frameCount = 1;
            frameWidth = texture.getWidth();
        }
        
        TextureRegion[][] temp = TextureRegion.split(texture, frameWidth, frameHeight);
        TextureRegion[] frames = new TextureRegion[temp[0].length];
        for (int i = 0; i < temp[0].length; i++) {
            frames[i] = temp[0][i];
        }
        
        animation = new Animation<>(0.2f, frames);
    }
    
    /**
     * Elige una dirección aleatoria para moverse.
     */
    private void chooseRandomDirection() {
        // 4 direcciones posibles: arriba, abajo, izquierda, derecha
        int dir = MathUtils.random(3);
        switch (dir) {
            case 0: // Arriba
                direction.set(0, 1);
                break;
            case 1: // Abajo
                direction.set(0, -1);
                break;
            case 2: // Izquierda
                direction.set(-1, 0);
                break;
            case 3: // Derecha
                direction.set(1, 0);
                break;
        }
        
        // Reiniciar el temporizador
        changeDirectionTime = MathUtils.random(MIN_DIRECTION_CHANGE_TIME, MAX_DIRECTION_CHANGE_TIME);
        moveTimer = 0;
    }
    
    /**
     * Actualiza el estado del enemigo.
     */
    @Override
    public void act(float delta) {
        super.act(delta);
        
        if (!isAlive) return;
        
        stateTime += delta;
        moveTimer += delta;
        
        // Cambiar dirección periódicamente
        if (moveTimer >= changeDirectionTime) {
            chooseRandomDirection();
        }
        
        // Movimiento basado en grid (similar al jugador)
        if (!isMoving) {
            tryMove();
        }
        
        if (isMoving) {
            moveTowardsTarget(delta);
        }
    }
    
    /**
     * Intenta moverse en la dirección actual.
     */
    private void tryMove() {
        int newGridX = gridX + (int) direction.x;
        int newGridY = gridY + (int) direction.y;
        
        float newX = newGridX * GRID_SIZE;
        float newY = newGridY * GRID_SIZE;
        
        // Verificar si puede moverse a la nueva posición
        Rectangle testBounds = new Rectangle(newX, newY, width, height);
        
        if (collisionChecker != null && !collisionChecker.checkCollision(testBounds)) {
            // Puede moverse
            targetX = newX;
            targetY = newY;
            isMoving = true;
        } else {
            // No puede moverse, elegir nueva dirección
            chooseRandomDirection();
        }
    }
    
    /**
     * Mueve al enemigo hacia la posición objetivo.
     */
    private void moveTowardsTarget(float delta) {
        float distanceX = targetX - position.x;
        float distanceY = targetY - position.y;
        
        if (Math.abs(distanceX) > speed * delta) {
            position.x += speed * delta * Math.signum(distanceX);
        } else {
            position.x = targetX;
        }
        
        if (Math.abs(distanceY) > speed * delta) {
            position.y += speed * delta * Math.signum(distanceY);
        } else {
            position.y = targetY;
        }
        
        // Actualizar bounds
        bounds.setPosition(position.x, position.y);
        
        // Verificar si llegó al destino
        if (position.x == targetX && position.y == targetY) {
            isMoving = false;
            gridX = (int) (position.x / GRID_SIZE);
            gridY = (int) (position.y / GRID_SIZE);
        }
    }
    
    /**
     * Dibuja el enemigo.
     */
    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (!isAlive) return;
        
        TextureRegion frame = animation.getKeyFrame(stateTime, true);
        batch.draw(frame, position.x, position.y, width, height);
    }
    
    /**
     * Verifica si el enemigo colisiona con un rectángulo dado.
     */
    public boolean collidesWith(Rectangle other) {
        return isAlive && bounds.overlaps(other);
    }
    
    /**
     * Mata al enemigo (por ejemplo, cuando es alcanzado por una explosión).
     */
    public void kill() {
        isAlive = false;
    }
    
    // Getters
    
    public boolean isAlive() {
        return isAlive;
    }
    
    public Rectangle getBounds() {
        return bounds;
    }
    
    public float getX() {
        return position.x;
    }
    
    public float getY() {
        return position.y;
    }
    
    public void setSpeed(float speed) {
        this.speed = speed;
    }
    
    @Override
    public void dispose() {
        // La textura se maneja externamente (no la liberamos aquí)
    }
}
