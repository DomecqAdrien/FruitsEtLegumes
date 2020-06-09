package kagura.project.com.a8

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.AdapterView.OnItemClickListener
import android.widget.GridView
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import kagura.project.com.a8.adapters.ImageAdapterJ
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.text.Normalizer
import java.util.*

class Encyclopedie : AppCompatActivity() {
    private var gridViewFruits: GridView? = null
    private var fruitsName: ArrayList<String>? = null
    private var isImagesLoaded = false
    private lateinit var idDrawables: IntArray
    private var isMenuView = true
    private var fruitPlein: ImageView? = null
    private var fruitCoupe: ImageView? = null
    private var fruitArbre: ImageView? = null
    private var fruitGraine: ImageView? = null

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        window.requestFeature(Window.FEATURE_ACTION_BAR)
        supportActionBar!!.hide()
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        setContentView(R.layout.activity_solution_menu)
        loadCards()
        loadMenu()
    }

    private fun loadMenu() {
        gridViewFruits = findViewById<View>(R.id.gridviewFruits) as GridView
        gridViewFruits!!.adapter = ImageAdapterJ(this, idDrawables.toTypedArray(), "test")
        gridViewFruits!!.onItemClickListener = OnItemClickListener { _, _, position, _ ->
            setContentView(R.layout.activity_solution)
            isMenuView = false
            loadFruit(position)
        }
    }

    override fun onBackPressed() {}
    private fun loadFruit(position: Int) {
        fruitPlein = findViewById<View>(R.id.fruitPlein) as ImageView
        fruitCoupe = findViewById<View>(R.id.fruitCoupe) as ImageView
        fruitArbre = findViewById<View>(R.id.fruitArbre) as ImageView
        fruitGraine = findViewById<View>(R.id.fruitGraine) as ImageView
        fruitPlein!!.setImageDrawable(getFruitResource(position, "plein"))
        fruitCoupe!!.setImageDrawable(getFruitResource(position, "coupe"))
        fruitArbre!!.setImageDrawable(getFruitResource(position, "arbre"))
        fruitGraine!!.setImageDrawable(getFruitResource(position, "graine"))
    }

    private fun getFruitResource(position: Int, type: String): Drawable? {
        val pathFruit = getString(resources.getIdentifier("fruit_" + type + "_path", "string", packageName)) + fruitsName!![position]
        return getDrawable(resources.getIdentifier(pathFruit, "drawable", packageName))
    }

    private fun loadCards() {
        if (!isImagesLoaded) {
            buildListFruits()
        }
        idDrawables = IntArray(fruitsName!!.size)
        for (i in fruitsName!!.indices) {
            idDrawables[i] = resources.getIdentifier(getString(R.string.fruit_plein_path) + fruitsName!![i], "drawable", packageName)
            Log.i("k", resources.getIdentifier(getString(R.string.fruit_plein_path) + fruitsName!![i], "drawable", packageName).toString())
        }
    }

    private fun buildListFruits() {
        isImagesLoaded = true
        fruitsName = ArrayList()
        try {
            val obj = JSONObject(LoadJson.loadJSONFromAsset(this, "fruits").toString())
            val arr: JSONArray = obj.getJSONArray("fruits")
            for (i in 0 until arr.length()) {
                fruitsName?.add(Normalizer.normalize(arr[i].toString().toLowerCase(Locale.ROOT), Normalizer.Form.NFD)
                        .replace("\\p{InCombiningDiacriticalMarks}+".toRegex(), ""))
                Log.i("fruit", fruitsName?.get(i).toString())
            }
            Log.i("kkk", fruitsName.toString())
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    fun backToMenu(@Suppress("UNUSED_PARAMETER") view: View?) {
        if (isMenuView) {
            super.onBackPressed()
            overridePendingTransition(R.anim.down_start, R.anim.down_end)
        } else {
            setContentView(R.layout.activity_solution_menu)
            loadMenu()
            isMenuView = true
        }
    }
}