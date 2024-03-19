package kagura.project.com.fruitlegumes

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kagura.project.com.fruitlegumes.association.memorielle.MemoryMenu
import kagura.project.com.fruitlegumes.association.semantique.SemantiqueMenu

class MainMenu : AppCompatActivity() {
    var name: String? = null

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        window.requestFeature(Window.FEATURE_ACTION_BAR)
        supportActionBar!!.hide()
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        //val preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        name = intent.extras?.getString("name").toString() //preferences.getString(getString(R.string.name), null)
        setContentView(R.layout.activity_main)
        val textHello: TextView = findViewById(R.id.textHello)
        textHello.append(", $name !")
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.up_start, R.anim.up_end)
    }

    fun startGame(v: View) {
        when (v.id) {
            R.id.spaceMemory -> {
                val intentGame = Intent(this, MemoryMenu::class.java)
                this.startActivityForResult(intentGame, 0)
                overridePendingTransition(R.anim.right_start, R.anim.right_end)
            }
            R.id.spaceAssociation -> {
                val intentGame = Intent(this, SemantiqueMenu::class.java)
                this.startActivityForResult(intentGame, 0)
                overridePendingTransition(R.anim.left_start, R.anim.left_end)
            }
        }
    }
}