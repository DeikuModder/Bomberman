package com.bomberman.Entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Disposable;
import com.bomberman.Scenario.TileMap;


public class Player extends Actor implements Disposable{
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
    private Vector2 velocity;
    private TileMap tileMap;
    private float speed = 5f;
    private float width;
    private float height;

    public Player(Texture texture, TileMap tileMap, float x, float y) {
        this.player = texture;
        this.tileMap = tileMap;
        position = new Vector2(x, y);
        velocity = new Vector2(speed, speed);
        rightTexture = new Texture("bomberman-sprite-2.png");
        leftTexture = new Texture("bomberman-sprite-4.png");
        upTexture = new Texture("bomberman-sprite-1.png");
        downTexture = new Texture("bomberman-sprite-3.png");
        this.width = 32; // Establece el ancho de la textura
        this.height = 32; // Establece el alto de la textura

        // Create animations
        TextureRegion[][] temp = new TextureRegion(player).split(player.getWidth() / 4, player.getHeight());
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

    @Override
    public void act(float delta) {
        super.act(delta);

        stateTime += delta;

      // Handle keyboard input
    if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
        moveRight();
        playerAnimation = rightAnimation;
    } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
        moveLeft();
        playerAnimation = leftAnimation;
    } else if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
        moveUp();
        playerAnimation = upAnimation;
    } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
        moveDown();
        playerAnimation = downAnimation;
    }
}

    private void moveRight() {
        if (canMoveRight()) {
            position.x += velocity.x;
        }
    }

    private void moveLeft() {
        if (canMoveLeft()) {
            position.x -= velocity.x;
        }
    }

    private void moveUp() {
        if (canMoveUp()) {
            position.y += velocity.y;
        }
    }

    private void moveDown() {
        if (canMoveDown()) {
            position.y -= velocity.y;
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        TextureRegion frame = playerAnimation.getKeyFrame(stateTime, true);
        batch.draw(frame, position.x, position.y, width, height);
    }

    private boolean canMoveRight() {
        int tileX = (int) (position.x + velocity.x) / tileMap.getTileWidth();
        int tileY = (int) position.y / tileMap.getTileHeight();
        return !tileMap.isCollidable(tileX, tileY);
    }
    
    private boolean canMoveLeft() {
        int tileX = (int) (position.x - velocity.x) / tileMap.getTileWidth();
        int tileY = (int) position.y / tileMap.getTileHeight();
        return !tileMap.isCollidable(tileX, tileY);
    }
    
    private boolean canMoveUp() {
        int tileX = (int) position.x / tileMap.getTileWidth();
        int tileY = (int) (position.y + velocity.y) / tileMap.getTileHeight();
        return !tileMap.isCollidable(tileX, tileY);
    }
    
    private boolean canMoveDown() {
        int tileX = (int) position.x / tileMap.getTileWidth();
        int tileY = (int) (position.y - velocity.y) / tileMap.getTileHeight();
        return !tileMap.isCollidable(tileX, tileY);
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