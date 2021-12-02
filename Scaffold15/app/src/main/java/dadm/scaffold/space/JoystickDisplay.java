package dadm.scaffold.space;

import android.graphics.Canvas;
import android.graphics.Paint;

import dadm.scaffold.engine.GameEngine;
import dadm.scaffold.engine.ScreenGameObject;

public class JoystickDisplay extends ScreenGameObject {

    private static final Paint CIRCLE_1 = new Paint();
    private static final Paint CIRCLE_2 = new Paint();

    static {
        CIRCLE_1.setColor(0xFF777777);
        CIRCLE_2.setColor(0x77777777);
    }

    private final GameEngine engine;

    public JoystickDisplay(GameEngine engine) {
        this.engine = engine;
    }

    @Override
    public void startGame() {
        positionX = 300;
        positionY = engine.height - 300;
    }

    @Override
    public void onUpdate(long elapsedMillis, GameEngine gameEngine) {

    }

    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawCircle((float) positionX, (float) positionY, 60, CIRCLE_1);

        float x = (float) engine.theInputController.horizontalFactor * 150;
        float y = (float) engine.theInputController.verticalFactor * 150;

        canvas.drawCircle((float) positionX + x, (float) positionY + y, 150, CIRCLE_2);
    }


    @Override
    public void onCollision(GameEngine gameEngine, ScreenGameObject otherObject) {
    }
}
