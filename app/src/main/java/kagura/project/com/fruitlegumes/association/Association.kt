package kagura.project.com.fruitlegumes.association

import android.content.Context
import android.util.Log
import kagura.project.com.fruitlegumes.collections.Card

abstract class Association protected constructor(protected var context: Context) {
    protected var level = 0
    var size = 0
        protected set
    var columns = 0
        private set

    protected lateinit var imagePositions: ArrayList<Int>
    protected lateinit var imageNames: ArrayList<String>
    protected lateinit var listPositionsAvailables: ArrayList<Int>

    @JvmField
    protected var isListFruitsCreated = false

    @JvmField
    protected var backImage: Int = 0

    // Crée les paramètres de la gridview en fonction d'un niveau donné
    // size : taille de la gridview
    // columns : nombre de colonnes dans la gridview (optimisation de l'affichage)
    fun setLevelParams(level: Int) {
        this.level = level
        when (level) {
            1 -> {
                columns = 3
                size = 6
            }
            2 -> {
                columns = 4
                size = 8
            }
            3 -> {
                columns = 4
                size = 12
            }
        }
        Log.i("size", size.toString())
        Log.i("column", columns.toString())
    }

    /**
     *
     * @return retourne une liste contenant deux tableaux d'identifiants de drawable
     */
    abstract val listDrawablesFrontAndBack: ArrayList<IntArray>

    // Construit une liste de nom de fruit/légumes à partir d'un JSON, cette liste sera exploitée pour récupérer les noms des différents drawable employés
    abstract fun buildListNames()

    /**
     * Construit une liste de "positions" disponibles dans la gridview
     * exemple : pour une gridview de 6 places :
     * [0,1,2,3,4,5]
     * 8 places :
     * [0,1,2,3,4,5,6,7]
     *
     */
    protected fun buildListPositionsAvailables() {
        listPositionsAvailables = ArrayList()
        for (i in 0 until size) {
            (listPositionsAvailables).add(i)
        }
        Log.i("listPositionsAvailables", listPositionsAvailables.toString())
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
    protected fun removePositionAvailable(position: Int) {
        listPositionsAvailables.removeAt(position)
    }

    /* vérifie si les deux cartes selectionnées sont :
     * memory : identiques
     * semantique : de la même famille
     */
    fun checkCards(firstCard: Card?, secondCard: Card?): Boolean {
        return imageNames[firstCard!!.position] == imageNames[secondCard!!.position]
    }

    /*
     * construit les tableaux d'integers et les intègre à une liste
     * @return une liste de deux tableaux d'entiers contenant les IDs des drawables utilisés
     */
    protected fun buildListDrawablesFrontAndBack(): ArrayList<IntArray> {
        val idDrawablesFrontAndBack: ArrayList<IntArray> = ArrayList()
        val mThumbIds = IntArray(size)
        val mThumbIdsBackground = IntArray(size)
        Log.i("count before i: ", imagePositions.size.toString())
        for (i in imagePositions.indices) {
            Log.i("count i: ", i.toString())
            mThumbIds[i] = imagePositions[i]
            mThumbIdsBackground[i] = backImage
        }
        idDrawablesFrontAndBack.add(0, mThumbIds)
        idDrawablesFrontAndBack.add(1, mThumbIdsBackground)
        return idDrawablesFrontAndBack
    }

    fun getNom(position: Int): String {
        return imageNames[position]
    }

}