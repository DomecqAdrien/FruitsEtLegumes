package kagura.project.com.a8.association;

import android.animation.AnimatorSet;
import android.content.pm.ActivityInfo;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Chronometer;
import android.widget.GridView;

import java.util.Calendar;

import kagura.project.com.a8.Association;
import kagura.project.com.a8.R;
import kagura.project.com.a8.memory.MemoryGame;
import kagura.project.com.a8.objects.Card;

public class AssociationGame extends AppCompatActivity {

    private static int level;

    private boolean isClickable = true;

    private int finish;
    private int tries = 0;

    private MemoryGame.UpdateCardsHandler handler;
    private Card firstCard, secondCard;
    private static final Object lock = new Object();

    GridView gridview;
    Fragment fragmentResult;

    Boolean isTimerStarted = false;
    Chronometer timer;
    Calendar calendar;

    FragmentManager fragmentManager;
    Association association;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        setContentView(R.layout.activity_association_game);
    }
}
