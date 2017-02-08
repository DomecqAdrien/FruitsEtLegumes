package kagura.project.com.a8.association.memorielle;


import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import kagura.project.com.a8.association.Association;
import kagura.project.com.a8.objects.Card;
import kagura.project.com.a8.objects.Legume;

class Memory extends Association {

    private List<Legume> legumes;
    private Random r = new Random();

    Memory(Context context) {
        super(context);
        backImage = context.getResources().getIdentifier("backcard", "drawable", context.getPackageName());
    }

    public void initGame() {
    }

    @Override
    public List<Integer[]> getListDrawablesFrontAndBack() {
        if (!isListFruitsCreated) {
            buildListFruits();
        }

        Log.i("getListDrawablesFAndB()", "size=" + size);

        imagePositions = new ArrayList<>(Collections.nCopies(size, 0));
        imageNames = new ArrayList<>(Collections.nCopies(size, ""));

        Log.i("imagePositionInit", imagePositions.toString());

        listPositionsAvailables = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            listPositionsAvailables.add(i);
        }

        for (int i = 0; i < (size / 2); i++) {
            int randomImage = r.nextInt(legumes.size());
            Log.i("legume size", Integer.toString(legumes.size()));

            if (!imagePositions.contains(legumes.get(randomImage).getLegume_id())) {
                // Ajout de la carte 1
                addCardInPosition(randomImage);
                // Ajout de la carte 2
                addCardInPosition(randomImage);
            } else {
                i--;
            }
        }
        Log.i("imagePositionsAfter", imagePositions.toString());

        return buildListDrawablesFrontAndBack();
    }

    @Override
    public void buildListFruits() {
        isListFruitsCreated = true;

        legumes = new ArrayList<>();

        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset("legumes.json"));
            JSONArray arr = obj.getJSONArray("legumes");
            Legume legume;

            for (int i = 0; i < arr.length(); i++) {

                legume = new Legume();
                JSONObject jsonObject = arr.getJSONObject(i);

                legume.setNom(jsonObject.getString("nom"));
                Log.i("legumenom", jsonObject.getString("nom"));
                legume.setLegume_id(context.getResources().getIdentifier(jsonObject.getString("path"), "drawable", context.getPackageName()));
                Log.i("legumeid", jsonObject.getString("path"));

                legumes.add(legume);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean checkCards(Card firstCard, Card secondCard) {
        this.position = firstCard.position;
        return imageNames.get(firstCard.position).equals(imageNames.get(secondCard.position));
    }


    private void addCardInPosition(int randomImage) {
        int randomPositionCard;
        randomPositionCard = r.nextInt(listPositionsAvailables.size());
        imagePositions.set(listPositionsAvailables.get(randomPositionCard), legumes.get(randomImage).getLegume_id());
        imageNames.set(listPositionsAvailables.get(randomPositionCard), legumes.get(randomImage).getNom());

        Log.i("carte 1 :", "Position " + listPositionsAvailables.get(randomPositionCard));
        listPositionsAvailables.remove(randomPositionCard);
    }


}