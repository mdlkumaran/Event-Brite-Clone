package com.kumaran.app.androidfirebaseapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

//import com.facebook.FacebookSdk;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class MainActivity extends AppCompatActivity {

    private Button mUploadImage;
    private ImageView mImageView;
    private Firebase mRef;
    private StorageReference mStorage;
    private ProgressDialog mProcessDialog;
    private static final int CAMERA_REQUEST_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Firebase.setAndroidContext(this);
        mStorage = FirebaseStorage.getInstance().getReference();
        mRef = new Firebase("https://androidfirebaseapp-51e0b.firebaseio.com/");
        mUploadImage = (Button) findViewById(R.id.mUploadButton);
        mImageView = (ImageView) findViewById(R.id.mImageView);
        mProcessDialog = new ProgressDialog(this);
        mUploadImage.setOnClickListener(new View.OnClickListener(){
            public void  onClick(View view)
            {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                startActivityForResult(intent, CAMERA_REQUEST_CODE);

            }
        });
    }
    @Override
    protected  void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        File photoFile = null;
        mProcessDialog.setMessage("Uploading...");
        mProcessDialog.show();
        if (resultCode != RESULT_CANCELED) {
            if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
                Bitmap photoCaptured = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                photoCaptured.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                String pathForImage = MediaStore.Images.Media.insertImage(this.getContentResolver(), photoCaptured, "Title", null);
                System.out.println("pathForImage:"+pathForImage);
                Uri uri =  Uri.parse(pathForImage);
                StorageReference filepath = mStorage.child("photos").child(uri.getLastPathSegment());
                filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        mProcessDialog.dismiss();
                        Toast.makeText(MainActivity.this, "Upload Done", Toast.LENGTH_LONG).show();
                        Uri downloadUri = taskSnapshot.getDownloadUrl();
                        Picasso.with(MainActivity.this).load(downloadUri).fit().centerCrop().into(mImageView);
                    }
                });
            }
        }
    }
}
