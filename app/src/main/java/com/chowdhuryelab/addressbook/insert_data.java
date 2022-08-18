package com.chowdhuryelab.addressbook;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import java.io.ByteArrayOutputStream;


public class insert_data extends AppCompatActivity {
    DatabaseHelper myDb;
    EditText editName, editPhn1, editPhn2, editemail, editaddree;
    Button pickImage, btnAddData;

    int permissions_code = 0;
    boolean hasImageChanged = false;
    Bitmap thumbnail;

    ImageView profileImageView;
    byte[] data;

    static final int CAMERA_CODE = 1;
    static final int GALLERY_CODE = 0;

    final int bmpHeight = 160;
    final int bmpWidth = 160;

    private CharSequence[] Items = {"Camera", "Gallery", "Remove"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_data);

        myDb = new DatabaseHelper(this);
        editName = findViewById(R.id.editText_name);
        editPhn1 = findViewById(R.id.editText_phn1);
        editPhn2 = findViewById(R.id.editText_phn2);
        editemail = findViewById(R.id.editText_eMail);
        editaddree = findViewById(R.id.editText_Address);
        btnAddData = findViewById(R.id.button_add);
        //pickImage = (Button) findViewById(R.id.imagePicker);

        profileImageView = findViewById(R.id.insert_profileImage);

        AddData();

    }
    public void InsertImageOnClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(insert_data.this);
        builder.setTitle("Select Any Option_");
        builder.setItems(Items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (Items[which].equals("Camera")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, CAMERA_CODE);
                } else if (Items[which].equals("Gallery")) {
                    Log.i("GalleryCode", "" + GALLERY_CODE);
                    Intent GalleryIntent = null;
                    GalleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    GalleryIntent.setType("image/*");
                    GalleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(GalleryIntent, GALLERY_CODE);
                } else {
                    profileImageView.setImageResource(R.drawable.cover1);
                }
            }
        });
        builder.show();
    }

    public void AddData() {

        btnAddData.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String name = editName.getText().toString();
                        String phn1 = editPhn1.getText().toString();
                        String phn2 = editPhn2.getText().toString();
                        String email = editemail.getText().toString();
                        String address = editaddree.getText().toString();


                        profileImageView.setDrawingCacheEnabled(true);
                        profileImageView.buildDrawingCache();
                        Bitmap bitMap = profileImageView.getDrawingCache();
                        ByteArrayOutputStream img = new ByteArrayOutputStream();
                        bitMap.compress(Bitmap.CompressFormat.PNG, 100, img);
                        data = img.toByteArray();

                        if (TextUtils.isEmpty(name)) {
                            Toast.makeText(insert_data.this, "Plase enter the Name", Toast.LENGTH_LONG).show();
                            return;
                        }
                        if (TextUtils.isEmpty(address)) {
                            Toast.makeText(insert_data.this, "Plase enter your address", Toast.LENGTH_LONG).show();
                            return;
                        }
                        if (TextUtils.isEmpty(email)) {
                            Toast.makeText(insert_data.this, "Plase enter the email", Toast.LENGTH_LONG).show();
                            return;
                        }
                        if (TextUtils.isEmpty(phn1)) {
                            Toast.makeText(insert_data.this, "Plase enter the Contact Number", Toast.LENGTH_LONG).show();
                            return;
                        }
                        //data =image
                        boolean isInserted = myDb.insertData(name, phn1, phn2, email, address, data);
                        if (isInserted == true) {
                            Toast.makeText(insert_data.this, "Data Inserted", Toast.LENGTH_LONG).show();
                            finish();
                            Intent intent = new Intent(insert_data.this, MainActivity.class);
                            startActivity(intent);
                            editName.setText("");
                            editaddree.setText("");
                            editPhn1.setText("");
                            editPhn2.setText("");
                            editemail.setText("");
                        } else
                            Toast.makeText(insert_data.this, "Data not Inserted", Toast.LENGTH_LONG).show();
                    }
                }
        );

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case 1:
                    Log.i("CameraCode", "" + CAMERA_CODE);
                    Bundle bundle = data.getExtras();
                    Bitmap bmp = (Bitmap) bundle.get("data");
                    //Bitmap resized = Bitmap.createScaledBitmap(bmp, bmpWidth,bmpHeight, true);
                    //userPhoto.setImageBitmap(resized);
                    profileImageView.setImageBitmap(bmp);

                case 0:
                    Log.i("GalleryCode", "" + requestCode);
                    Uri ImageURI = data.getData();
                    profileImageView.setImageURI(ImageURI);
            }


        }
    }

}
