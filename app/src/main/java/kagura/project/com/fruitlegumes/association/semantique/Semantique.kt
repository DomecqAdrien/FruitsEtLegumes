package kagura.project.com.fruitlegumes.association.semantique

import android.content.Context
import android.util.Log
import kagura.project.com.fruitlegumes.LoadJson
import kagura.project.com.fruitlegumes.R
import kagura.project.com.fruitlegumes.association.Association
import org.json.JSONException
import org.json.JSONObject
import java.text.Normalizer
import java.util.*

internal class Semantique(context: Context, private val type: String) : Association(context) {
    private var fruits: MutableList<String>? = null
    private val rand = Random()
    override val listDrawablesFrontAndBack: ArrayList<IntArray>
        get() {
            if (!isListFruitsCreated) {
                buildListNames()
            }
            defineFruitsPositions()
            return buildListDrawablesFrontAndBack()
        }

    override fun buildListNames() {
        fruits = ArrayList()
        try {
            val obj = JSONObject(LoadJson.loadJSONFromAsset(context, "fruits"))
            val arr = obj.getJSONArray("fruits")
            Log.i("Object", obj.toString())
            Log.i("Array", arr.toString())
            for (i in 0 until arr.length()) {
                fruits?.add(Normalizer.normalize(arr[i].toString().toLowerCase(Locale.ROOT), Normalizer.Form.NFD)
                        .replace("\\p{InCombiningDiacriticalMarks}+".toRegex(), ""))
                Log.i("fruit", fruits?.get(i).toString())
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    // définit aléatoirement la positions des éléments dans la gridview
    private fun defineFruitsPositions() {
        Log.i("gridSize", size.toString())
        imagePositions = ArrayList(Collections.nCopies(size, 0))
        imageNames = ArrayList(Collections.nCopies(size, ""))
        Log.i("imagePositionInit", imagePositions.toString())
        buildListPositionsAvailables()
        Log.i("size", size.toString())
        var i = 0
        while (i < size / 2) {
            Log.i("i", i.toString())
            val randomImage = rand.nextInt(fruits!!.size)
            var randomPosition: Int
            if (!imageNames.contains(fruits!![randomImage])) {
                randomPosition = rand.nextInt(listPositionsAvailables.size)
                addDrawableIdInPosition(randomImage, randomPosition, true)
                removePositionAvailable(randomPosition)
                randomPosition = rand.nextInt(listPositionsAvailables.size)
                addDrawableIdInPosition(randomImage, randomPosition, false)
                removePositionAvailable(randomPosition)
            } else {
                i--
            }
            i++
        }
    }

    // ajoute un identifiant à une position aléatoire parmis les positions disponibles dans la liste de positions disponibles
    private fun addDrawableIdInPosition(randomImage: Int, randomPosition: Int, isFirstCard: Boolean) {
        Log.i("fruits.size()", fruits!!.size.toString())
        Log.i("fruit_plein_id", getFruitImageId(fruits!![randomImage]).toString())
        imageNames[listPositionsAvailables[randomPosition]] = fruits!![randomImage]
        if (isFirstCard) {
            imagePositions[listPositionsAvailables[randomPosition]] = getFruitImageId(fruits!![randomImage])
        } else {
            when (type) {
                "coupes" -> imagePositions[listPositionsAvailables[randomPosition]] = getFruitImageId(R.string.fruit_coupe_path, fruits!![randomImage])
                "arbres" -> imagePositions[listPositionsAvailables[randomPosition]] = getFruitImageId(R.string.fruit_arbre_path, fruits!![randomImage])
                "graines" -> imagePositions[listPositionsAvailables[randomPosition]] = getFruitImageId(R.string.fruit_graine_path, fruits!![randomImage])
            }
        }
        Log.i("idDrawable", imagePositions[listPositionsAvailables[randomPosition]].toString())
        Log.i("carte :", "Position " + listPositionsAvailables[randomPosition])
    }

    // permet de récupérer l'identifiant d'un fruit plein donné, qui sera forcemment présent dans le jeu
    private fun getFruitImageId(fruit: String): Int {
        return context.resources.getIdentifier(context.getString(R.string.fruit_plein_path) + fruit, "drawable", context.packageName)
    }

    // permet de récupérer au choix l'identifiant d'un arbre, graine ou fruit coupé selon un fruit donné, paire complémentaire au fruit plein
    private fun getFruitImageId(type: Int, fruit: String): Int {
        return context.resources.getIdentifier(context.getString(type) + fruit, "drawable", context.packageName)
    }

    init {
        backImage = context.getColor(android.R.color.transparent)
    }
}