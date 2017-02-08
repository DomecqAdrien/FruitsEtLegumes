package kagura.project.com.a8.association.semantique;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import kagura.project.com.a8.R;
import kagura.project.com.a8.association.Association;
import kagura.project.com.a8.objects.Card;

class Semantique extends Association {

    private List<String> fruitsName;
    private Random r = new Random();
    private String type;

    Semantique(Context context, String type) {
        super(context);
        this.type = type;

    }

    @Override
    public List<Integer[]> loadCards(){

        if (!isImagesLoaded) {
            buildListFruits();
        }

        Log.i("loadCards()","size=" + size);

        imagePositions = new ArrayList<>(Collections.nCopies(size, 0));
        imageNames = new ArrayList<>(Collections.nCopies(size, ""));

        Log.i("imagePositionInit", imagePositions.toString());
        listIntegers = new ArrayList<>();

        for(int i = 0; i < size; i++) {
            listIntegers.add(i);
        }
        Log.i("listIntegers", listIntegers.toString());

        Log.i("size", Integer.toString(size));
        for(int i =0; i < size / 2; i++){
            Log.i("i", Integer.toString(i));
            int randomImage = r.nextInt(fruitsName.size());
            if(!imageNames.contains(fruitsName.get(randomImage))){
                addCardInPosition(randomImage);
            }else{
                i--;
            }
        }
        int backImage = context.getResources().getColor(android.R.color.transparent);

        return returnCards(backImage);

    }

    @Override
    public void buildListFruits(){
        fruitsName = new ArrayList<>();

        try{
            JSONObject obj = new JSONObject(loadJSONFromAsset("fruits.json"));
            JSONArray arr = obj.getJSONArray("fruits");

            for(int i = 0; i < arr.length(); i++){

                fruitsName.add(Normalizer.normalize(arr.get(i).toString().toLowerCase(), Normalizer.Form.NFD)
                        .replaceAll("\\p{InCombiningDiacriticalMarks}+", ""));
                Log.i("fruit", fruitsName.get(i));
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    @Override
    public void addCardInPosition(int randomImage) {
        int randomPositionCard1;
        int randomPositionCard2;
        Log.i("fruits.size()", Integer.toString(fruitsName.size()));
        Log.i("fruit_plein_id", Integer.toString(getFruitImageId(fruitsName.get(randomImage))));
        randomPositionCard1 = r.nextInt(listIntegers.size());
        Log.i("fruit", fruitsName.get(randomImage));
        imagePositions.set(listIntegers.get(randomPositionCard1), getFruitImageId(fruitsName.get(randomImage)));
        imageNames.set(listIntegers.get(randomPositionCard1), fruitsName.get(randomImage));
        Log.i("idDrawable 1", Integer.toString(imagePositions.get(listIntegers.get(randomPositionCard1))));

        Log.i("carte 1 :", "Position " + listIntegers.get(randomPositionCard1));
        listIntegers.remove(randomPositionCard1);

        randomPositionCard2 = r.nextInt(listIntegers.size());

        imageNames.set(listIntegers.get(randomPositionCard2), fruitsName.get(randomImage));

        switch (type){
            case "coupes":
                imagePositions.set(listIntegers.get(randomPositionCard2), getFruitImageId(R.string.fruit_coupe_path, fruitsName.get(randomImage)));
                break;
            case "arbres":
                imagePositions.set(listIntegers.get(randomPositionCard2), getFruitImageId(R.string.fruit_arbre_path, fruitsName.get(randomImage)));
                break;
            case "graines":
                imagePositions.set(listIntegers.get(randomPositionCard2), getFruitImageId(R.string.fruit_graine_path, fruitsName.get(randomImage)));
                break;
        }
        Log.i("idDrawable 2", Integer.toString(imagePositions.get(listIntegers.get(randomPositionCard2))));

        Log.i("carte 2 :", "Position " + listIntegers.get(randomPositionCard2));
        listIntegers.remove(randomPositionCard2);
    }


    // permet de récupérer l'identifiant d'un fruit plein donné, qui sera forcemment présent dans le jeu
    private int getFruitImageId(String fruit){
        return context.getResources().getIdentifier(context.getString(R.string.fruit_plein_path) + fruit, "drawable", context.getPackageName());
    }

    // permet de récupérer au choix l'identifiant d'un arbre, graine ou fruit coupé selon un fruit donné, paire complémentaire au fruit plein
    private int getFruitImageId(int type, String fruit){
        return context.getResources().getIdentifier(context.getString(type) + fruit, "drawable", context.getPackageName());
    }

    @Override
    public boolean checkCards(Card firstCard, Card secondCard){
        this.position = firstCard.position;
        return imageNames.get(firstCard.position).equals(imageNames.get(secondCard.position));
    }
}

