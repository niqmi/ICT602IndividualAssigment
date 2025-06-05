package com.example.electriccalculatormobile;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.util.Log;
import android.widget.Toast;

public class DetailActivity extends AppCompatActivity {

    private TextView monthTextView;
    private TextView unitsTextView;
    private TextView totalChargesTextView;
    private TextView rebateTextView;
    private TextView finalCostTextView;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Initialize UI elements by finding them from the layout
        monthTextView = findViewById(R.id.monthTextView);
        unitsTextView = findViewById(R.id.unitsTextView);
        totalChargesTextView = findViewById(R.id.totalChargesTextView);
        rebateTextView = findViewById(R.id.rebateTextView);
        finalCostTextView = findViewById(R.id.finalCostTextView);
        dbHelper = new DatabaseHelper(this); // Initialize the database helper

        // Get the bill ID passed from the previous activity (HistoryActivity)
        int billId = getIntent().getIntExtra("bill_id", -1); // Default value -1 if not found

        // Check if a valid bill ID was passed
        if (billId != -1) {
            // Retrieve the bill from the database using the ID
            Bill bill = dbHelper.getBillById(billId);

            // Check if the bill was successfully retrieved
            if (bill != null) {
                // Display the bill details in the respective TextViews
                monthTextView.setText("Month: " + bill.getMonth());
                unitsTextView.setText("Units: " + String.format("%.2f", bill.getUnits()) + " kWh");
                totalChargesTextView.setText("Total Charges: RM " + String.format("%.2f", bill.getTotalCharges()));
                rebateTextView.setText("Rebate: " + String.format("%.2f", bill.getRebate()) + "%");
                finalCostTextView.setText("Final Cost: RM " + String.format("%.2f", bill.getFinalCost()));
            } else {
                // Log an error if the bill with the given ID was not found
                Log.e("DetailActivity", "Bill with ID " + billId + " not found in database.");
                // Show a toast message to the user
                Toast.makeText(this, "Bill details not found.", Toast.LENGTH_SHORT).show();
                finish(); // Close the activity as there's no data to display
            }
        } else {
            // Log an error if no bill ID was passed to this activity
            Log.e("DetailActivity", "No bill ID passed to DetailActivity.");
            // Show a toast message to the user
            Toast.makeText(this, "Error: No bill selected.", Toast.LENGTH_SHORT).show();
            finish(); // Close the activity
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Close the database connection when the activity is destroyed to prevent leaks
        dbHelper.close();
    }
}