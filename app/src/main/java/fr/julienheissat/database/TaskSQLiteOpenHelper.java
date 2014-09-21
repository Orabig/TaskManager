package fr.julienheissat.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by juju on 16/09/2014.
 */
public class TaskSQLiteOpenHelper extends SQLiteOpenHelper
{

    public static final String TASK_DB_SQLITE = "task_db.sqlite";
    public static final int VERSION = 1;
    public static final String TASK_TABLE = "tasks";
    public static final String TASK_ID = "id";
    public static final String TASK_NAME = "name";
    public static final String TASK_COMPLETE = "complete";
    public static final String TASK_ADDRESS = "address";
    public static final String TASK_LATITUDE = "latitude";
    public static final String TASK_LONGITUDE = "longitude";
    public static final String TASK_PROJECT = "project";
    public static final String TASK_PRIORITY = "priority";
    public static final String TASK_DATE = "date";



    public TaskSQLiteOpenHelper(Context context)
    {
        super(context, TASK_DB_SQLITE, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        createTable(sqLiteDatabase);
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i2)
    {
//        db.execSQL("alter table "+ TASK_TABLE+" add column "+TASK_ADDRESS+" text");
//        db.execSQL("alter table "+ TASK_TABLE+" add column "+TASK_LATITUDE+" integer");
//        db.execSQL("alter table "+ TASK_TABLE+" add column "+TASK_LONGITUDE+" integer");
    }

    private void createTable(SQLiteDatabase db)
    {
        db.execSQL(
            "create table " + TASK_TABLE +" ("+
                    TASK_ID+" integer primary key autoincrement not null, " +
                    TASK_NAME+" text, "+
                    TASK_COMPLETE+" text, "+
                    TASK_ADDRESS+" text, "+
                    TASK_LATITUDE+" integer, "+
                    TASK_LONGITUDE+" integer, "+
                    TASK_PROJECT+" text, "+
                    TASK_PRIORITY+" text, "+
                    TASK_DATE+" bigint "+
                    ");"
    );



    }
}
