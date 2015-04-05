package com.example.pc21.phonebook;

import android.app.NotificationManager;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.widget.TextView;


public class MessageActivity extends ActionBarActivity {


    private static final int NOTIFY_ME_ID = 1338;

    TextView tvReminderResult;

    String number,message;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

//        tvReminderResult= (TextView) findViewById(R.id.tvReminderResult);

        SmsManager smsManager=SmsManager.getDefault();
        Intent intent=getIntent();
        number=intent.getStringExtra("toNumber");
        message=intent.getStringExtra("message");

        //smsManager.sendTextMessage(number,null,message,null,null);

        //Toast.makeText(this, "Message sent to=" + number, Toast.LENGTH_LONG).show();

        generateNotification(number,message);




        //tvReminderResult.setText(message+"\n"+number+"\n"+"Sent");



    }

    private void generateNotification(String number,String message) {


        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext())
                .setContentTitle("Message Sent"+"\n"+number).setContentText(message).setAutoCancel(true)
                .setSmallIcon(android.R.drawable.stat_notify_chat);

        NotificationManager manager= (NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
        manager.notify(0,builder.build());




    }


}

