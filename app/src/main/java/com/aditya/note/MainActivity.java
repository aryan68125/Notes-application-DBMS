package com.aditya.note;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
//import the androidx version Toolbar otherwise it will produce error
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //accessing toolbar and listView
    Toolbar toolbar;
    RecyclerView ListOfNotes;
    //Adapter adapter is my custome adapter that fills up the recycler View items list
    Adapter adapter;
    List<Note> notes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //getting our database for this function
        NoteDatabase db = new NoteDatabase(this);
        //pulling and storing data from the database inside the list<Note> notes
        notes = db.getNotes();
        ListOfNotes = findViewById(R.id.ListOfNotes);
        ListOfNotes.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Adapter(this,notes);
        ListOfNotes.setAdapter(adapter);
    }
    // Menu icons are inflated just as they were with actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.add){
            Intent intent = new Intent(MainActivity.this,AddNoteActivity.class);
            startActivity(intent);
        }
        else if(item.getItemId()==R.id.DevInfo){
           Intent intent = new Intent(MainActivity.this,DevActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}