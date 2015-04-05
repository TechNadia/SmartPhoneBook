package com.example.pc21.phonebook;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ScreenOnReceiver extends BroadcastReceiver {

    public ScreenOnReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.v("============= Dreaming Stopped =============", "==========================");
    }
}
