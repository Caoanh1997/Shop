package com.example.caoan.shop.Service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.example.caoan.shop.LoadEvent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;

public class CheckDataChangeFirebase extends Service {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    public CheckDataChangeFirebase() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Store");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //EventBus.getDefault().post(new LoadEvent(true,null));
                Intent intent = new Intent();
                intent.setAction("dataChange.Broadcast");
                sendBroadcast(intent);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
