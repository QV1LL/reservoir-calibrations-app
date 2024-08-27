package com.example.reservoircalibrations;

import android.util.Log;

import java.util.ArrayList;

public class Calibration {

    public ArrayList<Float> volume;
    public ArrayList<Float> volumePerMM;

    public int size;

    public Calibration() {
        volume = new ArrayList<>();
        volumePerMM = new ArrayList<>();

        this.size = 10;
    }

    public Calibration(int size) {
        volume = new ArrayList<>();
        volumePerMM = new ArrayList<>();

        this.size = size;
    }
}
