package com.example.electriccalculatormobile;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database name and version
    private static final String DATABASE_NAME = "electricity_bill_db";
    private static final int DATABASE_VERSION = 1;

    // Table name and column names for the bills table
    private static final String TABLE_BILLS = "bills";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_MONTH = "month";
    private static final String COLUMN_UNITS = "units";
    private static final String COLUMN_TOTAL_CHARGES = "total_charges";
    private static final String COLUMN_REBATE = "rebate";
    private static final String COLUMN_FINAL_COST = "final_cost";

    // SQL statement to create the bills table
    private static final String CREATE_TABLE_BILLS = "CREATE TABLE " + TABLE_BILLS + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_MONTH + " TEXT,"
            + COLUMN_UNITS + " REAL,"
            + COLUMN_TOTAL_CHARGES + " REAL,"
            + COLUMN_REBATE + " REAL,"
            + COLUMN_FINAL_COST + " REAL" + ")";

    // Constructor for DatabaseHelper
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Execute the SQL statement to create the table
        try {
            db.execSQL(CREATE_TABLE_BILLS);
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error creating table: " + e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if it exists and create a new one
        try {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_BILLS);
            onCreate(db);
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error upgrading database: " + e.getMessage());
        }
    }

    // Method to insert a new bill into the database
    public long insertBill(Bill bill) {
        SQLiteDatabase db = this.getWritableDatabase(); // Get writable database instance
        long id = -1; // Initialize id to -1 (indicates failure)
        try {
            ContentValues values = new ContentValues(); // Create ContentValues to store key-value pairs
            values.put(COLUMN_MONTH, bill.getMonth());
            values.put(COLUMN_UNITS, bill.getUnits());
            values.put(COLUMN_TOTAL_CHARGES, bill.getTotalCharges());
            values.put(COLUMN_REBATE, bill.getRebate());
            values.put(COLUMN_FINAL_COST, bill.getFinalCost());
            id = db.insert(TABLE_BILLS, null, values); // Insert the row
            if (id == -1) {
                Log.e("DatabaseHelper", "Failed to insert bill."); // Log if insertion failed
            }
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error inserting bill: " + e.getMessage()); // Log any exception
        } finally {
            db.close(); // Close the database connection
        }
        return id; // Return the ID of the newly inserted row, or -1 if failed
    }

    // Method to get all bills from the database
    public List<Bill> getAllBills() {
        List<Bill> billList = new ArrayList<>(); // List to hold Bill objects
        String selectQuery = "SELECT  * FROM " + TABLE_BILLS; // SQL query to select all bills
        SQLiteDatabase db = this.getWritableDatabase(); // Get writable database instance
        Cursor cursor = null; // Cursor to iterate over query results
        try {
            cursor = db.rawQuery(selectQuery, null); // Execute the query
            // Loop through all rows and add to list
            if (cursor.moveToFirst()) {
                do {
                    Bill bill = new Bill(); // Create a new Bill object
                    // Populate Bill object with data from the cursor
                    bill.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
                    bill.setMonth(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MONTH)));
                    bill.setUnits(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_UNITS)));
                    bill.setTotalCharges(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_TOTAL_CHARGES)));
                    bill.setRebate(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_REBATE)));
                    bill.setFinalCost(cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_FINAL_COST)));
                    billList.add(bill); // Add bill to the list
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error in getAllBills: " + e.getMessage()); // Log any errors
            billList = new ArrayList<>(); // Return an empty list in case of error
        } finally {
            if (cursor != null) {
                cursor.close(); // Close the cursor
            }
            db.close(); // Close the database connection
        }
        return billList; // Return the list of bills
    }

    // Method to get a single bill by its ID
    public Bill getBillById(int id) {
        SQLiteDatabase db = this.getReadableDatabase(); // Get readable database instance
        Cursor cursor = null; // Cursor to iterate over query results
        Bill bill = null; // Bill object to return
        try {
            // Query the database for a specific bill by ID
            cursor = db.query(TABLE_BILLS, new String[]{COLUMN_ID, COLUMN_MONTH, COLUMN_UNITS, COLUMN_TOTAL_CHARGES, COLUMN_REBATE, COLUMN_FINAL_COST},
                    COLUMN_ID + "=?", new String[]{String.valueOf(id)}, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                // Populate Bill object with data from the cursor
                bill = new Bill(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MONTH)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_UNITS)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_TOTAL_CHARGES)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_REBATE)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_FINAL_COST)));
                bill.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)));
            }
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error getting bill by ID: " + e.getMessage()); // Log any errors
        } finally {
            if (cursor != null) {
                cursor.close(); // Close the cursor
            }
            db.close(); // Close the database connection
        }
        return bill; // Return the Bill object, or null if not found/error
    }
}