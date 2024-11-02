package com.example.tp1;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddContact extends AppCompatActivity {

    DatabaseHelper myDb;
    EditText editName, editTel,editNickname;
    ImageView btnSave,logout;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        myDb = new DatabaseHelper(this);
        editName = findViewById(R.id.editTextName);
        editTel = findViewById(R.id.editTextTel);
        editNickname = findViewById(R.id.editTextNickname);
        btnSave = findViewById(R.id.buttonSave);
        logout = findViewById(R.id.btnlogout);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isValidPhoneNumber(editTel.getText().toString())||editName.getText().toString().isEmpty()||editNickname.getText().toString().isEmpty()) {
                    Toast.makeText(AddContact.this, "Data invalid", Toast.LENGTH_SHORT).show();
                    return;
                }
                myDb.insertData(editName.getText().toString(), editTel.getText().toString(),editNickname.getText().toString());
                Intent intent = new Intent(AddContact.this, ShowContact.class);
                startActivity(intent);
                finish();
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPref = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("connected", "false");
                editor.apply();
                Intent intent = new Intent(AddContact.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
    private boolean isValidPhoneNumber(String phone) {
        return phone.matches("^\\d{8}$");
    }
}
