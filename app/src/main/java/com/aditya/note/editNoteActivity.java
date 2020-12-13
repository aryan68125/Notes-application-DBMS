package com.aditya.note;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;

public class editNoteActivity extends AppCompatActivity {

    Toolbar toolbar;
    EditText title;
    EditText noteContent;

    String todaysDate;
    String currentTime;

    //for getting the current time and date from the device we will use calender
    Calendar calendar;

    NoteDatabase noteDatabase;
    Note note;

    NoteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);
        Intent intent = getIntent();
        Long detail_note_id = intent.getLongExtra("Id",0);

        db = new NoteDatabase(this);
        note = db.getNote(detail_note_id);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //setting up the title of the application action bar in the add note activity
        getSupportActionBar().setTitle(note.getTitle());
        title = findViewById(R.id.title);
        noteContent = findViewById(R.id.noteContent);

        title.setText(note.getTitle());
        noteContent.setText(note.getContent());

        //getting current date and time
        calendar = Calendar.getInstance();
        //calender is starting from 0 to 11 in case of months so we need to add 1 to the month i.e Month+1
        todaysDate = calendar.get(Calendar.YEAR)+"/"+(calendar.get(Calendar.MONTH)+1)+"/"+calendar.get(Calendar.DAY_OF_MONTH);

        //getting the current time from the device
        currentTime = pad(calendar.get(Calendar.HOUR))+":"+pad(calendar.get(Calendar.MINUTE));
        Log.i("current",todaysDate +" -> "+ currentTime);
    }

    //this method will add 0 if either hour or minutes are in single digits
    private String pad(int i) {
        if(i<10){
            return "0"+i;
        }
        return String.valueOf(i);
    }

    // Menu icons are inflated just as they were with actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.save_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.save){
            Log.i("save","save is clicked");
            //updating database with new data
            note.setTitle(title.getText().toString());
            note.setContent(noteContent.getText().toString());
            int id = db.editNote(note);
                Toast.makeText(this,"Note Updated!",Toast.LENGTH_SHORT).show();
            //this will update the list in the Recycler View of the Main Activity after the note is saved in the Database
            GotoMainMethod();
        }
        else if(item.getItemId()==R.id.delete){
            Log.i("delete","delete is clicked");
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    //this will update the list in the Recycler View of the Main Activity after the note is saved in the Database
    //this will refresh the details Activity with the updated content
    public void GotoMainMethod(){
        Intent intent = new Intent(editNoteActivity.this,MainActivity.class);
        intent.putExtra("ID",note.getID());
        startActivity(intent);
    }

}