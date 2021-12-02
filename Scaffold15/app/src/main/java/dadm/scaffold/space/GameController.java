package dadm.scaffold.space;

import android.graphics.Canvas;

import java.util.ArrayList;
import java.util.List;

import dadm.scaffold.engine.GameEngine;
import dadm.scaffold.engine.GameObject;

public class GameController extends GameObject {

    private static final int TIME_BETWEEN_ENEMIES = 500;
    private long currentMillis;
    private List<Asteroid> asteroidPool = new ArrayList<Asteroid>();
    private int enemiesSpawned;

    private static final int TIME_BETWEEN_POWERUPS = 10000;
    private List<Powerup> powerupPool = new ArrayList<Powerup>();
    private int powerupsSpawned;

    private static final int TIME_BETWEEN_HEARTS = 10000;
    private List<Heart> heartPool = new ArrayList<Heart>();
    private int heartsSpawned;

    public GameController(GameEngine gameEngine) {
        // We initialize the pool of items now
        for (int i=0; i<10; i++) {
            asteroidPool.add(new Asteroid(this, gameEngine));
        }
        for (int i=0; i<10; i++) {
            powerupPool.add(new Powerup(this, gameEngine));
        }
        for (int i=0; i<10; i++) {
            heartPool.add(new Heart(this, gameEngine));
        }
    }

    @Override
    public void startGame() {
        currentMillis = 0;
        enemiesSpawned = 0;
        powerupsSpawned = 0;
        heartsSpawned = 0;
    }

    @Override
    public void onUpdate(long elapsedMillis, GameEngine gameEngine) {
        currentMillis += elapsedMillis;

        long waveTimestamp = enemiesSpawned*TIME_BETWEEN_ENEMIES;
        long waveTimestamp2 = powerupsSpawned*TIME_BETWEEN_POWERUPS;
        long waveTimestamp3 = 5000 + heartsSpawned*TIME_BETWEEN_HEARTS;
        if (currentMillis > waveTimestamp) {
            // Spawn a new enemy
            Asteroid a = asteroidPool.remove(0);
            a.init(gameEngine);
            gameEngine.addGameObject(a);
            enemiesSpawned++;
            return;
        }
        if (currentMillis > waveTimestamp2) {
            // Spawn a new powerup
            Powerup p = powerupPool.remove(0);
            p.init(gameEngine);
            gameEngine.addGameObject(p);
            powerupsSpawned++;
            return;
        }
        if (currentMillis > waveTimestamp3) {
            // Spawn a new heart
            Heart h = heartPool.remove(0);
            h.init(gameEngine);
            gameEngine.addGameObject(h);
            heartsSpawned++;
            return;
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        // This game object does not draw anything
    }

    public void returnToPool(Asteroid asteroid) {
        asteroidPool.add(asteroid);
    }

    public void returnToPoolPowerup(Powerup powerup) {
        powerupPool.add(powerup);
    }

    public void returnToPoolHeart(Heart heart) {
        heartPool.add(heart);
    }
}
