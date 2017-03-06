package kagura.project.com.a8.association.semantique;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
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
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.plattysoft.leonids.ParticleSystem;

import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


import kagura.project.com.a8.R;
import kagura.project.com.a8.adapters.ImageAdapter;
import kagura.project.com.a8.association.Association;
import kagura.project.com.a8.database.ResultDAO;
import kagura.project.com.a8.association.ResultFragment;
import kagura.project.com.a8.collections.Card;
import kagura.project.com.a8.collections.Result;

public class SemantiqueGame extends AppCompatActivity {

    private static int level;

    private boolean isClickable = true;

    private int finish;
    private int tries = 0;

    private UpdateCardsHandler handler;
    private Card firstCard, secondCard;
    private static final Object lock = new Object();

    GridView gridview, gridviewBackground;
    RelativeLayout relativeLayout;
    Fragment fragmentResult, fragmentChoice;

    Boolean isTimerStarted = false;
    Chronometer timer;
    Calendar calendar;

    FragmentManager fragmentManager;
    Association association;
    ImageView buttonBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        handler = new UpdateCardsHandler();

        setContentView(R.layout.activity_association_game);

        gridview = (GridView) findViewById(R.id.gridview);
        gridviewBackground = (GridView) findViewById(R.id.gridviewBackground);

        relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);

        buttonBack = (ImageView) findViewById(R.id.buttonBack);

        level = getIntent().getIntExtra("level", 0);
        Log.i("level", Integer.toString(level));

        fragmentChoice = new SemantiqueChoiceFragment();
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragmentChoice).commit();

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                if (!isTimerStarted) {
                    timer = new Chronometer(getApplicationContext());
                    timer.start();
                    isTimerStarted = true;
                }

                if (isClickable) {
                    synchronized (lock) {

                        if (firstCard != null) {
                            if (firstCard.position == position) {
                                gridviewBackground.getChildAt(position).setBackgroundColor(getResources().getColor(android.R.color.transparent));
                                firstCard = null;
                                return; //the user pressed the same card
                            }
                        }
                        gridviewBackground.getChildAt(position).setBackgroundColor(getResources().getColor(R.color.white));
                        Log.i("position", Integer.toString(position));
                        selectCard(v, position);
                    }
                }

            }
        });
    }

    public void newGame(View view) {
        fragmentManager.beginTransaction().remove(fragmentChoice).commit();
        String type;

        switch (view.getId()){
            case R.id.coupes:
                type = "coupes";
                break;
            case R.id.arbres:
                type = "arbres";
                break;
            case R.id.graines:
                type = "graines";
                break;
            default:
                type = "coupes";
                break;
        }
        newGame(type);
    }

    @Override
    public void onBackPressed() {}

    private void newGame(String type) {

        association = new Semantique(this, type);

        // On initialise un tableau d'entiers contenant en position 0 le nombre de colonnes que fera la gridview, et en position 1 le nombre de cartes dans le jeu
        int levelParams[] = association.setLevelParams(level);
        gridviewBackground.setNumColumns(levelParams[0]);
        gridview.setNumColumns(levelParams[0]);
        finish = levelParams[1] / 2;

        List<Integer[]> idDrawablesFrontAndBack = association.getListDrawablesFrontAndBack();

        gridview.setAdapter(new ImageAdapter(this, idDrawablesFrontAndBack.get(0)));
        gridviewBackground.setAdapter(new ImageAdapter(this, idDrawablesFrontAndBack.get(1)));
    }

    private void selectCard(View v, int position) {


        if (firstCard == null) {
            firstCard = new Card(v, position);
            Log.i("first card", "ok");

        } else {

            isClickable = false;

            secondCard = new Card(v, position);
            Log.i("second card", "ok");


            TimerTask tt = new TimerTask() {

                @Override
                public void run() {
                    try {
                        synchronized (lock) {
                            Log.i("handler ?", "ok");
                            handler.sendEmptyMessage(0);
                        }
                    } catch (Exception e) {
                        Log.e("E1", e.getMessage());
                    }
                }
            };

            Timer t = new Timer(false);
            t.schedule(tt, 500);

        }


    }

    @SuppressLint("HandlerLeak")
    class UpdateCardsHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            synchronized (lock) {
                Log.i("Checkards ?", "ok");
                checkCards();
            }
        }

        void checkCards() {
            tries++;

            boolean isSameFruit = association.checkCards(firstCard, secondCard);
            Animation animFadeOut = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_out);

            if (isSameFruit) {
                firstCard.view.setBackgroundColor(getResources().getColor(R.color.green));
                secondCard.view.setBackgroundColor(getResources().getColor(R.color.green));
                new ParticleSystem(SemantiqueGame.this, 1000, getResources().getIdentifier(association.getNom().toLowerCase() + "_ico", "drawable", getPackageName()), 1000)
                        .setSpeedRange(0.2f, 0.5f)
                        .oneShot(firstCard.view, 100);
                firstCard.view.startAnimation(animFadeOut);
                gridviewBackground.getChildAt(firstCard.position).startAnimation(animFadeOut);
                firstCard.view.setVisibility(View.INVISIBLE);
                new ParticleSystem(SemantiqueGame.this, 1000, getResources().getIdentifier(association.getNom().toLowerCase() + "_ico", "drawable", getPackageName()), 1000)
                        .setSpeedRange(0.2f, 0.5f)
                        .oneShot(secondCard.view, 100);
                secondCard.view.startAnimation(animFadeOut);
                gridviewBackground.getChildAt(secondCard.position).startAnimation(animFadeOut);
                secondCard.view.setVisibility(View.INVISIBLE);

                finish--;
                isClickable = true;
            } else {

                gridviewBackground.getChildAt(firstCard.position).setBackgroundColor(getResources().getColor(R.color.red));
                gridviewBackground.getChildAt(secondCard.position).setBackgroundColor(getResources().getColor(R.color.red));
                gridviewBackground.getChildAt(firstCard.position).startAnimation(animFadeOut);
                gridviewBackground.getChildAt(secondCard.position).startAnimation(animFadeOut);

                isClickable = true;
            }

            firstCard = null;
            secondCard = null;

            if (finish == 0) {
                timer.stop();
                result();
            }
        }
    }

    private void result() {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        calendar = Calendar.getInstance();
        String monthString;
        int calendarMonth = calendar.get(Calendar.MONTH) + 1;
        if (calendarMonth < 10) {
            monthString = "0" + Integer.toString(calendarMonth);
        } else {
            monthString = Integer.toString(calendarMonth);
        }
        String date = calendar.get(Calendar.DAY_OF_MONTH) + "/" + monthString + "/" + calendar.get(Calendar.YEAR);
        long time = (SystemClock.elapsedRealtime() - timer.getBase()) / 1000;
        Log.i("nb coups", Integer.toString(tries));
        Log.i("timer : ", Long.toString(time));

        Result result = new Result();
        result.setName(preferences.getString(getString(R.string.name), null));
        result.setGame("Association");
        result.setLevel(level);
        result.setTries(tries);
        result.setTime(time);
        result.setDate(date);

        ResultDAO resultDAO = new ResultDAO(this);
        resultDAO.add(result);


        Bundle bundle = new Bundle();
        bundle.putInt("level", level);
        fragmentResult = new ResultFragment();
        fragmentResult.setArguments(bundle);
        buttonBack.setVisibility(View.INVISIBLE);
        final Handler handlerResult = new Handler();
        handlerResult.postDelayed(new Runnable() {
            @Override
            public void run() {
                fragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.up_start, R.anim.up_end)
                        .replace(R.id.fragment_container, fragmentResult).commit();
            }
        }, 1000);

    }

    public void replayLevel(View view) {
        fragmentManager.beginTransaction().remove(fragmentResult).commit();
        buttonBack.setVisibility(View.VISIBLE);
        fragmentManager.beginTransaction().replace(R.id.fragment_container, fragmentChoice).commit();

    }

    public void nextLevel(View view) {
        fragmentManager.beginTransaction().remove(fragmentResult).commit();
        buttonBack.setVisibility(View.VISIBLE);
        fragmentManager.beginTransaction().replace(R.id.fragment_container, fragmentChoice).commit();
        isTimerStarted = false;
        tries = 0;
        level++;

    }

    public void back(View v) {
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
    }
}
