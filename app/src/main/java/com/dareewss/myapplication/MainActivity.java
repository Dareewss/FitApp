package com.dareewss.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    float weight;
    float height;
    int sleep;
    int age;
    float activity;
    boolean isMale;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Sleep
        SeekBar seekBar    = findViewById(R.id.sleepSeekBar);
        TextView vSleep   = findViewById(R.id.tvSleepValue);
        //Stats
        EditText etWeight  = findViewById(R.id.etWeight);
        EditText etHeight  = findViewById(R.id.etHeight);
        EditText etAge  = findViewById(R.id.etAge);
        //Gender
        RadioButton male = findViewById(R.id.rbMale);
        RadioButton female = findViewById(R.id.rbFemale);
        //Activity level
        RadioButton sedentary = findViewById(R.id.rbSedentary);
        RadioButton light = findViewById(R.id.rbLight);
        RadioButton moderate = findViewById(R.id.rbModerate);
        RadioButton active = findViewById(R.id.rbActive);
        RadioButton veryActive = findViewById(R.id.rbVeryActive);

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

        btnContinue.setOnClickListener(v -> Store(etWeight, etHeight, seekBar, male, female, etAge, sedentary, light, moderate, active, veryActive));
    }

    public void Store(EditText etWeight, EditText etHeight, SeekBar seekBar, RadioButton m, RadioButton f, EditText etAge, RadioButton sed, RadioButton lig,RadioButton mod, RadioButton act, RadioButton veryact) {

        if (etWeight.getText().toString().isEmpty() || etHeight.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please fill in all fields!", Toast.LENGTH_SHORT).show();
            return;
        }

        //Gender for bmr
        if (m.isChecked()){
            isMale = true;
        }else if (f.isChecked()){
            isMale = false;
        }else {
            Toast.makeText(this, "Please check in all fields! (Gender)", Toast.LENGTH_SHORT).show();
            return;
        }

        //Activity level
        if(sed.isChecked()){
            activity = 1.2f;
        } else if (lig.isChecked()) {
            activity = 1.375f;
        } else if (mod.isChecked()){
            activity = 1.55f;
        } else if (act.isChecked()) {
            activity = 1.725f;
        } else if (veryact.isChecked()) {
            activity = 1.9f;
        }else {
            Toast.makeText(this, "Please check in all fields! (Activity level)", Toast.LENGTH_SHORT).show();
            return;
        }

        age = Integer.parseInt(etAge.getText().toString());
        weight = Float.parseFloat(etWeight.getText().toString());
        height = Float.parseFloat(etHeight.getText().toString());
        sleep  = seekBar.getProgress();

        SharedPreferences sharedPreferences = getSharedPreferences("BodyData", MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();


        edit.putBoolean("isMale" , isMale);
        edit.putFloat("Weight" , weight);
        edit.putFloat("Height", height);
        edit.putFloat("Active", activity);
        edit.putInt("Sleep" , sleep);
        edit.putInt("Age" , age);
        edit.putBoolean("Set", true);

        edit.apply();

        Intent i = new Intent(this, Home.class);
        startActivity(i);
//        System.out.println("Weight: " + weight);
//        System.out.println("Height: " + height);

    }
}