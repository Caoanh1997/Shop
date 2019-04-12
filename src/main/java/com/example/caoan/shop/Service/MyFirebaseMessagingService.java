package com.example.caoan.shop.Service;

import com.google.firebase.messaging.FirebaseMessagingService;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onNewToken(String s) {
        //super.onNewToken(s);
        System.out.println("New token: " + s);

        sendRegistrationToServer(s);
    }

    private void sendRegistrationToServer(String s) {
    }
}
