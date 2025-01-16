package com.example.warehousemanagementarifah;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SignIn extends AppCompatActivity {
    EditText email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin);

        email = findViewById(R.id.Email);
        password = findViewById(R.id.password);

        Button login = findViewById(R.id.signinButton);

        login.setOnClickListener(v->{
            String usermail = email.getText().toString().trim();
            String userpass = password.getText().toString().trim();

            DatabaseHelper db =new DatabaseHelper(SignIn.this);

            boolean isValid = db.ValidateUser(usermail, userpass);

            if(isValid){
                Toast.makeText(this, "Login Successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SignIn.this, ListItemsActivity.class);
                intent.putExtra("user_email", usermail);
                startActivity(intent);
            } else{
                Toast.makeText(this, "Login failed", Toast.LENGTH_SHORT).show();
            }
        });

        TextView signUpLink = findViewById(R.id.signUpLink);
        signUpLink.setOnClickListener(v->{
            Intent intent = new Intent(SignIn.this, SignUp.class);
            startActivity(intent);
        });


    }
}

