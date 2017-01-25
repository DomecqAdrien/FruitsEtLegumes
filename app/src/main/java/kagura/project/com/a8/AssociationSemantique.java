package kagura.project.com.a8;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class AssociationSemantique extends Association {

    public AssociationSemantique(Context context) {
        super(context);
    }

    @Override
    public List<Integer[]> loadCards(){
        List<Integer[]> b = new ArrayList<>();
        return b;

    }

    @Override
    public List<Integer> loadImages(){
        List<Integer> idImages = new ArrayList<>();

        return idImages;
    }
}

