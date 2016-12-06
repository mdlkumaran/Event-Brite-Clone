package com.kumaran.app.androidfirebaseapp;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SendFriendRequest extends AppCompatActivity {

    EditText friendsEmailIDEditext;
    Button sendFriendRequestButton;
    DatabaseReference dRef;
    FireApp fireApp;
    String username;
    String emailIDSendingFriendRequest;
    Intent intent;
    String friend_email_id;
    int k =0;
    String SFRKey="";
    String PFRKey="";
    int SFRCount = 0;
    int PFRCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_friend_request);

        fireApp = (FireApp) getApplicationContext();
        username = fireApp.getUsername();

        Toolbar my_toolbar = (Toolbar) findViewById(R.id.app_custom_toolbar);
        setSupportActionBar(my_toolbar);
        getSupportActionBar().setTitle("Send Friend Request");
        my_toolbar.setBackgroundColor(Color.parseColor("#fff17a0a"));


        friendsEmailIDEditext = (EditText) findViewById(R.id.friend_email_ID_edit_text);
        sendFriendRequestButton = (Button) findViewById(R.id.send_friend_request_button);

        sendFriendRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("friendsEmailIDEditext:" + friendsEmailIDEditext.getText());
                friend_email_id = friendsEmailIDEditext.getText().toString();
                CharSequence temp_emilID = friendsEmailIDEditext.getText().toString();//here username is the your edittext object...
                if (!isValidEmail(temp_emilID)) {
                    friendsEmailIDEditext.requestFocus();
                    friendsEmailIDEditext.setError("Enter Correct Mail_ID ..!!");

                    Toast.makeText(getApplicationContext(), "Enter Correct Mail_ID", Toast.LENGTH_SHORT).show();

                } else {
                    dRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://androidfirebaseapp-51e0b.firebaseio.com/Users/" + username);

                    dRef.child("emailID").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            emailIDSendingFriendRequest = dataSnapshot.getValue().toString();
                            System.out.println("emailIDSendingFriendRequest"+emailIDSendingFriendRequest);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    dRef.child("Friends").child("Sent_Friend_Request").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot child : dataSnapshot.getChildren()) {
                                SFRKey = child.getKey();
                                if(child.getValue().equals(friend_email_id))
                                {
                                    k=1;
                                    friendsEmailIDEditext.setError("Already sent friend request to this friend..!!");
                                }
                            }
                            System.out.println("Sent_Friend_Requests  SFRKey value"+  SFRKey);
                            if(k==0)
                            {
                                SFRKey = SFRKey.replaceAll("[^\\d.]", "");
                                if(SFRKey!="")
                                SFRCount = Integer.valueOf(SFRKey) + 1;
                                System.out.println("Sent_Friend_Requests1");
                                    String sfrKey = "SFR" + SFRCount;
                                    dRef.child("Friends").child("Sent_Friend_Request").child(sfrKey).setValue(friend_email_id).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            int position =friend_email_id.indexOf('@');
                                            username = friend_email_id.substring(0,position);
                                            System.out.println("Username:"+username);
                                            dRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://androidfirebaseapp-51e0b.firebaseio.com/Users/" + username);
                                            dRef.child("Friends").child("Pending_Friend_Request");
                                            dRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                                                        SFRKey = child.getKey();
                                                    }
                                                    PFRKey = PFRKey.replaceAll("[^\\d.]", "");
                                                    if(PFRKey!="")
                                                    PFRCount = Integer.valueOf(PFRKey) + 1;
                                                    String pfrKey = "SFR" + PFRCount;
                                                    dRef.child("Friends").child("Pending_Friend_Request").child(pfrKey).setValue(emailIDSendingFriendRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            Toast.makeText(getApplicationContext(), "Friend Request Sent", Toast.LENGTH_SHORT).show();
                                                            Intent intent = new Intent(getBaseContext(), friends_list.class);
                                                            startActivity(intent);
                                                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                                        }
                                                    });
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });

                                        }
                                    });
                                    System.out.println("Sent_Friend_Request");
                                }


                        }


                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    dRef.child("Friends").child("Accepted_Friend_Request").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot child : dataSnapshot.getChildren()) {
                                if(child.getValue().equals(friend_email_id))
                                {

                                    friendsEmailIDEditext.setError("Already in friend list..!!");
                                }
                            }

                        }


                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });




                }
            }
        });
    }

    public final static boolean isValidEmail(CharSequence target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_overflow, menu);
        menu.add(0, 0, 0, "Change Location");
        menu.add(0, 1, 0, "Personalize");
        menu.add(0, 2, 0, "DashBoard");
        menu.add(0, 3, 0, "User Profile");
        menu.add(0, 4, 0, "LogOut");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case 0:
                startActivity(new Intent(getBaseContext(), location_selection.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                return true;
            case 1:
                startActivity(new Intent(getBaseContext(), personalize_interest.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                return true;
            case 2:
                startActivity(new Intent(getBaseContext(), EventRecyclerView.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                return true;
            case 3:
                startActivity(new Intent(getBaseContext(), UserProfile.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                return true;
            case 4:
                AuthUI.getInstance()
                        .signOut(this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                startActivity(new Intent(getBaseContext(), MainActivity.class));
                            }
                        });
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed()
    {
        // Your Code Here. Leave empty if you want nothing to happen on back press.
    }



}
