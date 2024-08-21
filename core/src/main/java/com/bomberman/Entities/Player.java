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
    private float stateTime;
    private Vector2 position;
    private Vector2 velocity;
    private TileMap tileMap;
    private float speed = 5f;

    public Player(Texture texture, TileMap tileMap, float x, float y) {
        this.player = texture;
        this.tileMap = tileMap;
        position = new Vector2(x, y);
        velocity = new Vector2(speed, speed);

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
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        stateTime += delta;

        // Handle keyboard input
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            moveRight();
        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            moveLeft();
        } else if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            moveUp();
        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            moveDown();
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
        batch.draw(frame, position.x, position.y, 32, 32);
    }

    private boolean canMoveRight() {
        int tileX = (int) (position.x + velocity.x) / tileMap.getTileWidth();
        int tileY = (int) position.y / tileMap.getTileHeight();
        return tileMap.isCollidable(tileX, tileY) == false;
    }

    private boolean canMoveLeft() {
        int tileX = (int) (position.x - velocity.x) / tileMap.getTileWidth();
        int tileY = (int) position.y / tileMap.getTileHeight();
        return tileMap.isCollidable(tileX, tileY) == false;
    }

    private boolean canMoveUp() {
        int tileX = (int) position.x / tileMap.getTileWidth();
        int tileY = (int) (position.y + velocity.y) / tileMap.getTileHeight();
        return tileMap.isCollidable(tileX, tileY) == false;
    }

    private boolean canMoveDown() {
        int tileX = (int) position.x / tileMap.getTileWidth();
        int tileY = (int) (position.y - velocity.y) / tileMap.getTileHeight();
        return tileMap.isCollidable(tileX, tileY) == false;
    }

    @Override
    public void dispose() {

        player.dispose();
    
    }
}