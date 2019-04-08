package com.example.caoan.shop.Service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.example.caoan.shop.EventBus.LoadEvent;
import com.google.firebase.auth.FirebaseAuth;

import org.greenrobot.eventbus.EventBus;

public class CheckVerityEmail extends Service {
    public CheckVerityEmail() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (FirebaseAuth.getInstance().getCurrentUser().isEmailVerified()) {
            EventBus.getDefault().post(new LoadEvent(true, null));
        }
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
