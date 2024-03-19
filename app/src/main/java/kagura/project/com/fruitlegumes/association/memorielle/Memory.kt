package kagura.project.com.fruitlegumes.association.memorielle

import android.content.Context
import android.util.Log
import kagura.project.com.fruitlegumes.LoadJson
import kagura.project.com.fruitlegumes.R
import kagura.project.com.fruitlegumes.association.Association
import org.json.JSONException
import org.json.JSONObject
import java.text.Normalizer
import java.util.*

internal class Memory(context: Context) : Association(context) {
    private lateinit var legumes: ArrayList<String>
    private val r = Random()
    override val listDrawablesFrontAndBack: ArrayList<IntArray>
        get() {
            if (!isListFruitsCreated) {
                buildListNames()
            }
            defineLegumesPositions()
            return buildListDrawablesFrontAndBack()
        }

    override fun buildListNames() {
        isListFruitsCreated = true
        legumes = ArrayList()
        try {
            val obj = JSONObject(LoadJson.loadJSONFromAsset(context, "legumes"))
            Log.i("onj", obj.toString())
            val arr = obj.getJSONArray("legumes")
            for (i in 0 until arr.length()) {
                legumes.add(Normalizer.normalize(arr[i].toString().toLowerCase(Locale.ROOT), Normalizer.Form.NFD)
                        .replace("\\p{InCombiningDiacriticalMarks}+".toRegex(), ""))
                Log.i("fruit", legumes[i])
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    // définit aléatoirement la positions des éléments dans la gridview
    private fun defineLegumesPositions() {
        Log.i("getListDrawablesFAndB()", "size=$size")
        imagePositions = ArrayList(Collections.nCopies(size, 0))
        imageNames = ArrayList(Collections.nCopies(size, ""))
        Log.i("imagePositionInit", imagePositions.toString())
        buildListPositionsAvailables()
        var i = 0
        while (i < size / 2) {
            val randomImage = r.nextInt(legumes.size)
            Log.i("legume size", legumes.size.toString())
            if (!imageNames.contains(legumes[randomImage])) {
                // Ajout de la carte 1
                addDrawableIdInPosition(randomImage)
                // Ajout de la carte 2
                addDrawableIdInPosition(randomImage)
            } else {
                i--
            }
            i++
        }
        Log.i("imagePositionsAfter", imagePositions.toString())
    }

    // ajoute un identifiant à une position aléatoire parmis les positions disponibles dans la liste de positions disponibles
    private fun addDrawableIdInPosition(randomImage: Int) {
        val randomPositionCard = r.nextInt(listPositionsAvailables.size)
        imagePositions[listPositionsAvailables[randomPositionCard]] = getLegumeImageId(legumes[randomImage])
        imageNames[listPositionsAvailables[randomPositionCard]] = legumes[randomImage]
        Log.i("carte :", "Position " + listPositionsAvailables[randomPositionCard])
        listPositionsAvailables.removeAt(randomPositionCard)
    }

    // récupère l'ID d'un drawable en fonction d'un path précis + un nom de légume donné
    private fun getLegumeImageId(legume: String): Int {
        return context.resources.getIdentifier(context.getString(R.string.legume_path) + legume, "drawable", context.packageName)
    }

    init {
        backImage = context.resources.getIdentifier("backcard", "drawable", context.packageName)
    }
}