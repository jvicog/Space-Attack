package dadm.scaffold.space;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import dadm.scaffold.R;
import dadm.scaffold.engine.GameEngine;
import dadm.scaffold.engine.ScreenGameObject;
import dadm.scaffold.engine.Sprite;
import dadm.scaffold.input.InputController;
import dadm.scaffold.sound.GameEvent;

public class Sky extends Sprite {

    private int maxX;
    private int maxY;
    private double speedFactor;

    public Sky(GameEngine gameEngine, int sprite, float speed){
        super(gameEngine, sprite);
        speedFactor = speed * pixelFactor * 50d / 1000d; // We want to move at 100px per second on a 400px tall screen
        maxX = gameEngine.width - width;
        maxY = gameEngine.height - height;
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
        System.out.println(positionX + " - " + positionY);
    }

    private void updatePosition(long elapsedMillis, InputController inputController) {
        positionX -= speedFactor * inputController.horizontalFactor * elapsedMillis;
        if (positionX < -width/2) {
            positionX = -width/2;
        }
        if (positionX > width/2) {
            positionX = width/2;
        }
        positionY -= speedFactor * inputController.verticalFactor * elapsedMillis;
        if (positionY < -height/2) {
            positionY = -height/2;
        }
        if (positionY > height/2) {
            positionY = height/2;
        }
    }

    @Override
    public void onCollision(GameEngine gameEngine, ScreenGameObject otherObject) {
    }
}
