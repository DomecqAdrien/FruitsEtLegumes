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

import kagura.project.com.a8.objects.Fruit;

public class AssociationSemantique extends Association {

    List<Fruit> fruits;
    Random r = new Random();
    int randomMax;

    public AssociationSemantique(Context context) {
        super(context);
    }

    @Override
    public List<Integer[]> loadCards(){

        List<Integer[]> idDrawables = new ArrayList<>();

        loadImages();
        Integer[] mThumbIds = new Integer[size];

        Log.i("loadCards()","size=" + size);

        imagePositions = new ArrayList<>(Collections.nCopies(size, 0));
        Log.i("imagePositionInit", imagePositions.toString());
        List<Integer> listIntegers = new ArrayList<>();

        for(int i = 0; i < size; i++) {
            listIntegers.add(i);
        }


        for(int i =0; i < size /2; i++){
            int randomPositionCard1;
            int randomPositionCard2;
            int randomImage = r.nextInt(fruits.size());

            if(!imagePositions.contains(fruits.get(randomImage).getFruit_plein_id())){
                randomPositionCard1 = r.nextInt(listIntegers.size());
                imagePositions.set(listIntegers.get(randomPositionCard1), fruits.get(randomImage).getFruit_plein_id());

                Log.i("carte 1 :", "Position " + listIntegers.get(randomPositionCard1));
                listIntegers.remove(randomPositionCard1);

                randomPositionCard2 = r.nextInt(listIntegers.size());

                switch (randomMax){
                    case 0:
                        imagePositions.set(listIntegers.get(randomPositionCard1), fruits.get(randomImage).getFruit_coupe_id());
                        break;
                    case 1:
                        imagePositions.set(listIntegers.get(randomPositionCard1), fruits.get(randomImage).getFruit_arbre_id());
                        break;
                    case 2:
                        imagePositions.set(listIntegers.get(randomPositionCard1), fruits.get(randomImage).getFruit_graine_id());
                        break;
                }

                Log.i("carte 1 :", "Position " + listIntegers.get(randomPositionCard2));
                listIntegers.remove(randomPositionCard2);

            }
        }

        for(int i = 0; i < imagePositions.size(); i++){
            mThumbIds[i] = imagePositions.get(i);
        }

        idDrawables.add(0, mThumbIds);

        return idDrawables;

    }

    @Override
    public void loadImages(){
        if(level > 1){
            randomMax = r.nextInt(level - 1);
        }else{
            randomMax = 0;
        }


        fruits = new ArrayList<>();

        try{
            JSONObject obj = new JSONObject(loadJSONFromAsset("fruits.json"));
            JSONArray arr = obj.getJSONArray("fruits");

            Fruit fruit;

            for(int i = 0; i < arr.length(); i++){

                fruit = new Fruit();

                JSONObject jsonObject = arr.getJSONObject(i);

                fruit.setNom(jsonObject.getString("nom"));
                fruit.setFruit_plein(jsonObject.getString("fruit"));

                switch (randomMax){
                    case 0:
                        fruit.setFruit_coupe(jsonObject.getString("coupe"));
                        fruit.setFruit_coupe_id(context.getResources().getIdentifier(jsonObject.getString("coupe"), "drawable", context.getPackageName()));
                        break;
                    case 1:
                        fruit.setFruit_arbre(jsonObject.getString("arbre"));
                        fruit.setFruit_arbre_id(context.getResources().getIdentifier(jsonObject.getString("arbre"), "drawable", context.getPackageName()));
                        break;
                    case 2:
                        fruit.setFruit_graine(jsonObject.getString("graine"));
                        fruit.setFruit_graine_id(context.getResources().getIdentifier(jsonObject.getString("arbre"), "drawable", context.getPackageName()));
                        break;
                }


                fruits.add(fruit);

                Log.i("fruit", fruit.toString());

            }

            Log.i("fruits", fruits.toString());



        }catch (JSONException e){
            e.printStackTrace();
        }

    }
}

