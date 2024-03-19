package kagura.project.com.fruitlegumes.association.memorielle

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
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
import kagura.project.com.fruitlegumes.R
import kagura.project.com.fruitlegumes.adapters.ImageAdapter
import kagura.project.com.fruitlegumes.association.Association
import kagura.project.com.fruitlegumes.association.ResultFragment
import kagura.project.com.fruitlegumes.collections.Card
import kagura.project.com.fruitlegumes.collections.Result
import kagura.project.com.fruitlegumes.database.ResultDAO
import java.util.Calendar
import java.util.Timer
import java.util.TimerTask

class MemoryGame : AppCompatActivity() {
    private var isClickable = true
    private var finish = 0
    private var tries = 0
    private var handler: UpdateCardsHandler? = null
    private var firstCard: Card? = null
    private var secondCard: Card? = null
    private lateinit var setRightOutFirst: AnimatorSet
    private lateinit var setRightInFirst: AnimatorSet
    private lateinit var setRightOutSecond: AnimatorSet
    private lateinit var setRightInSecond: AnimatorSet
    private lateinit var gridviewFront: GridView
    private lateinit var gridviewBack: GridView
    private lateinit var fragmentResult: Fragment
    var buttonBack: ImageView? = null
    private var relativeLayout: RelativeLayout? = null
    private var isTimerStarted = false
    var timer: Chronometer? = null
    private lateinit var calendar: Calendar
    private var fragmentManager: FragmentManager? = null
    lateinit var memory: Association

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        window.requestFeature(Window.FEATURE_ACTION_BAR)
        supportActionBar!!.hide()
        handler = UpdateCardsHandler()
        setContentView(R.layout.activity_memory_game)

        setRightOutFirst = AnimatorInflater.loadAnimator(applicationContext,
                R.animator.card_flip_right_out) as AnimatorSet
        setRightInFirst = AnimatorInflater.loadAnimator(applicationContext,
                R.animator.card_flip_right_in) as AnimatorSet
        setRightOutSecond = AnimatorInflater.loadAnimator(applicationContext,
                R.animator.card_flip_right_out) as AnimatorSet
        setRightInSecond = AnimatorInflater.loadAnimator(applicationContext,
                R.animator.card_flip_right_in) as AnimatorSet
        gridviewFront = findViewById<View>(R.id.gridviewFront) as GridView
        gridviewBack = findViewById<View>(R.id.gridviewBack) as GridView
        buttonBack = findViewById<View>(R.id.buttonBack) as ImageView
        relativeLayout = findViewById<View>(R.id.relativeLayout) as RelativeLayout
        level = intent.getIntExtra("level", 0)
        Log.i("level", level.toString())
        newGame()
        Log.i("after", "new game")
        gridviewFront.onItemClickListener = OnItemClickListener { _, v, position, _ ->
            if (!isTimerStarted) {
                timer = Chronometer(applicationContext)
                timer!!.start()
                isTimerStarted = true
            }
            Log.i("after", "chrono")
            if (isClickable) {
                synchronized(lock) {
                    if (firstCard == null) {
                        setRightOutFirst.setTarget(v)
                        setRightInFirst.setTarget(gridviewBack.getChildAt(position))
                        setRightOutFirst.start()
                        setRightInFirst.start()
                    } else {
                        if (firstCard!!.position == position) {
                            return@OnItemClickListener  //the user pressed the same card
                        }
                        setRightOutSecond.setTarget(v)
                        setRightInSecond.setTarget(gridviewBack.getChildAt(position))
                        setRightOutSecond.start()
                        setRightInSecond.start()
                    }
                    Log.i("position", position.toString())
                    turnCard(v, position)
                }
            }
        }
    }

    override fun onBackPressed() {}

    private fun newGame() {
        memory = Memory(this)

        // On initialise un tableau d'entiers contenant en position 0 le nombre de colonnes que fera la gridview,
        // et en position 1 le nombre de cartes dans le jeu
        memory.setLevelParams(level)
        gridviewBack.numColumns = memory.columns
        gridviewFront.numColumns = memory.columns
        finish = memory.size / 2
        val idDrawablesFrontAndBack = memory.listDrawablesFrontAndBack

        // à la position 0 sont placés tous les dos de cartes, à la 1, les différents légumes chargés
        gridviewFront.adapter = ImageAdapter(this, idDrawablesFrontAndBack[1].toTypedArray(), relativeLayout!!.tag.toString())
        gridviewBack.adapter = ImageAdapter(this, idDrawablesFrontAndBack[0].toTypedArray(), relativeLayout!!.tag.toString())
    }

    private fun turnCard(v: View, position: Int) {
        Log.i("debut", "turn card")
        if (firstCard == null) {
            firstCard = Card(v, gridviewBack.getChildAt(position), position)
        } else {
            isClickable = false
            secondCard = Card(v, gridviewBack.getChildAt(position), position)
            val tt: TimerTask = object : TimerTask() {
                override fun run() {
                    try {
                        synchronized(lock) { handler!!.sendEmptyMessage(0) }
                    } catch (e: Exception) {
                        Log.e("E1", e.message.toString())
                    }
                }
            }
            val t = Timer(false)
            t.schedule(tt, 1300)
        }
    }

    @SuppressLint("HandlerLeak")
    private inner class UpdateCardsHandler : Handler() {
        override fun handleMessage(msg: Message) {
            synchronized(lock) { checkCards() }
        }

        fun checkCards() {
            tries++
            val isSameFruit = memory.checkCards(firstCard, secondCard)
            if (isSameFruit) {
                val animFadeOut = AnimationUtils.loadAnimation(applicationContext, R.anim.fade_out)
                ParticleSystem(this@MemoryGame, 1000, resources.getIdentifier(memory.getNom(firstCard!!.position) + "_ico", "drawable", packageName), 1000)
                        .setSpeedRange(0.2f, 0.5f)
                        .oneShot(firstCard!!.viewBack, 100)
                firstCard!!.viewBack.startAnimation(animFadeOut)
                firstCard!!.viewFront.visibility = View.INVISIBLE
                firstCard!!.viewBack.visibility = View.INVISIBLE
                ParticleSystem(this@MemoryGame, 1000, resources.getIdentifier(memory.getNom(secondCard!!.position) + "_ico", "drawable", packageName), 1000)
                        .setSpeedRange(0.2f, 0.5f)
                        .oneShot(secondCard!!.viewBack, 100)
                secondCard!!.viewBack.startAnimation(animFadeOut)
                secondCard!!.viewFront.visibility = View.INVISIBLE
                secondCard!!.viewBack.visibility = View.INVISIBLE
                finish--
                isClickable = true
            } else {
                setRightOutFirst.setTarget(firstCard!!.viewBack)
                setRightInFirst.setTarget(firstCard!!.viewFront)
                setRightOutSecond.setTarget(secondCard!!.viewBack)
                setRightInSecond.setTarget(secondCard!!.viewFront)
                setRightOutFirst.start()
                setRightInFirst.start()
                setRightOutSecond.start()
                setRightInSecond.start()
                val handlerFlip = Handler()
                handlerFlip.postDelayed({ isClickable = true }, 1000)
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
        calendar = Calendar.getInstance()
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
        result.game = "Memory"
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
        fragmentManager = supportFragmentManager
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
        newGame()
    }

    fun nextLevel(@Suppress("UNUSED_PARAMETER") view: View?) {
        fragmentManager!!.beginTransaction().remove(fragmentResult).commit()
        buttonBack!!.visibility = View.VISIBLE
        isTimerStarted = false
        tries = 0
        level++
        newGame()
    }

    fun back(@Suppress("UNUSED_PARAMETER") v: View?) {
        AlertDialog.Builder(this).setTitle("Quitter")
                .setMessage("Êtes vous sûrs de vouloir quitter ce jeu ?")
                .setIcon(android.R.drawable.ic_menu_help)
                .setPositiveButton("Oui") { _, _ ->
                    finish()
                    overridePendingTransition(R.anim.left_start, R.anim.left_end)
                }
                .setNegativeButton("Non", null).show()
    }

    companion object {
        private var level = 0
        private val lock = Any()
    }
}