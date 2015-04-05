package com.example.pc21.phonebook;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PC21 on 21/3/2015.
 */
public class ContactDBHandler extends SQLiteOpenHelper {
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String CONTACT_DB = "contactDB";

    private static final String CONTACT = "tbl_contact";

    public static final String ID = "_id";
    public static final String CONTACT_NAME = "name";
    public static final String CONTACT_PHONENO = "phoneNo";
    public static final String CONTACT_EMAIL = "email";
    public static final String CONTACT_ADDRESS = "address";
    public static final String CONTACT_BIRTHDAY = "birthday";
    public static final String CONTACT_ANNIVERSARY = "anniversary";
    public static final String CONTACT_IMAGE = "image";

    public static final String[] COLUMNS = new String[]{
            ID, CONTACT_NAME, CONTACT_PHONENO, CONTACT_EMAIL, CONTACT_ADDRESS
    };


    public ContactDBHandler(Context context) {
        super(context, CONTACT_DB, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_CONTACT_TABLE = "CREATE TABLE " + CONTACT + "("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + CONTACT_NAME + " TEXT,"
                + CONTACT_PHONENO + " TEXT," + CONTACT_EMAIL + " TEXT, "
                + CONTACT_ADDRESS + " TEXT, " + CONTACT_BIRTHDAY + " TEXT,"
                + CONTACT_ANNIVERSARY + " TEXT," + CONTACT_IMAGE + " TEXT" + ")";
        db.execSQL(SQL_CREATE_CONTACT_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + CONTACT);
        // Create tables again
        onCreate(db);

    }

    void addContact(Contact aContact) {
        SQLiteDatabase db = super.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CONTACT_NAME, aContact.getName());
        values.put(CONTACT_PHONENO, aContact.getPhoneNo()== null ? "" : aContact.getPhoneNo());
        values.put(CONTACT_EMAIL, aContact.getEmail()== null ? "":  aContact.getEmail());
        values.put(CONTACT_ADDRESS, aContact.getAddress()== null ? "": aContact.getAddress());
        values.put(CONTACT_BIRTHDAY, "");
        values.put(CONTACT_ANNIVERSARY, "");
        values.put(CONTACT_IMAGE, "");

       /* Log.d("=========== Name ==========", "=====================" + aContact.getName());
        Log.d("=========== Contact ==========", "=====================" + aContact.getPhoneNo());
        Log.d("=========== Email ==========", "=====================" + aContact.getEmail());
        Log.d("=========== Address ==========", "=====================" + aContact.getAddress());*/
        // Inserting Row
        long result = db.insert(CONTACT, null, values);
        Log.d("=========== Name ==========", "=====================" + result);

        db.close(); // Closing database connection
    }

    public Cursor getAllCursorContact(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor;

        cursor = db.query(CONTACT, COLUMNS, null, null, null, null, null);
        if (cursor != null){
            cursor.moveToFirst();
        }
        db.close();
        return cursor;
    }

    public List<Contact> getAllContact() {
        List<Contact> contactList = new ArrayList<Contact>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + CONTACT;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Contact contact = new Contact();
                contact.setId(Integer.parseInt(cursor.getString(0)));
                contact.setName(cursor.getString(1));
                contact.setPhoneNo(cursor.getString(2));
                contact.setEmail(cursor.getString(3));
                contact.setAddress(cursor.getString(4));
                contactList.add(contact);
            } while (cursor.moveToNext());
        }
        db.close();
        return contactList;
    }

    public Contact getContactByID(int id) {
        String selectQuery = "SELECT  * FROM " + CONTACT + " where _id = " + id;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        Contact contact = new Contact();

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
                contact.setId(Integer.parseInt(cursor.getString(0)));
                contact.setName(cursor.getString(1));
                contact.setPhoneNo(cursor.getString(2));
                contact.setEmail(cursor.getString(3));
                contact.setAddress(cursor.getString(4));
        }
        db.close();
        return contact;
    }


    public int updateContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CONTACT_NAME, contact.getName());
        values.put(CONTACT_PHONENO, contact.getPhoneNo());
        values.put(CONTACT_EMAIL, contact.getEmail());
        values.put(CONTACT_ADDRESS, contact.getAddress());
        values.put(CONTACT_BIRTHDAY, "");
        values.put(CONTACT_ANNIVERSARY, "");
        values.put(CONTACT_IMAGE, "");


        return db.update(CONTACT, values, ID + " = ?",
                new String[]{String.valueOf(contact.getId())});
    }

    public String viewAll() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM tbl_contact", null);
        // Checking if no records found
        if (c.getCount() == 0) {
            // showMsg("No Data");
            return "No Data";
        }
        // Appending records to a string buffer
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()) {
            buffer.append("RegNo: " + c.getString(0) + "\n");
            buffer.append("Name: " + c.getString(1) + "\n");
            buffer.append("email: " + c.getString(2) + "\n\n");
        }
        // Displaying all records
        return buffer.toString();

    }
    void deleteContact(int id){
        SQLiteDatabase db=this.getWritableDatabase();
        db.delete(CONTACT, ID +"= ?", new String[] {String.valueOf(id)});
        db.close();
    }
}

