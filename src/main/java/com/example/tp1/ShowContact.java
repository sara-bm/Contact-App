package com.example.tp1;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ShowContact extends AppCompatActivity {
    DatabaseHelper myDb;
    ImageView imgAddUser,logout;
    RecyclerView listViewUsers;
    SearchView searchView;
//    UserAdapter userAdapter;
    ArrayList<User> userList;
    ContactRecyclerAdapter adapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_contact);

        myDb = new DatabaseHelper(this);
        imgAddUser = findViewById(R.id.buttonAddUser1);
        logout = findViewById(R.id.buttonlogout);
        searchView=findViewById(R.id.search);
        listViewUsers = findViewById(R.id.rv);
        listViewUsers.setLayoutManager(new LinearLayoutManager(this));
        userList = new ArrayList<>();
        ld(); // load userobject into userList
        adapter = new ContactRecyclerAdapter(this, userList, myDb);
        listViewUsers.setAdapter(adapter);
        imgAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShowContact.this, AddContact.class);
                startActivity(intent);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPref = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("connected", "false");
                editor.apply();
                Intent intent = new Intent(ShowContact.this, MainActivity.class);
                startActivity(intent);
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.i("searching",newText);
                adapter.getFilter().filter(newText);
                return false;
            }
        });
    }
    public void ld(){
        userList.clear();
        Cursor res = myDb.getAllData();
        if (res.getCount() == 0) {
            return;
        }
        while (res.moveToNext()) {
            Log.i("azerty",""+res.getCount());
            Log.i("azerty",""+res.getCount());
            int id = res.getInt(0);
            String name = res.getString(1);
            String phone = res.getString(2);
            String nickname = res.getString(3);
            userList.add(new User(id, name, phone,nickname));
        }
    }
    public void loadData() {
        userList.clear();
        Cursor res = myDb.getAllData();
        if (res.getCount() == 0) {
            return;
        }
        while (res.moveToNext()) {
            Log.i("azerty",""+res.getCount());
            Log.i("azerty",""+res.getCount());
            int id = res.getInt(0);
            String name = res.getString(1);
            String phone = res.getString(2);
            String nickname = res.getString(3);
            userList.add(new User(id, name, phone,nickname));
        }
        adapter.notifyDataSetChanged();
    }
}