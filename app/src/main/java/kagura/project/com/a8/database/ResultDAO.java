package kagura.project.com.a8.database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import kagura.project.com.a8.objects.Result;


public class ResultDAO extends DAOBase {
    private static final String TABLE_NAME = "results";
    private static final String COLUMN_NAME_KEY = "id";
    private static final String COLUMN_NAME_NAME = "name";
    private static final String COLUMN_NAME_GAME = "game";
    private static final String COLUMN_NAME_LEVEL = "level";
    private static final String COLUMN_NAME_TRIES = "tries";
    private static final String COLUMN_NAME_TIME = "time";
    private static final String COLUMN_NAME_DATE =  "date";
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
            "DROP TABLE IF EXISTS " + TABLE_NAME + ";" + SQL_CREATE_ENTRIES + ";";

    public ResultDAO(Context context) {
        super(context);
    }

    public void add(Result result){
        ContentValues values = new ContentValues();
        values.put(ResultDAO.COLUMN_NAME_NAME, result.getName());
        values.put(ResultDAO.COLUMN_NAME_GAME, result.getGame());
        values.put(ResultDAO.COLUMN_NAME_LEVEL, result.getLevel());
        values.put(ResultDAO.COLUMN_NAME_TRIES, result.getTries());
        values.put(ResultDAO.COLUMN_NAME_TIME, result.getTime());
        values.put(ResultDAO.COLUMN_NAME_DATE, result.getDate());
        open();
        mDb.insert(ResultDAO.TABLE_NAME, null, values);
    }

    public void delete(int id){
        open();
        mDb.delete(TABLE_NAME, COLUMN_NAME_KEY + " = ?", new String[] {String.valueOf(id)} );
    }

    public void deleteAll(){
        open();
        mDb.delete("results", null, null);
    }

    public void modifier(Result result){
        open();
    }

    public List<Result> selectAll(){
        open();
        List<Result> results = new ArrayList<>();
        Cursor cursor = mDb.rawQuery("SELECT * FROM "+ TABLE_NAME, new String[] {});
        int i = 0;
        while (cursor.moveToNext()){
            Result result = new Result();
            result.setName(cursor.getString(1));
            result.setGame(cursor.getString(2));
            result.setLevel(cursor.getInt(3));
            result.setTries(cursor.getInt(4));
            result.setTime(cursor.getLong(5));
            result.setDate(cursor.getString(6));
            results.add(i, result);
        }
        cursor.close();
        return results;
    }

    public List<Result> selectByName(String name){
        open();
        return null;
    }
}

