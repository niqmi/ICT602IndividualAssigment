package com.example.electriccalculatormobile;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText; // Keeping EditText for now, but TextInputEditText is used in XML
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ArrayAdapter;
import android.content.Intent;
import android.util.Log; // Import the Log class

import com.google.android.material.textfield.TextInputEditText; // Correct import for TextInputEditText

public class MainActivity extends AppCompatActivity {

    private TextInputEditText unitEditText; // Changed to TextInputEditText
    private Spinner monthSpinner;
    private TextInputEditText rebateEditText; // Changed to TextInputEditText
    private Button calculateButton;
    private TextView resultTextView;
    private DatabaseHelper dbHelper;
    private Button aboutButton;
    private Button historyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI elements by finding them from the layout
        // Note: Accessing TextInputEditText directly by its ID
        unitEditText = findViewById(R.id.unitEditText);
        monthSpinner = findViewById(R.id.monthSpinner);
        rebateEditText = findViewById(R.id.rebateEditText);
        calculateButton = findViewById(R.id.calculateButton);
        resultTextView = findViewById(R.id.resultTextView);
        aboutButton = findViewById(R.id.aboutButton);
        historyButton = findViewById(R.id.historyButton);

        // Initialize the database helper
        dbHelper = new DatabaseHelper(this);

        // Populate the month spinner with data from arrays.xml
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.months_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        monthSpinner.setAdapter(adapter);

        // Set an OnClickListener for the calculate button
        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateAndSave(); // Call the method to perform calculation and save data
            }
        });

        // Set an OnClickListener for the About button
        aboutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    // Create an Intent to start the AboutActivity
                    Intent intent = new Intent(MainActivity.this, AboutActivity.class);
                    startActivity(intent); // Start the AboutActivity
                } catch (Exception e) {
                    // Log any errors that occur when trying to start AboutActivity
                    Log.e("MainActivity", "Error starting AboutActivity: " + e.getMessage());
                    // Show a toast message to the user
                    Toast.makeText(MainActivity.this, "Failed to open About page.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Set an OnClickListener for the History button
        historyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    // Create an Intent to start the HistoryActivity
                    Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
                    startActivity(intent); // Start the HistoryActivity
                } catch (Exception e) {
                    // Log any errors that occur when trying to start HistoryActivity
                    Log.e("MainActivity", "Error starting HistoryActivity: " + e.getMessage());
                    // Show a toast message to the user
                    Toast.makeText(MainActivity.this, "Failed to open History page.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Method to calculate electricity bill and save it to the database
    private void calculateAndSave() {
        // Get input values from EditText fields and Spinner
        String unitStr = unitEditText.getText().toString().trim();
        String month = monthSpinner.getSelectedItem().toString();
        String rebateStr = rebateEditText.getText().toString().trim();

        // Validate user input
        if (unitStr.isEmpty()) {
            Toast.makeText(this, "Please enter electricity unit used.", Toast.LENGTH_SHORT).show();
            return; // Exit if units field is empty
        }
        if (rebateStr.isEmpty()) {
            Toast.makeText(this, "Please enter rebate percentage.", Toast.LENGTH_SHORT).show();
            return; // Exit if rebate field is empty
        }

        try {
            // Parse input strings to double
            double units = Double.parseDouble(unitStr);
            double rebate = Double.parseDouble(rebateStr);

            // Validate rebate percentage range
            if (rebate < 0 || rebate > 5) {
                Toast.makeText(this, "Rebate percentage must be between 0 and 5.", Toast.LENGTH_SHORT).show();
                return; // Exit if rebate is out of range
            }

            // Perform electricity bill calculation
            double totalCharges = calculateTotalCharges(units);
            double finalCost = totalCharges - (totalCharges * (rebate / 100.0));

            // Display the calculated results in the result TextView
            String result = "Total Charges: RM " + String.format("%.2f", totalCharges) + "\n" +
                    "Final Cost After Rebate: RM " + String.format("%.2f", finalCost);
            resultTextView.setText(result);

            // Attempt to store the bill data in the database
            try {
                Bill bill = new Bill(month, units, totalCharges, rebate, finalCost);
                long insertedId = dbHelper.insertBill(bill); // Insert the bill into the database
                if (insertedId != -1) {
                    Toast.makeText(this, "Data saved successfully.", Toast.LENGTH_SHORT).show(); // Show success toast
                } else {
                    Toast.makeText(this, "Failed to save data.", Toast.LENGTH_SHORT).show(); // Show failure toast
                }
            } catch (Exception e) {
                // Log any errors during database saving
                Log.e("MainActivity", "Error saving to database: " + e.getMessage());
                Toast.makeText(this, "Failed to save data.", Toast.LENGTH_SHORT).show(); // Show failure toast
            }

            // Clear the input fields after successful calculation and saving
            unitEditText.getText().clear();
            rebateEditText.getText().clear();

        } catch (NumberFormatException e) {
            // Handle cases where input is not a valid number
            Toast.makeText(this, "Invalid input. Please enter numeric values.", Toast.LENGTH_SHORT).show();
        }
    }

    // Method to calculate total charges based on electricity units
    private double calculateTotalCharges(double units) {
        double totalCharges = 0;
        // Apply different rates based on electricity consumption blocks
        if (units <= 200) {
            totalCharges = units * 0.218;
        } else if (units <= 300) {
            totalCharges = (200 * 0.218) + ((units - 200) * 0.334);
        } else if (units <= 600) {
            totalCharges = (200 * 0.218) + (100 * 0.334) + ((units - 300) * 0.516);
        } else {
            totalCharges = (200 * 0.218) + (100 * 0.334) + (300 * 0.516) + ((units - 600) * 0.546);
        }
        return totalCharges;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Close the database connection when the activity is destroyed
        try {
            dbHelper.close();
        } catch (Exception e) {
            Log.e("MainActivity", "Error closing database: " + e.getMessage());
        }
    }
}