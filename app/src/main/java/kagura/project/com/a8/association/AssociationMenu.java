package kagura.project.com.a8.association;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import kagura.project.com.a8.R;

import static android.R.attr.button;

public class AssociationMenu extends AppCompatActivity {

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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 66){
            onBackPressed();

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.right_start, R.anim.right_end);
    }

    public void startGame(View v) {
        int button = 1;
        switch (v.getId()){
            case R.id.level_1tv:
                button = 1;
                break;
            case R.id.level_2tv:
                button = 2;
                break;
            case R.id.level_3tv:
                button = 3;
                break;
        }
        Intent intentGame = new Intent(this, AssociationGame.class);
        intentGame.putExtra("level", button);
        this.startActivityForResult(intentGame, 0);
        overridePendingTransition(R.anim.left_start, R.anim.left_end);
    }


    public void startEncyclopedie(View view) {
        Intent intentEncyclopedie = new Intent(this, Encyclopedie.class);
        intentEncyclopedie.putExtra("level", button);
        this.startActivityForResult(intentEncyclopedie, 0);
    }
    public void back(View v) {
        super.onBackPressed();
        overridePendingTransition(R.anim.right_start, R.anim.right_end);
    }
}
