package com.bmiservice.model;

/**
 * Adult BMI categories as defined by the World Health Organization.
 * Each category covers the half-open range [previous upper bound, upperBound).
 */
public enum BmiClassification {

    UNDERWEIGHT("Underweight", 18.5),
    NORMAL_WEIGHT("Normal weight", 25.0),
    OVERWEIGHT("Overweight", 30.0),
    OBESITY_CLASS_I("Obesity class I", 35.0),
    OBESITY_CLASS_II("Obesity class II", 40.0),
    OBESITY_CLASS_III("Obesity class III", Double.POSITIVE_INFINITY);

    private final String label;
    private final double upperBound;

    BmiClassification(String label, double upperBound) {
        this.label = label;
        this.upperBound = upperBound;
    }

    public String label() {
        return label;
    }

    public static BmiClassification of(double bmi) {
        for (BmiClassification classification : values()) {
            if (bmi < classification.upperBound) {
                return classification;
            }
        }
        return OBESITY_CLASS_III;
    }
}
