package com.bmiservice.controller;

import com.bmiservice.dto.ImcRequest;
import com.bmiservice.dto.UserRequest;
import com.bmiservice.dto.UserResponse;
import com.bmiservice.model.User;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import java.util.List;

import com.bmiservice.service.ImcService;
import com.bmiservice.service.UserService;

@RestController
@RequestMapping("/api/users")
public class ApiController {

    private final UserService userService;
    private final ImcService imcService;

    public ApiController(UserService userService, ImcService imcService) {
        this.userService = userService;
        this.imcService = imcService;
    }

    @Operation(summary = "Get All Users",
            description = "Retorna todos os usuários cadastrados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = {@Content (mediaType = "application/json",
                schema = @Schema(implementation = ApiController.class))}),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping(value = "/all")
    public List<UserResponse> getAllUsers() {

        return userService.findAll().stream().map(UserResponse::from).toList();
    }

    @Operation(summary = "Get Users by ID",
            description = "Retorna um usuário pelo ID.")
    @GetMapping(value = "/{id}")
    public UserResponse getUsersById(@PathVariable Long id){

        return UserResponse.from(userService.findById(id));
    }

    @Operation(summary = "Create User",
            description = "Cria um novo usuário.")
    @PostMapping()
    public  String saveUsers(@Valid @RequestBody UserRequest request){
        userService.create(request);

        return "User Saved!";
    }

    @Operation(summary = "Update  User",
            description = "Atualiza os dados de um usuário.")
    @PutMapping(value = "/{id}")
    public ResponseEntity<UserResponse> updateUsers(@PathVariable Long id, @Valid @RequestBody UserRequest request){
        User updatedUser = userService.update(id, request);

        return ResponseEntity.ok(UserResponse.from(updatedUser));
    }

    @Operation(summary = "Delete User",
            description = "Remove um novo usuário.")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteUsers(@PathVariable Long id){
        userService.delete(id);

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Calculate BMI",
            description = "Calcula um IMC.")
    @PostMapping(value = "/calculateImc")
    public String calculateImc(
            @RequestBody ImcRequest request)
    {
        return imcService.calculateImc(request.getWeight(), request.getHeight());
    }
}
