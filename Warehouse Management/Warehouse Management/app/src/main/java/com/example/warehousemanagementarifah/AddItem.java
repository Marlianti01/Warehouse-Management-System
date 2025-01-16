package com.example.warehousemanagementarifah;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;

public class AddItem extends AppCompatActivity {

    private static final int IMAGE_PICK_CODE = 1000;
    private ImageView uploadImageView, tickIconView;
    private EditText itemName, itemBarcode, itemCategory, itemQuantity, stockInDate, itemLocation, itemDescription;
    private Uri imageUri = null;
    private DatabaseHelper db;
    private BottomNavigationView bottomNavigationView;
    private FloatingActionButton fabAddItem;
    String userEmail;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

         db  = new DatabaseHelper(this);
         userEmail = getIntent().getStringExtra("user_email");

        // Initialize views
        uploadImageView = findViewById(R.id.upload_image);
        tickIconView = findViewById(R.id.tick_icon);
        itemName = findViewById(R.id.item_name);
        itemBarcode = findViewById(R.id.item_barcode);
        itemCategory = findViewById(R.id.item_category);
        itemQuantity = findViewById(R.id.item_quantity);
        stockInDate = findViewById(R.id.item_stock_in_date);
        itemLocation = findViewById(R.id.item_location);
        itemDescription = findViewById(R.id.item_description);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        fabAddItem = findViewById(R.id.floatingActionButton2);



        stockInDate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
            });


        // Set up image upload functionality
        uploadImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagePicker();
            }
        });

        // Set up the tick icon click listener to submit the form
        tickIconView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitForm();
            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.home) {
                    Intent intent = new Intent(AddItem.this, ListItemsActivity.class);
                    intent.putExtra("user_email", userEmail);
                    startActivity(intent);
                    return true;
                }  else if (item.getItemId() == R.id.profile) {
                    Intent intent = new Intent(AddItem.this, ProfileSettingActivity.class);
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
                Intent intent = new Intent(AddItem.this, AddItem.class);
                intent.putExtra("user_email", userEmail);
                startActivity(intent);
            }
        });
    }

    // Open the image picker for image upload
    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_CODE);
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
                        /*String formattedDate = selectedDay + "/" + (selectedMonth+1) + "/" +selectedYear;
                        stockInDate.setText(formattedDate);*/
                        if (selectedDay > 0) {
                            String formattedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                            stockInDate.setText(formattedDate);
                        } else {
                            // Handle any invalid day scenario if necessary
                            Toast.makeText(AddItem.this, "Invalid day selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                year, month,day
        );

        datePickerDialog.show();
    }

    // Handle the result of image selection
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_PICK_CODE && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            uploadImageView.setImageURI(imageUri);
        }
    }

    // Submit the form data
    private void submitForm() {
        // Validate input fields
        String name = itemName.getText().toString().trim();
        String barcode = itemBarcode.getText().toString().trim();
        String category = itemCategory.getText().toString().trim();
        int quantity = Integer.parseInt(itemQuantity.getText().toString().trim());
        String stockIn = stockInDate.getText().toString().trim();
        String location = itemLocation.getText().toString().trim();
        String description = itemDescription.getText().toString().trim();
        String imageUriString = (imageUri != null) ? imageUri.toString() : null;
        if (name.isEmpty() || barcode.isEmpty() || category.isEmpty() || quantity == 0 || stockIn.isEmpty() ||
                location.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean isItemAdded = db.addItem(barcode, name, category, quantity, stockIn, location, description, imageUriString);

        if (isItemAdded){
            Toast.makeText(this, "successfully Added", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(AddItem.this, ListItemsActivity.class);
            intent.putExtra("itemBarcode", barcode);
            intent.putExtra("user_email",userEmail);
            startActivity(intent);
        }
        else {
            Toast.makeText(this, "Failed to added", Toast.LENGTH_SHORT).show();
        }



        resetForm();
    }



    private void resetForm() {
        itemName.setText("");
        itemBarcode.setText("");
        itemCategory.setText("");
        itemQuantity.setText("");
        itemLocation.setText("");
        itemDescription.setText("");
        uploadImageView.setImageResource(R.drawable.ic_upload_image); // Reset image
    }

}

