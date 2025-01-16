package com.example.warehousemanagementarifah;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ListItemsActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private RecyclerView recyclerView;
    private ItemAdapter adapter;
    private DatabaseHelper db;
    private FloatingActionButton fabAdd;
    private List<Item> itemList;
    String userEmail = "";
    //String barcode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_items);

        userEmail = getIntent().getStringExtra("user_email");
      //  barcode = getIntent().getStringExtra("itemBarcode");

        // Initialize views
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        fabAdd = findViewById(R.id.floatingActionButton2);
        recyclerView = findViewById(R.id.recyclerView);

        // Set up RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ItemAdapter(itemList, userEmail);
        recyclerView.setAdapter(adapter);

        // Initialize database helper and item list
        db = new DatabaseHelper(this);
        itemList = new ArrayList<>();

        // Fetch items from the database and update the RecyclerView
        populateItemListFromDatabase();

        // Set the items in the adapter
        adapter.setItems(itemList);

        // Bottom navigation menu setup
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.home) {

                    Intent intent = new Intent(ListItemsActivity.this, ListItemsActivity.class);
                    intent.putExtra("user_email", userEmail);
                    startActivity(intent);
                    return true;
                } else if (item.getItemId() == R.id.profile) {

                    Intent intent = new Intent(ListItemsActivity.this, ProfileSettingActivity.class);
                    intent.putExtra("user_email", userEmail);
                    startActivity(intent);

                    return true;
                }
                return false;
            }
        });

        // Floating action button to add new item
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListItemsActivity.this, AddItem.class);
                intent.putExtra("user_email", userEmail);
                startActivity(intent);
            }
        });
    }

    // Method to populate the item list from the database
    private void populateItemListFromDatabase() {
        // Fetch all items from the database
        Cursor cursor = db.getAllItems();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int barcodeIndex = cursor.getColumnIndex(db.getBarcodeColumn());
                int nameIndex = cursor.getColumnIndex(db.getItemNameColumn());
                int categoryIndex = cursor.getColumnIndex(db.getCategoryColumn());
                int qtyIndex = cursor.getColumnIndex(db.getQtyColumn());
                int stockInIndex = cursor.getColumnIndex(db.getStockInColumn());
                int locationIndex = cursor.getColumnIndex(db.getLocationColumn());
                int descIndex = cursor.getColumnIndex(db.getDescColumn());
                int imageUriIndex = cursor.getColumnIndex(db.getImageUriColumn());

                // Ensure the column indices are valid
                if (nameIndex >= 0 && categoryIndex >= 0 && qtyIndex >= 0 &&
                        stockInIndex >= 0 && locationIndex >= 0 && descIndex >= 0 &&
                        imageUriIndex >= 0) {
                    // Retrieve data from the cursor
                    String barcode = cursor.getString(barcodeIndex);
                    String name = cursor.getString(nameIndex);
                    int qty = Integer.parseInt(cursor.getString(qtyIndex));
                    String location = cursor.getString(locationIndex);
                    String storedImage = cursor.getString(imageUriIndex);

                    // Create Item object and add it to the list
                    itemList.add(new Item(barcode,name, qty, location, storedImage));
                }
            } while (cursor.moveToNext());
            cursor.close();
        }
    }
}