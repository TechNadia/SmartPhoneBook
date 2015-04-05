package com.example.pc21.phonebook;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;


public class GmailContactFragment extends Fragment implements AdapterView.OnItemClickListener {

    ArrayList<Email> emailArrayList = new ArrayList<>();
    CustomAdapterEmail adapter;
    Email email;
    ListView listView;

    private SimpleCursorAdapter mCursorAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gmail,container,false);
        listView = (ListView) view.findViewById(R.id.lvGmail);
        getEmail();
        adapter = new CustomAdapterEmail(getActivity(), emailArrayList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        adapter.notifyDataSetChanged();
        return view;
    }

    public void getEmail() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                Properties props = new Properties();
                props.setProperty("mail.store.protocol", "imaps");
                try {
                    Session session = Session.getInstance(props, null);
                    Store store = session.getStore();
                    store.connect("imap.gmail.com", "tech.nadia@gmail.com", "JessorE1123581321");
                    Folder inbox = store.getFolder("INBOX");
                    inbox.open(Folder.READ_ONLY);
                    Message msg = inbox.getMessage(inbox.getMessageCount());
                    int messageCount = inbox.getMessageCount();

                    Log.e("======================", "Total Messages:- " + messageCount);
                    Message[] messages = inbox.getMessages();
                    System.out.println("------------------------------");
                    for (int i = 0; i <10; i++) {
                        email = new Email();
                        Address[] in = messages[i].getFrom();
                        for (Address address : in) {
                            email.setReceivedFrom(address.toString());
                            Log.v("==============", "FROM:" + address.toString());
                        }
                        email.setSubject(messages[i].getSubject());
                        email.setDate(String.valueOf(messages[i].getReceivedDate()));
                        Log.e("===================","Mail Subject:- " + messages[i].getSubject());
                        Log.e("===================","Mail Subject:- " + messages[i].getReceivedDate());
                        emailArrayList.add(email);
                    }
                    for (Email email1 : emailArrayList){
                        Log.d("=================", "=========" + email1.getSubject());
                    }

                    inbox.close(true);
                    store.close();
                } catch (Exception mex) {
                    mex.printStackTrace();
                }
            }
        }).start();


    }

    @Override
    public void onStart() {
        super.onStart();
       /* listView = (ListView) getActivity().findViewById(R.id.lvGmail);
        adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, emailArrayList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);*/
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(getActivity(), "clicked", Toast.LENGTH_SHORT).show();
    }
}
