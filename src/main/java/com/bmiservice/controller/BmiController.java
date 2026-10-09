package com.bmiservice.controller;

import com.bmiservice.dto.BmiRequest;
import com.bmiservice.dto.BmiResponse;
import com.bmiservice.service.BmiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/bmi")
@Tag(name = "BMI", description = "Body Mass Index calculation")
public class BmiController {

    private final BmiService bmiService;

    public BmiController(BmiService bmiService) {
        this.bmiService = bmiService;
    }

    @Operation(summary = "Calculate BMI",
            description = "Calculates the Body Mass Index from weight (kg) and height (m) and returns its WHO classification.")
    @ApiResponse(responseCode = "200", description = "BMI calculated")
    @ApiResponse(responseCode = "400", description = "Invalid weight or height")
    @PostMapping
    public BmiResponse calculate(@Valid @RequestBody BmiRequest request) {
        return bmiService.calculate(request.weight(), request.height());
    }
}
