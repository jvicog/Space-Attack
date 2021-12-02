package dadm.scaffold;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import dadm.scaffold.counter.GameFragment;
import dadm.scaffold.counter.MainMenuFragment;
import dadm.scaffold.engine.GameEngine;
import dadm.scaffold.sound.SoundManager;

public class ResultActivity extends AppCompatActivity {

    TextView show, victory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        show = (TextView) findViewById(R.id.resultText);
        victory = (TextView) findViewById(R.id.victoryText);
        show.setText(Integer.toString(getPoints()));
        if(isVictory()){
            victory.setText("¡VICTORIA!");
            victory.setTextColor(Color.GREEN);
        }else{
            victory.setText("HAS PERDIDO");
            victory.setTextColor(Color.RED);
        }
    }

        public int getPoints(){
        SharedPreferences settings = getSharedPreferences(ScaffoldActivity.PREFS_NAME, 0);
        int t = settings.getInt("points", 1);
        return t;
    }

        public boolean isVictory(){
            SharedPreferences settings = getSharedPreferences(ScaffoldActivity.PREFS_NAME, 0);
            boolean b = settings.getBoolean("isVictory", false);
            return b;
        }

    public void mainMenu(View view){
        Intent intent = new Intent(this, ScaffoldActivity.class);
        startActivity(intent);
    }
}
