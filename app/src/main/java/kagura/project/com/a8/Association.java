package kagura.project.com.a8;

import java.util.ArrayList;
import java.util.List;

public abstract class Association {

    List<Integer> idImages;
    int size;

    public void loadCards(){

    }

    public List<Integer> loadImages(){
        idImages = new ArrayList<>();

        return idImages;
    }
}

