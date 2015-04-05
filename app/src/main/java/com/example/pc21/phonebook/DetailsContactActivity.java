package com.example.pc21.phonebook;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;


public class DetailsContactActivity extends Activity {
    TextView tvName, tvPhoneNo, tvEmail, tvAddress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_contact);
        init();
    }

    private void init() {
        tvName = (TextView) findViewById(R.id.tvName);
        tvPhoneNo = (TextView) findViewById(R.id.tvPhoneNo);
        tvEmail = (TextView) findViewById(R.id.tvEmail);
        tvAddress = (TextView) findViewById(R.id.tvAddress);

        Bundle b = getIntent().getExtras();
        Contact contact = (Contact)b.getParcelable("contact");

        String str = "Name: " + contact.getName() + "\nContact: "+ contact.getPhoneNo() + "\nAddress: " + contact.getAddress();
        Toast.makeText(getApplicationContext(), str, Toast.LENGTH_LONG).show();

        String name = contact.getName();
        String phoneNo = contact.getPhoneNo();
        String email = contact.getEmail();
        String address = contact.getAddress();
        if (name.isEmpty()){
            tvName.setText("No Available Data");
        }else {
            tvName.setText(name);
        }
        if (phoneNo.isEmpty()){
            tvPhoneNo.setText("No Available Data");
        }else {
            tvPhoneNo.setText(phoneNo);
        }
        if (email.isEmpty()){
            tvEmail.setText("No Available Data");
        }else {
            tvEmail.setText(email);
        }
        if (address.isEmpty()){
            tvAddress.setText("No Available Data");
        }else {
            tvAddress.setText(address);
        }
    }
}
