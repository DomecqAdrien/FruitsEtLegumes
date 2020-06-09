package kagura.project.com.a8.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase

open class DAOBase(context: Context?) {
    private var mHandler: DatabaseHandler? = null

    @JvmField
    var mDb: SQLiteDatabase? = null
    fun open(): SQLiteDatabase? {
        mDb = mHandler!!.writableDatabase
        return mDb
    }

    companion object {
        private const val VERSION = 2
        private const val NOM = "results.db"
    }

    init {
        mHandler = DatabaseHandler(context, NOM, null, VERSION)
    }
}