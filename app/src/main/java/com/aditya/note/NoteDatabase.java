package com.aditya.note;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.TableRow;

import java.util.ArrayList;
import java.util.List;

//root class of our SQLite database is SQLiteOpenHelper
//extends SQLiteOpenHelper will import that root class of SQLite databse into our NoteDatabase class
public class NoteDatabase extends SQLiteOpenHelper {

    private static final int DATABASEVERSION= 2;
    private static final String DATABASE_NAME = "notedb";
    private static final String DATABASE_TABLE = "notestable";

    //column names for database
    private static final String KEY_ID="id";
    private static final String KEY_TITLE="title";
    private static final String KEY_CONTENT="content";
    private static final String KEY_DATE="date";
    private static final String KEY_TIME="time";

    public static List<Note> allNotes;

    //creating a constructor for our noteDatabase
    NoteDatabase(Context context){
        super(context,DATABASE_NAME,null,DATABASEVERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //onCreate is called everytime the noteDatabase instance is created inside any class of this application
        //creating the database table
        String query = "CREATE TABLE " + DATABASE_TABLE +" ("+ KEY_ID +" INTEGER PRIMARY KEY AUTOINCREMENT , "+
                KEY_TITLE+" TEXT, "+
                KEY_CONTENT+" TEXT, "+
                KEY_DATE+" TEXT, "+
                KEY_TIME+" TEXT "+")";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
     //checking for updates for our database
        if(oldVersion>=newVersion){
            return;
        }
        else{
            //update the table if new version is available
            db.execSQL("DROP TABLE IF EXISTS "+DATABASE_TABLE);
            onCreate(db);
        }
    }

    //it will take Note as a parameter this note will contain all the information like title, content , date and time
    //and it will send that note from addnote activity to this node database
    //Note note is the class
    public long addnote(Note note){
       //here we will insert the data
        SQLiteDatabase db = this.getWritableDatabase();
        //this contentValues will create a dictionary like structure
        ContentValues contentValues = new ContentValues();
        //now we can save our value to the keys that we have created in this class
       // contentValues.put(KEY_ID,note.getID()); ***Do not insert Id inside SQLite instead auto increament the id to be unique
        contentValues.put(KEY_TITLE,note.getTitle());
        contentValues.put(KEY_CONTENT,note.getContent());
        contentValues.put(KEY_TIME,note.getTime());
        contentValues.put(KEY_DATE,note.getDate());

        //now we will insert the data
        //if the data is inserted successfully it will return the long value of the primary key
        long ID = db.insert(DATABASE_TABLE,null,contentValues);
        db.close();
        Log.i("ID", Long.toString(ID));
        return ID;
    }

    //this method will get the details of a single note
    //it will take id of the database elements as a parameter
    public Note getNote(long id){

        //we will pull the data from the database using element's unique id
        //select * from databse where id = whatever the id we have passed on here
        //* means acessing all the data in that particular id elements
        //creating an instance of our database
        SQLiteDatabase db = this.getReadableDatabase();
        //Cursor is a pointer that can be used to access the elements present in the database
        // Cursor cursor = db.query(DATABASE TABLE NAME, Database column using a String array,KEY_ID is the selection criteria+"=?"(=? prevents sql injection),
        //  the id argument will replace the ? new String[]{String.valueOf(id)},null,null,null);
        Cursor cursor = db.query(DATABASE_TABLE, new String[]{KEY_ID,KEY_TITLE,KEY_CONTENT,KEY_DATE,KEY_TIME},KEY_ID+"=?",
                new String[]{String.valueOf(id)},null,null,null);

        //returning data
        if(cursor!=null){
            //cursor always starts at -1 position so we need to manually move the cursor to the first position
            cursor.moveToFirst();
        }
        //pulling the data from the database and saving it on the note function
        Note note = new Note(cursor.getLong(0),cursor.getString(1),
                cursor.getString(2),cursor.getString(3),cursor.getString(4));
        return note;
    }

    //this will get all the notes present in the database
    //so that we can desplay it in our ListView
    public List<Note> getNotes(){
        //we will pull the data from the database using element's unique id
        //select * from databse where id = whatever the id we have passed on here
        //* means acessing all the data in that particular id elements
        //creating an instance of our database
        SQLiteDatabase db = this.getReadableDatabase();
        //creating a list of generic type called Note
        allNotes = new ArrayList<>();
        String query = "SELECT * FROM "+DATABASE_TABLE;
        Cursor cursor = db.rawQuery(query,null);
        if(cursor.moveToFirst()){
            //i am going to pull all the data from the database and pass that data onto our listView
            do{

                //now creating a new note and save the data from the database by using cursor
                Note note = new Note();
                note.setID(cursor.getLong(0));
                note.setTitle(cursor.getString(1));
                note.setContent(cursor.getString(2));
                note.setDate(cursor.getString(3));
                note.setTime(cursor.getString(4));

                //adding this to lisView
                allNotes.add(note);

            }while(cursor.moveToNext());
        }
        return allNotes;
    }

    //updating the database
    //replacing the old data with the updated data set inside the database
    public int editNote(Note note){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_TITLE,note.getTitle());
        contentValues.put(KEY_CONTENT,note.getContent());
        contentValues.put(KEY_DATE,note.getDate());
        contentValues.put(KEY_TIME,note.getTime());
        return db.update(DATABASE_TABLE, contentValues,KEY_ID+"=?",new String[]{String.valueOf(note.getID())});
    }

    //the method will handel the deletion of the notes
    //this method will be called from the noteDetails.class
    void deleteNote(long id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DATABASE_TABLE,KEY_ID+"=?",new String[]{String.valueOf(id)});
        db.close();
    }

}
