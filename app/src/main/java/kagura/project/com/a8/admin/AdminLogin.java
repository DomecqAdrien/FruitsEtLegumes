package kagura.project.com.a8.admin;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import kagura.project.com.a8.R;

public class AdminLogin extends AppCompatActivity {

    EditText passAdmin;
    String passAdminCheck;
    Intent intentAdmin;

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

        setContentView(R.layout.activity_admin_login);

        passAdmin = (EditText) findViewById(R.id.passAdmin);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.down_start, R.anim.down_end);
    }

    public void checkPassword(View view) {

        passAdminCheck = passAdmin.getText().toString();
        if(passAdminCheck.equals(getString(R.string.passAdminVerif))){
            intentAdmin = new Intent(this, AdminSession.class);
            this.startActivityForResult(intentAdmin, 0);
            overridePendingTransition(R.anim.up_start, R.anim.up_end);


        }else if(passAdminCheck.equals("")){
            Toast.makeText(this, "Veuillez entrer un mot de passe", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this, "Mot de passe incorrect", Toast.LENGTH_LONG).show();
        }
    }
}