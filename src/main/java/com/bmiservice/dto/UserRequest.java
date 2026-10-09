package com.bmiservice.dto;

import com.bmiservice.model.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

@Schema(description = "Payload used to create or update a user")
public record UserRequest(

        @Schema(example = "John Doe")
        @NotBlank
        @Size(max = 100)
        String name,

        @Schema(example = "30")
        @NotNull
        @Positive
        @Max(150)
        Integer age,

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

    public User toEntity() {
        return new User(name.strip(), age, weight, height);
    }
}
