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

public class SignUp extends AppCompatActivity {

    Button createUser;
    EditText setPassword;
    EditText retypePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        createUser = findViewById(R.id.createUser);
        setPassword = findViewById(R.id.setPassword);
        retypePassword = findViewById(R.id.retypePassword);

        createUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(setPassword.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Password can not be empty", Toast.LENGTH_LONG).show();
                    return;
                }
                if(setPassword.getText().toString().length() > 12 || setPassword.getText().toString().length() < 8) {
                    Toast.makeText(getApplicationContext(), "Password must between 8 to 12 characters", Toast.LENGTH_LONG).show();
                    return;
                }
                if(setPassword.getText().toString().equals(retypePassword.getText().toString())) {
                    if(!Authenticator.authenticate(setPassword.getText().toString())) {
                        Toast.makeText(getApplicationContext(), "Failed to sign up\nPlease try again", Toast.LENGTH_LONG).show();
                        return;
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "Password is not the same", Toast.LENGTH_LONG).show();
                    return;
                }

                new SecureNote(setPassword.getText().toString().getBytes());

                Intent intent = new Intent(SignUp.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
