package dadm.scaffold.space;

import dadm.scaffold.R;
import dadm.scaffold.engine.GameEngine;
import dadm.scaffold.engine.ScreenGameObject;
import dadm.scaffold.engine.Sprite;

public class Powerup extends Sprite {

    private final GameController gameController;

    private double speed;
    private double speedX;
    private double speedY;
    private double rotationSpeed;

    public Powerup(GameController gameController, GameEngine gameEngine) {
        super(gameEngine, R.drawable.ammo);
        this.speed = 100d * pixelFactor/1000d;
        this.gameController = gameController;
    }

    public void init(GameEngine gameEngine) {
        double angle = gameEngine.random.nextDouble()*Math.toRadians(140)-Math.toRadians(20);
        speedX = speed * Math.sin(angle);
        speedY = speed * Math.cos(angle);
        positionY = gameEngine.random.nextInt(gameEngine.height/2)-gameEngine.height/4;
        positionX = -width;
        rotationSpeed = angle*(180d / Math.PI)/250d; // They rotate 4 times their ange in a second.
        rotation = gameEngine.random.nextInt(360);
    }

    @Override
    public void startGame() {
    }

    public void removeObject(GameEngine gameEngine) {
        // Return to the pool
        gameEngine.removeGameObject(this);
        gameController.returnToPoolPowerup(this);
    }

    @Override
    public void onUpdate(long elapsedMillis, GameEngine gameEngine) {
        positionX += speedX * elapsedMillis;
        positionY += speedY * elapsedMillis;
        rotation += rotationSpeed * elapsedMillis;
        if (rotation > 360) {
            rotation = 0;
        }
        else if (rotation < 0) {
            rotation = 360;
        }
        // Check of the sprite goes out of the screen and return it to the pool if so
        if (positionY > gameEngine.height) {
            // Return to the pool
            gameEngine.removeGameObject(this);
            gameController.returnToPoolPowerup(this);
        }
    }

    @Override
    public void onCollision(GameEngine gameEngine, ScreenGameObject otherObject) {

    }
}
