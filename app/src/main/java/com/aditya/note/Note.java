package com.aditya.note;

public class Note {
    //this class will hold the data from add note activity and then pass it to the SQLite database
    //it will also be used to enter or retrieve the data from the database
    private long ID;
    private String title,content,date,time;
    //creating a construction so that we can pass the data from other activities
    Note(){

    }
    //before saving the data to our database
    Note(String title,String content,String date,String time){

        this.title = title;
        this.content = content;
        this.date = date;
        this.time = time;
    }
    //this constructer will handel data after its been saved in the database
    //it will handle the data when we are editing or retieveing our note
    Note(long id,String title,String content,String date,String time){
        this.ID = id;
        this.title = title;
        this.content = content;
        this.date = date;
        this.time = time;
    }

    //now we will need getter and setter so that we can individually set the ID, title,content,date and time

    public long getID() {
        return ID;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
