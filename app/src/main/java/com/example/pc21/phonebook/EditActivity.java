package com.example.pc21.phonebook;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class EditActivity extends Activity implements View.OnClickListener {
    EditText etName, etContact, etEmail, etAddress;
    Button btnUpdate;
    int id;
    ContactDBHandler dbHandler;
    //String name, contactString, email, address;
    Contact contact1, contact2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        init();
    }

    private void init() {
        etName = (EditText) findViewById(R.id.etName);
        etContact = (EditText) findViewById(R.id.etContact);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etAddress = (EditText) findViewById(R.id.etAddress);

        btnUpdate = (Button) findViewById(R.id.btnUpdate);
        dbHandler = new ContactDBHandler(EditActivity.this);
        id = CustomAdapterSelect.idList.get(CustomAdapterSelect.pos);

        contact1 = dbHandler.getContactByID(id);

        etName.setText(contact1.getName());
        etContact.setText(contact1.getPhoneNo());
        etEmail.setText(contact1.getEmail());
        etAddress.setText(contact1.getAddress());

        btnUpdate.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        contact2 = new Contact();

        contact2.setId(id);
        contact2.setName(etName.getText().toString());
        contact2.setPhoneNo(etContact.getText().toString());
        contact2.setEmail(etEmail.getText().toString());
        contact2.setAddress(etAddress.getText().toString());

        dbHandler.updateContact(contact2);
        Toast.makeText(getApplicationContext(), "Updated Successfully", Toast.LENGTH_SHORT).show();
        CustomAdapterSelect.idList.clear();
        CustomAdapterSelect.itemChecked.clear();
        finish();
    }
}
