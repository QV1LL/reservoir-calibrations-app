package com.example.reservoircalibrations;

import android.os.Bundle;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.reservoircalibrations.databinding.ActivityCalibrationBinding;

public class calibration extends AppCompatActivity {

    private ActivityCalibrationBinding binding;

    private Button returnButton;
    private Button addButton;
    private Button deleteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCalibrationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        returnButton = findViewById(R.id.return_activity_main);
        addButton = findViewById(R.id.add_one);
        deleteButton = findViewById(R.id.remove_button);

        Log.i("myTag", "Imma here");

        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setContentView(R.layout.activity_main);
            }
        });
    }
}