package com.aditya.note;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import java.util.Calendar;

public class AddNoteActivity extends AppCompatActivity {

    Toolbar toolbar;
    EditText title;
    EditText noteContent;

    String todaysDate;
    String currentTime;

    //for getting the current time and date from the device we will use calender
    Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //setting up the title of the application action bar in the add note activity
        getSupportActionBar().setTitle("New Note");
        title = findViewById(R.id.title);
        noteContent = findViewById(R.id.noteContent);

        //adding a textChange listener to title editText
        title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //getting the title of the note and setting it to the action bar title of the add new note activity
                if(s.length()!=0){
                    getSupportActionBar().setTitle(s);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

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
       //creating the object of Note note class
            //when inserting the data we dont have any ID
            Note note = new Note(title.getText().toString(),noteContent.getText().toString(),todaysDate,currentTime);
            //calling our database
            NoteDatabase noteDatabase = new NoteDatabase(this);
            //inserting data in the database
            noteDatabase.addnote(note);
            Toast.makeText(this,"Note Saved!",Toast.LENGTH_SHORT).show();
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
    public void GotoMainMethod(){
        Intent intent = new Intent(AddNoteActivity.this,MainActivity.class);
        startActivity(intent);
    }

}