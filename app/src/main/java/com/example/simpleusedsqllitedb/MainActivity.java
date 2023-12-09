package com.example.simpleusedsqllitedb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.example.simpleusedsqllitedb.adapter.ContactAdapter;
import com.example.simpleusedsqllitedb.database.DatabaseHelper;
import com.example.simpleusedsqllitedb.database.entity.Contact;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ContactAdapter contactAdapter;
    private ArrayList<Contact> contactArrayList = new ArrayList<>();
    private RecyclerView recyclerView;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("My favorite contacts");

        recyclerView = findViewById(R.id.recyclerViewContact);
        db = new DatabaseHelper(this);

        contactArrayList.addAll(db.getAllContacts());

        contactAdapter = new ContactAdapter(
                this,
                contactArrayList,
                MainActivity.this
        );

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(contactAdapter);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addAndEditContacts(false, null, -1);
            }
        });
    }

    public void addAndEditContacts(final boolean isUpdated, final Contact contact, final int position) {
        if (isUpdated) {
            editContact(contact, position);
        } else {
            addContact();
        }
    }

    private void editContact(final Contact contact, final int position) {
        LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
        View view = layoutInflater.inflate(R.layout.layout_add_contact, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder.setView(view);

        final EditText newContactEditText = view.findViewById(R.id.name);
        final EditText contactEmailEditText = view.findViewById(R.id.email);

        newContactEditText.setText(contact.getName());
        contactEmailEditText.setText(contact.getEmail());

        alertDialogBuilder.setTitle("Edit Contact")
                .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        UpdateContact(newContactEditText.getText().toString(),
                                contactEmailEditText.getText().toString(), position);
                    }
                })
                .setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        DeleteContact(contact, position);
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
    private void addContact() {
        LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
        View view = layoutInflater.inflate(R.layout.layout_add_contact, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder.setView(view);

        final EditText newContact = view.findViewById(R.id.name);
        final EditText contactEmail = view.findViewById(R.id.email);
        alertDialogBuilder.setTitle("Add new contact")
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        CreateContact(newContact.getText().toString(),
                                contactEmail.getText().toString());
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void CreateContact(String name, String email) {
        long id = db.insertContact(name, email);
        Contact contact = db.getContact(id);
        if (contact != null) {
            contactArrayList.add(0, contact);
            contactAdapter.notifyDataSetChanged();
        }
    }

    private void UpdateContact(String name, String email, int position) {
        Contact contact = contactArrayList.get(position);

        contact.setEmail(email);
        contact.setName(name);

        db.updateContact(contact);

        contactArrayList.set(position, contact);
        contactAdapter.notifyDataSetChanged();
    }

    private void DeleteContact(Contact contact, int position) {
        contactArrayList.remove(position);
        db.deleteContact(contact);
        contactAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.actionAndSettings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
