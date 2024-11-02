package com.example.tp1;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EditContact extends AppCompatActivity {

    DatabaseHelper myDb;
    EditText editName, editTel,editNickname;
    ImageView btnUpdate;
    String userId;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);

        myDb = new DatabaseHelper(this);

        editName = findViewById(R.id.updateTextName);
        editTel = findViewById(R.id.updateTextTel);
        editNickname = findViewById(R.id.updateTextNickname);
        btnUpdate = findViewById(R.id.buttonUpdate);

        // Recevoir les données de l'utilisateur à modifier
        Intent intent = getIntent();
        userId = intent.getStringExtra("USER_ID");
        editName.setText(intent.getStringExtra("USER_NAME"));
        editTel.setText(intent.getStringExtra("USER_TEL"));
        editNickname.setText(intent.getStringExtra("USER_NICKNAME"));

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isValidPhoneNumber(editTel.getText().toString())||editName.getText().toString().isEmpty()) {
                    Toast.makeText(EditContact.this, "Data invalid", Toast.LENGTH_SHORT).show();
                    return;
                }
                myDb.updateData(userId, editName.getText().toString(), editTel.getText().toString(), editNickname.getText().toString());
                Intent intent = new Intent(EditContact.this, ShowContact.class);
                startActivity(intent);
                finish(); // Fermer l'activité après la mise à jour
            }
        });
    }
    private boolean isValidPhoneNumber(String phone) {
        return phone.matches("^\\d{8}$");
    }
}
