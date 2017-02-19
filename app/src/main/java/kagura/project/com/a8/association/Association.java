package kagura.project.com.a8.association;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import kagura.project.com.a8.LoadJson;
import kagura.project.com.a8.objects.Card;

public abstract class Association {

    protected int level;
    protected int size;
    protected int position;
    protected Context context;
    protected List<Integer> imagePositions;
    protected List<String> imageNames;
    protected List<Integer> listPositionsAvailables;
    protected LoadJson lj = new LoadJson();
    protected boolean isListFruitsCreated;
    protected int backImage;

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
    public abstract List<Integer[]> getListDrawablesFrontAndBack();

    public abstract void buildListFruits();

    protected void buildListPositionsAvailables() {
        listPositionsAvailables = new ArrayList<>();

        for(int i = 0; i < size; i++) {
            listPositionsAvailables.add(i);
        }
        Log.i("listPositionsAvailables", listPositionsAvailables.toString());
    }

    protected void removePositionAvailable(int position){
        listPositionsAvailables.remove(position);
    }
    public abstract boolean checkCards(Card firstCard, Card secondCard);

    public String getNom(){
        return imageNames.get(position);
    }

    protected List<Integer[]> buildListDrawablesFrontAndBack(){
        List<Integer[]> idDrawablesFrontAndBack = new ArrayList<>();

        Integer[] mThumbIds = new Integer[size];
        Integer[] mThumbIdsBackground = new Integer[size];

        for(int i = 0; i < imagePositions.size(); i++){
            mThumbIds[i] = imagePositions.get(i);
            mThumbIdsBackground[i] = backImage;
        }
        idDrawablesFrontAndBack.add(0, mThumbIds);
        idDrawablesFrontAndBack.add(1, mThumbIdsBackground);

        return idDrawablesFrontAndBack;

    }
}

