package com.bmiservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Schema(description = "Body measurements used to calculate the BMI")
public record BmiRequest(

        @Schema(description = "Weight in kilograms", example = "80.0")
        @NotNull
        @Positive
        @DecimalMax("500.0")
        Double weight,

        @Schema(description = "Height in meters", example = "1.80")
        @NotNull
        @Positive
        @DecimalMax("3.0")
        Double height
) {
}
