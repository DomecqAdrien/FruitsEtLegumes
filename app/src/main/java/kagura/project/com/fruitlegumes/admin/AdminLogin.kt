package kagura.project.com.fruitlegumes.admin

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kagura.project.com.fruitlegumes.R

class AdminLogin : AppCompatActivity() {
    private var passAdmin: EditText? = null
    private var passAdminCheck: String? = null

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
        setContentView(R.layout.activity_admin_login)
        passAdmin = findViewById<View>(R.id.passAdmin) as EditText
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.down_start, R.anim.down_end)
    }

    fun checkPassword(@Suppress("UNUSED_PARAMETER") view: View?) {
        passAdminCheck = passAdmin!!.text.toString()
        when (passAdminCheck) {
            getString(R.string.passAdminVerif) -> {
                val intentAdmin = Intent(this, AdminSession::class.java)
                this.startActivityForResult(intentAdmin, 0)
                overridePendingTransition(R.anim.up_start, R.anim.up_end)
            }
            "" -> {
                Toast.makeText(this, "Veuillez entrer un mot de passe", Toast.LENGTH_LONG).show()
            }
            else -> {
                Toast.makeText(this, "Mot de passe incorrect", Toast.LENGTH_LONG).show()
            }
        }
    }
}