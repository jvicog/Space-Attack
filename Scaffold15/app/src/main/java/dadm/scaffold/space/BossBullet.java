package dadm.scaffold.space;

import dadm.scaffold.R;
import dadm.scaffold.engine.GameEngine;
import dadm.scaffold.engine.ScreenGameObject;
import dadm.scaffold.engine.Sprite;
import dadm.scaffold.sound.GameEvent;

public class BossBullet extends Sprite {

    private double speedFactor;

    private float direction = 180; //Bullet direction in degrees (0 = up, 90 = right...)

    private Boss parent;

    public BossBullet(GameEngine gameEngine, float nDirection){
        super(gameEngine, R.drawable.bullet3);
        this.direction = nDirection;
        speedFactor = gameEngine.pixelFactor * -300d / 1000d;
    }

    @Override
    public void startGame() {}

    @Override
    public void onUpdate(long elapsedMillis, GameEngine gameEngine) {
        positionY += speedFactor * Math.cos(Math.toDegrees(direction)) * elapsedMillis;
        positionX += speedFactor * Math.sin(Math.toDegrees(direction)) * elapsedMillis;
        if (positionY < -height ||positionY > gameEngine.height+this.height || positionX < width || positionX > gameEngine.width+this.width) {
            gameEngine.removeGameObject(this);
        }
    }


    public void init(Boss parentBoss, double initPositionX, double initPositionY) {
        positionX = initPositionX - width/2;
        positionY = initPositionY - height/2;
        parent = parentBoss;
    }

    private void removeObject(GameEngine gameEngine) {
        gameEngine.removeGameObject(this);
    }

    @Override
    public void onCollision(GameEngine gameEngine, ScreenGameObject otherObject) {
    }
}
