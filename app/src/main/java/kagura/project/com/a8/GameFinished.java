package kagura.project.com.a8;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import kagura.project.com.a8.association.AssociationMenu;
import kagura.project.com.a8.memory.MemoryMenu;

public class GameFinished extends AppCompatActivity {

    public String typeGameFinished;

    Intent intentNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_finished);

        typeGameFinished = getIntent().getStringExtra("typeGame");
    }

    public void onBackPressed(){
        switch (typeGameFinished){
            case "memory":
                intentNext = new Intent(this, MemoryMenu.class);
                break;
            case "association":
                intentNext = new Intent(this, AssociationMenu.class);
                break;
        }
        startActivityForResult(intentNext, 0);

    }
}
