package kagura.project.com.a8.association.semantique

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import kagura.project.com.a8.Encyclopedie
import kagura.project.com.a8.R

class SemantiqueMenu : AppCompatActivity() {
    var avatar: ImageView? = null
    private var avatarName: String? = null
    private var isClickable = true
    //var fragmentResult: Fragment? = null
    //var fragmentManager: FragmentManager? = null

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        window.requestFeature(Window.FEATURE_ACTION_BAR)
        supportActionBar!!.hide()
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        setContentView(R.layout.activity_association_menu)
        val preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        avatarName = preferences.getString(getString(R.string.avatar), null)
        avatar = findViewById<View>(R.id.imageAvatar) as ImageView
        when (avatarName) {
            "verMince" -> avatar!!.background = getDrawable(R.drawable.ver_mince_hd_reverse)
            "verGros" -> avatar!!.background = getDrawable(R.drawable.ver_gros_hd)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        isClickable = true
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == 66) {
            onBackPressed()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.right_start, R.anim.right_end)
    }

    fun startEncyclopedie(@Suppress("UNUSED_PARAMETER") view: View?) {
        if (isClickable) {
            isClickable = false
            val intentEncyclopedie = Intent(this, Encyclopedie::class.java)
            this.startActivityForResult(intentEncyclopedie, 0)
            overridePendingTransition(R.anim.up_start, R.anim.up_end)
        }
    }

    fun back(@Suppress("UNUSED_PARAMETER") v: View?) {
        onBackPressed()
    }

    fun startGame(@Suppress("UNUSED_PARAMETER") v: View) {
        if (isClickable) {
            isClickable = false
            var button = 1
            when (v.id) {
                R.id.level_1tv -> button = 1
                R.id.level_2tv -> button = 2
                R.id.level_3tv -> button = 3
            }
            val intentGame = Intent(this, SemantiqueGame::class.java)
            intentGame.putExtra("level", button)
            this.startActivityForResult(intentGame, 0)
            overridePendingTransition(R.anim.left_start, R.anim.left_end)
        }
    }
}