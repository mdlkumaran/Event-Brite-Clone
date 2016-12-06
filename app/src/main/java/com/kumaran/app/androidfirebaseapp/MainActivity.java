package com.kumaran.app.androidfirebaseapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;

import static android.R.style.Theme;
import static android.R.style.Theme_Holo;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    DatabaseReference mRef;
    DatabaseReference mRefChild;
    String fbChildUsername = "";
    String email = "";

    private ProgressDialog mProcessDialog;
    public static final int EB_SIGN_IN = 0;
    FireApp fireApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://androidfirebaseapp-51e0b.firebaseio.com/");
//        mRef = new Firebase("https://androidfirebaseapp-51e0b.firebaseio.com/Users/mdlkumaran/emailID");
//        mRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                System.out.println("Please come");
//            }
//
//            @Override
//            public void onCancelled(FirebaseError firebaseError) {
//
//            }
//        });
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fireApp = (FireApp)getApplicationContext();
        Toolbar my_toolbar = (Toolbar) findViewById(R.id.app_custom_toolbar);
        setSupportActionBar(my_toolbar);
        getSupportActionBar().setTitle("");
        my_toolbar.setBackgroundColor(Color.parseColor("#FFFF9800"));

        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            // user logged in
        } else {
            startActivityForResult(AuthUI.getInstance()
                    .createSignInIntentBuilder().setTheme(Theme_Holo).setProviders(
                            AuthUI.GOOGLE_PROVIDER,
                            AuthUI.EMAIL_PROVIDER).build(), EB_SIGN_IN);
        }
        findViewById(R.id.log_out_button).setOnClickListener(this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        mRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://androidfirebaseapp-51e0b.firebaseio.com/");
        mRef.keepSynced(true);
        if (requestCode == EB_SIGN_IN) {
            if (resultCode == RESULT_OK) {

                email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                System.out.println("email:" + email);
                fireApp.setUserEmail(email);
                int index = email.indexOf("@");
                fbChildUsername = email.substring(0, index);
                fireApp.setUsername(fbChildUsername);
                mRefChild = mRef.child("Users");
                mRefChild.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        System.out.println(" Datasnapshot:"+dataSnapshot);
                        System.out.println("dataSnapshot.hasChild"+dataSnapshot.hasChild(fbChildUsername));
                        if (!dataSnapshot.hasChild(fbChildUsername)) {
                            System.out.println("do correctly");
                            mRefChild = mRef.child("Users").child(fbChildUsername).child("emailID");
                            mRefChild.setValue(email);
                            mRefChild = mRef.child("Users").child(fbChildUsername).child("category");
                            ArrayList<String> pInterest = new ArrayList<>();
                            ArrayList<Boolean> status = new ArrayList<>();
                            pInterest.addAll(Arrays.asList("Festival", "Halloween", "Music", "Party", "Science"));
                            status.addAll(Arrays.asList(false, false, false, false, false));
                            for (int i = 0; i < pInterest.size(); i++) {
                                mRefChild = mRefChild.child(pInterest.get(i));
                                mRefChild.setValue(status.get(i));
                                mRefChild = mRef.child("Users").child(fbChildUsername).child("category");

                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
/*                if( !mRef.child("Users").child(fbChildUsername).child("category").child("Festival").equals(false)) {
                    System.out.println("Why changing");
                    mRefChild = mRef.child("Users").child(fbChildUsername).child("emailID");
                    mRefChild.setValue(email);
                    mRefChild = mRef.child("Users").child(fbChildUsername).child("category");
                    ArrayList<String>pInterest = new ArrayList<>();
                    ArrayList<Boolean> status = new ArrayList<>();
                    pInterest.addAll(Arrays.asList( "Festival","Halloween","Music", "Party","Science" ));
                    status.addAll(Arrays.asList(false, false, false, false, false));
                    DatabaseReference mRefChildChild;
                    for(int i=0;i<pInterest.size();i++)
                    {
                        mRefChild = mRefChild.child(pInterest.get(i));
                        mRefChild.setValue(status.get(i));
                        mRefChild = mRef.child("Users").child(fbChildUsername).child("category");

                    }

                }*/
                mRef.keepSynced(true);
                mRefChild.keepSynced(true);
                try {
                    Thread.sleep(200);
                } catch(InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
                Intent intent = new Intent(getApplicationContext(), personalize_interest.class);

                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            } else {
                //super.onCreate(savedInstanceState);
            }
        }
    }

    @Override
    public void onClick(View view) {
        if(isOnline())
        {

        }
        else
        {
            Toast.makeText(getApplicationContext(), "No Internet Connectivity. Check Internet Connection", Toast.LENGTH_SHORT).show();
        }
        if (view.getId() == R.id.log_out_button) {
            AuthUI.getInstance()
                    .signOut(this)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            startActivityForResult(AuthUI.getInstance()
                                    .createSignInIntentBuilder().setTheme(Theme_Holo).setProviders(
                                            AuthUI.GOOGLE_PROVIDER,
                                            AuthUI.EMAIL_PROVIDER).build(), EB_SIGN_IN);
                        }
                    });
            findViewById(R.id.log_out_button).setOnClickListener(this);
        }
    }

    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

}
