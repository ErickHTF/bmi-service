package com.bmiservice.service;

import com.bmiservice.dto.BmiResponse;
import com.bmiservice.model.BmiClassification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class BmiService {

    /**
     * Calculates the BMI as weight / height².
     *
     * @param weightKg weight in kilograms, must be positive
     * @param heightM  height in meters, must be positive
     */
    public BmiResponse calculate(double weightKg, double heightM) {
        if (!(weightKg > 0) || !(heightM > 0) || Double.isInfinite(weightKg) || Double.isInfinite(heightM)) {
            throw new IllegalArgumentException("Weight and height must be positive finite numbers");
        }

        double bmi = BigDecimal.valueOf(weightKg / (heightM * heightM))
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();

        // Classify the rounded value so the returned number and label never disagree.
        return new BmiResponse(bmi, BmiClassification.of(bmi).label());
    }
}
