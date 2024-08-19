package com.example.reservoircalibrations;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.reservoircalibrations.databinding.ActivityCalibrationBinding;

import java.util.ArrayList;

public class Calibrations extends AppCompatActivity {

    private ActivityCalibrationBinding binding;

    private LayoutInflater inflater;
    private ViewGroup table;

    private Button addButton;
    private Button deleteButton;

    private ArrayList<Float> _volume;
    private ArrayList<Float> _volumePerMM;

    private int _size;

    public Calibrations () {
        _volume = new ArrayList<>();
        _volumePerMM = new ArrayList<>();

        this._size = 600;
    }

    public Calibrations (int size) {
        _volume = new ArrayList<>();
        _volumePerMM = new ArrayList<>();

        this._size = size;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCalibrationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        inflater = getLayoutInflater();
        table = findViewById(R.id.table_layout);

        addButton = findViewById(R.id.add_one);
        deleteButton = findViewById(R.id.delete_last);

        for (int i = 0; i < this._size; i++) {
            addRow(i + 1);
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
    }

    private void deleteLastRow() {
        if (table.getChildCount() > 1) table.removeView(table.getChildAt(table.getChildCount() - 1));
    }

    private void addRow(int level) {
        View inflatedItem = inflater.inflate(R.layout.calibrations_row, null, false);
        table.addView(inflatedItem);

        ((TextView) inflatedItem.findViewById(R.id.level)).setText(String.valueOf(level));

        float volume = Float.parseFloat((((TextView) inflatedItem.findViewById(R.id.volume)).getText().toString().isEmpty()) ? "0f" : ((TextView) inflatedItem.findViewById(R.id.volume)).getText().toString());
        _volume.add(volume);

        float volumePerMM = Float.parseFloat((((TextView) inflatedItem.findViewById(R.id.meter3per_mm)).getText().toString().isEmpty()) ? "0f" : ((TextView) inflatedItem.findViewById(R.id.meter3per_mm)).getText().toString());
        _volumePerMM.add(volumePerMM);
    }

    private void addOneRow() {
        addRow(table.getChildCount());
    }
}