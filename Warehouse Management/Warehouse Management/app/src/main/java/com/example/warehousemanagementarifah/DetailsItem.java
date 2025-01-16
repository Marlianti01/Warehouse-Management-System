package com.example.warehousemanagementarifah;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;

public class DetailsItem extends AppCompatActivity {

    private TextView itemName, barcode, quantity, category, stockIn, location, description;
    private ImageButton editIcon, deleteIcon;
    private String imageUri;
    private DatabaseHelper db;
    private Button btnSave;
    String userEmail = "";
    private BottomNavigationView bottomNavigationView;
    private FloatingActionButton fabAdd;

    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_item);

        // Initialize Views
        itemName = findViewById(R.id.title_text);
        barcode = findViewById(R.id.item_barcode);
        quantity = findViewById(R.id.item_quantity);
        category = findViewById(R.id.item_category);
        stockIn = findViewById(R.id.item_stock_in_date);
        location = findViewById(R.id.item_location);
        description = findViewById(R.id.item_description);
        ImageView itemImage = findViewById(R.id.item_image);
        editIcon = findViewById(R.id.edit_icon);
        deleteIcon = findViewById(R.id.delete_icon);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        fabAdd = findViewById(R.id.floatingActionButton2);

        db = new DatabaseHelper(this);
        userEmail = getIntent().getStringExtra("user_email");

        // Retrieve Intent Data
        String itemBarcode = getIntent().getStringExtra("itemBarcode");

        if (itemBarcode != null) {
            Cursor cursor = db.getItemByBarcode(itemBarcode);
            if (cursor != null && cursor.moveToFirst()) {
                // Retrieve item details from the cursor
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("name"));
                String itemCategory = cursor.getString(cursor.getColumnIndex("category"));
                String itemQuantity = cursor.getString(cursor.getColumnIndex("qty"));
                String stockInDate = cursor.getString(cursor.getColumnIndex("stockIn"));
                String itemLocation = cursor.getString(cursor.getColumnIndex("location"));
                String itemDescription = cursor.getString(cursor.getColumnIndex("description"));
                imageUri = cursor.getString(cursor.getColumnIndex("image_uri"));

                // Set data to views
                barcode.setText("Barcode: " + itemBarcode);
                itemName.setText("Item Name: " + name);
                category.setText("Category: " + itemCategory);
                quantity.setText("Quantity: " + itemQuantity);
                stockIn.setText("Stock In Date: " + stockInDate);
                location.setText("Location: " + itemLocation);
                description.setText("Description: " + itemDescription);

                Glide.with(this)
                        .load(Uri.parse(imageUri))
                        .placeholder(R.drawable.image_placeholder)
                        .into(itemImage);
            }
        }

        // Edit Item
        editIcon.setOnClickListener(v -> {
            // Extract data from TextViews
            String currentBarcode = barcode.getText().toString().replace("Barcode: ", "");
            String currentName = itemName.getText().toString().replace("Item Name: ", "");
            String currentCategory = category.getText().toString().replace("Category: ", "");
            String currentQuantity = quantity.getText().toString().replace("Quantity: ", "");
            String currentStockIn = stockIn.getText().toString().replace("Stock In Date: ", "");
            String currentLocation = location.getText().toString().replace("Location: ", "");

            // Show the Update Dialog
            showDialogUpdate(currentBarcode, currentName, currentCategory, currentQuantity, currentStockIn, currentLocation);
        });

        // Delete Item
        deleteIcon.setOnClickListener(v -> {
            // Show confirmation dialog before deleting
            new AlertDialog.Builder(DetailsItem.this)
                    .setMessage("Are you sure you want to delete this item?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", (dialog, id) -> {
                        // Delete the item from the database using the barcode
                        boolean isDeleted = db.deleteItem(itemBarcode); // Assuming itemBarcode is the barcode of the item
                        if (isDeleted) {
                            Toast.makeText(DetailsItem.this, "Item Deleted Successfully", Toast.LENGTH_SHORT).show();


                            finish(); // Close the activity and go back to the list
                        } else {
                            Toast.makeText(DetailsItem.this, "Failed to Delete Item", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        });


        // Bottom Navigation
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.home) {
                Intent intent = new Intent(DetailsItem.this, ListItemsActivity.class);
                intent.putExtra("user_email", userEmail);
                startActivity(intent);
                return true;
            } else if (item.getItemId() == R.id.profile) {
                Intent intent = new Intent(DetailsItem.this, ProfileSettingActivity.class);
                intent.putExtra("user_email", userEmail);
                startActivity(intent);
                return true;
            }
            return false;
        });

        // Floating Action Button

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to AddItemActivity when FAB is clicked
                Intent intent = new Intent(DetailsItem.this, AddItem.class);
                intent.putExtra("user_email", userEmail);
                startActivity(intent);
            }
        });
    }

    private void showDialogUpdate(String barcode, String DialogitemName, String Dialogcategory, String Dialogqty, String DialogstockIn, String Dialoglocation) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_update_item, null);
        builder.setView(dialogView);

        EditText editBarcode = dialogView.findViewById(R.id.dialog_item_barcode);
        EditText editName = dialogView.findViewById(R.id.dialog_item_name);
        EditText editQty = dialogView.findViewById(R.id.dialog_item_qty);
        EditText editStockIn = dialogView.findViewById(R.id.dialog_item_stockIn);
        EditText editCategory = dialogView.findViewById(R.id.dialog_item_category);
        EditText editLocation = dialogView.findViewById(R.id.dialog_item_location);
        btnSave = dialogView.findViewById(R.id.dialog_btn_save);

        editStockIn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                showDatePickerDialog(editStockIn);
            }
        });

        // Populate fields with current data
        editBarcode.setText(barcode);
        editName.setText(DialogitemName);
        editQty.setText(Dialogqty);
        editStockIn.setText(DialogstockIn);
        editCategory.setText(Dialogcategory);
        editLocation.setText(Dialoglocation);

        AlertDialog dialog = builder.create();

        btnSave.setOnClickListener(v -> {
            String updatedName = editName.getText().toString().trim();
            String updatedQty = editQty.getText().toString().trim();
            String updatedStockIn = editStockIn.getText().toString().trim();
            String updatedCategory = editCategory.getText().toString().trim();
            String updatedLocation = editLocation.getText().toString().trim();

            // Validate that all fields are filled
            if (updatedName.isEmpty() || updatedQty.isEmpty() || updatedStockIn.isEmpty() ||
                    updatedCategory.isEmpty() || updatedLocation.isEmpty()) {
                Toast.makeText(this, "All fields must be filled", Toast.LENGTH_SHORT).show();
                return;
            }

            // Call updateItem method
            updateItem(barcode, updatedName, updatedQty, updatedStockIn, updatedCategory, updatedLocation);

            itemName.setText("Item Name: " + updatedName);
            quantity.setText("Quantity: " + updatedQty);
            stockIn.setText("Stock In Date: " + updatedStockIn);
            category.setText("Category: " + updatedCategory);
            location.setText("Location: " + updatedLocation);
            dialog.dismiss();
        });

        dialog.show();
    }
    private void updateItem(String barcode, String updatedName, String updatedQty, String updatedStockIn, String updatedCategory, String updatedLocation) {
        try {
            int quantity = Integer.parseInt(updatedQty); // Ensure quantity is an integer
            boolean success = db.updateItem(barcode, updatedName, updatedCategory, quantity, updatedStockIn, updatedLocation);
            if (success) {
                Toast.makeText(this, "Item updated successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Failed to update item", Toast.LENGTH_SHORT).show();
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Quantity must be a valid number", Toast.LENGTH_SHORT).show();
        }
    }

    private void showDatePickerDialog(EditText editStockIn){
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
                        editStockIn.setText(formattedDate);
                    }
                },
                year, month,day
        );

        datePickerDialog.show();
    }


}
