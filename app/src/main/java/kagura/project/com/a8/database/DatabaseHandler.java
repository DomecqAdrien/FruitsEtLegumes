package kagura.project.com.a8.database;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


class DatabaseHandler extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "results.db";

    private static final String TABLE_NAME = "results";
    private static final String COLUMN_NAME_KEY = "id";
    private static final String COLUMN_NAME_NAME = "name";
    private static final String COLUMN_NAME_GAME = "game";
    private static final String COLUMN_NAME_LEVEL = "level";
    private static final String COLUMN_NAME_TRIES = "tries";
    private static final String COLUMN_NAME_TIME = "time";
    private static final String COLUMN_NAME_DATE = "date";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_NAME_KEY + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_NAME_NAME + " TEXT," +
                    COLUMN_NAME_GAME + " TEXT," +
                    COLUMN_NAME_LEVEL + " INT," +
                    COLUMN_NAME_TRIES + " INT," +
                    COLUMN_NAME_TIME + " REAL," +
                    COLUMN_NAME_DATE + " TEXT);";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME + ";" + SQL_CREATE_ENTRIES ;
    
    public DatabaseHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }


}
