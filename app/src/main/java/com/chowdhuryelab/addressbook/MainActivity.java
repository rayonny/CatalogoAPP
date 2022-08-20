package com.chowdhuryelab.addressbook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper myDb;
    SQLiteDatabase mDatabase;
   ExtendedFloatingActionButton extendedFAB;
   SwipeRefreshLayout swipeRefreshLayout;
    //Button extendedFAB;

    Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myDb = new DatabaseHelper(this);
        extendedFAB = findViewById(R.id.extFab);
        swipeRefreshLayout = findViewById(R.id.swiperefresh);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Adapter(this, myDb.getAllData());
        adapter.swapCursor(myDb.getAllData());
        recyclerView.setAdapter(adapter);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });



        extendedFAB.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent myintent= new Intent(MainActivity.this, insert_data.class);
                startActivity(myintent);
            }
        });
    }
}
