package com.example.electriccalculatormobile;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.text.Html;
import android.text.method.LinkMovementMethod;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        // Find the TextView for displaying about information
        TextView aboutTextView = findViewById(R.id.aboutTextView);

        // Define the about text with HTML formatting for bolding and a clickable link
        String aboutText = "<p><strong>Student Information:</strong></p>" +
                "<p>Full Name: Muhammad Aniq Najmi Bin Hasrizam</p>" +
                "<p>Student ID: 2023141423</p>" +
                "<p>Course Code: ICT602</p>" +
                "<p>Course Name: Mobile Technology And Developemnt</p>" +
                "<p>Copyright &copy; 2025 Aniq. All rights reserved.</p>" +
                "<p>Application Website: <a href=\"https://github.com/yourusername/yourrepository\">" +
                "GitHub Repository</a></p>";
        // Set the HTML formatted text to the TextView
        aboutTextView.setText(Html.fromHtml(aboutText, Html.FROM_HTML_MODE_COMPACT));
        // Make the link clickable
        aboutTextView.setMovementMethod(LinkMovementMethod.getInstance());
    }
}