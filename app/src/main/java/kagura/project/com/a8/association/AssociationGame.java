package kagura.project.com.a8.association;

import android.animation.AnimatorSet;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Chronometer;
import android.widget.GridView;

import com.plattysoft.leonids.ParticleSystem;

import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import kagura.project.com.a8.Association;
import kagura.project.com.a8.AssociationPicturale;
import kagura.project.com.a8.AssociationSemantique;
import kagura.project.com.a8.R;
import kagura.project.com.a8.adapters.ImageAdapter;
import kagura.project.com.a8.database.ResultDAO;
import kagura.project.com.a8.memory.MemoryGame;
import kagura.project.com.a8.memory.MemoryResultFragment;
import kagura.project.com.a8.objects.Card;
import kagura.project.com.a8.objects.Result;

import static kagura.project.com.a8.R.id.gridviewBack;
import static kagura.project.com.a8.R.id.gridviewFront;

public class AssociationGame extends AppCompatActivity {

    private static int level;

    private boolean isClickable = true;

    private int finish;
    private int tries = 0;

    private UpdateCardsHandler handler;
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

        gridview = (GridView) findViewById(R.id.gridview);

        level = getIntent().getIntExtra("level", 0);
        Log.i("level", Integer.toString(level));
        
        newGame();

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                if(!isTimerStarted){
                    timer = new Chronometer(getApplicationContext());
                    timer.start();
                    isTimerStarted = true;
                }

                if(isClickable){
                    synchronized (lock) {

                        if(firstCard != null){
                            if(firstCard.position == position){
                                return; //the user pressed the same card
                            }
                        }

                        Log.i("position", Integer.toString(position));
                        selectCard(v,position);
                    }
                }

            }
        });
    }

    /*@Override
    public void onBackPressed() {
        new AlertDialog.Builder(this).setTitle("Quitter")
                .setMessage("Êtes vous sûrs de vouloir quitter ce jeu ?")
                .setIcon(android.R.drawable.ic_menu_help)
                .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        overridePendingTransition(R.anim.right_start, R.anim.right_end);
                    }
                })
                .setNegativeButton("Non", null).show();

    }*/

    private void selectCard(View v, int position) {

        if(firstCard==null){
            firstCard = new Card(v, position);
        }
        else{

            isClickable = false;

            secondCard = new Card(v, position);


            TimerTask tt = new TimerTask() {

                @Override
                public void run() {
                    try{
                        synchronized (lock) {
                            handler.sendEmptyMessage(0);
                        }
                    }
                    catch (Exception e) {
                        Log.e("E1", e.getMessage());
                    }
                }
            };

            Timer t = new Timer(false);
            t.schedule(tt, 1300);

        }


    }

    private void newGame() {

        association = new AssociationSemantique(this);

        // On initialise un tableau d'entiers contenant en position 0 le nombre de colonnes que fera la gridview, et en position 1 le nombre de cartes dans le jeu
        int levelParams[] = association.setLevelParams(level);
        gridview.setNumColumns(levelParams[0]);
        finish = levelParams[1] / 2;

        List<Integer[]> idDrawablesFrontAndBack = association.loadCards();

        gridview.setAdapter(new ImageAdapter(this, idDrawablesFrontAndBack.get(0)));
    }

    @SuppressLint("HandlerLeak")
    class UpdateCardsHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            synchronized (lock) {
                checkCards();
            }
        }
        void checkCards(){
            tries++;

            if(association.getImagePositions().get(firstCard.position).equals(association.getImagePositions().get(secondCard.position))){
                Animation animFadeOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out);
                new ParticleSystem(AssociationGame.this, 1000, getResources().getIdentifier("star_pink", "drawable", getPackageName()), 1000)
                        .setSpeedRange(0.2f, 0.5f)
                        .oneShot(firstCard.view, 100);
                firstCard.view.startAnimation(animFadeOut);
                firstCard.view.setVisibility(View.INVISIBLE);
                new ParticleSystem(AssociationGame.this, 1000, getResources().getIdentifier("star_pink", "drawable", getPackageName()), 1000)
                        .setSpeedRange(0.2f, 0.5f)
                        .oneShot(secondCard.view, 100);
                secondCard.view.startAnimation(animFadeOut);
                secondCard.view.setVisibility(View.INVISIBLE);

                finish--;
                isClickable = true;
            }else{

                final Handler handlerFlip = new Handler();
                handlerFlip.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isClickable = true;
                    }
                }, 1000);
            }

            firstCard = null;
            secondCard = null;

            if(finish == 0){
                timer.stop();
                result();
            }
        }
    }

    private void result(){

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        calendar = Calendar.getInstance();
        String monthString;
        int calendarMonth = calendar.get(Calendar.MONTH) + 1;
        if(calendarMonth < 10){
            monthString = "0" + Integer.toString(calendarMonth);
        }else{
            monthString = Integer.toString(calendarMonth);
        }
        String date = calendar.get(Calendar.DAY_OF_MONTH) + "/" + monthString + "/" + calendar.get(Calendar.YEAR);
        long time = (SystemClock.elapsedRealtime() - timer.getBase()) / 1000;
        Log.i("nb coups", Integer.toString(tries));
        Log.i("timer : ", Long.toString(time));

        Result result = new Result();
        result.setName(preferences.getString(getString(R.string.name), null));
        result.setGame("Memory");
        result.setLevel(level);
        result.setTries(tries);
        result.setTime(time);
        result.setDate(date);

        ResultDAO resultDAO = new ResultDAO(this);
        resultDAO.add(result);


        Bundle bundle = new Bundle();
        bundle.putInt("level", level);
        fragmentResult = new MemoryResultFragment();
        fragmentResult.setArguments(bundle);
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.right_end, R.anim.right_start)
                .replace(R.id.fragment_container, fragmentResult).commit();


    }

    public void replayLevel(View view) {

        fragmentManager.beginTransaction().remove(fragmentResult).commit();
        newGame();

    }

    public void nextLevel(View view) {

        fragmentManager.beginTransaction().remove(fragmentResult).commit();
        isTimerStarted = false;
        tries = 0;
        level++;
        newGame();

    }

    public void goHome(View view) {

        Intent intentHomeMenu = new Intent();
        setResult(66, intentHomeMenu);
        finish();
    }

    public void goLevelMenu(View view) {
        finish();
    }
    
    
}
