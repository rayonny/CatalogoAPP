package com.chowdhuryelab.addressbook;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import java.io.ByteArrayOutputStream;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class update_data extends AppCompatActivity{
    DatabaseHelper myDb;
    EditText editName, editPhn1, editPhn2, editemail, editaddree ;
    Button pickImage, btnUpdate;
    Bitmap bitmap;
    ProgressDialog progressBar;
    int progressBarStatus = 0;
    Handler progressBarbHandler = new Handler();
    boolean hasImageChanged = false;

   ImageView profileImageView;

    byte[] data;
    Uri image_uri;
    static final int CAMERA_CODE = 1;
    static final int GALLERY_CODE = 0;

    final int bmpHeight = 160;
    final int bmpWidth = 160;

    private static final int CAMERA_REQUEST_CODE =200;
    private static final int STORAGE_REQUEST_CODE =300;
    private static final int IMAGE_PICK_GALLERY_CODE =400;
    private static final int IMAGE_PICK_CAMERA_CODE =500;


    private String[] cameraPermission;
    private String[] storagePermission;


    String ID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_data);

        myDb = new DatabaseHelper(this);
        editName = findViewById(R.id.editText_name);
        editPhn1 = findViewById(R.id.editText_phn1);
        editPhn2 = findViewById(R.id.editText_phn2);
        editemail = findViewById(R.id.editText_eMail);
        editaddree = findViewById(R.id.editText_Address);
        btnUpdate = findViewById(R.id.btn_update_update);


        profileImageView = findViewById(R.id.profileImageView);

        //Read permission from manifest
        cameraPermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        Intent i = getIntent();
        ID = i.getStringExtra("GetID");


        Cursor res = myDb.readData(ID);
        while (res.moveToNext()) {
            editName.setText(res.getString(1));
            editPhn1.setText("0"+res.getString(2));

            if(!(res.getString(3).toString().length() >0))
                editPhn2.setText(res.getString(3));
            else editPhn2.setText(String.format("0"+res.getString(3)));

            editemail.setText(res.getString(4));
            editaddree.setText(res.getString(5));
            byte[] img = res.getBlob(6);

            bitmap = BitmapFactory.decodeByteArray(img, 0, img.length);
            profileImageView.setImageBitmap(Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), false));

        }


        Update_Data();
    }

    private boolean isValidMail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    private boolean isValidMobile(String phone) {
        return android.util.Patterns.PHONE.matcher(phone).matches();
    }


    public void Update_Data() {
        btnUpdate.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String name = editName.getText().toString();
                        String phn1 = editPhn1.getText().toString();
                        String phn2 = editPhn2.getText().toString();
                        String email = editemail.getText().toString();
                        String address = editaddree.getText().toString();

                        //Check input data validation
                        String error = "";

                        if(name.length()<2){
                            editName.setError("Error");
                            error = error +"\nName";
                        }

                        if(!isValidMobile(phn1) || phn1.length()<11){
                            editPhn1.setError("Error");
                            error = error +"\nPhone(Home)";
                        }
                        if(!phn2.isEmpty()){
                            if(!isValidMobile(phn2) || phn2.length()<11) {
                                editPhn2.setError("Error");
                                error = error + "\nPhone(Office)";
                            }
                        }

                        if (!isValidMail(email))
                        {
                            editemail.setError("email");
                            error = error +"\nEmail";
                        }

                        if(address.length()<2){
                            editaddree.setError("Error");
                            error = error +"\nAddress";
                        }

                        profileImageView.setDrawingCacheEnabled(true);
                        profileImageView.buildDrawingCache();
                        Bitmap bitMap = profileImageView.getDrawingCache();
                        ByteArrayOutputStream img = new ByteArrayOutputStream();
                        bitMap.compress(Bitmap.CompressFormat.PNG, 100, img);
                        data = img.toByteArray();

                        if(error.isEmpty()){
                            String id = ID;
                            boolean isUpdate = myDb.updateData(id, name, phn1, phn2, email, address, data);
                            if (isUpdate == true) {
                                Toast.makeText(update_data.this, "Data Update", Toast.LENGTH_LONG).show();
                                editName.setText("");
                                editPhn1.setText("");
                                editPhn2.setText("");
                                editemail.setText("");
                                editaddree.setText("");

                                Intent intent = new Intent(update_data.this, MainActivity.class);
                                finish();
                                startActivity(intent);
                            } else {
                                Toast.makeText(update_data.this, "Data not Updated", Toast.LENGTH_SHORT).show();
                            }
                        }else  Toast.makeText(update_data.this, "Please check invalid fields", Toast.LENGTH_SHORT).show();
                        
                    }
                }
        );
    }


    public void ImageOnClick(View v) {
        String[] options = {"Camera", "Gallery", "remove"};

        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setTitle("pick Image")
                .setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which==0){
                            if (checkCameraPermission()){
                                pickFromCamera();
                            }
                            else {
                                requestCameraPermission();
                            }
                        }
                        else if(which==1){
                            if (checkStoragePermission()){
                                pickFromGalery();
                            }
                            else {
                                requestStoragePermission();
                            }
                        }
                        else {
                            profileImageView.setImageResource(R.drawable.cover1);
                        }
                    }
                }).show();
    }

    private void pickFromGalery(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_GALLERY_CODE);
    }

    private void pickFromCamera(){
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE, "Image_Title");
        contentValues.put(MediaStore.Images.Media.DESCRIPTION, "Image_Description");

        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(intent, IMAGE_PICK_CAMERA_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK){
            if (requestCode == IMAGE_PICK_GALLERY_CODE){
                image_uri = data.getData();
                profileImageView.setImageURI(image_uri);
            }
            else if(requestCode == IMAGE_PICK_CAMERA_CODE){
                profileImageView.setImageURI(image_uri);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case CAMERA_REQUEST_CODE:{
                if (grantResults.length > 0) {
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted && storageAccepted) {
                        pickFromCamera();
                    } else {
                        Toast.makeText(this, "Not Working", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;

            case STORAGE_REQUEST_CODE:{
                if (grantResults.length > 0) {
                    boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (storageAccepted) {
                        pickFromGalery();
                    } else {
                        Toast.makeText(this, "Not Working", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    private boolean checkStoragePermission(){
        boolean result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private void requestStoragePermission(){
        ActivityCompat.requestPermissions(this, storagePermission, STORAGE_REQUEST_CODE);
    }

    private boolean checkCameraPermission(){
        boolean result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) ==
                (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                (PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }

    private void requestCameraPermission(){
        ActivityCompat.requestPermissions(this, cameraPermission, CAMERA_REQUEST_CODE);
    }
}

