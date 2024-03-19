package kagura.project.com.fruitlegumes.admin

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import kagura.project.com.fruitlegumes.R

class AdminSession : AppCompatActivity() {
    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        window.requestFeature(Window.FEATURE_ACTION_BAR)
        supportActionBar!!.hide()
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        setContentView(R.layout.activity_admin_session)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.down_start, R.anim.down_end)
    }

    fun results(view: View) {
        val intentResults = Intent(this, AdminResults::class.java)
        when (view.id) {
            R.id.tv_result_association -> {
                intentResults.putExtra("type", "Association")
                this.startActivity(intentResults)
                overridePendingTransition(R.anim.left_start, R.anim.left_end)
            }
            R.id.tv_result_memory -> {
                intentResults.putExtra("type", "Memory")
                this.startActivity(intentResults)
                overridePendingTransition(R.anim.right_start, R.anim.right_end)
            }
        }
    }
}