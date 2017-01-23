package kagura.project.com.a8.association;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import kagura.project.com.a8.MainMenu;
import kagura.project.com.a8.R;
import kagura.project.com.a8.memory.MemoryGame;

import static android.R.attr.button;

public class AssociationMenu extends AppCompatActivity {

    Intent intentMemory;
    ImageView avatar;
    String avatarName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        setContentView(R.layout.activity_association_menu);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        avatarName = preferences.getString(getString(R.string.avatar), null);

        avatar = (ImageView) findViewById(R.id.imageAvatar);

        switch(avatarName) {
            case "verMince":
                avatar.setBackgroundDrawable(getResources().getDrawable(R.drawable.ver_mince_hd_reverse));
                break;
            case "verGros":
                avatar.setBackgroundDrawable(getResources().getDrawable(R.drawable.ver_gros_hd));
                break;
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intentBack = new Intent(this, MainMenu.class);
        finish();
        startActivity(intentBack);
    }

    public void startGame(View view) {
    }
    public void startSolution(View view) {
        intentMemory = new Intent(this, Solution.class);
        intentMemory.putExtra("level", button);
        this.startActivityForResult(intentMemory, 0);
    }
}
