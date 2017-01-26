package kagura.project.com.a8;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import kagura.project.com.a8.objects.Card;

public abstract class Association {

    int level;
    int size;
    Context context;
    List<Integer> imagePositions;
    List<String> imageNames;

    public Association(Context context){
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

    public List<Integer[]>  loadCards(){
        List<Integer[]> idDrawablesFrontAndBack = new ArrayList<>();

        return idDrawablesFrontAndBack;

    }

    public void loadImages(){}

    public List<Integer> getImagePositions(){
        return imagePositions;
    }

    public List<String> getImageNames(){
        return imageNames;
    }

    public String loadJSONFromAsset(String jsonPath) {
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

    public boolean checkCards(Card firstCard, Card secondCard){
        return true;
    }
}

