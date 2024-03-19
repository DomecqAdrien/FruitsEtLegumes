package kagura.project.com.fruitlegumes.database

import android.content.ContentValues
import android.content.Context
import kagura.project.com.fruitlegumes.collections.Result
import java.util.*

class ResultDAO(context: Context?) : DAOBase(context) {
    fun add(result: Result) {
        val values = ContentValues()
        values.put(COLUMN_NAME_NAME, result.name)
        values.put(COLUMN_NAME_GAME, result.game)
        values.put(COLUMN_NAME_LEVEL, result.level)
        values.put(COLUMN_NAME_TRIES, result.tries)
        values.put(COLUMN_NAME_TIME, result.time)
        values.put(COLUMN_NAME_DATE, result.date)
        open()
        mDb!!.insert(TABLE_NAME, null, values)
    }

    fun deleteAll() {
        open()
        mDb!!.delete("results", null, null)
    }

    fun selectAll(type: String): List<Result> {
        open()
        val results: ArrayList<Result> = ArrayList()
        val cursor = mDb!!.rawQuery("SELECT * FROM $TABLE_NAME WHERE game = ?", arrayOf(type))
        val i = 0
        while (cursor.moveToNext()) {
            val result = Result()
            result.name = cursor.getString(1)
            result.game = cursor.getString(2)
            result.level = cursor.getInt(3)
            result.tries = cursor.getInt(4)
            result.time = cursor.getLong(5)
            result.date = cursor.getString(6)
            results.add(i, result)
        }
        cursor.close()
        return results
    }

    companion object {
        private const val TABLE_NAME = "results"
        private const val COLUMN_NAME_NAME = "name"
        private const val COLUMN_NAME_GAME = "game"
        private const val COLUMN_NAME_LEVEL = "level"
        private const val COLUMN_NAME_TRIES = "tries"
        private const val COLUMN_NAME_TIME = "time"
        private const val COLUMN_NAME_DATE = "date"
    }
}