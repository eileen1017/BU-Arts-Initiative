package com.cs591.mooncake.FirebaseUtils;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.cs591.mooncake.MainActivity;
import com.cs591.mooncake.login.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;

public class FirebaseInitialize {

    private static FirebaseAuth mAuth;

    public static void Initialize(final Context context){
        mAuth = FirebaseAuth.getInstance();
        FirebaseAuth.AuthStateListener mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    Toast.makeText(context, "Logout!", Toast.LENGTH_LONG).show();
                    context.startActivity(new Intent(context, LoginActivity.class));
                }
            }
        };
        mAuth.addAuthStateListener(mAuthListener);
    }
}
