package kagura.project.com.a8.association;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

import kagura.project.com.a8.objects.Card;

public abstract class Association {

    protected int level;
    protected int size;
    protected int position;
    protected Context context;
    protected List<Integer> imagePositions;
    protected List<String> imageNames;
    protected List<Integer> listIntegers;
    protected boolean isImagesLoaded;

    protected Association(Context context){
        this.context = context;

    }


    public int[] setLevelParams(int level){
        this.level = level;
        int columns = 3;
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
        Log.i("size", Integer.toString(size));
        Log.i("column", Integer.toString(columns));
        return new int[]{columns, size};
    }

    /**
     *
     * @return
     */
    public abstract List<Integer[]> loadCards();

    public abstract void buildListFruits();

    protected String loadJSONFromAsset(String jsonPath) {
        String json;
        try {
            InputStream is = context.getAssets().open(jsonPath);
            int size = is.available();
            byte[] buffer = new byte[size];

            is.read(buffer);
            is.close();

            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public abstract boolean checkCards(Card firstCard, Card secondCard);

    public String getNom(){

        String normalized = Normalizer.normalize(imageNames.get(position), Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        Log.i("nom", normalized);
        return normalized;
    }

    public abstract void addCardInPosition(int randomImage);

    protected List<Integer[]> returnCards(int backImage){
        List<Integer[]> idDrawables = new ArrayList<>();

        Integer[] mThumbIds = new Integer[size];
        Integer[] mThumbIdsBackground = new Integer[size];

        for(int i = 0; i < imagePositions.size(); i++){
            mThumbIds[i] = imagePositions.get(i);
            mThumbIdsBackground[i] = backImage;
        }
        idDrawables.add(0, mThumbIds);
        idDrawables.add(1, mThumbIdsBackground);

        return idDrawables;

    }
}

