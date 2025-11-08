package com.comerbien.backend.model.enums;

public enum WeightGoal {
    LOSE("Perder peso"),
    MAINTAIN("Mantener peso"),
    GAIN("Ganar peso");

    private final String displayName;

    WeightGoal(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}