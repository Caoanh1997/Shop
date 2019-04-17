package com.example.caoan.shop.BroadcastReceiver;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

public class InternetBroadcast extends BroadcastReceiver {

    private Activity activity;

    public InternetBroadcast(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Crouton.makeText(activity, "Kiểm tra kết nối Internet", Style.ALERT).show();
    }
}
