package dadm.scaffold.space;

import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewTreeObserver;

import dadm.scaffold.BaseFragment;
import dadm.scaffold.R;
import dadm.scaffold.ScaffoldActivity;
import dadm.scaffold.counter.GameFragment;
import dadm.scaffold.engine.GameEngine;
import dadm.scaffold.engine.ScreenGameObject;
import dadm.scaffold.engine.Sprite;
import dadm.scaffold.input.InputController;
import dadm.scaffold.sound.GameEvent;
import dalvik.system.DelegateLastClassLoader;

public class SpaceShipPlayer extends Sprite {

    private long timeBetweenBullets = 1000;
    private long timeBetweenBullets2 = 3000;
    private long timeSinceLastFire;
    private long timeSinceLastFire2;

    private int maxX;
    private int maxY;
    private double speedFactor;
    private int hp = 3;
    public int myPoints = 0;

    private int type; //1 = X ship, 2 = T ship

    public SpaceShipPlayer(GameEngine gameEngine, int sprite, int type){
        super(gameEngine, sprite);
        speedFactor = pixelFactor * 100d / 1000d; // We want to move at 100px per second on a 400px tall screen
        maxX = gameEngine.width - width;
        maxY = gameEngine.height - height;
        this.type = type;
        if(type == 2){
            speedFactor = speedFactor*2;
        }else if (type == 1){
            timeBetweenBullets = 700;
            timeBetweenBullets2 = 2000;
            hp = 4;
            gameEngine.gameEngineUpdateHealth(hp);
        }
        gameEngine.gameEngineUpdateHealth(hp);
        gameEngine.gameEngineUpdatePoints(myPoints);
    }

    @Override
    public void startGame() {
        positionX = maxX / 2;
        positionY = maxY / 2;
    }

    @Override
    public void onUpdate(long elapsedMillis, GameEngine gameEngine) {
        // Get the info from the inputController
        updatePosition(elapsedMillis, gameEngine.theInputController);
        autoFiring(elapsedMillis, gameEngine);
        checkFiring2(elapsedMillis, gameEngine);
    }

    private void updatePosition(long elapsedMillis, InputController inputController) {
        positionX += speedFactor * inputController.horizontalFactor * elapsedMillis;
        if (positionX < 0) {
            positionX = 0;
        }
        if (positionX > maxX) {
            positionX = maxX;
        }
        positionY += speedFactor * inputController.verticalFactor * elapsedMillis;
        if (positionY < 0) {
            positionY = 0;
        }
        if (positionY > maxY) {
            positionY = maxY;
        }
    }

    private void autoFiring(long elapsedMillis, GameEngine gameEngine) {
        if (timeSinceLastFire > timeBetweenBullets) {
            //Bullet bullet = getBullet();
            Bullet bullet = new Bullet(gameEngine);
            bullet.init(this, positionX + width/2, positionY);
            gameEngine.addGameObject(bullet);
            timeSinceLastFire = 0;
            gameEngine.onGameEvent(GameEvent.LaserFired);
        }
        else {
            timeSinceLastFire += elapsedMillis;
        }
    }

    private void checkFiring2(long elapsedMillis, GameEngine gameEngine) {
        if (gameEngine.theInputController.isFiring && timeSinceLastFire2 > timeBetweenBullets2) {
            //Bullet bullet = getBullet();
            Bullet bullet = new Bullet(gameEngine, R.drawable.bullet2, 0);
            bullet.init(this, positionX + width/2, positionY);
            gameEngine.addGameObject(bullet);

            Bullet bullet4 = new Bullet(gameEngine, R.drawable.bullet2, 270);
            bullet4.init(this, positionX + width/2, positionY);
            gameEngine.addGameObject(bullet4);

            if(type == 1){
                Bullet bullet2 = new Bullet(gameEngine, R.drawable.bullet2, 90);
                bullet2.init(this, positionX + width/2, positionY);
                gameEngine.addGameObject(bullet2);

                Bullet bullet3 = new Bullet(gameEngine, R.drawable.bullet2, 180);
                bullet3.init(this, positionX + width/2, positionY);
                gameEngine.addGameObject(bullet3);
            }

            timeSinceLastFire2 = 0;
            gameEngine.onGameEvent(GameEvent.LaserFired);
        }
        else {
            timeSinceLastFire2 += elapsedMillis;
        }
    }


    @Override
    public void onCollision(GameEngine gameEngine, ScreenGameObject otherObject) {
        Resources r = gameEngine.getContext().getResources();
        if (otherObject instanceof Asteroid) {
            gameEngine.onGameEvent(GameEvent.SpaceshipHit);
            hp--;
            gameEngine.gameEngineUpdateHealth(hp);
            gameEngine.removeGameObject(otherObject);
            if(hp==0){
                gameEngine.stopGame();
                gameEngine.gameEngineShowResult(false);
            }
        }else if (otherObject instanceof Powerup) {
            gameEngine.onGameEvent(GameEvent.Reload);
            if(timeBetweenBullets >= 199){
                timeBetweenBullets -= 100;
            }
            gameEngine.gameEngineUpdateHealth(hp);
            gameEngine.removeGameObject(otherObject);
        }else if (otherObject instanceof Heart) {
            gameEngine.onGameEvent(GameEvent.Reload);
            hp++;
            gameEngine.gameEngineUpdateHealth(hp);
            gameEngine.removeGameObject(otherObject);
        }else if(otherObject instanceof  BossBullet){
            gameEngine.onGameEvent(GameEvent.SpaceshipHit);
            hp--;
            gameEngine.gameEngineUpdateHealth(hp);
            gameEngine.removeGameObject(otherObject);
            if(hp==0){
                gameEngine.stopGame();
                gameEngine.gameEngineShowResult(false);
            }
        }
    }
}
