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
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.simpleusedsqllitedb.database.DatabaseHelper;
import com.example.simpleusedsqllitedb.database.entity.Contact;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
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

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
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

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                addAndEditContacts(false, null, -1);
            }
        });


    }

    public void addAndEditContacts(final boolean isUpdated, final Contact contact, final int position) {
        LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
        View view = layoutInflater.inflate(R.layout.layout_add_contact, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder.setView(view);

        TextView contactTitle = view.findViewById(R.id.new_contact_title);
        final EditText newContact = view.findViewById(R.id.name);
        final EditText contactEmail = view.findViewById(R.id.email);

        contactTitle.setText(!isUpdated ? "Add new contact" : "Edit Contact");

        if (isUpdated && contact != null) {
            newContact.setText(contact.getName());
            contactEmail.setText(contact.getEmail()); // Fix: Use contactEmail instead of newContact
        }

        alertDialogBuilder.setTitle(!isUpdated ? "Add new contact" : "Edit Contact")
                .setPositiveButton(isUpdated ? "Update" : "Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        if (isUpdated) {
                            DeleteContact(contact, position);
                        } else {
                            dialogInterface.cancel();
                        }
                    }
                });

        final AlertDialog alertDialog = alertDialogBuilder.create(); // Fix: Create the AlertDialog using alertDialogBuilder
        alertDialog.show();

        alertDialog.getButton(alertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(newContact.getText().toString())) {
                    Toast.makeText(MainActivity.this, "Please enter a name", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    alertDialog.dismiss();
                }
                if (isUpdated && contact != null) { // Fix: Check if contact is not null
                    UpdateContact(newContact.getText().toString(), contactEmail.getText().toString(), position);
                } else {
                    CreateContact(newContact.getText().toString(), contactEmail.getText().toString());
                }
            }
        });
    }

    private void CreateContact(String name, String email) {
        long id = db.insertContact(name,email);
        Contact contact = db.getContact(id);
        if (contact != null){
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
        int id  = item.getItemId();

        if (id == R.id.actionAndSettings){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}