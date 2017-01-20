package kagura.project.com.a8.database;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class DAOBase {

    protected final static int VERSION = 2;
    protected final static String NOM = "results.db";

    protected SQLiteDatabase mDb = null;
    protected DatabaseHandler mHandler = null;

    public DAOBase(Context context){
        this.mHandler = new DatabaseHandler(context, NOM, null, VERSION);
    }

    public SQLiteDatabase open(){
        mDb = mHandler.getWritableDatabase();
        return mDb;
    }

    public void close(){
        mDb.close();
    }

    public SQLiteDatabase getDb(){
        return mDb;
    }
}
