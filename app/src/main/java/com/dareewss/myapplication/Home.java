package com.dareewss.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Home extends AppCompatActivity {

    private TextView tvWeightValue;
    private TextView tvHeightValue;
    private TextView tvSleepValue;
    private TextView tvBMIValue;
    private TextView tvBMILabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        tvWeightValue = findViewById(R.id.tvWeightValue);
        tvHeightValue = findViewById(R.id.tvHeightValue);
        tvSleepValue  = findViewById(R.id.tvSleepValue);
        tvBMIValue    = findViewById(R.id.tvBMIValue);
        tvBMILabel    = findViewById(R.id.tvBMILabel);

        SharedPreferences sharedPreferences = getSharedPreferences("BodyData", MODE_PRIVATE);

        boolean setUp = sharedPreferences.getBoolean("Set", false);
        if (!setUp) {
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
            finish();
            return;
        }

        float weight = sharedPreferences.getFloat("Weight", 0);
        float height = sharedPreferences.getFloat("Height", 0);
        int sleep  = sharedPreferences.getInt("Sleep", 0);


        tvWeightValue.setText(weight + " kg");
        tvHeightValue.setText(height + " cm");
        tvSleepValue.setText(String.valueOf(sleep));

        if (height > 0) {
            float heightInMeters = height / 100f;
            float bmi = weight / (heightInMeters * heightInMeters);
            tvBMIValue.setText(String.format("%.1f", bmi));

            if (bmi < 18.5) {
                tvBMILabel.setText("Underweight");
            } else if (bmi < 25.0) {
                tvBMILabel.setText("Normal weight");
            } else if (bmi < 30.0) {
                tvBMILabel.setText("Overweight");
            } else {
                tvBMILabel.setText("Obese");
            }
        }

        // Edge to edge insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }


}