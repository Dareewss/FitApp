package com.dareewss.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    float weight;
    float height;
    int sleep;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SeekBar seekBar    = findViewById(R.id.sleepSeekBar);
        TextView vSleep   = findViewById(R.id.tvSleepValue);
        EditText etWeight  = findViewById(R.id.etWeight);
        EditText etHeight  = findViewById(R.id.etHeight);
        Button btnContinue = findViewById(R.id.btnContinue);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                vSleep.setText(progress + " hours");
                sleep = progress;
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        btnContinue.setOnClickListener(v -> Store(etWeight, etHeight, seekBar));
    }

    public void Store(EditText etWeight, EditText etHeight, SeekBar seekBar) {

        if (etWeight.getText().toString().isEmpty() || etHeight.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please fill in all fields!", Toast.LENGTH_SHORT).show();
            return;
        }

        weight = Float.parseFloat(etWeight.getText().toString());
        height = Float.parseFloat(etHeight.getText().toString());
        sleep  = seekBar.getProgress();

        SharedPreferences sharedPreferences = getSharedPreferences("BodyData", MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();

        edit.putFloat("Weight" , weight);
        edit.putFloat("Height", height);
        edit.putInt("Sleep" , sleep);
        edit.putBoolean("Set", true);

        edit.apply();

        Intent i = new Intent(this, Home.class);
        startActivity(i);
//        System.out.println("Weight: " + weight);
//        System.out.println("Height: " + height);

    }
}