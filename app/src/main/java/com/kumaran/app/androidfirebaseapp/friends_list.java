package com.kumaran.app.androidfirebaseapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class friends_list extends AppCompatActivity {
    private ArrayList<PendingFriendListModel> pendingFriendsListdata = new ArrayList<>();
    private ArrayList<AcceptedFriendListModel> acceptedFriendsListdata = new ArrayList<>();
    ArrayList<String> pendingFriendsListString;
    ArrayList<String> acceptedFriendsListString;

    DatabaseReference dRef;
    DatabaseReference dRef1;
    FireApp fireApp;
    String username;
    String SFRKey="";
    String AFRKey="";
    String friendEmailReject;
    String friendEmailAccept;
    String userEmail;
    int AFRCount = 0;
    int index = 0;
    String friendUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_list);

        fireApp = (FireApp) getApplicationContext();
        username = fireApp.getUsername();
        dRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://androidfirebaseapp-51e0b.firebaseio.com/Users/" + username);
        dRef1 = FirebaseDatabase.getInstance().getReferenceFromUrl("https://androidfirebaseapp-51e0b.firebaseio.com/Users/");
        Toolbar my_toolbar = (Toolbar) findViewById(R.id.app_custom_toolbar);
        setSupportActionBar(my_toolbar);
        getSupportActionBar().setTitle("Friends List & Messages");
        my_toolbar.setBackgroundColor(Color.parseColor("#fff17a0a"));

        ListView PFRlv = (ListView)findViewById(R.id.your_pending_friends_list_view);
        ListView AFRlv = (ListView)findViewById(R.id.your_accepted_friends_list_view);
        generatePendingFriendsListData();
        generateAcceptedFriendsListData();
        PFRlv.setAdapter(new PendingFriendListAdapter(this,pendingFriendsListdata));
        AFRlv.setAdapter(new AcceptedFriendListAdapter(this,acceptedFriendsListdata));

        Button friends_send_req_button = (Button)findViewById(R.id.friend_request_button);
        Button view_msg_button = (Button)findViewById(R.id.view_msg_button);
        friends_send_req_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), SendFriendRequest.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        view_msg_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), view_msg_list.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });


    }

    private void generatePendingFriendsListData()
    {
        pendingFriendsListString = new ArrayList<>();

        dRef.child("Friends").child("Pending_Friend_Request").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    pendingFriendsListString.add(child.getValue().toString());
                }
                System.out.println("pendingFriendsListdata:"+pendingFriendsListString);
                for(int i=0; i<pendingFriendsListString.size();i++)

                    pendingFriendsListdata.add(new PendingFriendListModel(pendingFriendsListString.get(i)));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });





    }

    private void generateAcceptedFriendsListData()
    {
        acceptedFriendsListString = new ArrayList<>();

        dRef.child("Friends").child("Accepted_Friend_Request").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    acceptedFriendsListString.add(child.getValue().toString());
                }
                System.out.println("acceptedFriendsListdata:"+acceptedFriendsListString);
                for(int i=0; i<acceptedFriendsListString.size();i++)
                    acceptedFriendsListdata.add(new AcceptedFriendListModel(acceptedFriendsListString.get(i)));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




    }

     class AcceptedFriendListModel{
        private String acceptedFriendsEmail;

        public AcceptedFriendListModel(String acceptedFriendsEmail) {
            this.acceptedFriendsEmail = acceptedFriendsEmail;
        }

        public String getAcceptedFriendsEmail() {
            return acceptedFriendsEmail;
        }

        public void setAcceptedFriendsEmail(String acceptedFriendsEmail) {
            this.acceptedFriendsEmail = acceptedFriendsEmail;
        }
    }

    class PendingFriendListModel{
        private String pendingFriendsEmail;

        public PendingFriendListModel(String pendingFriendsEmail) {
            this.pendingFriendsEmail = pendingFriendsEmail;
        }


        public String getPendingFriendsEmail() {
            return pendingFriendsEmail;
        }

        public void setPendingFriendsEmail(String pendingFriendsEmail) {
            this.pendingFriendsEmail = pendingFriendsEmail;
        }
    }

    private class PendingFriendListAdapter extends ArrayAdapter<PendingFriendListModel> {

        public PendingFriendListAdapter(Context context, ArrayList<PendingFriendListModel> pendingFriendLists) {
            super(context, R.layout.pendingfriendlistunit, pendingFriendLists);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            PendingFriendListModel pendingFriendListsView = getItem(position);
            ViewHolder viewHolder = null;
            if (convertView == null){
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.pendingfriendlistunit,parent,false);
                viewHolder = new ViewHolder();
                viewHolder.pendingFriendsEmail = (TextView)convertView.findViewById(R.id.pending_friend_request_name);
                viewHolder.AcceptFriend =(Button)convertView.findViewById(R.id.accept_friend_button);
                viewHolder.RejectFriend = (Button)convertView.findViewById(R.id.reject_friend_button);

                viewHolder.AcceptFriend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getContext(),"Accept Friend", Toast.LENGTH_LONG).show();
                        RelativeLayout parent = (RelativeLayout) v.getParent();
                        TextView textView =(TextView) parent.findViewById(R.id.pending_friend_request_name);
                        friendEmailAccept = textView.getText().toString();
                        index = friendEmailAccept.indexOf("@");
                        friendUsername = friendEmailAccept.substring(0,index);
                        dRef.child("Friends").child("Pending_Friend_Request").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot child : dataSnapshot.getChildren()) {
                                    System.out.println("child.getValue().toString():"+child.getValue().toString());
                                    if(child.getValue().toString().equals(friendEmailAccept))
                                    {
                                        SFRKey = child.getKey().toString();

                                        break;
                                    }
                                }
                                dRef.child("Friends").child("Pending_Friend_Request").child(SFRKey).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        dRef.child("Friends").child("Accepted_Friend_Request").addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                for (DataSnapshot child : dataSnapshot.getChildren()) {
                                                    AFRKey =child.getKey().toString();
                                                }
                                                AFRKey = AFRKey.replaceAll("[^\\d.]", "");
                                                if(AFRKey!="")
                                                    AFRCount = Integer.valueOf(AFRKey) + 1;
                                                System.out.println("Sent_Friend_Requests1");
                                                String afrKey = "AFR" + AFRCount;

                                                userEmail = fireApp.getUserEmail();

                                                String key = dRef1.child(friendUsername).child("Friends").push().getKey();
                                                dRef1.child(friendUsername).child("Friends").child("Accepted_Friend_Request").child(key).setValue(userEmail);
                                                dRef.child("Friends").child("Accepted_Friend_Request").child(afrKey).setValue(friendEmailAccept).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
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
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                });

                viewHolder.RejectFriend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        RelativeLayout parent = (RelativeLayout) v.getParent();
                        TextView textView =(TextView) parent.findViewById(R.id.pending_friend_request_name);
                        friendEmailReject = textView.getText().toString();
                        dRef.child("Friends").child("Pending_Friend_Request").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot child : dataSnapshot.getChildren()) {
                                    System.out.println("child.getValue().toString():"+child.getValue().toString());
                                    if(child.getValue().toString().equals(friendEmailReject))
                                    {
                                        SFRKey = child.getKey().toString();

                                        break;
                                    }
                                }
                                dRef.child("Friends").child("Pending_Friend_Request").child(SFRKey).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
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

                convertView.setTag(viewHolder);
            }
            else{
                viewHolder = (ViewHolder)convertView.getTag();
            }

            viewHolder.pendingFriendsEmail.setText(pendingFriendListsView.getPendingFriendsEmail());
            return convertView;
        }
    }

    public  class ViewHolder {
        TextView pendingFriendsEmail;
        Button AcceptFriend;
        Button RejectFriend;
    }

    private class AcceptedFriendListAdapter extends ArrayAdapter<AcceptedFriendListModel> {
        public AcceptedFriendListAdapter(Context context, ArrayList<AcceptedFriendListModel> acceptedFriendLists) {
            super(context, R.layout.acceptedfriendlistunit, acceptedFriendLists);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            AcceptedFriendListModel acceptedFriendListsView = getItem(position);
            ViewHolderForAcceptedFriendList viewHolder;
            if (convertView == null){
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.acceptedfriendlistunit,parent,false);
                viewHolder = new ViewHolderForAcceptedFriendList();
                viewHolder.acceptedFriendsEmail = (TextView)convertView.findViewById(R.id.accepted_friend_request_name);
                viewHolder.SendMessage = (Button)convertView.findViewById(R.id.send_message_friend_button);

                viewHolder.SendMessage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RelativeLayout parent = (RelativeLayout) v.getParent();
                        TextView textView =(TextView) parent.findViewById(R.id.accepted_friend_request_name);
                        Intent intent = new Intent(getBaseContext(), messagetofriendeventlist.class);
                        intent.putExtra("friendName",textView.getText().toString());
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        Toast.makeText(getContext(),"Send Message", Toast.LENGTH_LONG).show();

                    }
                });

                convertView.setTag(viewHolder);
            }

            else{
                viewHolder = (ViewHolderForAcceptedFriendList) convertView.getTag();

             }


            viewHolder.acceptedFriendsEmail.setText(acceptedFriendListsView.getAcceptedFriendsEmail());
            return convertView;
        }
    }

    public  class ViewHolderForAcceptedFriendList {
        TextView acceptedFriendsEmail;
        Button SendMessage;
    }

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
