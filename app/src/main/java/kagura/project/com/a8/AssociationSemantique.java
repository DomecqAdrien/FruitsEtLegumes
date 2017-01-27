package kagura.project.com.a8;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import kagura.project.com.a8.objects.Card;
import kagura.project.com.a8.objects.Fruit;

public class AssociationSemantique extends Association {

    private List<Fruit> fruits;
    private Random r = new Random();
    private int randomMax;

    public AssociationSemantique(Context context) {
        super(context);
    }

    @Override
    public List<Integer[]> loadCards(){

        List<Integer[]> idDrawables = new ArrayList<>();

        loadImages();
        Integer[] mThumbIds = new Integer[size];
        Integer[] mThumbIdsBackground = new Integer[size];

        Log.i("loadCards()","size=" + size);

        imagePositions = new ArrayList<>(Collections.nCopies(size, 0));
        imageNames = new ArrayList<>(Collections.nCopies(size, ""));

        Log.i("imagePositionInit", imagePositions.toString());
        List<Integer> listIntegers = new ArrayList<>();

        for(int i = 0; i < size; i++) {
            listIntegers.add(i);
        }
        Log.i("listIntegers", listIntegers.toString());

        Log.i("size", Integer.toString(size));
        for(int i =0; i < size / 2; i++){
            Log.i("i", Integer.toString(i));
            int randomPositionCard1;
            int randomPositionCard2;
            int randomImage = r.nextInt(fruits.size());
            Log.i("fruits.size()", Integer.toString(fruits.size()));
            Log.i("fruit_plein_id", Integer.toString(fruits.get(randomImage).getFruit_plein_id()));
            if(!imagePositions.contains(fruits.get(randomImage).getFruit_plein_id())){
                randomPositionCard1 = r.nextInt(listIntegers.size());
                imagePositions.set(listIntegers.get(randomPositionCard1), fruits.get(randomImage).getFruit_plein_id());
                imageNames.set(listIntegers.get(randomPositionCard1), fruits.get(randomImage).getNom());

                Log.i("carte 1 :", "Position " + listIntegers.get(randomPositionCard1));
                listIntegers.remove(randomPositionCard1);

                randomPositionCard2 = r.nextInt(listIntegers.size());

                imageNames.set(listIntegers.get(randomPositionCard2), fruits.get(randomImage).getNom());

                switch (randomMax){
                    case 0:
                        imagePositions.set(listIntegers.get(randomPositionCard2), fruits.get(randomImage).getFruit_coupe_id());
                        break;
                    case 1:
                        imagePositions.set(listIntegers.get(randomPositionCard2), fruits.get(randomImage).getFruit_arbre_id());
                        break;
                    case 2:
                        imagePositions.set(listIntegers.get(randomPositionCard2), fruits.get(randomImage).getFruit_graine_id());
                        break;
                }

                Log.i("carte 2 :", "Position " + listIntegers.get(randomPositionCard2));
                listIntegers.remove(randomPositionCard2);

            }else{
                i--;
            }
        }

        for(int i = 0; i < imagePositions.size(); i++){
            mThumbIds[i] = imagePositions.get(i);
            mThumbIdsBackground[i] = context.getResources().getColor(android.R.color.transparent);
        }
        idDrawables.add(0, mThumbIds);
        idDrawables.add(1, mThumbIdsBackground);

        return idDrawables;

    }

    @Override
    public void loadImages(){
        if(level > 1){
            randomMax = r.nextInt(level);
        }else{
            randomMax = 0;
        }
        Log.i("random", Integer.toString(randomMax));

        fruits = new ArrayList<>();

        try{
            JSONObject obj = new JSONObject(loadJSONFromAsset("fruits.json"));
            JSONArray arr = obj.getJSONArray("fruits");
            Fruit fruit;

            for(int i = 0; i < arr.length(); i++){

                fruit = new Fruit();
                JSONObject jsonObject = arr.getJSONObject(i);

                fruit.setNom(jsonObject.getString("nom"));
                fruit.setFruit_plein_id(context.getResources().getIdentifier(jsonObject.getString("fruit"), "drawable", context.getPackageName()));

                switch (randomMax){
                    case 0:
                        fruit.setFruit_coupe_id(context.getResources().getIdentifier(jsonObject.getString("coupe"), "drawable", context.getPackageName()));
                        break;
                    case 1:
                        fruit.setFruit_arbre_id(context.getResources().getIdentifier(jsonObject.getString("arbre"), "drawable", context.getPackageName()));
                        break;
                    case 2:
                        fruit.setFruit_graine_id(context.getResources().getIdentifier(jsonObject.getString("graine"), "drawable", context.getPackageName()));
                        break;
                }
                fruits.add(fruit);

            }
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean checkCards(Card firstCard, Card secondCard){
        this.position = firstCard.position;

        return imageNames.get(firstCard.position).equals(imageNames.get(secondCard.position));

    }
}

