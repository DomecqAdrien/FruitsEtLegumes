package kagura.project.com.a8.admin;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import kagura.project.com.a8.R;

public class AdminSession extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        setContentView(R.layout.activity_admin_session);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.down_start, R.anim.down_end);
    }

    public void results(View view) {
        Intent intentResults = new Intent(this, AdminResults.class);

        switch (view.getId()){
            case R.id.buttonResultAssociation:
                intentResults.putExtra("type", "Association");
                this.startActivity(intentResults);
                overridePendingTransition(R.anim.left_start, R.anim.left_end);
                break;
            case R.id.buttonResultMemory:
                intentResults.putExtra("type", "Memory");
                this.startActivity(intentResults);
                overridePendingTransition(R.anim.right_start, R.anim.right_end);
                break;
        }
    }
}
