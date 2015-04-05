package com.example.pc21.phonebook;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class AppContactFragment extends Fragment implements AdapterView.OnItemClickListener {

    ContactDBHandler contactDBHandler;
    ListView lvName;
    CustomAdapter adapter;
    Contact contact;
    String number;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_app_contacts, container, false);
        lvName = (ListView) view.findViewById(R.id.lvName);
        contactDBHandler = new ContactDBHandler(getActivity());

        List<Contact> contactList = contactDBHandler.getAllContact();
        Collections.sort(contactList, new Comparator<Contact>() {
            @Override
            public int compare(Contact lhs, Contact rhs) {
                Contact map1 = lhs;
                Contact map2 = rhs;
                String s1 = map1.getName();
                String s2 = map2.getName();
                return s1.compareTo(s2);
            }
        });
        List<Contact> contactListUri = getContacts();

        /*for ( int i = 0; i < contactListUri.size(); i++) {
            String name = contactListUri.get(i).getName();
            if (!name.equals(contactList.get(i).getName())){
                Log.d("================", "===============New :" + name);
                Contact contact = contactListUri.get(i);
                contactDBHandler.addContact(contact);
            }
        }*/
       /* for (Contact contact1: contactList){
            String str = "Name: " + contact1.getName() + "\nContact: "+ contact1.getPhoneNo() + "\nAddress: " + contact1.getAddress();
            Toast.makeText(getActivity(), str, Toast.LENGTH_LONG).show();
        }*/

        adapter = new CustomAdapter(getActivity(), contactList);

        lvName.setAdapter(adapter);
        registerForContextMenu(lvName);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        lvName.setOnItemClickListener(this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_details_contact, menu);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        contact = (Contact) lvName.getItemAtPosition(position);
        /*String str = "Name: " + contact.getName() + "\nContact: "+ contact.getPhoneNo() + "\nAddress: " + contact.getAddress();
        Toast.makeText(getActivity(), str, Toast.LENGTH_LONG).show();*/
        Intent intent = new Intent(getActivity(), DetailsContactActivity.class);
        intent.putExtra("contact", contact);
        startActivity(intent);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId() == R.id.lvName){
            ListView lv = (ListView) v;
            AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) menuInfo;
            Contact obj = (Contact) lv.getItemAtPosition(acmi.position);
            Log.d("===================", "=============" + obj.getPhoneNo());
            number = obj.getPhoneNo();
            menu.add(0, v.getId(), 0, "Call");//groupId, itemId, order, title
            menu.add(0, v.getId(), 0, "SMS");
        }


    }

    @Override
    public boolean onContextItemSelected(MenuItem item){
        if(item.getTitle()=="Call"){
            Toast.makeText(getActivity(),"calling...",Toast.LENGTH_LONG).show();
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + number));
            startActivity(callIntent);
        }
        else if(item.getTitle()=="SMS"){
            Toast.makeText(getActivity(),"sending...",Toast.LENGTH_LONG).show();
//            SmsManager manager =
            Intent sendIntent = new Intent(Intent.ACTION_VIEW);
            sendIntent.setData(Uri.parse("smsto:" + number));
            sendIntent.putExtra("address", number);
            startActivity(sendIntent);
        }else{
            return false;
        }
        return true;
    }
    public List<Contact> getContacts() {

        List<Contact> contacts = new ArrayList<Contact>();
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
