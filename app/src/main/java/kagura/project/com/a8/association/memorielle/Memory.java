package kagura.project.com.a8.association.memorielle;


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

class Memory extends Association {


    private List<String> legumes;
    private Random r = new Random();

    Memory(Context context) {
        super(context);
        backImage = context.getResources().getIdentifier("backcard", "drawable", context.getPackageName());
    }

    @Override
    public List<Integer[]> getListDrawablesFrontAndBack() {
        if (!isListFruitsCreated) {
            buildListNames();
        }

        defineLegumesPositions();

        return buildListDrawablesFrontAndBack();
    }

    @Override
    public void buildListNames() {
        isListFruitsCreated = true;

        legumes = new ArrayList<>();

        try {
            LoadJson lj = new LoadJson();
            JSONObject obj = new JSONObject(lj.loadJSONFromAsset(context, "legumes"));
            Log.i("onj", obj.toString());
            JSONArray arr = obj.getJSONArray("legumes");

            for (int i = 0; i < arr.length(); i++) {
                legumes.add(Normalizer.normalize(arr.get(i).toString().toLowerCase(), Normalizer.Form.NFD)
                        .replaceAll("\\p{InCombiningDiacriticalMarks}+", ""));
                Log.i("fruit", legumes.get(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    // définit aléatoirement la positions des éléments dans la gridview
    private void defineLegumesPositions(){
        Log.i("getListDrawablesFAndB()", "size=" + size);

        imagePositions = new ArrayList<>(Collections.nCopies(size, 0));
        imageNames = new ArrayList<>(Collections.nCopies(size, ""));

        Log.i("imagePositionInit", imagePositions.toString());

        buildListPositionsAvailables();

        for (int i = 0; i < (size / 2); i++) {
            int randomImage = r.nextInt(legumes.size());
            Log.i("legume size", Integer.toString(legumes.size()));

            if (!imageNames.contains(legumes.get(randomImage))) {
                // Ajout de la carte 1
                addDrawableIdInPosition(randomImage);
                // Ajout de la carte 2
                addDrawableIdInPosition(randomImage);
            } else {
                i--;
            }
        }
        Log.i("imagePositionsAfter", imagePositions.toString());
    }

    // ajoute un identifiant à une position aléatoire parmis les positions disponibles dans la liste de positions disponibles
    private void addDrawableIdInPosition(int randomImage) {
        int randomPositionCard;
        randomPositionCard = r.nextInt(listPositionsAvailables.size());
        imagePositions.set(listPositionsAvailables.get(randomPositionCard), getLegumeImageId(legumes.get(randomImage)));
        imageNames.set(listPositionsAvailables.get(randomPositionCard), legumes.get(randomImage));

        Log.i("carte :", "Position " + listPositionsAvailables.get(randomPositionCard));
        listPositionsAvailables.remove(randomPositionCard);
    }

    // récupère l'ID d'un drawable en fonction d'un path précis + un nom de légume donné
    private int getLegumeImageId(String legume){
        return context.getResources().getIdentifier(context.getString(R.string.legume_path) + legume, "drawable", context.getPackageName());
    }


}