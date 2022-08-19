package com.chowdhuryelab.addressbook;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import de.hdodenhof.circleimageview.CircleImageView;


public class read_data extends AppCompatActivity {
    Button btn_update, btn_delete;
    DatabaseHelper myDb;
    TextView textView_Name, textView_Address, textView_Email, textView_Phn1,textView_Phn2;
    Bitmap bitmap;
    ImageView profileImageView;
    LinearLayout llphn2;
    String ID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_data);
        myDb = new DatabaseHelper(this);
        btn_update = findViewById(R.id.button_Update);
        btn_delete = findViewById(R.id.button_delete);

        textView_Name = findViewById(R.id.textView_Name);
        textView_Phn1 = findViewById(R.id.textView_phn1);
        textView_Phn2 = findViewById(R.id.textView_phn2);
        textView_Address = findViewById(R.id.textView_Address);
        textView_Email = findViewById(R.id.textView_Email);
        profileImageView = findViewById(R.id.profileImageView);

        llphn2 = findViewById(R.id.llphn2);

        Intent i = getIntent();
        ID = i.getStringExtra("GetID");

        btn_update.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(read_data.this, update_data.class);
                intent.putExtra("GetID",ID);
                startActivity(intent);
            }
        });

        btn_delete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (!isFinishing()) {
                            new AlertDialog.Builder(read_data.this)
                                    .setTitle("Delete")
                                    .setMessage("Do you want to delete the record")
                                    .setCancelable(true)
                                    .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                           int rows = myDb.deleteData(ID);
                                            if(rows >0){
                                                Toast.makeText(read_data.this,"Date Deleted",Toast.LENGTH_LONG).show();
                                            }
                                            else Toast.makeText(read_data.this,"Date not Deleted",Toast.LENGTH_LONG).show();
                                            Intent myintent = new Intent(read_data.this, MainActivity.class);
                                            finish();
                                            startActivity(myintent);
                                        }
                                    })
                                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    })
                                    .show();

                        }
                    }
                });
            }
        });

setData();

    }

    public void setData() {

        Cursor res = myDb.readData(ID);
        if (res.getCount() == 0) {
            // show message
            System.out.println("No Data Found");
            return;
        }

        while (res.moveToNext()) {
            textView_Name.setText(String.format(res.getString(1)));
            textView_Phn1.setText(String.format("0"+res.getString(2)));

            if(!(res.getString(3).toString().length() >0))
                llphn2.setVisibility(View.GONE);
            else textView_Phn2.setText(String.format("0"+res.getString(3)));

            textView_Email.setText(String.format(res.getString(4)));
            textView_Address.setText(String.format(res.getString(5)));

            byte[] img = res.getBlob(6);
            bitmap = BitmapFactory.decodeByteArray(img, 0, img.length);
            profileImageView.setImageBitmap(Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), false));
        }

    }

    public void call1(View view) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:"+textView_Phn1.getText().toString()));
        startActivity(intent);
    }

    public void message1(View view) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("smsto:" + textView_Phn1.getText().toString()));
        startActivity(intent);
    }

    public void call2(View view) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:"+textView_Phn2.getText().toString()));
        startActivity(intent);
    }

    public void message2(View view) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("smsto:" + textView_Phn2.getText().toString()));
        startActivity(intent);
    }

    public void SenMail(View view) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:" + textView_Email.getText().toString()));
        startActivity(intent);
    }


}