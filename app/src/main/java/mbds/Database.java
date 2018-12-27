package mbds;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import java.util.ArrayList;
import java.util.List;

public class Database {

    public final class User {
        private User() {}
        public class FeedUser implements BaseColumns {
            public static final String TABLE_NAME = "User";
            public static final String COLUMN_NAME_LOGIN = "login";
            public static final String COLUMN_NAME_PASSWORD = "Password";
        }
    }


    public final class ContactContact {
        private ContactContact() {}
        public class FeedContact implements BaseColumns {
            public static final String TABLE_NAME = "Contact";
            public static final String COLUMN_NAME_LASTNAME = "Nom";
            public static final String COLUMN_NAME_FIRSTNAME = "Prenom";
        }
    }

    private static Database idatabase;
    private ContactHelper mDbHelper;

    private Database(ContactHelper sqlp){
        mDbHelper = sqlp;
    }

    public static Database getIstance(Context ctxt){
        if(idatabase==null) {
            idatabase =new Database(new ContactHelper(ctxt));
        }
        return idatabase;
    }

    public void addUser(String name, String lname) {
        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(ContactContact.FeedContact.COLUMN_NAME_LASTNAME, name);
        values.put(ContactContact.FeedContact.COLUMN_NAME_FIRSTNAME, lname);

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(ContactContact.FeedContact.TABLE_NAME, null, values);
    }

    public void addPerson(String login, String password) {
        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(User.FeedUser.COLUMN_NAME_LOGIN, login);
        values.put(User.FeedUser.COLUMN_NAME_PASSWORD, password);

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(User.FeedUser.TABLE_NAME, null, values);
    }

    public List<Person> readPerson() {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String[] projection = {
                BaseColumns._ID,
                User.FeedUser.COLUMN_NAME_LOGIN,
                User.FeedUser.COLUMN_NAME_PASSWORD
        };
        String selection = "";
        String[] selectionArgs = null;
        String sortOrder = User.FeedUser.COLUMN_NAME_PASSWORD + " DESC";
        Cursor cursor = db.query(
                User.FeedUser.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,          // don't group the rows
                null,           // don't filter by row groups
                sortOrder               // The sort order
        );

        List persons = new ArrayList<Person>();
        while(cursor.moveToNext())
        {
            long itemId = cursor.getLong(cursor.getColumnIndexOrThrow(User.FeedUser._ID));
            String nom = cursor.getString(cursor.getColumnIndex(User.FeedUser.COLUMN_NAME_LOGIN));
            String prenom = cursor.getString(cursor.getColumnIndex(User.FeedUser.COLUMN_NAME_PASSWORD));
            persons.add(new Person(nom,prenom));
        }
        cursor.close();
        return persons;
    }

/*
       public List<Person> readPerson() {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String[] projection = {
                BaseColumns._ID,
                ContactContact.FeedContact.COLUMN_NAME_LASTNAME,
                ContactContact.FeedContact.COLUMN_NAME_FIRSTNAME
        };
        String selection = "";
        String[] selectionArgs = null;
        String sortOrder = ContactContact.FeedContact.COLUMN_NAME_LASTNAME + " DESC";
        Cursor cursor = db.query(
                ContactContact.FeedContact.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,          // don't group the rows
                null,           // don't filter by row groups
                sortOrder               // The sort order
        );

        List persons = new ArrayList<Person>();
        while(cursor.moveToNext())
        {
            long itemId = cursor.getLong(cursor.getColumnIndexOrThrow(ContactContact.FeedContact._ID));
            String nom = cursor.getString(cursor.getColumnIndex(ContactContact.FeedContact.COLUMN_NAME_LASTNAME));
            String prenom = cursor.getString(cursor.getColumnIndex(ContactContact.FeedContact.COLUMN_NAME_FIRSTNAME));
            persons.add(new Person(nom,prenom));
        }
        cursor.close();
        return persons;
    }
     */

}

