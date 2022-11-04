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
    RecyclerView recyclerView;
    //Button extendedFAB;

    Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myDb = new DatabaseHelper(this);
        extendedFAB = findViewById(R.id.extFab);
        swipeRefreshLayout = findViewById(R.id.swiperefresh);

        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Adapter(this, myDb.getAllData());
        adapter.swapCursor(myDb.getAllData());
        recyclerView.setAdapter(adapter);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                adapter = new Adapter(MainActivity.this, myDb.getAllData());
                adapter.swapCursor(myDb.getAllData());
                recyclerView.setAdapter(adapter);
                swipeRefreshLayout.setRefreshing(false);
            }
        });



        extendedFAB.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent myintent= new Intent(MainActivity.this, insert_data.class);
               // finish();
                startActivity(myintent);
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        adapter = new Adapter(MainActivity.this, myDb.getAllData());
        adapter.swapCursor(myDb.getAllData());
        recyclerView.setAdapter(adapter);
        System.out.println("@MainActivity.onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        adapter = new Adapter(MainActivity.this, myDb.getAllData());
        adapter.swapCursor(myDb.getAllData());
        recyclerView.setAdapter(adapter);
        System.out.println("@MainActivity.onPause");
    }

    @Override
    public void onRestart() {
        super.onRestart();
        System.out.println("@MainActivity.onRestart");
        // re-load events from database after coming back from the next page
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        adapter = new Adapter(MainActivity.this, myDb.getAllData());
        adapter.swapCursor(myDb.getAllData());
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onStop() {
        super.onStop();
        System.out.println("@MainActivity.onStop");
        // clear the event data from memory as the page is completely hidden by now
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("@MainActivity.onDestroy");
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
