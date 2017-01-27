package kagura.project.com.a8;

import android.content.Context;
import android.util.Log;
import android.util.Pair;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import kagura.project.com.a8.objects.Card;
import kagura.project.com.a8.objects.Fruit;
import kagura.project.com.a8.objects.Legume;


public class AssociationPicturale extends Association {

    private List<Legume> legumes;
    private Random r = new Random();

    public AssociationPicturale(Context context) {
        super(context);
    }

    @Override
    public  List<Integer[]> loadCards(){
        loadImages();
        List<Integer[]> idDrawablesFrontAndBack = new ArrayList<>();

        Integer[] mThumbIdsFront = new Integer[size];
        Integer[] mThumbIdsBack = new Integer[size];

        Log.i("loadCards()","size=" + size);

        imagePositions = new ArrayList<>(Collections.nCopies(size, 0));
        imageNames = new ArrayList<>(Collections.nCopies(size, ""));

        Log.i("imagePositionInit", imagePositions.toString());

        List<Integer> listIntegers = new ArrayList<>();

        for(int i = 0; i < size; i++) {
            listIntegers.add(i);
        }

        for(int i = 0; i < (size / 2); i++){
            int randomPositionCard1;
            int randomPositionCard2;
            Log.i("legume size", Integer.toString(legumes.size()));
            int randomImage = r.nextInt(legumes.size());

            if(!imagePositions.contains(legumes.get(randomImage).getLegume_id())){
                randomPositionCard1 = r.nextInt(listIntegers.size());
                imagePositions.set(listIntegers.get(randomPositionCard1), legumes.get(randomImage).getLegume_id());
                imageNames.set(listIntegers.get(randomPositionCard1), legumes.get(randomImage).getNom());

                Log.i("carte 1 :", "Position " + listIntegers.get(randomPositionCard1));
                listIntegers.remove(randomPositionCard1);

                randomPositionCard2 = r.nextInt(listIntegers.size());

                imageNames.set(listIntegers.get(randomPositionCard2), legumes.get(randomImage).getNom());
                imagePositions.set(listIntegers.get(randomPositionCard2), legumes.get(randomImage).getLegume_id());

                Log.i("carte 2 :", "Position " + listIntegers.get(randomPositionCard2));
                listIntegers.remove(randomPositionCard2);

            }else{
                i--;
            }
        }
        Log.i("imagePositionsAfter", imagePositions.toString());

        int backImage = context.getResources().getIdentifier("test", "drawable", context.getPackageName());
        for(int i = 0; i < imagePositions.size(); i++){
            mThumbIdsFront[i] = backImage;
            mThumbIdsBack[i] = imagePositions.get(i);
        }

        idDrawablesFrontAndBack.add(0, mThumbIdsFront);
        idDrawablesFrontAndBack.add(1, mThumbIdsBack);

        return idDrawablesFrontAndBack;



    }

    @Override
    public void loadImages(){

        legumes = new ArrayList<>();

        try{
            JSONObject obj = new JSONObject(loadJSONFromAsset("legumes.json"));
            JSONArray arr = obj.getJSONArray("legumes");
            Legume legume;

            for(int i = 0; i < arr.length(); i++){

                legume = new Legume();
                JSONObject jsonObject = arr.getJSONObject(i);

                legume.setNom(jsonObject.getString("nom"));
                Log.i("legumenom", jsonObject.getString("nom"));
                legume.setLegume_id(context.getResources().getIdentifier(jsonObject.getString("path"), "drawable", context.getPackageName()));
                Log.i("legumeid", jsonObject.getString("path"));

                legumes.add(legume);

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