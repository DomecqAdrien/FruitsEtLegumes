package kagura.project.com.fruitlegumes

import android.content.Context
import android.util.Log
import java.io.IOException
import java.nio.charset.Charset

object LoadJson {
    @JvmStatic
    fun loadJSONFromAsset(c: Context, file: String): String? {
        val json: String
        try {
            val `is` = c.assets.open("$file.json")
            val size = `is`.available()
            val buffer = ByteArray(size)
            `is`.read(buffer)
            `is`.close()
            json = String(buffer, Charset.forName("UTF-8"))
            Log.i("json", json)
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }
        return json
    }
}