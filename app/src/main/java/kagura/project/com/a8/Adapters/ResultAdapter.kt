package kagura.project.com.a8.adapters

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.text.HtmlCompat
import kagura.project.com.a8.R
import kagura.project.com.a8.collections.Result

class ResultAdapter(private val activity: Activity, private val row: Int, private val items: List<Result>?) : ArrayAdapter<Result?>(activity, row, items!!) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        val holder: ViewHolder
        if (view == null) {
            val inflater = activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(row, null)
            holder = ViewHolder()
            view.tag = holder
        } else {
            holder = view.tag as ViewHolder
        }
        if (items == null || position + 1 > items.size) return view!!
        val result = items[position]
        holder.name = view!!.findViewById<View>(R.id.name) as TextView
        holder.game = view.findViewById<View>(R.id.game) as TextView
        holder.level = view.findViewById<View>(R.id.level) as TextView
        holder.tries = view.findViewById<View>(R.id.tries) as TextView
        holder.time = view.findViewById<View>(R.id.time) as TextView
        holder.date = view.findViewById<View>(R.id.date) as TextView
        if (holder.name != null && null != result.name && result.name!!.trim { it <= ' ' }.isNotEmpty()) {
            holder.name!!.text = HtmlCompat.fromHtml(result.name!!, HtmlCompat.FROM_HTML_MODE_LEGACY)
        }
        holder.game!!.text = HtmlCompat.fromHtml(result.game!!, HtmlCompat.FROM_HTML_MODE_LEGACY)
        holder.level!!.text = HtmlCompat.fromHtml(result.level.toString(), HtmlCompat.FROM_HTML_MODE_LEGACY)
        holder.tries!!.text = HtmlCompat.fromHtml(result.tries.toString(), HtmlCompat.FROM_HTML_MODE_LEGACY)
        holder.time!!.text = HtmlCompat.fromHtml(result.time.toString() + " s", HtmlCompat.FROM_HTML_MODE_LEGACY)
        holder.date!!.text = HtmlCompat.fromHtml(result.date!!, HtmlCompat.FROM_HTML_MODE_LEGACY)
        return view
    }

    private inner class ViewHolder {
        var name: TextView? = null
        var game: TextView? = null
        var level: TextView? = null
        var tries: TextView? = null
        var time: TextView? = null
        var date: TextView? = null
    }

}