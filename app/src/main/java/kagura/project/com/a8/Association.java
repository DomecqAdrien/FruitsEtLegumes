package kagura.project.com.a8;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public abstract class Association {

    int size;
    Context context;
    List<Integer> imagePositions;

    public Association(Context context){
        this.context = context;

    }


    public int setLevelParams(int level){
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
        return columns;
    }

    public List<Integer[]>  loadCards(){
        List<Integer[]> idDrawablesFrontAndBack = new ArrayList<>();

        return idDrawablesFrontAndBack;

    }

    public List<Integer> loadImages(){
        List <Integer> idImages = new ArrayList<>();

        return idImages;
    }

    public List<Integer> getImagePositions(){
        return imagePositions;
    }
}

