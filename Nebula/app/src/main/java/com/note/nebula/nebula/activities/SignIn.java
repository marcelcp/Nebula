package com.note.nebula.nebula.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.note.nebula.nebula.R;
import com.note.nebula.nebula.apps_security.Authenticator;
import com.note.nebula.nebula.apps_security.SecureNote;

public class SignIn extends AppCompatActivity {

    Button login;
    EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        login = findViewById(R.id.login);
        password = findViewById(R.id.password);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!Authenticator.authenticate(password.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Failed to sign in. Please try again.", Toast.LENGTH_LONG).show();
                    return;
                }

                new SecureNote(password.getText().toString().getBytes());

                Intent intent = new Intent(SignIn.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
