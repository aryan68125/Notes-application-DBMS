package com.aditya.note;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.List;

//this class is responsible to set the items inside the recycler View by use of Adapter
public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder>{

    LayoutInflater inflater;
    List<Note> notes;

    //creating Adapter constructor so that we can pass the data from mainActivity to the list of note titles that is to be shown in the recycler View
    Adapter(Context context,List<Note> notes){
        //setting up our inflater
        this.inflater= LayoutInflater.from(context);
        this.notes = notes;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custome_list_view,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter.ViewHolder holder, int position) {

        //setting up the textViews with the pushed data from the database
        String title = notes.get(position).getTitle();
        String date = notes.get(position).getDate();
        String time = notes.get(position).getTime();
        //now we can bind the pulled data
        //TextView notesTitle;
        holder.notesTitle.setText(title);
        //TextView Date;
        holder.Date.setText(date);
        //TextView Time;
        holder.Time.setText(time);
    }

    @Override
    public int getItemCount() {
        //returning the size of the notes
        return notes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        //Binding our data
        //pulling data from notes and pushing data to their respective textViews
        TextView notesTitle;
        TextView Date;
        TextView Time;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            notesTitle = itemView.findViewById(R.id.noteTitle);
            Date = itemView.findViewById(R.id.date);
            Time = itemView.findViewById(R.id.time);
            //here we can handle the click on our recycler View
            //using the itemView we can set an onClickListener on our RecyclerView
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(),noteDetails.class);
                    //notes.get(getAdapterPosition()).getID() it will return the id of the item clicked on the RecyclerView
                    intent.putExtra("ID", notes.get(getAdapterPosition()).getID());
                    v.getContext().startActivity(intent);
                }
            });
        }
    }
}
