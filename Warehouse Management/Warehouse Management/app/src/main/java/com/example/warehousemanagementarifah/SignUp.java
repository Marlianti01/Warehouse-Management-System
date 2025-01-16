package com.example.warehousemanagementarifah;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class SignUp extends AppCompatActivity {

    EditText username, nophone, email, birthday, password, comPass;
    Button buttonreg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        username = findViewById(R.id.username);
        nophone = findViewById(R.id.nophone);
        email = findViewById(R.id.Email);
        birthday = findViewById(R.id.birthday);
        password = findViewById(R.id.password);
        comPass = findViewById(R.id.comPass);

        buttonreg = findViewById(R.id.register);

        birthday.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(SignUp.this,
                        (view, selectedYear, selectedMonth, selectedDay) -> {

                            String date = String.format("%02d", selectedDay) + "/" +
                                    String.format("%02d", selectedMonth + 1) + "/" +
                                    selectedYear;

                            birthday.setText(date);

                        }, year, month, day);

                datePickerDialog.show();

            }
        });

        buttonreg.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (!password.getText().toString().equals(comPass.getText().toString())) {
                    Toast.makeText(SignUp.this, "Password not match!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (username.getText().toString().isEmpty() || nophone.getText().toString().isEmpty() || email.getText().toString().isEmpty() ||
                        birthday.getText().toString().isEmpty() || password.getText().toString().isEmpty() || comPass.getText().toString().isEmpty()) {
                    Toast.makeText(SignUp.this, "Please fill all the field", Toast.LENGTH_SHORT).show();
                    return;
                }


                DatabaseHelper myDB = new DatabaseHelper(SignUp.this);

                boolean isInsert = myDB.addUser(username.getText().toString().trim(), Integer.valueOf(nophone.getText().toString().trim()),
                        email.getText().toString().trim(), birthday.getText().toString().trim(), password.getText().toString().trim());

                if (isInsert) {
                    Toast.makeText(SignUp.this, "Successfully Registered!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignUp.this, SignIn.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(SignUp.this, "Register Failed :(", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}