package com.bmiservice.service;

import com.bmiservice.exception.ResourceNotFoundException;
import com.bmiservice.model.Users;

//TODO org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.bmiservice.repository.UsersRepo;

import java.util.List;

@Service
public class UserService {

    private final UsersRepo usersRepo;

    public UserService(UsersRepo usersRepo) {
        this.usersRepo = usersRepo;
    }

    public List<Users> findAllUsers() {
        return usersRepo.findAll();
    }

    public Users findUserById(Long id){
        return usersRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found on: " + id));
    }

    public Users saveUser(Users users){
        return usersRepo.save(users);
    }

    public Users updateUser(Long id, Users users){
        // Busca primeiro para garantir que tem o que ser atualizado
        Users updateUser = usersRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found on: " + id));

        updateUser.setName(users.getName());
        updateUser.setAge(users.getAge());
        updateUser.setWeight(users.getWeight());
        updateUser.setHeight(users.getHeight());

        return usersRepo.save(updateUser);
    }

    public void deleteUser(Long id) {
        // Busca primeiro para garantir que tem o que ser deletado
        Users user = usersRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found on: " + id));

        usersRepo.delete(user);
    }
}

