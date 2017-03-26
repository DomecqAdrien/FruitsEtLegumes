package kagura.project.com.a8.association;

import android.content.Context;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

import kagura.project.com.a8.collections.Card;

public abstract class Association {

    protected int level;
    protected int size;
    private int columns;

    public int getSize() {
        return size;
    }

    public int getColumns() {
        return columns;
    }


    protected Context context;
    protected List<Integer> imagePositions;
    protected List<String> imageNames;
    protected List<Integer> listPositionsAvailables;
    protected boolean isListFruitsCreated;
    protected int backImage;

    protected Association(Context context){
        super();
        this.context = context;
    }


    // Crée les paramètres de la gridview en fonction d'un niveau donné
    // size : taille de la gridview
    // columns : nombre de colonnes dans la gridview (optimisation de l'affichage)
    public void setLevelParams(int level){
        this.level = level;
        switch(level){
            case 1:
                this.columns = 3;
                this.size = 6;
                break;
            case 2:
                this.columns = 4;
                this.size = 8;
                break;
            case 3:
                this.columns = 4;
                this.size = 12;
                break;
        }
        Log.i("size", Integer.toString(size));
        Log.i("column", Integer.toString(columns));
    }

    /**
     *
     * @return retourne une liste contenant deux tableaux d'identifiants de drawable
     */
    public abstract List<Integer[]> getListDrawablesFrontAndBack();

    // Construit une liste de nom de fruit/légumes à partir d'un JSON, cette liste sera exploitée pour récupérer les noms des différents drawable employés
    public abstract void buildListNames();

    /**
     * Construit une liste de "positions" disponibles dans la gridview
     * exemple : pour une gridview de 6 places :
     * [0,1,2,3,4,5]
     * 8 places :
     * [0,1,2,3,4,5,6,7]
     *
     */
    protected void buildListPositionsAvailables() {
        listPositionsAvailables = new ArrayList<>();

        for(int i = 0; i < size; i++) {
            listPositionsAvailables.add(i);
        }
        Log.i("listPositionsAvailables", listPositionsAvailables.toString());
    }

    /**
     *
     * @param position
     * enlève une "position" de la liste de positions disponibles construite auparavant, une fois qu'un élément est défini à cette position
     * exemple : pour un gridview de 6 places :
     * avant suppression : [0,1,2,3,4,5]
     * un élément est défini à la position 3 :
     * après suppression : [0,1,3,4,5]
     */
    protected void removePositionAvailable(int position){
        listPositionsAvailables.remove(position);
    }

    /* vérifie si les deux cartes selectionnées sont :
     * memory : identiques
     * semantique : de la même famille
     */
    public boolean checkCards(Card firstCard, Card secondCard){
        return imageNames.get(firstCard.position).equals(imageNames.get(secondCard.position));
    }

    /*
     * construit les tableaux d'integers et les intègre à une liste
     * @return une liste de deux tableaux d'entiers contenant les IDs des drawables utilisés
     */
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

    public String getNom(int position){
        return imageNames.get(position);
    }
}

