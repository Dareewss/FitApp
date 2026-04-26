package com.dareewss.myapplication;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Home extends AppCompatActivity {

    double bmr;

    private TextView    tvCalorieSummary;
    private ProgressBar pbProtein, pbCarbs, pbFat;
    private TextView    tvProteinPct, tvCarbsPct, tvFatPct;
    private TextView    tvProteinValue, tvCarbsValue, tvFatValue;
    private Button      btnLogMeal;

    private int targetProteinG;
    private int targetCarbsG;
    private int targetFatG;
    private int targetKcal;

    private float loggedProteinG = 0f;
    private float loggedCarbsG   = 0f;
    private float loggedFatG     = 0f;

    private static final String PREFS         = "BodyData";
    private static final String KEY_PROT_LOG  = "logged_protein";
    private static final String KEY_CARBS_LOG = "logged_carbs";
    private static final String KEY_FAT_LOG   = "logged_fat";
    private static final String KEY_LOG_DATE  = "log_date";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        TextView tvBMRValue    = findViewById(R.id.tvBMRValue);
        TextView tvBMRlabel    = findViewById(R.id.tvBMRLabel);
        TextView tvWeightValue = findViewById(R.id.tvWeightValue);
        TextView tvHeightValue = findViewById(R.id.tvHeightValue);
        TextView tvSleepValue  = findViewById(R.id.tvSleepValue);
        TextView tvBMIValue    = findViewById(R.id.tvBMIValue);
        TextView tvBMILabel    = findViewById(R.id.tvBMILabel);

        tvCalorieSummary = findViewById(R.id.tvCalorieSummary);
        pbProtein        = findViewById(R.id.pbProtein);
        pbCarbs          = findViewById(R.id.pbCarbs);
        pbFat            = findViewById(R.id.pbFat);
        tvProteinPct     = findViewById(R.id.tvProteinPct);
        tvCarbsPct       = findViewById(R.id.tvCarbsPct);
        tvFatPct         = findViewById(R.id.tvFatPct);
        tvProteinValue   = findViewById(R.id.tvProteinValue);
        tvCarbsValue     = findViewById(R.id.tvCarbsValue);
        tvFatValue       = findViewById(R.id.tvFatValue);
        btnLogMeal       = findViewById(R.id.btnLogMeal);

        SharedPreferences sharedPreferences = getSharedPreferences(PREFS, MODE_PRIVATE);

        boolean setUp = sharedPreferences.getBoolean("Set", false);
        if (!setUp) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }

        float   weight   = sharedPreferences.getFloat("Weight", 0);
        float   height   = sharedPreferences.getFloat("Height", 0);
        int     sleep    = sharedPreferences.getInt("Sleep", 0);
        int     age      = sharedPreferences.getInt("Age", 16);
        float   activity = sharedPreferences.getFloat("Active", 1.2f);
        boolean isMale   = sharedPreferences.getBoolean("isMale", true);

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

        if (isMale) {
            bmr = (10 * weight + 6.25 * height - 5 * age + 5) * activity;
            tvBMRValue.setText(String.format("%.0f", bmr / activity) + " calories");
            tvBMRlabel.setText("You need " + String.format("%.0f", bmr) + " calories to maintain your weight");
        } else {
            bmr = (10 * weight + 6.25 * height - 5 * age - 161) * activity;
            tvBMRValue.setText(String.format("%.0f", bmr / activity) + " calories");
            tvBMRlabel.setText("You need " + String.format("%.0f", bmr) + " calories to maintain your weight");
        }

        targetKcal     = (int) Math.round(bmr);
        targetProteinG = (int) Math.round(bmr * 0.30 / 4.0);
        targetCarbsG   = (int) Math.round(bmr * 0.40 / 4.0);
        targetFatG     = (int) Math.round(bmr * 0.30 / 9.0);

        loadTodayLogs(sharedPreferences);

        refreshMacroUI();

        btnLogMeal.setOnClickListener(v -> showLogMealDialog());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void loadTodayLogs(SharedPreferences prefs) {
        String today    = getTodayDateString();
        String savedDay = prefs.getString(KEY_LOG_DATE, "");

        if (!today.equals(savedDay)) {
            prefs.edit()
                    .putFloat(KEY_PROT_LOG,  0f)
                    .putFloat(KEY_CARBS_LOG, 0f)
                    .putFloat(KEY_FAT_LOG,   0f)
                    .putString(KEY_LOG_DATE, today)
                    .apply();
        }

        loggedProteinG = prefs.getFloat(KEY_PROT_LOG,  0f);
        loggedCarbsG   = prefs.getFloat(KEY_CARBS_LOG, 0f);
        loggedFatG     = prefs.getFloat(KEY_FAT_LOG,   0f);
    }

    private void refreshMacroUI() {
        int loggedKcal = Math.round(loggedProteinG * 4 + loggedCarbsG * 4 + loggedFatG * 9);
        tvCalorieSummary.setText(loggedKcal + " / " + targetKcal + " kcal");

        int protPct = clamp(Math.round(loggedProteinG / targetProteinG * 100f));
        pbProtein.setProgress(protPct);
        tvProteinPct.setText(protPct + "%");
        tvProteinValue.setText(Math.round(loggedProteinG) + " / " + targetProteinG + "g");

        int carbPct = clamp(Math.round(loggedCarbsG / targetCarbsG * 100f));
        pbCarbs.setProgress(carbPct);
        tvCarbsPct.setText(carbPct + "%");
        tvCarbsValue.setText(Math.round(loggedCarbsG) + " / " + targetCarbsG + "g");

        int fatPct = clamp(Math.round(loggedFatG / targetFatG * 100f));
        pbFat.setProgress(fatPct);
        tvFatPct.setText(fatPct + "%");
        tvFatValue.setText(Math.round(loggedFatG) + " / " + targetFatG + "g");
    }

    private void showLogMealDialog() {
        View dialogView = LayoutInflater.from(this)
                .inflate(R.layout.dialog, null);

        EditText etProtein = dialogView.findViewById(R.id.etDialogProtein);
        EditText etCarbs   = dialogView.findViewById(R.id.etDialogCarbs);
        EditText etFat     = dialogView.findViewById(R.id.etDialogFat);

        new AlertDialog.Builder(this)
                .setTitle("Log a Meal")
                .setView(dialogView)
                .setPositiveButton("Log", (dialog, which) -> {
                    float p = parseFloatSafe(etProtein);
                    float c = parseFloatSafe(etCarbs);
                    float f = parseFloatSafe(etFat);
                    addMacros(p, c, f);
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void addMacros(float proteinG, float carbsG, float fatG) {
        loggedProteinG += proteinG;
        loggedCarbsG   += carbsG;
        loggedFatG     += fatG;

        getSharedPreferences(PREFS, MODE_PRIVATE).edit()
                .putFloat(KEY_PROT_LOG,  loggedProteinG)
                .putFloat(KEY_CARBS_LOG, loggedCarbsG)
                .putFloat(KEY_FAT_LOG,   loggedFatG)
                .apply();

        refreshMacroUI();
    }

    private int clamp(int value) {
        return Math.max(0, Math.min(100, value));
    }

    private float parseFloatSafe(EditText et) {
        try {
            return Float.parseFloat(et.getText().toString().trim());
        } catch (NumberFormatException e) {
            return 0f;
        }
    }

    private String getTodayDateString() {
        java.text.SimpleDateFormat sdf =
                new java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault());
        return sdf.format(new java.util.Date());
    }
}