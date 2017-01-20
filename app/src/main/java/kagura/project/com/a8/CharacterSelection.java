package kagura.project.com.a8;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import kagura.project.com.a8.admin.AdminMenu;

public class CharacterSelection extends AppCompatActivity {

    EditText name;
    String nameValid;
    String avatar;
    ImageButton fraise;
    ImageButton pomme;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        nameValid = preferences.getString(getString(R.string.name), null);
        avatar = preferences.getString(getString(R.string.avatar), null);

        setContentView(R.layout.activity_character_selection);

        name = (EditText) this.findViewById(R.id.name);
        fraise = (ImageButton) findViewById(R.id.buttonFraise);
        pomme = (ImageButton) findViewById(R.id.buttonPomme);

        if(nameValid != null){
            name.append(nameValid);
        }
        if(avatar != null){
            switch (avatar){
                case "fraise":
                    fraise.setBackgroundColor(getResources().getColor(R.color.black));
                    break;
                case "pomme":
                    pomme.setBackgroundColor(getResources().getColor(R.color.black));
                    break;
            }
        }


    }

    @Override
    public void onBackPressed() {
                new AlertDialog.Builder(this).setTitle("Quitter")
                .setMessage("Êtes vous sûrs de vouloir quitter l'application ?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton("Non", null).show();
    }

    public void CharacterOnClick(View view) {
        view.setBackgroundColor(getResources().getColor(R.color.black));
        switch (view.getId()){
            case R.id.buttonFraise:
                avatar = "fraise";
                pomme.setBackgroundColor(getResources().getColor(R.color.white));
                break;
            case R.id.buttonPomme:
                avatar  = "pomme";
                fraise.setBackgroundColor(getResources().getColor(R.color.white));
                break;
        }
    }

    public void start(View v) {
        nameValid = name.getText().toString();
        if(nameValid.equals("")) {
            Toast.makeText(this, "Tu n'as pas rentré de prénom", Toast.LENGTH_LONG).show();
        }else if (avatar.equals("")){
            Toast.makeText(this, "Tu n'as pas choisi d\'avatar", Toast.LENGTH_LONG).show();
        }else{
            nameValid = nameValid.substring(0,1).toUpperCase() + nameValid.substring(1);
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(getString(R.string.name), nameValid);
            editor.putString(getString(R.string.avatar), avatar);
            editor.apply();


            intent = new Intent(this, MainMenu.class);
            finish();
            this.startActivityForResult(intent, 0);
        }

    }

    public void adminSession(View view) {
        intent = new Intent(this, AdminMenu.class);
        finish();
        this.startActivityForResult(intent, 0);
    }
}
