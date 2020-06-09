package kagura.project.com.a8.association

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import kagura.project.com.a8.R

class ResultFragment : Fragment() {
    var level = 0
    private var buttonNextLevel: ImageView? = null
    private var buttonLevelSelection: ImageView? = null
    private var buttonHome: ImageView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        level = this.arguments!!.getInt("level")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val rl = inflater.inflate(R.layout.fragment_result_game, container, false) as RelativeLayout
        buttonNextLevel = rl.findViewById<View>(R.id.buttonNextLevel) as ImageView
        buttonNextLevel!!.setImageDrawable(resources.getDrawable(R.drawable.next1))
        if (level == 3) {
            buttonNextLevel!!.isEnabled = false
            buttonNextLevel!!.setImageDrawable(resources.getDrawable(R.drawable.nextgrey))
        }
        buttonLevelSelection = rl.findViewById<View>(R.id.buttonLevelSelection) as ImageView
        buttonLevelSelection!!.setOnClickListener {
            activity!!.finish()
            Log.i("nameActivity", activity!!.javaClass.simpleName)
            when (activity!!.javaClass.simpleName) {
                "SemantiqueGame" -> activity!!.overridePendingTransition(R.anim.right_start, R.anim.right_end)
                "MemoryGame" -> activity!!.overridePendingTransition(R.anim.left_start, R.anim.left_end)
            }
        }
        buttonHome = rl.findViewById(R.id.buttonHome)
        buttonHome?.setOnClickListener {
            val intentHomeMenu = Intent()
            activity!!.setResult(66, intentHomeMenu)
            activity!!.finish()
        }
        return rl
    }
}