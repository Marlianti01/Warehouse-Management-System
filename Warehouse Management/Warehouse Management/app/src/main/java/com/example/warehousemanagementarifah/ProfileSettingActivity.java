package com.example.warehousemanagementarifah;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;

public class ProfileSettingActivity extends AppCompatActivity {
    private EditText usernameEditText;
    private EditText phoneNumberEditText;
    private EditText birthdayEditText;
    private EditText emailEditText;
    private Button updateButton;
    private BottomNavigationView bottomNavigationView;
    private FloatingActionButton fabAddItem;

    private DatabaseHelper db;

    String userEmail ;

    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setting);

        // Initialize UI components
        usernameEditText = findViewById(R.id.usernameEditText);
        phoneNumberEditText = findViewById(R.id.phoneNumberEditText);
        birthdayEditText = findViewById(R.id.birthdayEditText);
        emailEditText = findViewById(R.id.emailEditText);
        updateButton = findViewById(R.id.updateButton);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        fabAddItem = findViewById(R.id.floatingActionButton2);

        db = new DatabaseHelper(this);
        userEmail = getIntent().getStringExtra("user_email");


        if(userEmail != null){
            Cursor cursor = db.getUserByEmail(userEmail);
            if(cursor != null && cursor.moveToFirst()){
                int usernameIndex = cursor.getColumnIndex("username");
                phoneNumberEditText.setText(cursor.getString(cursor.getColumnIndex("nophone")));
                birthdayEditText.setText(cursor.getString(cursor.getColumnIndex("birthday")));
                emailEditText.setText(cursor.getString(cursor.getColumnIndex("email")));
                if (usernameIndex != -1){
                    usernameEditText.setText(cursor.getString(usernameIndex));}

            }
        }

        birthdayEditText.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve user input
                String username = usernameEditText.getText().toString();
                String phoneNumber = phoneNumberEditText.getText().toString();
                String birthday = birthdayEditText.getText().toString();
                String email = emailEditText.getText().toString();

                db.updateUser(username, Integer.parseInt(phoneNumber), email, birthday);
                Toast.makeText(ProfileSettingActivity.this, "Success", Toast.LENGTH_SHORT).show();
            }
        });



        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.home) {
                    Intent intent = new Intent(ProfileSettingActivity.this, ListItemsActivity.class);
                    intent.putExtra("user_email", userEmail);
                    startActivity(intent);
                    return true;
                }  else if (item.getItemId() == R.id.profile) {
                    Intent intent = new Intent(ProfileSettingActivity.this, ProfileSettingActivity.class);
                    intent.putExtra("user_email", userEmail);
                    startActivity(intent);
                    return true;
                }
                return false;
            }
        });

        // Set the OnClickListener for the Floating Action Button (FAB)
        fabAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to AddItemActivity when FAB is clicked
                Intent intent = new Intent(ProfileSettingActivity.this, AddItem.class);
                intent.putExtra("user_email", userEmail);
                startActivity(intent);
            }
        });
    }
    private void showDatePickerDialog(){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public  void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay){
                        String formattedDate = selectedDay + "/" + (selectedMonth+1) + "/" +selectedYear;
                        birthdayEditText.setText(formattedDate);
                    }
                },
                year, month,day
        );

        datePickerDialog.show();
    }
}