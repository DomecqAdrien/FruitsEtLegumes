package kagura.project.com.a8.database;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

class DAOBase {

    private final static int VERSION = 2;
    private final static String NOM = "results.db";
    private DatabaseHandler mHandler = null;
    SQLiteDatabase mDb = null;

    DAOBase(Context context){
        this.mHandler = new DatabaseHandler(context, NOM, null, VERSION);
    }

    public SQLiteDatabase open(){
        mDb = mHandler.getWritableDatabase();
        return mDb;
    }
}
