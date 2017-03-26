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

import kagura.project.com.a8.LoadJson;
import kagura.project.com.a8.R;
import kagura.project.com.a8.association.Association;

class Semantique extends Association {

    private List<String> fruits;
    private Random rand = new Random();
    private String type;

    Semantique(Context context, String type) {
        super(context);
        this.type = type;
        backImage = context.getResources().getColor(android.R.color.transparent);

    }
    
    @Override
    public List<Integer[]> getListDrawablesFrontAndBack(){
        if (!isListFruitsCreated) {
            buildListNames();
        }
        
        defineFruitsPositions();
        return buildListDrawablesFrontAndBack();
    }

    @Override
    public void buildListNames(){
        fruits = new ArrayList<>();

        try{
            LoadJson lj = new LoadJson();
            JSONObject obj = new JSONObject(lj.loadJSONFromAsset(context, "fruits"));
            JSONArray arr = obj.getJSONArray("fruits");
            Log.i("Object", obj.toString());
            Log.i("Array", arr.toString());


            for(int i = 0; i < arr.length(); i++){

                fruits.add(Normalizer.normalize(arr.get(i).toString().toLowerCase(), Normalizer.Form.NFD)
                        .replaceAll("\\p{InCombiningDiacriticalMarks}+", ""));
                Log.i("fruit", fruits.get(i));
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    // définit aléatoirement la positions des éléments dans la gridview
    private void defineFruitsPositions() {

        Log.i("gridSize", Integer.toString(size));

        imagePositions = new ArrayList<>(Collections.nCopies(size, 0));
        imageNames = new ArrayList<>(Collections.nCopies(size, ""));

        Log.i("imagePositionInit", imagePositions.toString());

        buildListPositionsAvailables();

        Log.i("size", Integer.toString(size));
        for(int i =0; i < size / 2; i++){
            Log.i("i", Integer.toString(i));
            int randomImage = rand.nextInt(fruits.size());
            int randomPosition;
            if(!imageNames.contains(fruits.get(randomImage))){
                randomPosition = rand.nextInt(listPositionsAvailables.size());
                addDrawableIdInPosition(randomImage, randomPosition, true);
                removePositionAvailable(randomPosition);

                randomPosition = rand.nextInt(listPositionsAvailables.size());
                addDrawableIdInPosition(randomImage, randomPosition, false);
                removePositionAvailable(randomPosition);

            }else{
                i--;
            }
        }
    }

    // ajoute un identifiant à une position aléatoire parmis les positions disponibles dans la liste de positions disponibles
    private void addDrawableIdInPosition(int randomImage, int randomPosition, boolean isFirstCard) {
        Log.i("fruits.size()", Integer.toString(fruits.size()));
        Log.i("fruit_plein_id", Integer.toString(getFruitImageId(fruits.get(randomImage))));

        imageNames.set(listPositionsAvailables.get(randomPosition), fruits.get(randomImage));
        if(isFirstCard){
            imagePositions.set(listPositionsAvailables.get(randomPosition), getFruitImageId(fruits.get(randomImage)));
        }else {
            switch (type){
                case "coupes":
                    imagePositions.set(listPositionsAvailables.get(randomPosition), getFruitImageId(R.string.fruit_coupe_path, fruits.get(randomImage)));
                    break;
                case "arbres":
                    imagePositions.set(listPositionsAvailables.get(randomPosition), getFruitImageId(R.string.fruit_arbre_path, fruits.get(randomImage)));
                    break;
                case "graines":
                    imagePositions.set(listPositionsAvailables.get(randomPosition), getFruitImageId(R.string.fruit_graine_path, fruits.get(randomImage)));
                    break;
            }
        }

        Log.i("idDrawable", Integer.toString(imagePositions.get(listPositionsAvailables.get(randomPosition))));
        Log.i("carte :", "Position " + listPositionsAvailables.get(randomPosition));
    }


    // permet de récupérer l'identifiant d'un fruit plein donné, qui sera forcemment présent dans le jeu
    private int getFruitImageId(String fruit){
        return context.getResources().getIdentifier(context.getString(R.string.fruit_plein_path) + fruit, "drawable", context.getPackageName());
    }

    // permet de récupérer au choix l'identifiant d'un arbre, graine ou fruit coupé selon un fruit donné, paire complémentaire au fruit plein
    private int getFruitImageId(int type, String fruit){
        return context.getResources().getIdentifier(context.getString(type) + fruit, "drawable", context.getPackageName());
    }
}

