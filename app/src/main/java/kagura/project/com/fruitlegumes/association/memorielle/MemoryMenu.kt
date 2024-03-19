package kagura.project.com.fruitlegumes.association.memorielle

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import kagura.project.com.fruitlegumes.R

class MemoryMenu : AppCompatActivity() {
    private var avatar: ImageView? = null
    private var avatarName: String? = null
    private var isClickable = true

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        window.requestFeature(Window.FEATURE_ACTION_BAR)
        supportActionBar!!.hide()
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        setContentView(R.layout.activity_memory_menu)
        val preferences = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        avatarName = preferences.getString(getString(R.string.avatar), null)
        avatar = findViewById<View>(R.id.imageAvatar) as ImageView
        Log.i("Avatar", avatarName.toString())
        when (avatarName) {
            "verMince" -> avatar!!.background = getDrawable(R.drawable.ver_mince_hd)
            "verGros" -> avatar!!.background = getDrawable(R.drawable.ver_gros_hd_reverse)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.left_start, R.anim.left_end)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        isClickable = true
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == 66) {
            onBackPressed()
        }
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
            Log.i("button", button.toString())
            val intentMemory = Intent(this, MemoryGame::class.java)
            intentMemory.putExtra("level", button)
            this.startActivityForResult(intentMemory, 0)
            overridePendingTransition(R.anim.right_start, R.anim.right_end)
        }
    }

    fun back(@Suppress("UNUSED_PARAMETER") v: View?) {
        onBackPressed()
    }
}