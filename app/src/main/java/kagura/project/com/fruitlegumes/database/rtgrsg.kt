package kagura.project.com.fruitlegumes.database

import android.content.Context
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.BaseAdapter
import android.widget.ImageView

class rtgrsg(private val mContext: Context, private val mThumbIds: Array<Int>, var layoutType: String) : BaseAdapter() {
    override fun getCount(): Int {
        return mThumbIds.size
    }

    override fun getItem(position: Int): Any? {
        return null
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    // create a new ImageView for each item referenced by the Adapter
    override fun getView(position: Int, convertView: View, parent: ViewGroup): View {
        val imageView: ImageView
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = ImageView(mContext)
            Log.i("type", layoutType)
            if (layoutType === "xxhdpi") {
                imageView.layoutParams = AbsListView.LayoutParams(280, 280)
            } else {
                imageView.layoutParams = AbsListView.LayoutParams(280, 280)
            }
            imageView.scaleType = ImageView.ScaleType.CENTER_CROP
            imageView.setPadding(8, 8, 8, 8)
        } else {
            imageView = convertView as ImageView
        }
        imageView.setImageResource(mThumbIds[position])
        return imageView
    }

}