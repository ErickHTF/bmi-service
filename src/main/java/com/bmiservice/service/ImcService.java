package com.bmiservice.service;

import org.springframework.stereotype.Service;

@Service
public class ImcService {

    public String calculateImc(double weight, double height) {
        double imc = weight / (height * height);
        String classification = classifyImc(imc);
        String formattedImc = String.format("%.2f", imc);

        return "IMC: " + formattedImc + ", Classification: " + classifyImc(imc);
    }

    public String classifyImc(double imc) {
        if (imc < 18.5) {
            return "Underweight";
        } else if (imc >= 18.5 && imc <= 24.9) {
            return "Normal weight";
        } else if (imc >= 25.0 && imc <= 29.9) {
            return "Overweight";
        } else if (imc >= 30.0 && imc <= 39.9) {
            return "Obesity";
        } else {
            return "Severe obesity";
        }
    }

}
