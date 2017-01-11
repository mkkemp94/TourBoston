package com.example.android.tourboston;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    // The username of the user
    public static String username;

    // Clicking this button sends the user to the exploration activity
    private Button buttonToExploration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonToExploration = (Button) findViewById(R.id.button_to_exploration);
        buttonToExploration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToExplorationActivity();
            }
        });
    }

    /**
     * On button click, send the user to the exploration activity
     */
    private void goToExplorationActivity() {

        // Set the edit text to be the username of the user
        TextInputEditText editName = (TextInputEditText) findViewById(R.id.edit_name);
        username = editName.getText().toString();

        if (username.isEmpty()) {
            Toast.makeText(this, "Please enter your name to begin.", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(this, ExplorationActivity.class);
            startActivity(intent);
        }
    }


}
