package mbds;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class ContactHelper extends SQLiteOpenHelper
{
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + Database.ContactContact.FeedContact.TABLE_NAME + " (" +
                    Database.ContactContact.FeedContact._ID + " INTEGER PRIMARY KEY," +
                    Database.ContactContact.FeedContact.COLUMN_NAME_LASTNAME + " TEXT,";


    private static final String SQL_CREATE_BDD_USER =
            "CREATE TABLE " + Database.User.FeedUser.TABLE_NAME + " (" +
                    Database.User.FeedUser._ID + " INTEGER PRIMARY KEY," +
                    Database.User.FeedUser.COLUMN_NAME_LOGIN + " TEXT," +
                    Database.User.FeedUser.COLUMN_NAME_PASSWORD + " TEXT)" ;


    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + Database.ContactContact.FeedContact.TABLE_NAME;

    private static final String SQL_DELETE_BDD_USER =
            "DROP TABLE IF EXISTS " + Database.User.FeedUser.TABLE_NAME;

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "ContactDb.db";

    public ContactHelper(Context context)
    {super(context, DATABASE_NAME, null, DATABASE_VERSION);}

    public void onCreate(SQLiteDatabase db) {

        db.execSQL(SQL_CREATE_ENTRIES);
        db.execSQL(SQL_CREATE_BDD_USER);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        db.execSQL(SQL_DELETE_BDD_USER);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
