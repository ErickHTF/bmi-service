package com.bmiservice.repository;

import com.bmiservice.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepo extends JpaRepository<Users, Long> {
}
