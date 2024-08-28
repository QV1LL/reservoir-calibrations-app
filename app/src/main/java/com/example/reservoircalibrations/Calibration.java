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

    public float getVolume(float level) {
        int wholeNumberPart = (int) level;
        float floatNumberPart = level - wholeNumberPart;

        try {
            return volume.get(wholeNumberPart) + (volumePerMM.get(wholeNumberPart) * floatNumberPart * 10);
        }
        catch (Exception e) {
            return 0f;
        }
    }
}
