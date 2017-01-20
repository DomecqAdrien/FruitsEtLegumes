package kagura.project.com.a8.association;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import kagura.project.com.a8.MainMenu;
import kagura.project.com.a8.R;

public class AssociationMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_association_menu);
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
}
