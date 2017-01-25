package kagura.project.com.a8;

import java.util.List;

import kagura.project.com.a8.memory.Association;

public class AssociationSemantique extends Association {

    @Override
    public void loadCards(){

    }

    @Override
    public List<Integer> loadImages(){

        return idImages;
    }
}

