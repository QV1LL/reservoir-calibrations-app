package com.example.reservoircalibrations;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.reservoircalibrations.databinding.ActivityCalibrationBinding;

import java.util.Locale;

import com.google.gson.Gson;

public class CalibrationsActivity extends AppCompatActivity {

    private ActivityCalibrationBinding binding;

    private LayoutInflater inflater;
    private ViewGroup table;

    private Button addButton;
    private Button deleteButton;

    private Calibration calibration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        calibration = null;

        binding = ActivityCalibrationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        inflater = getLayoutInflater();
        table = findViewById(R.id.table_layout);

        addButton = findViewById(R.id.add_one);
        deleteButton = findViewById(R.id.delete_last);

        loadData();

        if (calibration == null) {
            calibration = new Calibration(10);

            for (int i = 0; i < calibration.size; i++) {
                addRow(i);
            }
        }

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addOneRow();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteLastRow();
            }
        });

        setupAutoSave(table);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        saveData();
    }

    private void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences("calibrations", MODE_PRIVATE);
        Gson gson = new Gson();

        try {
            Log.i(MainActivity.TAG, "Trying to get calibration" + MainActivity.currentItemIndex + "...");
            Calibration calibrationObject = gson.fromJson(sharedPreferences.getString("calibration" + MainActivity.currentItemIndex, ""), Calibration.class);

            calibration = calibrationObject;

            for (int i = 0; i < calibration.size; i++) {
                addRow(i);
            }

            for (int i = 1; i < table.getChildCount(); i++) {
                ViewGroup parentChild = (ViewGroup) table.getChildAt(i);

                ((TextView) parentChild.findViewById(R.id.volume)).setText(String.format(Locale.ROOT, "%.3f" ,calibration.volume.get(i - 1)));
                ((TextView) parentChild.findViewById(R.id.meter3per_mm)).setText(String.format(Locale.ROOT, "%.4f", calibration.volumePerMM.get(i - 1)));
            }

            Log.i(MainActivity.TAG, "Successfully execute data!");
        }
        catch (Exception e) {
            Log.i(MainActivity.TAG, "Exception:  " + e);
        }
    }

    private void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences("calibrations", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();

        String jsonObject = gson.toJson(calibration);
        editor.putString("calibration" + MainActivity.currentItemIndex, jsonObject);

        editor.apply();
    }

    private void deleteLastRow() {
        if (table.getChildCount() > 1) table.removeView(table.getChildAt(table.getChildCount() - 1));
    }

    private void addRow(int level) {
        View inflatedItem = inflater.inflate(R.layout.calibrations_row, null, false);
        table.addView(inflatedItem);

        ((TextView) inflatedItem.findViewById(R.id.level)).setText(String.valueOf(level));

        float volume = Float.parseFloat((((TextView) inflatedItem.findViewById(R.id.volume)).getText().toString().isEmpty()) ? "0f" : ((TextView) inflatedItem.findViewById(R.id.volume)).getText().toString());
        calibration.volume.add(volume);
        calibration.volumePerMM.add(0f);
    }

    private void addOneRow() {
        addRow(table.getChildCount());
    }

    private void setupAutoSave(ViewGroup parent) {
        for (int i = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);

            if (child instanceof EditText) {
                ((EditText) child).addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        for (int i = 1; i < table.getChildCount(); i++) {
                            ViewGroup parentChild = (ViewGroup) table.getChildAt(i);

                            calibration.volume.set(i - 1, Float.valueOf((((TextView) parentChild.findViewById(R.id.volume)).getText().toString().isEmpty()) ? "0f" : ((TextView) parentChild.findViewById(R.id.volume)).getText().toString()));

                            if (i < calibration.volume.size())
                                calibration.volumePerMM.set(i - 1, (calibration.volume.get(i) - calibration.volume.get(i - 1)) / 10);
                        }
                    }
                });

                child.setOnFocusChangeListener((v, hasFocus) -> {
                    if (!hasFocus) {
                        for (int j = 1; j < table.getChildCount(); j++) {
                            ViewGroup parentChild = (ViewGroup) table.getChildAt(j);

                            ((TextView) parentChild.findViewById(R.id.volume)).setText(String.format(Locale.ROOT, "%.3f" ,calibration.volume.get(j - 1)));
                            ((TextView) parentChild.findViewById(R.id.meter3per_mm)).setText(String.format(Locale.ROOT, "%.4f", calibration.volumePerMM.get(j - 1)));
                        }
                    }
                });
            }

            else if (child instanceof ViewGroup) {
                setupAutoSave((ViewGroup) child);
            }
        }
    }
}