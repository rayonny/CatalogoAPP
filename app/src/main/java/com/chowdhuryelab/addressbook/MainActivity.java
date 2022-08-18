package com.chowdhuryelab.addressbook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper myDb;
    SQLiteDatabase mDatabase;
   ExtendedFloatingActionButton extendedFAB;
    //Button extendedFAB;

    Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myDb = new DatabaseHelper(this);
        extendedFAB = findViewById(R.id.extFab);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Adapter(this, myDb.getAllData());
        adapter.swapCursor(myDb.getAllData());
        recyclerView.setAdapter(adapter);



        extendedFAB.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent myintent= new Intent(MainActivity.this, insert_data.class);
                startActivity(myintent);
            }
        });
    }
}
