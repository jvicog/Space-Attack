package dadm.scaffold.space;

import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import dadm.scaffold.R;
import dadm.scaffold.engine.GameEngine;
import dadm.scaffold.engine.ScreenGameObject;
import dadm.scaffold.engine.Sprite;
import dadm.scaffold.input.InputController;
import dadm.scaffold.sound.GameEvent;

public class Boss extends Sprite {

    private long timeBetweenBullets = 500;
    private long timeSinceLastFire;

    private int maxX;
    private int maxY;
    private double speedFactor;
    private double speedFactor2;
    private int hp = 8;
    private SpaceShipPlayer player;

    public Boss(GameEngine gameEngine, SpaceShipPlayer player){
        super(gameEngine, R.drawable.boss0);
        speedFactor = pixelFactor * 200d / 1000d; // We want to move at 100px per second on a 400px tall screen
        maxX = gameEngine.width - width;
        maxY = gameEngine.height - height;
        gameEngine.gameEngineUpdateHealth(hp);
        this.player = player;
        rotation = Math.toRadians(180);
        positionX = maxX / 2;
        positionY = -height;
        speedFactor2 = speedFactor;
    }

    @Override
    public void startGame() {
    }

    @Override
    public void onUpdate(long elapsedMillis, GameEngine gameEngine) {
        // Get the info from the inputController
        updatePosition(elapsedMillis, gameEngine.theInputController);
        autoFiring(elapsedMillis, gameEngine);
    }

    private void updatePosition(long elapsedMillis, InputController inputController) {
        positionX += speedFactor * elapsedMillis;
        if (positionX < 0) {
            positionX = 0;
            speedFactor *= -1;
        }
        if (positionX > maxX) {
            positionX = maxX;
            speedFactor *= -1;
        }

        positionY += speedFactor2 * elapsedMillis;
        if(positionY >= 0){
            positionY =0;
        }
    }

    private void autoFiring(long elapsedMillis, GameEngine gameEngine) {
        if (timeSinceLastFire > timeBetweenBullets) {
            float dir = 180;

            dir = (float) Math.atan((player.positionX - this.positionX)/(player.positionY - this.positionY)) + +180;
            dir = (float) Math.toRadians(dir);
            BossBullet bullet = new BossBullet(gameEngine, dir);
            bullet.init(this, positionX + width/2, positionY);
            gameEngine.addGameObject(bullet);
            timeSinceLastFire = 0;
            gameEngine.onGameEvent(GameEvent.LaserFired);
        }
        else {
            timeSinceLastFire += elapsedMillis;
        }
    }

    @Override
    public void onCollision(GameEngine gameEngine, ScreenGameObject otherObject) {
        Resources r = gameEngine.getContext().getResources();
        if (otherObject instanceof Bullet) {
            gameEngine.onGameEvent(GameEvent.AsteroidHit);
            hp--;
            switch(hp){
                case 7:
                case 6:
                    Drawable spriteDrawable = r.getDrawable(R.drawable.boss1);
                    this.bitmap = ((BitmapDrawable) spriteDrawable).getBitmap();
                    break;
                case 5:
                case 4:
                    Drawable spriteDrawable2 = r.getDrawable(R.drawable.boss2);
                    this.bitmap = ((BitmapDrawable) spriteDrawable2).getBitmap();
                    break;
                case 3:
                case 2:
                    Drawable spriteDrawable3 = r.getDrawable(R.drawable.boss3);
                    this.bitmap = ((BitmapDrawable) spriteDrawable3).getBitmap();
                    break;
                case 1:
                    Drawable spriteDrawable4 = r.getDrawable(R.drawable.boss4);
                    this.bitmap = ((BitmapDrawable) spriteDrawable4).getBitmap();
                    break;
            }
            gameEngine.removeGameObject(otherObject);
            if (hp <= 0) {
                gameEngine.gameEngineShowResult(true);
                gameEngine.removeGameObject(this);
            }
        }
    }
}
