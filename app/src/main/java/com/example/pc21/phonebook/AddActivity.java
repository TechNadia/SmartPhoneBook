package com.example.pc21.phonebook;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class AddActivity extends Activity implements View.OnClickListener {
    private EditText etName, etContact, etEmail, etAddress;
    private Button btnAdd;
    private int id;
    private ContactDBHandler dbHandler;
    private Contact contact;
    private String name, phoneNo, email, address;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        init();
    }

    private void init() {
        etName = (EditText) findViewById(R.id.etName);
        etContact = (EditText) findViewById(R.id.etContact);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etAddress = (EditText) findViewById(R.id.etAddress);

        btnAdd = (Button) findViewById(R.id.btnAdd);
        dbHandler = new ContactDBHandler(AddActivity.this);

        btnAdd.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        contact = new Contact();

        name = etName.getText().toString();
        phoneNo = etContact.getText().toString();
        email = etEmail.getText().toString();
        address = etAddress.getText().toString();
        contact.setName(name);
        contact.setPhoneNo(phoneNo);
        contact.setEmail(email);
        contact.setAddress(address);
        /*Log.d("=========== Name ==========", "=====================" + etName.getText().toString());
        Log.d("=========== Contact ==========", "=====================" + etContact.getText().toString());
        Log.d("=========== Email ==========", "=====================" + etEmail.getText().toString());
        Log.d("=========== Address ==========", "=====================" + etAddress.getText().toString());*/

        dbHandler.addContact(contact);
        Toast.makeText(getApplicationContext(), "Added Successfully", Toast.LENGTH_SHORT).show();
        finish();
    }
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        name = etName.getText().toString();
        phoneNo = etContact.getText().toString();
        email = etEmail.getText().toString();
        address = etAddress.getText().toString();

        savedInstanceState.putString("name", name);
        savedInstanceState.putString("phoneNo", phoneNo);
        savedInstanceState.putString("email", email);
        savedInstanceState.putString("address", address);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        name = savedInstanceState.getString("name");
        phoneNo = savedInstanceState.getString("phoneNo");
        email = savedInstanceState.getString("email");
        address = savedInstanceState.getString("address");

        etName.setText(name);
        etContact.setText(phoneNo);
        etEmail.setText(email);
        etAddress.setText(address);
    }
}
