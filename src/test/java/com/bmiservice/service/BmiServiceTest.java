package com.bmiservice.service;

import com.bmiservice.dto.BmiResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class BmiServiceTest {

    private final BmiService bmiService = new BmiService();

    @Test
    void calculatesBmiAsWeightDividedBySquaredHeight() {
        BmiResponse response = bmiService.calculate(80.0, 1.80);

        assertThat(response.bmi()).isEqualTo(24.69);
        assertThat(response.classification()).isEqualTo("Normal weight");
    }

    @ParameterizedTest(name = "BMI {0} -> {1}")
    @CsvSource({
            "18.49, Underweight",
            "18.5,  Normal weight",
            "24.99, Normal weight",
            "25.0,  Overweight",
            "29.99, Overweight",
            "30.0,  Obesity class I",
            "34.99, Obesity class I",
            "35.0,  Obesity class II",
            "39.99, Obesity class II",
            "40.0,  Obesity class III"
    })
    void classifiesUsingWhoBoundaries(double bmi, String expected) {
        // height of 1 m makes the BMI equal to the weight
        assertThat(bmiService.calculate(bmi, 1.0).classification()).isEqualTo(expected);
    }

    @Test
    void valuesBetweenOldBoundariesAreNoLongerMisclassified() {
        // 24.95 used to fall through every branch and be reported as "Severe obesity"
        assertThat(bmiService.calculate(24.95, 1.0).classification()).isEqualTo("Normal weight");
    }

    @Test
    void classificationMatchesTheRoundedValue() {
        BmiResponse response = bmiService.calculate(24.996, 1.0);

        assertThat(response.bmi()).isEqualTo(25.0);
        assertThat(response.classification()).isEqualTo("Overweight");
    }

    @ParameterizedTest
    @CsvSource({"0, 1.8", "80, 0", "-1, 1.8", "80, -1.8", "NaN, 1.8", "80, Infinity"})
    void rejectsInvalidInput(double weight, double height) {
        assertThatIllegalArgumentException().isThrownBy(() -> bmiService.calculate(weight, height));
    }
}
