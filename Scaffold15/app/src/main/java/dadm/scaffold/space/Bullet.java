package dadm.scaffold.space;

import dadm.scaffold.R;
import dadm.scaffold.engine.GameEngine;
import dadm.scaffold.engine.ScreenGameObject;
import dadm.scaffold.engine.Sprite;
import dadm.scaffold.sound.GameEvent;

public class Bullet extends Sprite {

    private double speedFactor;

    private int direction = 0; //Bullet direction in degrees (0 = up, 90 = right...)

    private SpaceShipPlayer parent;

    public Bullet(GameEngine gameEngine){
        super(gameEngine, R.drawable.bullet);

        speedFactor = gameEngine.pixelFactor * -300d / 1000d;
    }

    public Bullet(GameEngine gameEngine, int sprite, int nDirection){
        super(gameEngine, sprite);
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


    public void init(SpaceShipPlayer parentPlayer, double initPositionX, double initPositionY) {
        positionX = initPositionX - width/2;
        positionY = initPositionY - height/2;
        parent = parentPlayer;
    }

    private void removeObject(GameEngine gameEngine) {
        gameEngine.removeGameObject(this);
    }

    @Override
    public void onCollision(GameEngine gameEngine, ScreenGameObject otherObject) {
        if (otherObject instanceof Asteroid) {
            // Remove both from the game (and return them to their pools)
            removeObject(gameEngine);
            Asteroid a = (Asteroid) otherObject;
            a.removeObject(gameEngine);
            parent.myPoints +=10;
            gameEngine.gameEngineUpdatePoints(parent.myPoints);
            gameEngine.onGameEvent(GameEvent.AsteroidHit);
            gameEngine.onGameEvent(GameEvent.GetPoints);
            // Add some score
        }
    }
}
