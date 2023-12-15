package com.innoveworkshop.gametest;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class GameOver extends AppCompatActivity {
    TextView tvPoints;
    ImageView ivNewHighest;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set the content view to the layout defined in game_over.xml
        setContentView(R.layout.game_over);

        // Initialize ImageView and TextView by finding them in the layout
        ivNewHighest = findViewById(R.id.ivNewHeighest);
        tvPoints = findViewById(R.id.tvPoints);

        // retrieve the points passed from the previous activity
        int points = getIntent().getExtras().getInt("points");

        // check if the points are equal to 240, and if so, make the new highest ImageView visible
        if (points == 240) {
            ivNewHighest.setVisibility(View.VISIBLE);
        }

        tvPoints.setText("" + points);
    }

    // method to restart the game when the restart button is clicked
    public void restart(View view) {
        Intent intent = new Intent(GameOver.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    //method to exit the game when the exit button is clicked
    public void exit(View view) {
        finish();
    }
}
