package kagura.project.com.a8.association.semantique

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.os.SystemClock
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.AdapterView.OnItemClickListener
import android.widget.Chronometer
import android.widget.GridView
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.preference.PreferenceManager
import com.plattysoft.leonids.ParticleSystem
import kagura.project.com.a8.R
import kagura.project.com.a8.adapters.ImageAdapterJ
import kagura.project.com.a8.association.Association
import kagura.project.com.a8.association.ResultFragment
import kagura.project.com.a8.collections.Card
import kagura.project.com.a8.collections.Result
import kagura.project.com.a8.database.ResultDAO
import java.util.*

class SemantiqueGame : AppCompatActivity() {
    private var isClickable = true
    private var finish = 0
    private var tries = 0
    private var handler: UpdateCardsHandler? = null
    private var firstCard: Card? = null
    private var secondCard: Card? = null
    var gridview: GridView? = null
    var gridviewBackground: GridView? = null
    private var relativeLayout: RelativeLayout? = null
    private lateinit var fragmentResult: Fragment
    private lateinit var fragmentChoice: Fragment
    private var isTimerStarted = false
    var timer: Chronometer? = null
    private var fragmentManager: FragmentManager? = null
    private lateinit var semantique: Association
    var buttonBack: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        window.requestFeature(Window.FEATURE_ACTION_BAR)
        supportActionBar!!.hide()
        handler = UpdateCardsHandler()
        setContentView(R.layout.activity_association_game)


        gridview = findViewById<View>(R.id.gridview) as GridView
        gridviewBackground = findViewById<View>(R.id.gridviewBackground) as GridView
        relativeLayout = findViewById<View>(R.id.relativeLayout) as RelativeLayout
        buttonBack = findViewById<View>(R.id.buttonBack) as ImageView
        level = intent.getIntExtra("level", 0)
        Log.i("level", level.toString())

        fragmentChoice = SemantiqueChoiceFragment()
        fragmentManager = supportFragmentManager
        fragmentManager!!.beginTransaction()
                .replace(R.id.fragment_container, fragmentChoice).commit()
        gridview!!.onItemClickListener = OnItemClickListener { _, v, position, _ ->
            if (!isTimerStarted) {
                timer = Chronometer(applicationContext)
                timer!!.start()
                isTimerStarted = true
            }
            if (isClickable) {
                synchronized(lock) {
                    if (firstCard != null) {
                        if (firstCard!!.position == position) {
                            gridviewBackground!!.getChildAt(position).setBackgroundColor(getColor(android.R.color.transparent))
                            firstCard = null
                            return@OnItemClickListener  //the user pressed the same card
                        }
                    }
                    gridviewBackground!!.getChildAt(position).setBackgroundColor(getColor(R.color.white))
                    Log.i("position", position.toString())
                    selectCard(v, position)
                }
            }
        }
    }

    fun newGame(view: View) {
        fragmentManager!!.beginTransaction().remove(fragmentChoice).commit()
        val type: String = when (view.id) {
            R.id.coupes -> "coupes"
            R.id.arbres -> "arbres"
            R.id.graines -> "graines"
            else -> "coupes"
        }
        newGame(type)
    }

    override fun onBackPressed() {}
    private fun newGame(type: String) {
        semantique = Semantique(this, type)

        // On initialise un tableau d'entiers contenant en position 0 le nombre de colonnes que fera la gridview, et en position 1 le nombre de cartes dans le jeu
        semantique.setLevelParams(level)
        gridviewBackground!!.numColumns = semantique.columns
        gridview!!.numColumns = semantique.columns
        finish = semantique.size / 2
        val idDrawablesFrontAndBack = semantique.listDrawablesFrontAndBack
        gridview!!.adapter = ImageAdapterJ(this, idDrawablesFrontAndBack[0].toTypedArray(), "test")
        gridviewBackground!!.adapter = ImageAdapterJ(this, idDrawablesFrontAndBack[1].toTypedArray(), "test")
    }

    private fun selectCard(v: View, position: Int) {
        if (firstCard == null) {
            firstCard = Card(v, position)
            Log.i("first card", "ok")
        } else {
            isClickable = false
            secondCard = Card(v, position)
            Log.i("second card", "ok")
            val tt: TimerTask = object : TimerTask() {
                override fun run() {
                    try {
                        synchronized(lock) {
                            Log.i("handler ?", "ok")
                            handler!!.sendEmptyMessage(0)
                        }
                    } catch (e: Exception) {
                        Log.e("E1", e.message.toString())
                    }
                }
            }
            val t = Timer(false)
            t.schedule(tt, 500)
        }
    }

    @SuppressLint("HandlerLeak")
    private inner class UpdateCardsHandler : Handler() {
        override fun handleMessage(msg: Message) {
            synchronized(lock) {
                Log.i("Checkards ?", "ok")
                checkCards()
            }
        }

        fun checkCards() {
            tries++
            val isSameFruit = semantique.checkCards(firstCard, secondCard)
            val animFadeOut = AnimationUtils.loadAnimation(applicationContext, R.anim.fade_out)
            if (isSameFruit) {
                firstCard!!.view.setBackgroundColor(getColor(R.color.green))
                secondCard!!.view.setBackgroundColor(getColor(R.color.green))
                ParticleSystem(this@SemantiqueGame, 1000, resources.getIdentifier(semantique.getNom(firstCard!!.position).toLowerCase(Locale.ROOT) + "_ico", "drawable", packageName), 1000)
                        .setSpeedRange(0.2f, 0.5f)
                        .oneShot(firstCard!!.view, 100)
                firstCard!!.view.startAnimation(animFadeOut)
                gridviewBackground!!.getChildAt(firstCard!!.position).startAnimation(animFadeOut)
                firstCard!!.view.visibility = View.INVISIBLE
                ParticleSystem(this@SemantiqueGame, 1000, resources.getIdentifier(semantique.getNom(secondCard!!.position).toLowerCase(Locale.ROOT) + "_ico", "drawable", packageName), 1000)
                        .setSpeedRange(0.2f, 0.5f)
                        .oneShot(secondCard!!.view, 100)
                secondCard!!.view.startAnimation(animFadeOut)
                gridviewBackground!!.getChildAt(secondCard!!.position).startAnimation(animFadeOut)
                secondCard!!.view.visibility = View.INVISIBLE
                finish--
                isClickable = true
            } else {
                gridviewBackground!!.getChildAt(firstCard!!.position).setBackgroundColor(getColor(R.color.red))
                gridviewBackground!!.getChildAt(secondCard!!.position).setBackgroundColor(getColor(R.color.red))
                gridviewBackground!!.getChildAt(firstCard!!.position).startAnimation(animFadeOut)
                gridviewBackground!!.getChildAt(secondCard!!.position).startAnimation(animFadeOut)
                isClickable = true
            }
            firstCard = null
            secondCard = null
            if (finish == 0) {
                timer!!.stop()
                result()
            }
        }
    }

    private fun result() {
        val preferences = PreferenceManager.getDefaultSharedPreferences(this)
        val calendar = Calendar.getInstance()
        val monthString: String
        val calendarMonth = calendar.get(Calendar.MONTH) + 1
        monthString = if (calendarMonth < 10) {
            "0$calendarMonth"
        } else {
            calendarMonth.toString()
        }
        val date = calendar.get(Calendar.DAY_OF_MONTH).toString() + "/" + monthString + "/" + calendar.get(Calendar.YEAR)
        val time = (SystemClock.elapsedRealtime() - timer!!.base) / 1000
        Log.i("nb coups", tries.toString())
        Log.i("timer : ", time.toString())
        val result = Result()
        result.name = preferences.getString(getString(R.string.name), null)
        result.game = "Association"
        result.level = level
        result.tries = tries
        result.time = time
        result.date = date
        val resultDAO = ResultDAO(this)
        resultDAO.add(result)
        val bundle = Bundle()
        bundle.putInt("level", level)
        fragmentResult = ResultFragment()
        fragmentResult.arguments = bundle
        buttonBack!!.visibility = View.INVISIBLE
        val handlerResult = Handler()
        handlerResult.postDelayed({
            fragmentManager!!.beginTransaction()
                    .setCustomAnimations(R.anim.up_start, R.anim.up_end)
                    .replace(R.id.fragment_container, fragmentResult).commit()
        }, 1000)
    }

    fun replayLevel(@Suppress("UNUSED_PARAMETER") view: View?) {
        fragmentManager!!.beginTransaction().remove(fragmentResult).commit()
        buttonBack!!.visibility = View.VISIBLE
        fragmentManager!!.beginTransaction().replace(R.id.fragment_container, fragmentChoice).commit()
    }

    fun nextLevel(@Suppress("UNUSED_PARAMETER") view: View?) {
        fragmentManager!!.beginTransaction().remove(fragmentResult).commit()
        buttonBack!!.visibility = View.VISIBLE
        fragmentManager!!.beginTransaction().replace(R.id.fragment_container, fragmentChoice).commit()
        isTimerStarted = false
        tries = 0
        level++
    }

    fun back(@Suppress("UNUSED_PARAMETER") v: View?) {
        AlertDialog.Builder(this).setTitle("Quitter")
                .setMessage("Êtes vous sûrs de vouloir quitter ce jeu ?")
                .setIcon(android.R.drawable.ic_menu_help)
                .setPositiveButton("Oui") { _, _ ->
                    finish()
                    overridePendingTransition(R.anim.right_start, R.anim.right_end)
                }
                .setNegativeButton("Non", null).show()
    }

    companion object {
        private var level = 0
        private val lock = Any()
    }
}