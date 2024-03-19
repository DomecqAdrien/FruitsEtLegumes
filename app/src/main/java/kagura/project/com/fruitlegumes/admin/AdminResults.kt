package kagura.project.com.fruitlegumes.admin

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kagura.project.com.fruitlegumes.R
import kagura.project.com.fruitlegumes.adapters.ResultAdapter
import kagura.project.com.fruitlegumes.collections.Result
import kagura.project.com.fruitlegumes.database.ResultDAO

class AdminResults : AppCompatActivity() {
    private var type: String? = null
    private var resultDAO: ResultDAO? = null
    private var listResults: ListView? = null
    var results: List<Result>? = null

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        window.requestFeature(Window.FEATURE_ACTION_BAR)
        supportActionBar!!.hide()
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        setContentView(R.layout.activity_admin_results)
        type = intent.getStringExtra("type")
        listResults = findViewById<View>(R.id.listResults) as ListView
        val fontFamily = Typeface.createFromAsset(assets, "fonts/fontawesome.ttf")
        val sampleText = findViewById<View>(R.id.trashcan) as TextView
        sampleText.typeface = fontFamily
        sampleText.text = "\uf1f8"
        resultDAO = ResultDAO(this)
        results = resultDAO!!.selectAll(type!!)
        val resAdapter = ResultAdapter(this, R.layout.row, results)
        listResults!!.adapter = resAdapter
    }

    override fun onBackPressed() {
        super.onBackPressed()
        when (type) {
            "Association" -> overridePendingTransition(R.anim.right_start, R.anim.right_end)
            "Memory" -> overridePendingTransition(R.anim.left_start, R.anim.left_end)
        }
    }

    fun deleteAll(@Suppress("UNUSED_PARAMETER") view: View?) {
        AlertDialog.Builder(this).setTitle("Supprimer")
                .setMessage("Êtes vous sûrs de vouloir supprimer tous les résultats?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("Oui") { _, _ ->
                    resultDAO!!.deleteAll()
                    listResults!!.visibility = View.INVISIBLE
                    Toast.makeText(this@AdminResults, "Les résultats ont été supprimés", Toast.LENGTH_LONG).show()
                }
                .setNegativeButton("Non", null).show()
    }
}