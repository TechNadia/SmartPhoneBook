package com.example.pc21.phonebook;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class PhoneContactFragment extends Fragment{

    String phoneNo;
    ListView mContactsList;
    ArrayList<Contact> contactArrayList;
    CustomAdapter adapter;
    Contact contact;
    ProgressDialog progressDialog;
    ContactDBHandler contactDBHandler;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contacts, container, false);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        contactDBHandler = new ContactDBHandler(getActivity());
        List<Contact> contacts = contactDBHandler.getAllContact();
        if (contacts.size()<=0){
            contactArrayList = getContacts();
            for (Contact contact1: contactArrayList){
                contactDBHandler.addContact(contact1);
            }
            contacts = contactDBHandler.getAllContact();
        }

        mContactsList =
                (ListView) getActivity().findViewById(android.R.id.list);

        adapter = new CustomAdapter(getActivity(), contacts);

        // Sets the adapter for the ListView
        mContactsList.setAdapter(adapter);
        Toast.makeText(getActivity(), "size: " +contacts.size(), Toast.LENGTH_SHORT).show();
        mContactsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                contact = (Contact) parent.getItemAtPosition(position);
                Intent intent = new Intent(getActivity(), DetailsContactActivity.class);
                intent.putExtra("contact", contact);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_details_contact, menu);
    }

    public ArrayList<Contact> getContacts() {

        ArrayList<Contact> contacts = new ArrayList<Contact>();
        final String[] projection = new String[] { ContactsContract.RawContacts.CONTACT_ID, ContactsContract.RawContacts.DELETED };

        @SuppressWarnings("deprecation")
        final Cursor rawContacts = getActivity().getContentResolver().query(
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
                Contact map1= lhs;
                Contact map2= rhs;
                String s1= map1.getName();
                String s2= map2.getName();
                return s1.compareTo(s2);
            }
        });
        return contacts;
    }


    public String getName(int contactId) {
        String name = "";
        final String[] projection = new String[] { ContactsContract.Contacts.DISPLAY_NAME };

        final Cursor contact = getActivity().getContentResolver().query(
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

        final Cursor email = getActivity().getContentResolver().query(
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

        final Cursor contact = getActivity().getContentResolver().query(
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
        final Cursor photo = getActivity().getContentResolver().query(
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

        Cursor addrCur = getActivity().getContentResolver().query(ContactsContract.Data.CONTENT_URI, null, addrWhere, addrWhereParams, null);

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
        final Cursor phone = getActivity().getContentResolver().query(
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
