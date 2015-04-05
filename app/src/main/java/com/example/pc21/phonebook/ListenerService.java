package com.example.pc21.phonebook;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ListenerService extends Service {
    MyObserver myObserver;
    public ListenerService() {
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(getApplicationContext(), "Service Started", Toast.LENGTH_SHORT).show();
        Log.d("=============Started===============", "=============Started===============");
        myObserver = new MyObserver(new Handler());
        getContentResolver().registerContentObserver(ContactsContract.Contacts.CONTENT_URI, true, myObserver);

        SharedPreferences.Editor editor = getSharedPreferences("Change", MODE_PRIVATE).edit();
        SharedPreferences prefs = getSharedPreferences("change", MODE_PRIVATE);
        String change = prefs.getString("changed", null);
        if (change != null) {
            ContactDBHandler dbHandler = new ContactDBHandler(getApplicationContext());
            List<Contact> contactListDb = dbHandler.getAllContact();
            List<Contact> contactListUri = getContacts();

            for ( int i = 0; i < contactListUri.size(); i++) {
                String name = contactListUri.get(i).getName();
                if (!name.equals(contactListDb.get(i).getName())){
                    Log.d("================", "===============New :" + name);
                    Contact contact = contactListUri.get(i);
                    dbHandler.addContact(contact);
                }
            }
            editor.putString("changed", null);
            editor.commit();

        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getContentResolver().unregisterContentObserver(myObserver);
        Toast.makeText(getApplicationContext(), "Service Destroyed", Toast.LENGTH_SHORT).show();
        Log.d("=============Destroyed===============", "=============Destroyed===============");
    }


    private class MyObserver extends ContentObserver{

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            Toast.makeText(getApplicationContext(), "Changed", Toast.LENGTH_SHORT).show();
            Log.d("=============Changed===============", "=============Changed===============");
            SharedPreferences.Editor editor = getSharedPreferences("Change", MODE_PRIVATE).edit();
            editor.putString("changed", "changed");
            editor.commit();
            SharedPreferences prefs = getSharedPreferences("change", MODE_PRIVATE);
            String change = prefs.getString("changed", null);
            if (change != null) {
                ContactDBHandler dbHandler = new ContactDBHandler(getApplicationContext());
                List<Contact> contactListDb = dbHandler.getAllContact();
                List<Contact> contactListUri = getContacts();

                for ( int i = 0; i < contactListUri.size(); i++) {
                    String name = contactListUri.get(i).getName();
                    if (!name.equals(contactListDb.get(i).getName())){
                        Log.d("================", "===============New :" + name);
                        Contact contact = contactListUri.get(i);
                        dbHandler.addContact(contact);
                    }
                }
                editor.putString("changed", null);
                editor.commit();

            }
        }

        @SuppressLint("NewApi")
        @Override
        public void onChange(boolean selfChange, Uri uri) {
            super.onChange(selfChange, uri);
            Toast.makeText(getApplicationContext(), "Changed", Toast.LENGTH_SHORT).show();
            Log.d("=============Changed===============", "=============Changed===============");
            SharedPreferences.Editor editor = getSharedPreferences("Change", MODE_PRIVATE).edit();
            editor.putString("changed", "changed");
            editor.commit();
            SharedPreferences prefs = getSharedPreferences("change", MODE_PRIVATE);
            String change = prefs.getString("changed", null);
            if (change != null) {
                ContactDBHandler dbHandler = new ContactDBHandler(getApplicationContext());
                List<Contact> contactListDb = dbHandler.getAllContact();
                List<Contact> contactListUri = getContacts();

                for ( int i = 0; i < contactListUri.size(); i++) {
                    String name = contactListUri.get(i).getName();
                    if (!name.equals(contactListDb.get(i).getName())){
                        Log.d("================", "===============New :" + name);
                        Contact contact = contactListUri.get(i);
                        dbHandler.addContact(contact);
                    }
                }
                editor.putString("changed", null);
                editor.commit();

            }
        }

        public MyObserver(Handler handler) {
            super(handler);
        }
    }
    public List<Contact> getContacts() {

        List<Contact> contacts = new ArrayList<Contact>();
        final String[] projection = new String[] { ContactsContract.RawContacts.CONTACT_ID, ContactsContract.RawContacts.DELETED };

        @SuppressWarnings("deprecation")
        final Cursor rawContacts = getApplicationContext().getContentResolver().query(
                ContactsContract.RawContacts.CONTENT_URI,
                projection,
                null,
                null,
                null);

        final int contactIdColumnIndex = rawContacts.getColumnIndex(ContactsContract.RawContacts.CONTACT_ID);
        final int deletedColumnIndex = rawContacts.getColumnIndex(ContactsContract.RawContacts.DELETED);

        if (rawContacts.moveToFirst()) {
            while (!rawContacts.isAfterLast()) {
                final int contactId = rawContacts.getInt(contactIdColumnIndex);
                final boolean deleted = (rawContacts.getInt(deletedColumnIndex) == 1);

                if (!deleted) {
                    Contact contact = new Contact();
                    contact.setId(contactId);
                    contact.setName(getName(contactId));
                    contact.setEmail(getEmail(contactId));
                    contact.setAddress(getAddress(contactId));
                    contact.setPhoneNo(getPhoneNumber(contactId));
                    contact.setImage(getPhoto(contactId) != null ? getPhoto(contactId) : null);

                    contacts.add(contact);
                }
                rawContacts.moveToNext();
            }
        }
        rawContacts.close();
        Collections.sort(contacts, new Comparator<Contact>() {
            @Override
            public int compare(Contact lhs, Contact rhs) {
                Contact map1 = lhs;
                Contact map2 = rhs;
                String s1 = map1.getName();
                String s2 = map2.getName();
                return s1.compareTo(s2);
            }
        });
        return contacts;
    }


    private String getName(int contactId) {
        String name = "";
        final String[] projection = new String[] { ContactsContract.Contacts.DISPLAY_NAME };

        final Cursor contact = getApplicationContext().getContentResolver().query(
                ContactsContract.Contacts.CONTENT_URI,
                projection,
                ContactsContract.Contacts._ID + "=?",
                new String[]{String.valueOf(contactId)},
                null);

        if (contact.moveToFirst()) {
            name = contact.getString(contact.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            contact.close();
        }
        contact.close();
        return name;

    }

    private String getEmail(int contactId) {
        String emailStr = "";
        final String[] projection = new String[] { ContactsContract.CommonDataKinds.Email.DATA,
                ContactsContract.CommonDataKinds.Email.TYPE };

        final Cursor email = getApplicationContext().getContentResolver().query(
                ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                projection,
                ContactsContract.Data.CONTACT_ID + "=?",
                new String[]{String.valueOf(contactId)},
                null);

        if (email.moveToFirst()) {
            final int contactEmailColumnIndex = email.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA);

            while (!email.isAfterLast()) {
                emailStr = emailStr + email.getString(contactEmailColumnIndex) + ";";
                email.moveToNext();
            }
        }
        email.close();
        return emailStr;

    }

    private Bitmap getPhoto(int contactId) {
        Bitmap photo = null;
        final String[] projection = new String[] { ContactsContract.Contacts.PHOTO_ID };

        final Cursor contact = getApplicationContext().getContentResolver().query(
                ContactsContract.Contacts.CONTENT_URI, projection,
                ContactsContract.Contacts._ID + "=?",
                new String[]{String.valueOf(contactId)},
                null);

        if (contact.moveToFirst()) {
            final String photoId = contact.getString(contact.getColumnIndex(ContactsContract.Contacts.PHOTO_ID));
            if (photoId != null) {
                photo = getBitmap(photoId);
            } else {
                photo = null;
            }
        }
        contact.close();

        return photo;
    }

    private Bitmap getBitmap(String photoId) {
        final Cursor photo = getApplicationContext().getContentResolver().query(
                ContactsContract.Data.CONTENT_URI,
                new String[]{ContactsContract.CommonDataKinds.Photo.PHOTO},
                ContactsContract.Contacts.Data._ID + "=?",
                new String[]{photoId},
                null);

        final Bitmap photoBitmap;
        if (photo.moveToFirst()) {
            byte[] photoBlob = photo.getBlob(photo.getColumnIndex(ContactsContract.CommonDataKinds.Photo.PHOTO));
            photoBitmap = BitmapFactory.decodeByteArray(photoBlob, 0, photoBlob.length);
        } else {
            photoBitmap = null;
        }
        photo.close();
        return photoBitmap;
    }

    private String getAddress(int contactId) {

        String postalData = "";
        String addrWhere = ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
        String[] addrWhereParams = new String[] {
                String.valueOf(contactId),
                ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE };

        Cursor addrCur = getApplicationContext().getContentResolver().query(ContactsContract.Data.CONTENT_URI, null, addrWhere, addrWhereParams, null);

        if (addrCur.moveToFirst()) {
            postalData = addrCur.getString(addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.FORMATTED_ADDRESS));
        }
        addrCur.close();
        return postalData;
    }

    private String getPhoneNumber(int contactId) {

        String phoneNumber = "";
        final String[] projection = new String[] {
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone.TYPE };
        final Cursor phone = getApplicationContext().getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                projection,
                ContactsContract.Data.CONTACT_ID + "=?",
                new String[]{String.valueOf(contactId)},
                null);

        if (phone.moveToFirst()) {
            final int contactNumberColumnIndex = phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);

            while (!phone.isAfterLast()) {
                phoneNumber = phoneNumber + phone.getString(contactNumberColumnIndex);
                phone.moveToNext();
            }

        }
        phone.close();
        return phoneNumber;
    }
}
