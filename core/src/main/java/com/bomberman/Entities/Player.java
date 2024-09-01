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
import com.bomberman.Scenario.TileMap;


public class Player extends Actor implements Disposable {
    private int maxBombs = 1;  // Cantidad máxima de bombas
    private int currentBombs = 0;  // Bombas colocadas actualmente
    private int bombSize = 32;  // Tamaño de las explosiones (esto puede cambiar con power-ups)
    private float bombTimer = 5f;  // Tiempo hasta que la bomba explota (en segundos)
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
    private float speed = 300f; // velocidad del personaje (píxeles por segundo)
    private float targetX, targetY; // posición objetivo del personaje
    private float width;
    private float height;
    private int gridX;
    private int gridY;
    private final float GRID_SIZE = 32;
    private boolean isMoving = false;
    private Rectangle playerBounds; // Rectángulo del jugador para colisiones

    public Player(Texture texture, TileMap tileMap, float x, float y) {
        this.player = texture;
        this.tileMap = tileMap;
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

        // Create animations
        TextureRegion[][] temp = new TextureRegion(player).split(player.getWidth() , player.getHeight());
        TextureRegion[] frames = new TextureRegion[temp.length * temp[0].length];
        int index = 0;
        for (int i = 0; i < temp.length; i++) {
            for (int j = 0; j < temp[i].length; j++) {
                frames[index++] = temp[i][j];
            }
        }

        playerAnimation = new Animation<>(0.3f, frames);
        
        // Create right animation
    temp = new TextureRegion(rightTexture).split(rightTexture.getWidth() / 4, rightTexture.getHeight());
    frames = new TextureRegion[temp.length * temp[0].length];
    index = 0;
    for (int i = 0; i < temp.length; i++) {
        for (int j = 0; j < temp[i].length; j++) {
            frames[index++] = temp[i][j];
        }
    }
    rightAnimation = new Animation<>(0.3f, frames);

    // Create left animation
    temp = new TextureRegion(leftTexture).split(leftTexture.getWidth() / 4, leftTexture.getHeight());
    frames = new TextureRegion[temp.length * temp[0].length];
    index = 0;
    for (int i = 0; i < temp.length; i++) {
        for (int j = 0; j < temp[i].length; j++) {
            frames[index++] = temp[i][j];
        }
    }
    leftAnimation = new Animation<>(0.3f, frames);

    // Create up animation
    temp = new TextureRegion(upTexture).split(upTexture.getWidth() / 4, upTexture.getHeight());
    frames = new TextureRegion[temp.length * temp[0].length];
    index = 0;
    for (int i = 0; i < temp.length; i++) {
        for (int j = 0; j < temp[i].length; j++) {
            frames[index++] = temp[i][j];
        }
    }
    upAnimation = new Animation<>(0.3f, frames);

    // Create down animation
    temp = new TextureRegion(downTexture).split(downTexture.getWidth() / 4, downTexture.getHeight());
    frames = new TextureRegion[temp.length * temp[0].length];
    index = 0;
    for (int i = 0; i < temp.length; i++) {
        for (int j = 0; j < temp[i].length; j++) {
            frames[index++] = temp[i][j];
        }
    }
    downAnimation = new Animation<>(0.3f, frames);
}
public Bomb placeBomb(Texture bombTexture) {
    if (currentBombs < maxBombs) {
        // Alinear la posición del jugador a la cuadrícula
        float bombX = Math.round(position.x / bombSize) * bombSize;
        float bombY = Math.round(position.y / bombSize) * bombSize;

        currentBombs++;
        return new Bomb(bombTexture, bombX, bombY, bombTimer, bombSize);
    }
    return null;
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
}