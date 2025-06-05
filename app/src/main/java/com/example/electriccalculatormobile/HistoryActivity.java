package com.example.electriccalculatormobile;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.content.Intent;
import android.view.View;
import java.util.List;
import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {

    private ListView historyListView;
    private DatabaseHelper dbHelper;
    private List<Bill> billList; // List to hold Bill objects retrieved from the database

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history); // Set the layout for this activity

        // Initialize UI elements
        historyListView = findViewById(R.id.historyListView);
        dbHelper = new DatabaseHelper(this); // Initialize the database helper

        // Get all bills from the database
        billList = dbHelper.getAllBills();
        List<String> billDisplayList = new ArrayList<>(); // List to hold formatted strings for display

        // Populate the list with formatted bill details (Month - Final Cost)
        for (Bill bill : billList) {
            billDisplayList.add(bill.getMonth() + " - RM " + String.format("%.2f", bill.getFinalCost()));
        }

        // Create an ArrayAdapter to display the bill details in the ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, billDisplayList);
        historyListView.setAdapter(adapter); // Set the adapter to the ListView

        // Set item click listener to open DetailActivity when a list item is tapped
        historyListView.setOnItemClickListener((parent, view, position, id) -> {
            // Get the selected bill object from the billList using the clicked position
            Bill selectedBill = billList.get(position);
            // Create an Intent to start the DetailActivity
            Intent intent = new Intent(HistoryActivity.this, DetailActivity.class);
            // Pass the ID of the selected bill to the DetailActivity using putExtra
            intent.putExtra("bill_id", selectedBill.getId());
            startActivity(intent); // Start the DetailActivity
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Close the database connection when the activity is destroyed to prevent leaks
        dbHelper.close();
    }
}