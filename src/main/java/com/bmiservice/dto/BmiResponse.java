package com.bmiservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Result of a BMI calculation")
public record BmiResponse(

        @Schema(description = "Body Mass Index rounded to two decimal places", example = "24.69")
        double bmi,

        @Schema(description = "WHO classification for the BMI value", example = "Normal weight")
        String classification
) {
}
