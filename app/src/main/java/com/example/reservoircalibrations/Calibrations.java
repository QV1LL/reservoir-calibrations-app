package com.example.reservoircalibrations;

import java.util.ArrayList;

public class Calibrations {

    private ArrayList<Float> _level;
    private ArrayList<Float> _volume;
    private ArrayList<Float> _volumePerMM;

    public Calibrations () {
        _level = new ArrayList<>();
        _volume = new ArrayList<>();
        _volumePerMM = new ArrayList<>();
    }
}
