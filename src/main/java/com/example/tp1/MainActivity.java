package com.example.tp1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private EditText nameLogin, telLogin;
    private Button buttonLogin;
    private DatabaseHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        SharedPreferences sharedPre = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
        String con = sharedPre.getString("connected", "false");
        Log.d("SharedPreferences", "Username: " + con);
        if(con.equals("true")){
            Intent intent = new Intent(MainActivity.this, ShowContact.class);
            startActivity(intent);
            finish();
        }
        setContentView(R.layout.activity_main);
        nameLogin = findViewById(R.id.nameLogin);
        telLogin = findViewById(R.id.telLogin);
        buttonLogin = findViewById(R.id.buttonLogin);
        dbHelper = new DatabaseHelper(this);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get user input
                String name = nameLogin.getText().toString().trim();
                String pass = telLogin.getText().toString().trim();

                // Validate the credentials against the database
                if (name.equals("sarra")&&pass.equals("sarra123")) {
                    // Successful login
//                    System.out.println("hi");
                    SharedPreferences sharedPref = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("connected", "true");
                    editor.apply();
                    Intent intent = new Intent(MainActivity.this, ShowContact.class);
                    startActivity(intent);
                    finish(); // Close the login activity
                } else {
                    // Show an error message if the credentials are incorrect
                    Toast.makeText(MainActivity.this, "Data incorrect", Toast.LENGTH_SHORT).show();
                }
            }
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

//        startActivity(new Intent(MainActivity.this,ShowContact.class));
    }
}