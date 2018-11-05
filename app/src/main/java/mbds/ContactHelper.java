package mbds;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ContactHelper extends SQLiteOpenHelper
{
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + Database.ContactContact.FeedContact.TABLE_NAME + " (" +
                    Database.ContactContact.FeedContact._ID + " INTEGER PRIMARY KEY," +
                    Database.ContactContact.FeedContact.COLUMN_NAME_LASTNAME + " TEXT," +
                    Database.ContactContact.FeedContact.COLUMN_NAME_FIRSTNAME + " TEXT)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + Database.ContactContact.FeedContact.TABLE_NAME;
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "ContactDb.db";

    public ContactHelper(Context context)
    {super(context, DATABASE_NAME, null, DATABASE_VERSION);}
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}