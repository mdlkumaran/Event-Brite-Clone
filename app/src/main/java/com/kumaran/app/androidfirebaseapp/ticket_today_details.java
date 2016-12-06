

package com.kumaran.app.androidfirebaseapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class ticket_today_details extends AppCompatActivity {

    private ImageView mImageView;

    int i = 0;
    ArrayList<String> combEventDetailNames = new ArrayList<String>(Arrays.asList("Ticket Purchased: ", "Ticket Purchased Date: ", "Event Date: ", "Event Location: ", "Event Name: ", "Event Time: ", "Event Venue: ", "Event ID: "));
    String ticket_name;
    DatabaseReference dRef;
    private String username;
    Uri downloadUri = null;
    private StorageReference mStorage;
    private Firebase mRef;
    private ProgressDialog mProcessDialog;
    Button btn_event_takepicture;
    Button btn_event_viewpicture;
    FireApp fireApp;
    Uri pictureUri;
    String event_id;
    File imageFile;
    private ListView tic_today_ind_list;
    private static final int CAMERA_REQUEST = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_today_details);

        fireApp = (FireApp) getApplicationContext();

        Toolbar my_toolbar = (Toolbar) findViewById(R.id.app_custom_toolbar);
        setSupportActionBar(my_toolbar);
        getSupportActionBar().setTitle("Tickets for Today Event");
        my_toolbar.setBackgroundColor(Color.parseColor("#fff17a0a"));

        username = fireApp.getUsername();
        tic_today_ind_list = (ListView) findViewById(R.id.ticket_future_indi_list);
        ticket_name = getIntent().getStringExtra("value");
        btn_event_takepicture = (Button) findViewById(R.id.camera_button);
        btn_event_viewpicture = (Button) findViewById(R.id.image_view_button);
        System.out.println(ticket_name);

        dRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://androidfirebaseapp-51e0b.firebaseio.com/Users/" + username + "/tickets/" + ticket_name);

        dRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    System.out.println(child.getKey());
                    if (child.getKey().equals("eventID")) {
                        System.out.println("going inside");
                        event_id = "event" + ((String) child.getValue());
                        fireApp.setEventIDForImage(event_id);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        FirebaseListAdapter<String> firebaseListAdapter = new FirebaseListAdapter<String>(
                this,
                String.class, android.R.layout.simple_list_item_1,
                dRef
        ) {
            @Override
            protected void populateView(View v, String model, int position) {
                if (i == 8) {
                    i = 0;
                }
                System.out.println("combEventDetailNames:" + combEventDetailNames.get(i));
                model = combEventDetailNames.get(i) + model;
                TextView textView = (TextView) v.findViewById(android.R.id.text1);
                textView.setText(model);
                i++;
            }
        };

        tic_today_ind_list.setAdapter(firebaseListAdapter);


        btn_event_viewpicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), viewImageEvent.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        btn_event_takepicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("going inside");
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                Firebase.setAndroidContext(v.getContext());
                mRef = new Firebase("https://androidfirebaseapp-51e0b.firebaseio.com/event_photos/" + event_id);
                mProcessDialog = new ProgressDialog(v.getContext());
                mStorage = FirebaseStorage.getInstance().getReference();
                File pictureDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                String pictureName = getPictureName();
                imageFile = new File(pictureDirectory, pictureName);
                pictureUri = Uri.fromFile(imageFile);

                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, pictureUri);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }

        });

    }

    private String getPictureName() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HHmmss");
        String timestamp = sdf.format(new Date());
        return "Full" + event_id + "_" + timestamp + ".jpg";

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mProcessDialog.setMessage("Uploading...");
        mProcessDialog.show();
        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_REQUEST) {
                StorageReference filepath = mStorage.child("photos").child(event_id).child("Original").child(pictureUri.getLastPathSegment());
                filepath.putFile(pictureUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        mProcessDialog.dismiss();
                        downloadUri = taskSnapshot.getDownloadUrl();
                        System.out.println("downloadUri:" + downloadUri);
                        if (downloadUri != null)
                        {
                            String key = mRef.push().getKey();
                            System.out.println("key:" + key);
                            mRef.child(key).setValue(downloadUri.toString());
                            Toast.makeText(getBaseContext(), "Upload Done", Toast.LENGTH_LONG).show();
                        }
                    }
                });


            }
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

}
