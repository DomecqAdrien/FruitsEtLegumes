package kagura.project.com.a8.memory;


import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Chronometer;
import android.widget.GridView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import kagura.project.com.a8.ImageAdapter;
import kagura.project.com.a8.R;
import kagura.project.com.a8.database.ResultDAO;
import kagura.project.com.a8.objects.Card;
import kagura.project.com.a8.objects.Result;

public class MemoryGame extends AppCompatActivity {

    private static int level, size, columns;

    private boolean isClickable = true;

    private int backImage;
    private int finish;
    private int tries = 0;

    private String name;
    private UpdateCardsHandler handler;
    private List<Integer> imagesId;
    private List<Integer> imagePositions;
    private Card firstCard, secondCard;
    private AnimatorSet setRightOutFirst, setRightInFirst, setRightOutSecond, setRightInSecond;
    private static final Object lock = new Object();

    GridView gridviewFront, gridviewBack;
    Fragment fragmentResult;

    Boolean isTimerStarted = false;
    Chronometer timer;
    Calendar calendar;

    FragmentManager fragmentManager;


    public MemoryGame() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        setRightOutFirst = (AnimatorSet) AnimatorInflater.loadAnimator(getApplicationContext(),
                R.animator.card_flip_right_out);

        setRightInFirst = (AnimatorSet) AnimatorInflater.loadAnimator(getApplicationContext(),
                R.animator.card_flip_right_in);

        setRightOutSecond = (AnimatorSet) AnimatorInflater.loadAnimator(getApplicationContext(),
                R.animator.card_flip_right_out);

        setRightInSecond = (AnimatorSet) AnimatorInflater.loadAnimator(getApplicationContext(),
                R.animator.card_flip_right_in);

        handler = new UpdateCardsHandler();

        backImage = getResources().getIdentifier("backcard", "drawable", getPackageName());

        loadImages("legume");

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        name = preferences.getString(getString(R.string.name), null);


        setContentView(R.layout.activity_memory_game);

        gridviewFront = (GridView) findViewById(R.id.gridviewFront);
        gridviewBack = (GridView) findViewById(R.id.gridviewBack);

        level = getIntent().getIntExtra("level", 0);
        Log.i("level", Integer.toString(level));

        Association association = new AssociationPicturale();

        newGame();



        gridviewFront.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                if(!isTimerStarted){
                    timer = new Chronometer(getApplicationContext());
                    timer.start();
                    isTimerStarted = true;
                }

                if(isClickable){
                    synchronized (lock) {

                        if(firstCard == null){
                            setRightOutFirst.setTarget(v);
                            setRightInFirst.setTarget(gridviewBack.getChildAt(position));
                            setRightOutFirst.start();
                            setRightInFirst.start();
                        }else{

                            if(firstCard.position == position){
                                return; //the user pressed the same card
                            }

                            setRightOutSecond.setTarget(v);
                            setRightInSecond.setTarget(gridviewBack.getChildAt(position));
                            setRightOutSecond.start();
                            setRightInSecond.start();
                        }




                        Log.i("position", Integer.toString(position));
                        turnCard(v,position);
                    }
                }

            }
        });
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this).setTitle("Quitter")
                .setMessage("Êtes vous sûrs de vouloir quitter ce jeu ?")
                .setIcon(android.R.drawable.ic_menu_help)
                .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton("Non", null).show();

    }

    private void newGame() {

        loadCards();

    }

    private void loadCards() {

        switch(level){
            case 1:
                columns = 3;
                size = 6;
                break;
            case 2:
                columns = 4;
                size = 8;
                break;
            case 3:
                columns = 4;
                size = 12;
                break;
        }

        gridviewBack.setNumColumns(columns);
        gridviewFront.setNumColumns(columns);

        finish = size / 2;


        Integer[] mThumbIdsFront = new Integer[size];
        Integer[] mThumbIdsBack = new Integer[size];

        Log.i("loadCards()","size=" + size);

        imagePositions = new ArrayList<>(Collections.nCopies(size, 0));
        Log.i("imagePositionInit", imagePositions.toString());
        List<Integer> listIntegers = new ArrayList<>();

        for(int i = 0; i < size; i++) {
            listIntegers.add(i);
        }

        Random r = new Random();

        for(int i = 0; i < (size / 2); i++){
            int randomPositionCard1;
            int randomPositionCard2;
            int randomImage = r.nextInt(imagesId.size());

            Log.i("k","k");
            if(!imagePositions.contains(imagesId.get(randomImage))){
                randomPositionCard1 = r.nextInt(listIntegers.size());
                imagePositions.set(listIntegers.get(randomPositionCard1), imagesId.get(randomImage));

                Log.i("carte 1 :", "Position " + listIntegers.get(randomPositionCard1));
                listIntegers.remove(randomPositionCard1);



                randomPositionCard2 = r.nextInt(listIntegers.size());
                imagePositions.set(listIntegers.get(randomPositionCard2), imagesId.get(randomImage));

                Log.i("carte 2 :", "Position " + listIntegers.get(randomPositionCard2));
                listIntegers.remove(randomPositionCard2);

            }
        }
        Log.i("imagePositionsAfter", imagePositions.toString());
        for(int i = 0; i < imagePositions.size(); i++){
            mThumbIdsFront[i] = backImage;
            mThumbIdsBack[i] = imagePositions.get(i);
        }




        gridviewFront.setAdapter(new ImageAdapter(this, mThumbIdsFront));
        gridviewBack.setAdapter(new ImageAdapter(this, mThumbIdsBack));

    }

    private void loadImages(String type) {

        List<String> nameImages = new ArrayList<>();
        imagesId = new ArrayList<>();

        Field[] fields = R.drawable.class.getDeclaredFields();
        int i = 0;
        for (Field field : fields) {
            if (field.getName().startsWith(type)) {
                nameImages.add(field.getName());
                Log.i("name", nameImages.get(i));
                i++;
            }
        }

        for (int j = 0; j < nameImages.size(); j++){
            int cardId = getResources().getIdentifier(nameImages.get(j), "drawable", getPackageName());
            imagesId.add(cardId);
        }
        Log.i("nameImages", nameImages.toString());
        Log.i("images ID", imagesId.toString());

    }

    private void turnCard(View v, int position) {



        if(firstCard==null){
            firstCard = new Card(v, gridviewBack.getChildAt(position), position);
        }
        else{

            isClickable = false;

            secondCard = new Card(v, gridviewBack.getChildAt(position), position);


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

    class AssociationPicturale extends Association{

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

            if(imagePositions.get(firstCard.position).equals(imagePositions.get(secondCard.position))){

                firstCard.viewFront.setVisibility(View.INVISIBLE);
                firstCard.viewBack.setVisibility(View.INVISIBLE);
                secondCard.viewFront.setVisibility(View.INVISIBLE);
                secondCard.viewBack.setVisibility(View.INVISIBLE);

                finish--;
                isClickable = true;
            }else{
                setRightOutFirst.setTarget(firstCard.viewBack);
                setRightInFirst.setTarget(firstCard.viewFront);

                setRightOutSecond.setTarget(secondCard.viewBack);
                setRightInSecond.setTarget(secondCard.viewFront);


                setRightOutFirst.start();
                setRightInFirst.start();
                setRightOutSecond.start();
                setRightInSecond.start();

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
        result.setName(name);
        result.setGame("Memory");
        result.setLevel(level);
        result.setTries(tries);
        result.setTime(time);
        result.setDate(date);

        ResultDAO resultDAO = new ResultDAO(this);
        resultDAO.add(result);


        Bundle bundle = new Bundle();
        bundle.putInt("level", level);
        fragmentResult = new MemoryEndFragment();
        fragmentResult.setArguments(bundle);
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.push_up_in, R.anim.push_up_out)
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
