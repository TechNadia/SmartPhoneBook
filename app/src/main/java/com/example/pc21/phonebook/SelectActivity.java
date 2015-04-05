package com.example.pc21.phonebook;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


public class SelectActivity extends Activity implements AdapterView.OnItemClickListener {
    ListView lvContact;
    ArrayList<Contact> contacts;
    CustomAdapterSelect adapter;
    ContactDBHandler contactDBHandler;
    String from;
//    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
        init();
    }

    private void init() {
        Intent intent = getIntent();
        from = intent.getExtras().getString("from");
        lvContact = (ListView) findViewById(R.id.lvContact);
        contactDBHandler = new ContactDBHandler(SelectActivity.this);
        contacts = (ArrayList<Contact>) contactDBHandler.getAllContact();
        adapter = new CustomAdapterSelect(this, contacts);
        lvContact.setAdapter(adapter);

        lvContact.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        lvContact.setItemsCanFocus(false);
        lvContact.setOnItemClickListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (from.equals("select")){
            getMenuInflater().inflate(R.menu.menu_select, menu);
        }
        else if (from.equals("delete")){
            getMenuInflater().inflate(R.menu.menu_delete, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.edit) {
            Intent intent = new Intent(SelectActivity.this, EditActivity.class);
            startActivity(intent);
            return true;
        }
        else if (id == R.id.delete){
            new AlertDialog.Builder(SelectActivity.this)
                    .setTitle("Delete Log")
                    .setMessage("This log will be deleted")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            for (int i : CustomAdapterSelect.idList.keySet()) {
                                int id = CustomAdapterSelect.idList.get(i);
                                contactDBHandler.deleteContact(id);

                                Toast.makeText(getApplicationContext(), "id: " + id, Toast.LENGTH_SHORT).show();
                                Log.v("----------------------name" + i + "-------------------------", "id: " + id);
                            }
                            Toast.makeText(getApplicationContext(), "Deleted Successfully", Toast.LENGTH_SHORT).show();
                            CustomAdapterSelect.idList.clear();
                            CustomAdapterSelect.itemChecked.clear();
                            finish();
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //
                        }
                    })
                    .show();
            return true;
        }
        else if (id == R.id.menuDelete){
            new AlertDialog.Builder(SelectActivity.this)
                    .setTitle("Delete Log")
                    .setMessage("This log will be deleted")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            for (int i : CustomAdapterSelect.idList.keySet()) {
                                int id = CustomAdapterSelect.idList.get(i);
                                contactDBHandler.deleteContact(id);

                                Toast.makeText(getApplicationContext(), "id: " + id, Toast.LENGTH_SHORT).show();
                                Log.v("----------------------name" + i + "-------------------------", "id: " + id);
                            }
                            Toast.makeText(getApplicationContext(), "Deleted Successfully", Toast.LENGTH_SHORT).show();

                            CustomAdapterSelect.idList.clear();
                            CustomAdapterSelect.itemChecked.clear();
                            finish();
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //
                        }
                    })
                    .show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (from.equals("select")){
            MenuItem edit = menu.findItem(R.id.edit);
            if (CustomAdapterSelect.idList.size() > 1){
                edit.setVisible(false);
            }
            else if (CustomAdapterSelect.idList.size() <= 1){
                edit.setVisible(true);
            }
        }

        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        CheckBox che = (CheckBox) view.findViewById(R.id.checkbox);
        if (!che.isChecked()){
            che.setChecked(true);
        }
        else if (che.isChecked()){
            che.setChecked(false);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        CustomAdapterSelect.idList.clear();
        CustomAdapterSelect.itemChecked.clear();
    }
}
