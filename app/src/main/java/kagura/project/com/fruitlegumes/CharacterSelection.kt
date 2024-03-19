package kagura.project.com.fruitlegumes

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kagura.project.com.fruitlegumes.admin.AdminSession
import java.util.Locale

class CharacterSelection : AppCompatActivity() {
    var name: EditText? = null
    private var nameValid: String? = null
    var avatar: String? = null
    private var verMince: ImageView? = null
    private var verGros: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        window.requestFeature(Window.FEATURE_ACTION_BAR)
        supportActionBar!!.hide()
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)

        //SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        //nameValid = preferences.getString(getString(R.string.name), null);
        //avatar = preferences.getString(getString(R.string.avatar), null);
        setContentView(R.layout.activity_character_selection)
        name = findViewById<View>(R.id.name) as EditText
        verMince = findViewById<View>(R.id.buttonVerMince) as ImageView
        verGros = findViewById<View>(R.id.buttonVerGros) as ImageView
        verGros!!.setImageDrawable(getDrawable(R.drawable.ver_gros))
        verMince!!.setImageDrawable(getDrawable(R.drawable.ver_mince))

        /*if(nameValid != null){
            name.append(nameValid);
        }
        if(avatar != null){
            switch (avatar){
                case "verMince":
                    verMince.setBackgroundDrawable(getResources().getDrawable(R.drawable.ver_mince_aura));
                    break;
                case "verGros":
                    verGros.setBackgroundDrawable(getResources().getDrawable(R.drawable.ver_gros_aura));
                    break;
            }
        }*/
    }

    override fun onBackPressed() {
        AlertDialog.Builder(this).setTitle("Quitter")
                .setMessage("Êtes vous sûrs de vouloir quitter l'application ?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("Oui") { _, _ -> finish() }
                .setNegativeButton("Non", null).show()
    }

    fun characterOnClick(view: View) {
        when (view.id) {
            R.id.buttonVerMince -> {
                avatar = "verMince"
                verGros!!.setImageDrawable(getDrawable(R.drawable.ver_gros))
                verMince!!.setImageDrawable(getDrawable(R.drawable.ver_mince_aura))
            }
            R.id.buttonVerGros -> {
                avatar = "verGros"
                verMince!!.setImageDrawable(getDrawable(R.drawable.ver_mince))
                verGros!!.setImageDrawable(getDrawable(R.drawable.ver_gros_aura))
            }
        }
    }

    fun start(@Suppress("UNUSED_PARAMETER") v: View?) {
        nameValid = name!!.text.toString()
        when {
            nameValid == "" -> {
                Toast.makeText(this, "Tu n'as pas rentré de prénom", Toast.LENGTH_LONG).show()
            }
            avatar == null -> {
                Toast.makeText(this, "Tu n'as pas choisi d\'avatar", Toast.LENGTH_LONG).show()
            }
            else -> {
                nameValid = nameValid!!.substring(0, 1).toUpperCase(Locale.ROOT) + nameValid!!.substring(1)
                //val prefs = PreferenceManager.getDefaultSharedPreferences(applicationContext)
                //val editor = prefs.edit()
                //editor.putString(getString(R.string.name), nameValid)
                //editor.putString(getString(R.string.avatar), avatar)
                //editor.apply()
                val intent = Intent(this, MainMenu::class.java).apply {
                    putExtra(getString(R.string.name), nameValid)
                    putExtra("followers", avatar)
                }
                startActivity(intent)
                //this.startActivityForResult(intent, 0)
                overridePendingTransition(R.anim.down_start, R.anim.down_end)
            }
        }
    }

    fun adminSession(@Suppress("UNUSED_PARAMETER") view: View?) {
        intent = Intent(this, AdminSession::class.java)
        this.startActivityForResult(intent, 0)
        overridePendingTransition(R.anim.up_start, R.anim.up_end)
    }
}