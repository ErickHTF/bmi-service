package com.bmiservice.service;

import com.bmiservice.dto.UserRequest;
import com.bmiservice.exception.ResourceNotFoundException;
import com.bmiservice.model.User;
import com.bmiservice.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    @Transactional
    public User create(UserRequest request) {
        return userRepository.save(request.toEntity());
    }

    @Transactional
    public User update(Long id, UserRequest request) {
        User user = findById(id);
        user.update(request.name().strip(), request.age(), request.weight(), request.height());
        return user;
    }

    @Transactional
    public void delete(Long id) {
        userRepository.delete(findById(id));
    }
}
