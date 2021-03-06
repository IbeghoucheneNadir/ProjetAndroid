package mbds;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;
import android.util.Pair;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import mbds.api.Message;
import mbds.api.MessageEntry;

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
            public static final String COLUMN_NAME_USERID = "UserID";
        }
    }

    public final class Message {
        private Message() {}
        public class FeedMessage implements BaseColumns {
            public static final String TABLE_NAME = "Message";
            public static final String COLUMN_NAME_ID = "Id";
            public static final String COLUMN_NAME_AUTHOR = "Author";
            public static final String COLUMN_NAME_RECIPIENT = "Recipient";
            public static final String COLUMN_NAME_CONTACT = "Contact";
            public static final String COLUMN_NAME_MESSSAGE = "Message";
            public static final String COLUMN_NAME_DATE = "Date";
            public static final String COLUMN_NAME_USERID = "userID";
        }
    }

    private static Database idatabase;
    private final ContactHelper mDbHelper;

    private Database(ContactHelper sqlp){ mDbHelper = sqlp; }

    public static Database getIstance(Context ctxt){
        if(idatabase==null) {
            idatabase =new Database(new ContactHelper(ctxt));
        }
        return idatabase;
    }

    public void addPerson(String name, long userID) {
        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(ContactContact.FeedContact.COLUMN_NAME_LASTNAME, name);
        values.put(ContactContact.FeedContact.COLUMN_NAME_USERID, userID);

        // Insert the new row, returning the primary key value of the new row
        db.insert(ContactContact.FeedContact.TABLE_NAME, null, values);
    }

    public void addMessage(String author, String recipient, String contact, String message, String date , long userID){
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Message.FeedMessage.COLUMN_NAME_AUTHOR, author);
        values.put(Message.FeedMessage.COLUMN_NAME_RECIPIENT, recipient);
        values.put(Message.FeedMessage.COLUMN_NAME_CONTACT, contact);
        values.put(Message.FeedMessage.COLUMN_NAME_MESSSAGE, message);
        values.put(Message.FeedMessage.COLUMN_NAME_DATE, date);
        values.put(Message.FeedMessage.COLUMN_NAME_USERID, userID);
        // Insert the new row, returning the primary key value of the new row
        db.insert(Message.FeedMessage.TABLE_NAME, null, values);
    }

    public List<MessageEntry> readMessages(String name, long userID){
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String[] projection = {
                Message.FeedMessage.COLUMN_NAME_ID,
                Message.FeedMessage.COLUMN_NAME_AUTHOR,
                Message.FeedMessage.COLUMN_NAME_RECIPIENT,
                Message.FeedMessage.COLUMN_NAME_CONTACT,
                Message.FeedMessage.COLUMN_NAME_MESSSAGE,
                Message.FeedMessage.COLUMN_NAME_DATE
        };
        String selection = Message.FeedMessage.COLUMN_NAME_CONTACT + " LIKE '" + name + "' " +" AND " +
                Message.FeedMessage.COLUMN_NAME_USERID + " LIKE '" + userID + "'";
        String[] selectionArgs = null;
        String sortOrder = "date(" + Message.FeedMessage.COLUMN_NAME_DATE + ") DESC";
        Cursor cursor = db.query(
                Message.FeedMessage.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,          // don't group the rows
                null,           // don't filter by row groups
                sortOrder               // The sort order
        );
        List<MessageEntry> messages = new ArrayList<>();
        while(cursor.moveToNext())
        {
            long id = cursor.getLong(cursor.getColumnIndex(Message.FeedMessage.COLUMN_NAME_ID));
            String author = cursor.getString(cursor.getColumnIndex(Message.FeedMessage.COLUMN_NAME_AUTHOR));
            String recipient = cursor.getString(cursor.getColumnIndex(Message.FeedMessage.COLUMN_NAME_RECIPIENT));
            String contact = cursor.getString(cursor.getColumnIndex(Message.FeedMessage.COLUMN_NAME_CONTACT));
            String message = cursor.getString(cursor.getColumnIndex(Message.FeedMessage.COLUMN_NAME_MESSSAGE));
            String date = cursor.getString(cursor.getColumnIndex(Message.FeedMessage.COLUMN_NAME_DATE));
            messages.add(new MessageEntry((int) id, author, recipient, contact, message, date, userID));
        }
        cursor.close();
        return messages;
    }




    public void removePerson(String name, long userID) {
        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Insert the new row, returning the primary key value of the new row
        db.delete(ContactContact.FeedContact.TABLE_NAME,
                ContactContact.FeedContact.COLUMN_NAME_LASTNAME + " LIKE '" + name + "' AND " +
                        ContactContact.FeedContact.COLUMN_NAME_USERID + " LIKE '" + userID + "'", null);
        String selection = Message.FeedMessage.COLUMN_NAME_CONTACT + " LIKE '" + name + "' " +" AND " +
                Message.FeedMessage.COLUMN_NAME_USERID + " LIKE '" + userID + "'";
        db.delete(Message.FeedMessage.TABLE_NAME, selection, null);
    }


    public void addUser(String login, String password) {
        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(User.FeedUser.COLUMN_NAME_LOGIN, login);
        values.put(User.FeedUser.COLUMN_NAME_PASSWORD, password);

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(User.FeedUser.TABLE_NAME, null, values);
    }

    public int checkUser(String login, String password)
    {
        int id=-1;
        SQLiteDatabase db=mDbHelper.getReadableDatabase();
        Cursor cursor=db.rawQuery("SELECT password FROM user WHERE login=? AND password=?" , new String[]{login,password});
        if(cursor.getCount()>0) {
            cursor.moveToFirst();
            id=cursor.getInt(0);
            cursor.close();
        }
        return id;
    }

    public List<Person> readUser() {
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
        List<Person> persons = new ArrayList<>();
        while(cursor.moveToNext())
        {
            long itemId = cursor.getLong(cursor.getColumnIndexOrThrow(User.FeedUser._ID));
            String nom = cursor.getString(cursor.getColumnIndex(User.FeedUser.COLUMN_NAME_LOGIN));
            Log.i("DATABASE", nom);
            String password = cursor.getString(cursor.getColumnIndex(User.FeedUser.COLUMN_NAME_PASSWORD));
            persons.add(new Person(nom,password));
        }
        cursor.close();
        return persons;
    }

    public long readUserID(String userLogin) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String[] projection = {
                BaseColumns._ID,
                User.FeedUser.COLUMN_NAME_LOGIN,
                User.FeedUser.COLUMN_NAME_PASSWORD
        };
        String selection = User.FeedUser.COLUMN_NAME_LOGIN + " LIKE '" + userLogin + "'";
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
        cursor.moveToNext();
        long itemId = cursor.getLong(cursor.getColumnIndexOrThrow(User.FeedUser._ID));
        cursor.close();
        return itemId;
    }


    public String getLogin(long userID){
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String[] projection = {
                User.FeedUser.COLUMN_NAME_LOGIN
        };
        String selection = User.FeedUser._ID + " LIKE '" + userID + "'";
        String[] selectionArgs = null;
        String sortOrder = "";
        Cursor cursor = db.query(
                User.FeedUser.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,          // don't group the rows
                null,           // don't filter by row groups
                sortOrder               // The sort order
        );
        cursor.moveToNext();
        String login = cursor.getString(cursor.getColumnIndexOrThrow(User.FeedUser.COLUMN_NAME_LOGIN));
        cursor.close();
        return login;
    }

    public List<Person> readPerson(long userID) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String[] projection = {
                BaseColumns._ID,
                ContactContact.FeedContact.COLUMN_NAME_LASTNAME,
                ContactContact.FeedContact.COLUMN_NAME_USERID
        };
        String selection = ContactContact.FeedContact.COLUMN_NAME_USERID + " LIKE '" + userID + "'";
        String[] selectionArgs = null;
        String sortOrder = ContactContact.FeedContact.COLUMN_NAME_LASTNAME + " ASC";
        Cursor cursor = db.query(
                ContactContact.FeedContact.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,          // don't group the rows
                null,           // don't filter by row groups
                sortOrder               // The sort order
        );

        List<Person> persons = new ArrayList<>();
        while(cursor.moveToNext())
        {
            long itemId = cursor.getLong(cursor.getColumnIndexOrThrow(ContactContact.FeedContact._ID));
            String nom = cursor.getString(cursor.getColumnIndex(ContactContact.FeedContact.COLUMN_NAME_LASTNAME));
            persons.add(new Person(nom));
        }
        cursor.close();
        return persons;
    }
}

