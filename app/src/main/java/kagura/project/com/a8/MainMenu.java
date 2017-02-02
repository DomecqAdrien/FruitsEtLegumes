package kagura.project.com.a8;


import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import kagura.project.com.a8.association.semantique.SemantiqueMenu;
import kagura.project.com.a8.association.memorielle.MemoryMenu;

public class MainMenu extends AppCompatActivity {

    Intent intentGame;
    String name;
    TextView textHello;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        name = preferences.getString(getString(R.string.name), null);


        setContentView(R.layout.activity_main);

        textHello = (TextView) findViewById(R.id.textHello);

        textHello.append(", " + name + " !");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.up_start, R.anim.up_end);
    }

    public void startGame(View v) {
        switch(v.getId()){
            case R.id.spaceMemory:
                intentGame = new Intent(this, MemoryMenu.class);
                this.startActivityForResult(intentGame, 0);
                overridePendingTransition(R.anim.right_start, R.anim.right_end);
                break;
            case R.id.spaceAssociation:
                intentGame = new Intent (this, SemantiqueMenu.class);
                this.startActivityForResult(intentGame, 0);
                overridePendingTransition(R.anim.left_start, R.anim.left_end);
                break;
        }
    }
}
