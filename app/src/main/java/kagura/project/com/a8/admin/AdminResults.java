package kagura.project.com.a8.admin;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import kagura.project.com.a8.R;
import kagura.project.com.a8.adapters.ResultAdapter;
import kagura.project.com.a8.database.ResultDAO;
import kagura.project.com.a8.objects.Result;

public class AdminResults extends AppCompatActivity {

    private String type;
    ResultDAO resultDAO;
    ListView listResults;
    List<Result> results;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_admin_results);

        type = getIntent().getStringExtra("type");

        listResults = (ListView) findViewById(R.id.listResults);

        Typeface fontFamily = Typeface.createFromAsset(getAssets(), "fonts/fontawesome.ttf");
        TextView sampleText = (TextView) findViewById(R.id.trashcan);
        sampleText.setTypeface(fontFamily);
        sampleText.setText("\uf1f8");


        resultDAO = new ResultDAO(this);
        results = resultDAO.selectAll(type);


        ArrayAdapter<Result> resAdapter = new ResultAdapter(this, R.layout.row, results);
        listResults.setAdapter(resAdapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        switch (type){
            case "Association":
                overridePendingTransition(R.anim.right_start, R.anim.right_end);
                break;
            case "Memory":
                overridePendingTransition(R.anim.left_start, R.anim.left_end);
                break;
        }

    }

    public void deleteAll(View view) {
        new AlertDialog.Builder(this).setTitle("Supprimer")
                .setMessage("Êtes vous sûrs de vouloir supprimer tous les résultats?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        resultDAO.deleteAll();
                        listResults.setVisibility(View.INVISIBLE);
                        Toast.makeText(AdminResults.this, "Les résultats ont été supprimés", Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton("Non", null).show();
    }
}
