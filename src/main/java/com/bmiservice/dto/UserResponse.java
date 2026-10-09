package com.bmiservice.dto;

import com.bmiservice.model.User;

public record UserResponse(Long id, String name, int age, double weight, double height) {

    public static UserResponse from(User user) {
        return new UserResponse(user.getId(), user.getName(), user.getAge(), user.getWeight(), user.getHeight());
    }
}
