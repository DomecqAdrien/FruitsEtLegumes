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
import android.widget.Button;
import android.widget.TextView;

import kagura.project.com.a8.association.AssociationMenu;
import kagura.project.com.a8.memory.MemoryMenu;

public class MainMenu extends AppCompatActivity {

    Button btn_game_memory;
    Intent intentGame;
    String anim;
    String name;
    StringBuilder strb;
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
        finish();
        Intent intentBack = new Intent(this, CharacterSelection.class);
        this.startActivity(intentBack);
    }

    public void startGame(View v) {
        switch(v.getId()){
            case R.id.button_game_memory:
                intentGame = new Intent(this, MemoryMenu.class);
                anim = "push_left_in";
                break;
            case R.id.button_game_association:
                intentGame = new Intent (this, AssociationMenu.class);
                anim = "push_left_out";
                break;
        }
        int animId = getResources().getIdentifier(anim, "animator", getPackageName());
        finish();
        this.startActivityForResult(intentGame, 0);
        overridePendingTransition(animId, R.animator.push_up_out);
    }
}
