package com.example.warehousemanagementarifah;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {


    private Context context;
    private static final String DATABASE_NAME = "WarehouseManagement.db";
    private static final int DATABASE_VERSION = 5;
    //Column in database

    //User table
    private static final String USERTABLE = "user";
    private static String EMAIL = "email";
    private static String USERNAME = "username";
    private static String NOPHONE = "nophone";
    private static String BIRTHDAY = "birthday";
    private static String PASSWORD = "password";

    //Item table
    private static final String ITEMTABLE = "item";
    private static final String COL_BARCODE = "barcode";
    private static final String COL_ITEMNAME = "name";
    private static final String COL_CATEGORY = "category";
    private static final String COL_QTY = "qty";
    private static final String COL_STOCK_IN = "stockIn";
    private static final String COL_LOCATION = "location";
    private static final String COL_DESC ="description";
    private static final String COL_IMG_URI = "image_uri";


    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + USERTABLE +
                " ("  + USERNAME + " TEXT, " +
                NOPHONE + " INTEGER, "+
                EMAIL + " TEXT PRIMARY KEY, " +
                BIRTHDAY + " TEXT, " +
                PASSWORD + " TEXT);";
        db.execSQL(query);

        String itemQuery = "CREATE TABLE " + ITEMTABLE + " (" +
                COL_BARCODE + " TEXT PRIMARY KEY," +
                COL_ITEMNAME + " TEXT," +
                COL_CATEGORY + " TEXT," +
                COL_QTY + " INTEGER," +
                COL_STOCK_IN + " TEXT," +
                COL_LOCATION + " TEXT," +
                COL_DESC + " TEXT," +
                COL_IMG_URI + " TEXT)";
        db.execSQL(itemQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ITEMTABLE);
        db.execSQL("DROP TABLE IF EXISTS " + USERTABLE);
        onCreate(db);
    }

    boolean addUser(String username,int nophone, String email, String birthday,  String password){
        SQLiteDatabase db = this.getWritableDatabase();
        // cv will store data from the application
        ContentValues cv = new ContentValues();
        cv.put(USERNAME, username);
        cv.put(NOPHONE, nophone);
        cv.put(EMAIL, email);
        cv.put(BIRTHDAY, birthday);
        cv.put(PASSWORD, password);
        long result = db.insert(USERTABLE, null, cv);

        return result != -1;
    }

    boolean ValidateUser(String email, String password){
        SQLiteDatabase db = this.getReadableDatabase();
        String validate = "SELECT * FROM "+ USERTABLE +" WHERE " + EMAIL + " = ? AND " + PASSWORD + " = ?";

        Cursor cursor = db.rawQuery(validate, new String[]{email, password});

        //if user account exist
        boolean isValid = cursor.getCount() > 0;
        cursor.close();

        return isValid;
    }

    public boolean updateUser(String username,int nophone, String email, String birthday){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(USERNAME, username);
        cv.put(NOPHONE, nophone);
        cv.put(BIRTHDAY, birthday);

        Cursor cursor = db.rawQuery("SELECT * FROM user where email = ?", new String[]{email});
        if(cursor.getCount()>0) {
            long result = db.update(USERTABLE, cv, "email=?", new String[]{email});
            return result != -1;
        }
        else {
            return false;
        }
    }

    public Cursor getUserByEmail(String email){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("SELECT * FROM user WHERE email = ?",new String[]{email});
    }


    public boolean addItem(String barcode, String itemName, String category, int qty,
                           String stockIn, String location, String desc, String imageUri){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_BARCODE, barcode);
        cv.put(COL_ITEMNAME,itemName);
        cv.put(COL_CATEGORY, category);
        cv.put(COL_QTY, qty);
        cv.put(COL_STOCK_IN, stockIn);
        cv.put(COL_LOCATION, location);
        cv.put(COL_DESC, desc);
        cv.put(COL_IMG_URI, imageUri);

        long result = db.insert(ITEMTABLE,null, cv);
        return result != -1;
    }

    public Cursor getAllItems(){
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + ITEMTABLE,null);
    }

    public boolean updateItem(String barcode, String itemName, String category, int qty,
                              String stockIn, String location){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_ITEMNAME,itemName);
        cv.put(COL_CATEGORY, category);
        cv.put(COL_QTY, qty);
        cv.put(COL_STOCK_IN, stockIn);
        cv.put(COL_LOCATION, location);

        return db.update(ITEMTABLE, cv, COL_BARCODE + " = ?", new String[]{barcode}) > 0;

    }

    public boolean deleteItem(String barcode) {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete the item using its barcode (assuming barcode is the unique identifier)
        int rowsAffected = db.delete(ITEMTABLE, "barcode = ?", new String[]{barcode});
        db.close();
        return rowsAffected > 0; // Returns true if at least one row was deleted
    }


    public Cursor getItemByBarcode(String barcode){
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(ITEMTABLE, null, "barcode = ?", new String[]{barcode}, null, null, null);
    }



    public String getBarcodeColumn() {
        return COL_BARCODE;
    }
    public String getItemNameColumn() {
        return COL_ITEMNAME;
    }

    public String getCategoryColumn() {
        return COL_CATEGORY;
    }

    public String getQtyColumn() {
        return COL_QTY;
    }

    public String getStockInColumn() {
        return COL_STOCK_IN;
    }

    public String getLocationColumn() {
        return COL_LOCATION;
    }

    public String getDescColumn() {
        return COL_DESC;
    }

    public String getImageUriColumn() {
        return COL_IMG_URI;
    }

    // Getter methods for user table columns

    public String getEmailColumn() {
        return EMAIL;
    }

    public String getUsernameColumn() {
        return USERNAME;
    }

    public String getNophoneColumn() {
        return NOPHONE;
    }

    public String getBirthdayColumn() {
        return BIRTHDAY;
    }

    public String getPasswordColumn() {
        return PASSWORD;
    }


}