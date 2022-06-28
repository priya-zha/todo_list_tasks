package com.example.todotasklist;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class login extends AppCompatActivity {
    EditText username, password;
    Button login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String get_name = username.getText().toString();
                String get_password = password.getText().toString();

                if(get_name.equals("testname") && get_password.equals("testpass")){
                    Intent i = new Intent(login.this,ListActivity.class);
                    startActivity(i);
                }
                else{
                    Toast.makeText(login.this, "Username or password incorrect", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}