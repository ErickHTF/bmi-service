package com.bmiservice.controller;

import com.bmiservice.dto.ImcRequest;
import com.bmiservice.model.Users;
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
    public List<Users> getAllUsers() {

        return userService.findAllUsers();
    }

    @Operation(summary = "Get Users by ID",
            description = "Retorna um usuário pelo ID.")
    @GetMapping(value = "/{id}")
    public Users getUsersById(@PathVariable Long id){

        return userService.findUserById(id);
    }

    @Operation(summary = "Create User",
            description = "Cria um novo usuário.")
    @PostMapping()
    public  String saveUsers(@RequestBody Users users){
        userService.saveUser(users);

        return "User Saved!";
    }

    @Operation(summary = "Update  User",
            description = "Atualiza os dados de um usuário.")
    @PutMapping(value = "/{id}")
    public ResponseEntity<Users> updateUsers(@PathVariable Long id, @RequestBody Users user){
        Users updatedUser = userService.updateUser(id, user);

        return ResponseEntity.ok(updatedUser);
    }

    @Operation(summary = "Delete User",
            description = "Remove um novo usuário.")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteUsers(@PathVariable Long id){
        userService.deleteUser(id);

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
