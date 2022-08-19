package com.chowdhuryelab.addressbook;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
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

import androidx.appcompat.app.AppCompatActivity;


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
    Bitmap thumbnail;
   ImageView profileImageView;
    byte[] data;

    static final int CAMERA_CODE = 1;
    static final int GALLERY_CODE = 0;

    final int bmpHeight = 160;
    final int bmpWidth = 160;

    private CharSequence[] Items = {"Camera", "Gallery", "Remove"};

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
        AlertDialog.Builder builder = new AlertDialog.Builder(update_data.this);
        builder.setTitle("Select Any Option_");
        builder.setItems(Items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (Items[which].equals("Camera")){
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, CAMERA_CODE);
                }else if (Items[which].equals("Gallery")){
                    Log.i("GalleryCode",""+GALLERY_CODE);
                    Intent GalleryIntent = null;
                    GalleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    GalleryIntent.setType("image/*");
                    GalleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(GalleryIntent,GALLERY_CODE);
                }
                else{
                    profileImageView.setImageResource(R.drawable.cover1);
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK){
            switch(requestCode){
                case 1:
                    Log.i("CameraCode",""+CAMERA_CODE);
                    Bundle bundle = data.getExtras();
                    Bitmap bmp = (Bitmap) bundle.get("data");
                    //Bitmap resized = Bitmap.createScaledBitmap(bmp, bmpWidth,bmpHeight, true);
                    //userPhoto.setImageBitmap(resized);
                    profileImageView.setImageBitmap(bmp);

                case 0:
                    Log.i("GalleryCode",""+requestCode);
                    Uri ImageURI = data.getData();
                    profileImageView.setImageURI(ImageURI);
            }


        }
    }


}

