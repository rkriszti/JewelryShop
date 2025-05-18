package com.example.jewelryshop;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Alarm elindult!", Toast.LENGTH_SHORT).show();
        // Itt lehet akár notificationt is küldeni
    }
}
