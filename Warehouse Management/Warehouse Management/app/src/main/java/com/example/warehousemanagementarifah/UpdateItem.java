package com.example.warehousemanagementarifah;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class UpdateItem extends AppCompatActivity {


    private static final int IMAGE_PICK_CODE = 1000;
    private ImageView uploadImageView, tickIconView;
    private EditText itemName, itemBarcodes, itemCategory, itemQuantity, stockInDate, itemLocation, itemDescription;
    private Uri imageUri = null;
    private DatabaseHelper db;
    private String itemBarcode;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_item);

        uploadImageView = findViewById(R.id.upload_image);
        tickIconView = findViewById(R.id.tick_icon);
        itemName = findViewById(R.id.item_name);
        itemBarcodes = findViewById(R.id.item_barcode);
        itemCategory = findViewById(R.id.item_category);
        itemQuantity = findViewById(R.id.item_quantity);
        stockInDate = findViewById(R.id.item_stock_in_date);
        itemLocation = findViewById(R.id.item_location);
        itemDescription = findViewById(R.id.item_description);

        db = new DatabaseHelper(this);
        itemBarcode = getIntent().getStringExtra("itemBarcode");

        String imageUriString = getIntent().getStringExtra("imageUri");
        if (imageUriString != null){
            imageUri = Uri.parse(imageUriString);
            uploadImageView.setImageURI(imageUri);
        }

        loadItemData();


        uploadImageView.setOnClickListener(v -> openImagePicker());

        tickIconView.setOnClickListener(v -> updateForm());
    }


    @SuppressLint("Range")
    private void loadItemData() {
        if(itemBarcode != null){
            Cursor cursor = db.getItemByBarcode(itemBarcode);
            if(cursor.moveToFirst()){
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String category = cursor.getString(cursor.getColumnIndex("category"));
                String qty = cursor.getString(cursor.getColumnIndex("qty"));
                String stockIn = cursor.getString(cursor.getColumnIndex("stockIn"));
                String location = cursor.getString(cursor.getColumnIndex("location"));
                String desc = cursor.getString(cursor.getColumnIndex("description"));
                String storedImage = cursor.getString(cursor.getColumnIndex("image_uri"));

                itemName.setText(name);
                itemCategory.setText(category);
                itemQuantity.setText(qty);
                stockInDate.setText(stockIn);
                itemLocation.setText(location);
                itemDescription.setText(desc);

                if (storedImage != null){
                    imageUri = Uri.parse(storedImage);
                    uploadImageView.setImageURI(imageUri);
                }
            }
        }
    }

    // Open the image picker for image upload
    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_CODE);
    }

    // Handle the result of image selection
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_PICK_CODE && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            uploadImageView.setImageURI(imageUri); // Display the selected image
        }
    }

    // Update the form data
    private void updateForm() {
        // Validate input fields

        String name = itemName.getText().toString().trim();
        String category = itemCategory.getText().toString().trim();
        String stockIn = stockInDate.getText().toString().trim();
        int quantity = Integer.parseInt(itemQuantity.getText().toString().trim());
        String location = itemLocation.getText().toString().trim();
        String description = itemDescription.getText().toString().trim();
        //String imageUri = imageUri.toString();


        if (name.isEmpty() || itemBarcode == null || category.isEmpty() || quantity == 0 || stockIn.isEmpty() ||
                location.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        String imageUriString =imageUri != null ? imageUri.toString() : null;

        boolean isUpdated = db.updateItem(

                itemBarcode,
                name,
                category,
                quantity,
                stockIn,
                location

        );

        if (isUpdated){
            Toast.makeText(this, "Item updated successfully!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(UpdateItem.this, DetailsItem.class);
            intent.putExtra("itemBarcode", itemBarcodes.getText().toString());
            startActivity(intent);

        }
        else {
            Toast.makeText(this, "update Failed", Toast.LENGTH_SHORT).show();
        }

    }
}

