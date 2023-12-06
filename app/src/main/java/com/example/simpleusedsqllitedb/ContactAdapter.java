package com.example.simpleusedsqllitedb;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.simpleusedsqllitedb.database.entity.Contact;

import java.security.PublicKey;
import java.util.ArrayList;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.MyViewHolder> {


    private Context context;
    private ArrayList<Contact> listOfContacts;
    private MainActivity mainActivity;

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView name;
        public TextView email;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.name = itemView.findViewById(R.id.name);
            this.email =  itemView.findViewById(R.id.email);
        }


    }
    public ContactAdapter(Context context, ArrayList<Contact> listOfContacts, MainActivity mainActivity){
        this.context = context;
        this.listOfContacts = listOfContacts;
        this.mainActivity = mainActivity;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_list_item,parent,false);

    return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int positions) {
        final Contact contact = listOfContacts.get(positions);
        holder.name.setText(contact.getName());
        holder.email.setText(contact.getEmail());

        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mainActivity.addAndEditContacts(true, contact, positions);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listOfContacts.size();
    }
}
