package dadm.scaffold;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.Image;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import dadm.scaffold.counter.GameFragment;
import dadm.scaffold.counter.MainMenuFragment;
import dadm.scaffold.engine.GameEngine;
import dadm.scaffold.sound.SoundManager;

public class ScaffoldActivity extends AppCompatActivity {

    private static final String TAG_FRAGMENT = "content";

    private SoundManager soundManager;

    private GameEngine gameEngine;

    private ImageButton buttonSelectShip1, buttonSelectShip2;

    public static final String PREFS_NAME = "MyPrefsFile";

    public GameFragment gameFragment;

    public int p = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scaffold);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new MainMenuFragment(), TAG_FRAGMENT)
                    .commit();
        }
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        soundManager = new SoundManager(getApplicationContext());
    }

    public SoundManager getSoundManager() {
        return soundManager;
    }

    public void startGame() {
        // Navigate the the game fragment, which makes the start automatically
        gameFragment = new GameFragment();
        navigateToFragment(gameFragment);
        //navigateToFragment( new GameFragment());
    }

    public void spawnBoss(){
        gameFragment.spawnBoss();
    }

    private void navigateToFragment(BaseFragment dst) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, dst, TAG_FRAGMENT)
                .addToBackStack(null)
                .commit();
    }

    public void showResult(boolean isVictory){
        Intent intent = new Intent(this, ResultActivity.class);
        setPoints(p);
        setVictory(isVictory);
        startActivity(intent);
    }

    public boolean bossSpawned = false;

    public void updatePoints(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //showResult(p);
                TextView pointsText = (TextView) findViewById(R.id.points);
                pointsText.setText(Integer.toString(p));
                if(p >= 200 && !bossSpawned){
                    spawnBoss();
                    bossSpawned = true;
                }
            }
        });
    }

    public int h = 0;

    public void updateHealth(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView pointsText = (TextView) findViewById(R.id.health);
                pointsText.setText(Integer.toString(h));
            }
        });
    }

    @Override
    public void onBackPressed() {
        final BaseFragment fragment = (BaseFragment) getSupportFragmentManager().findFragmentByTag(TAG_FRAGMENT);
        if (fragment == null || !fragment.onBackPressed()) {
            super.onBackPressed();
        }
    }

    public void navigateBack() {
        // Do a push on the navigation history
        super.onBackPressed();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            View decorView = getWindow().getDecorView();
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LOW_PROFILE);
            }
            else {
                decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
            }
        }
    }

    public void selectShip1(View view){
        buttonSelectShip1 = (ImageButton) findViewById(R.id.buttonSelectShip1);
        buttonSelectShip2 = (ImageButton) findViewById(R.id.buttonSelectShip2);
        buttonSelectShip1.setBackgroundResource(R.drawable.blue_button_selected);
        buttonSelectShip2.setBackgroundResource(R.drawable.blue_button);
        setShip(1);
    }

    public void selectShip2(View view){
        buttonSelectShip1 = (ImageButton) findViewById(R.id.buttonSelectShip1);
        buttonSelectShip2 = (ImageButton) findViewById(R.id.buttonSelectShip2);
        buttonSelectShip1.setBackgroundResource(R.drawable.blue_button);
        buttonSelectShip2.setBackgroundResource(R.drawable.blue_button_selected);
        setShip(2);
    }

    public void setShip(int type){
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("shipType", type);
        editor.commit();
    }

    public void setPoints(int points){
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("points", points);
        editor.commit();
    }

    public void setVictory(boolean isVictory){
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("isVictory", isVictory);
        editor.commit();
    }

    public int getShip(){
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        int t = settings.getInt("shipType", 1);
        return t;
    }
}
