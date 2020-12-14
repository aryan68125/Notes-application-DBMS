package com.aditya.note;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.speech.tts.TextToSpeech;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashSet;
import java.util.Locale;

public class noteDetails extends AppCompatActivity {

    TextView detailOfNote;
    NoteDatabase db;
    Note note;

    //setting up text to speech listener
    TextToSpeech mtts;
    int everythingIsOKmttsIsGoodToGo = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //receving item ID that has been clicked by the user from the Adapter class
        Intent intent = getIntent();
        Long id = intent.getLongExtra("ID", 0);
        Log.i("itemid", Long.toString(id));

        //creating our note database instnace
        db = new NoteDatabase(this);
        //this will return the details of the item note clicked by the user on the recycler View
        note = db.getNote(id);

        //setting up the title of this detail Activity to be the title of the note
        getSupportActionBar().setTitle(note.getTitle());

        //seting up the textView to display the content of the note
        detailOfNote = (TextView) findViewById(R.id.detailOfNote);
        String detail = String.valueOf(note.getContent());
        detailOfNote.setText(detail);
        /*after adding these two lines in the texView
        android:scrollbars="vertical"
        android:fadeScrollbars="true"
         */
        detailOfNote.setMovementMethod(new ScrollingMovementMethod());

        //code related to text to speech engine
        //setting up text to speech engine
        mtts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    //checking if this set language method was successfull
                    int result = mtts.setLanguage(Locale.ENGLISH); //passing language to our text to speech engine if its initializaton is a success
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        //if there is a missing data or language not supported by the device then we will show an error message
                        Toast.makeText(getApplicationContext(), "Either the language is not supported by your device or the input field is empty", Toast.LENGTH_LONG).show();
                    } else {
                        //if there is no error and text to speech is successfully loaded then button is enabled
                        everythingIsOKmttsIsGoodToGo = 1;
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Initialization of text to speech engine failed!!", Toast.LENGTH_LONG).show();
                }
            }
        });

        FloatingActionButton fab = findViewById(R.id.delete);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //creating dialog box that will ask the user whether they will delete the note or not
                //creating a new alert dialog box to ask the user weather they truely want to delete the note or not
                new AlertDialog.Builder(noteDetails.this).setIcon(R.drawable.danger).setTitle("Are you sure you want to delete the note?")
                        .setMessage("The deleted notes cant be recovered!...gaur se dekh lo RAJA bhaya :-)")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //here we will write the code to delete a note from the applications listView and from the memory
                                db.deleteNote(note.getID());
                                Toast.makeText(getApplicationContext(), "Note deleted!", Toast.LENGTH_SHORT).show();
                                //now redirecting user to the main Activity
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                            }
                        }).setNegativeButton("No",null).show();
            }
        });
    }

    // Menu icons are inflated just as they were with actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.detail_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.edit) {
            Intent intent = new Intent(noteDetails.this, editNoteActivity.class);
            //sending the contents of the textView from note detail class to editNoteActivity.class
            intent.putExtra("Id", note.getID());
            startActivity(intent);
        } else if (item.getItemId() == R.id.speak) {
            if (everythingIsOKmttsIsGoodToGo == 1) {
                String text = detailOfNote.getText().toString();
                mtts.setPitch(1.1f); //setting up the pitch and speed of the speech in text to speech engine
                mtts.setSpeechRate(1.1f);
                //making text to speech engine to speek our entered text
                //TextToSpeech.QUEUE_FLUSH = current txt is cancled to speak a new one
                //TextToSpeech.QUEUE_ADD the next text is spoken after the previous text is finished
                //mtts.speak(Passing the content of our editText, TextToSpeech.QUEUE_FLUSH,null);
                mtts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        //stopping mtts when the app is closed
        if(mtts!=null){
            mtts.stop();
            mtts.shutdown();
        }
        super.onDestroy();
    }
}