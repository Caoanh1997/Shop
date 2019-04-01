package com.example.caoan.shop.BroadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class DataChangeBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context,"Data change",Toast.LENGTH_SHORT).show();
    }
}
