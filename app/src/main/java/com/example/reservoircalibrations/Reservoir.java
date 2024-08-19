package com.example.reservoircalibrations;

public class Reservoir {

    public String name;
    private final String _defaultName = "reservoir";

    public float beforeLevel;
    public float currentLevel;

    public double beforeVolume;
    public double currentVolume;

    public double receivedVolume;

    public Calibrations calibrations;

    public Reservoir() {
        this.name = _defaultName;
        calibrations = new Calibrations(600);
    }

    public Reservoir(String name) {
        this.name = (name.isEmpty()) ? _defaultName : name;

        calibrations = new Calibrations(600);
    }
}
