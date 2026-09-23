package com.bomberman.Entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Disposable;
import com.bomberman.Bomberman;
import com.bomberman.ConstantValues;
import com.bomberman.Scenario.TileMap;
import com.bomberman.Scenario.TileMap.CollisionListener;
import com.bomberman.States.DeathState;


public class Player extends Actor implements Disposable, CollisionListener {
    private int lifebar = ConstantValues.DEFAULT_PLAYER_LIVES;
    private int maxBombs = ConstantValues.DEFAULT_MAX_BOMBS;  // Cantidad máxima de bombas
    private int currentBombs = 0;  // Bombas colocadas actualmente
    private int bombSize = 1;  // Tamaño de las explosiones (esto puede cambiar con power-ups)
    private float bombTimer = ConstantValues.DEFAULT_BOMB_TIMER;  // Tiempo hasta que la bomba explota (en segundos)
    private Texture player;
    private Animation<TextureRegion> playerAnimation;
    private Animation<TextureRegion> rightAnimation;
    private Animation<TextureRegion> leftAnimation;
    private Animation<TextureRegion> upAnimation;
    private Animation<TextureRegion> downAnimation;
    private Texture rightTexture;
    private Texture leftTexture;
    private Texture upTexture;
    private Texture downTexture;
    private float stateTime;
    private Vector2 position;  
    private TileMap tileMap;
    private float speed = ConstantValues.DEFAULT_PLAYER_SPEED; // velocidad del personaje (píxeles por segundo)
    private float targetX, targetY; // posición objetivo del personaje
    private float width;
    private float height;
    private int gridX;
    private int gridY;
    private final float GRID_SIZE = ConstantValues.GRID_SIZE;
    private boolean isMoving = false;
    private Rectangle playerBounds; // Rectángulo del jugador para colisiones
    private float invulnerabilityTimer = 0;
    private final float INVULNERABILITY_DURATION = ConstantValues.INVULNERABILITY_DURATION;  // 1 segundo de invulnerabilidad

    public Player(Texture texture, TileMap tileMap, float x, float y) {
        this.player = texture;
        this.tileMap = tileMap;
        tileMap.addCollisionListener(this);
        position = new Vector2(x, y);
        rightTexture = new Texture("bomberman-sprite-2.png");
        leftTexture = new Texture("bomberman-sprite-4.png");
        upTexture = new Texture("bomberman-sprite-1.png");
        downTexture = new Texture("bomberman-sprite-3.png");
        this.width = 32; // Establece el ancho de la textura
        this.height = 32; // Establece el alto de la textura
    
        // Alinea la posición inicial a la cuadrícula
        gridX = (int) (x / GRID_SIZE);
        gridY = (int) (y / GRID_SIZE);
        position.x = gridX * GRID_SIZE;
        position.y = gridY * GRID_SIZE;
    
        targetX = position.x; // Inicializa targetX y targetY para evitar movimientos erráticos
        targetY = position.y;

         // Inicializa el área de colisión del jugador
         playerBounds = new Rectangle(position.x, position.y, width, height);

        // Crear animaciones usando el método helper
        playerAnimation = createAnimation(player, 1, 0.3f);
        rightAnimation = createAnimation(rightTexture, 4, 0.3f);
        leftAnimation = createAnimation(leftTexture, 4, 0.3f);
        upAnimation = createAnimation(upTexture, 4, 0.3f);
        downAnimation = createAnimation(downTexture, 4, 0.3f);
    }
    
    /**
     * Crea una animación a partir de una textura dividida en frames horizontales.
     * 
     * @param texture La textura que contiene los frames de la animación
     * @param frameCount Número de frames horizontales en la textura
     * @param frameDuration Duración de cada frame en segundos
     * @return La animación creada
     */
    private Animation<TextureRegion> createAnimation(Texture texture, int frameCount, float frameDuration) {
        TextureRegion[][] temp = new TextureRegion(texture).split(texture.getWidth() / frameCount, texture.getHeight());
        TextureRegion[] frames = new TextureRegion[temp.length * temp[0].length];
        int index = 0;
        for (int i = 0; i < temp.length; i++) {
            for (int j = 0; j < temp[i].length; j++) {
                frames[index++] = temp[i][j];
            }
        }
        return new Animation<>(frameDuration, frames);
    }
public Bomb placeBomb(Texture bombTexture) {
    if (currentBombs >= maxBombs) {
        return null;
    }
    // Alinear la posición del jugador a la cuadrícula (usar GRID_SIZE, no bombSize)
    float bombX = Math.round(position.x / GRID_SIZE) * GRID_SIZE;
    float bombY = Math.round(position.y / GRID_SIZE) * GRID_SIZE;

    // No se puede colocar una bomba donde ya hay otra
    if (tileMap.isBombAt(bombX, bombY)) {
        return null;
    }

    currentBombs++;
    Bomb bomb = new Bomb(bombTexture, bombX, bombY, bombTimer, bombSize, tileMap);

    // Registrar listener para recuperar la bomba cuando explote
    bomb.setExplosionListener(new Bomb.BombExplodedListener() {
        @Override
        public void onBombExploded() {
            bombExploded();
        }
    });

    return bomb;
}

public void bombExploded() {
    currentBombs--;
}
    @Override
    public void act(float delta) {
        super.act(delta);

        stateTime += delta;

        if (!isMoving) {
            // Detectar la primera pulsación de una tecla
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && canMoveTo(gridX + 1, gridY)) {
                targetX = position.x + GRID_SIZE;
                isMoving = true;
                playerAnimation = rightAnimation;
            } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && canMoveTo(gridX - 1, gridY)) {
                targetX = position.x - GRID_SIZE;
                isMoving = true;
                playerAnimation = leftAnimation;
            } else if (Gdx.input.isKeyPressed(Input.Keys.UP) && canMoveTo(gridX, gridY + 1)) {
                targetY = position.y + GRID_SIZE;
                isMoving = true;
                playerAnimation = upAnimation;
            } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN) && canMoveTo(gridX, gridY - 1)) {
                targetY = position.y - GRID_SIZE;
                isMoving = true;
                playerAnimation = downAnimation;
            }
        }

        if (isMoving) {
            moveTowardsTarget(delta);
        }
         // Actualizar el temporizador de invulnerabilidad
        if (invulnerabilityTimer > 0) {
             invulnerabilityTimer -= delta;
        }
    }

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

        if (position.x == targetX && position.y == targetY) {
            isMoving = false;
            gridX = (int) (position.x / GRID_SIZE);
            gridY = (int) (position.y / GRID_SIZE);
        }

        // Actualiza la posición del rectángulo de colisión
        playerBounds.setPosition(position.x, position.y);
    }
    
    private boolean canMoveTo(int newGridX, int newGridY) {
        // Calcula las coordenadas del rectángulo en la nueva posición
        float newX = newGridX * GRID_SIZE;
        float newY = newGridY * GRID_SIZE;

        // Actualiza temporalmente el rectángulo de colisión a la nueva posición
        playerBounds.setPosition(newX, newY);

        // Verifica si el rectángulo del jugador colisiona con algún tile del TileMap
        return !tileMap.checkCollision(playerBounds);
    }
    

    @Override
    public void draw(Batch batch, float parentAlpha) {
        TextureRegion frame = playerAnimation.getKeyFrame(stateTime, true);
        batch.draw(frame, position.x, position.y, width, height);
    }

    @Override
    public void dispose() {
        player.dispose();
        rightTexture.dispose();
        leftTexture.dispose();
        upTexture.dispose();
        downTexture.dispose();
    }
    @Override 
    public Rectangle getCollisionBounds() {
        return playerBounds;
    }

  @Override
  public void onCollision() {
    if (invulnerabilityTimer <= 0) {
        lifebar--;
        System.out.println("has recibido daño");

        if (lifebar <= 0){
            Bomberman.getInstance().setState(new DeathState());
        } else {
            invulnerabilityTimer = INVULNERABILITY_DURATION;  // Activar la invulnerabilidad
        }
    }
}

    public int getLifebar() {
        return lifebar;
    }

    public boolean isAlive() {
        return lifebar > 0;
    }
}