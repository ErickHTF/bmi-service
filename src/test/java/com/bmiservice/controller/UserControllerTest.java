package com.bmiservice.controller;

import com.bmiservice.dto.UserRequest;
import com.bmiservice.exception.ResourceNotFoundException;
import com.bmiservice.model.User;
import com.bmiservice.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.endsWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    private static final String VALID_BODY = """
            {"name": "John Doe", "age": 30, "weight": 80.0, "height": 1.80}
            """;

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Test
    void createReturns201WithLocationAndBody() throws Exception {
        given(userService.create(any(UserRequest.class))).willReturn(user(1L, "John Doe"));

        mockMvc.perform(post("/api/users").contentType(MediaType.APPLICATION_JSON).content(VALID_BODY))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", endsWith("/api/users/1")))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.height").value(1.80));
    }

    @Test
    void createReturns400WithFieldErrorsWhenBodyIsInvalid() throws Exception {
        String invalid = """
                {"name": " ", "age": -1, "weight": 0, "height": 5}
                """;

        mockMvc.perform(post("/api/users").contentType(MediaType.APPLICATION_JSON).content(invalid))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.fieldErrors.name").exists())
                .andExpect(jsonPath("$.fieldErrors.age").exists())
                .andExpect(jsonPath("$.fieldErrors.weight").exists())
                .andExpect(jsonPath("$.fieldErrors.height").exists());

        verify(userService, never()).create(any());
    }

    @Test
    void createReturns400WhenFieldsAreMissing() throws Exception {
        mockMvc.perform(post("/api/users").contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors.age").exists());
    }

    @Test
    void createReturns400WhenJsonIsMalformed() throws Exception {
        mockMvc.perform(post("/api/users").contentType(MediaType.APPLICATION_JSON).content("{not json"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Malformed JSON request body"));
    }

    @Test
    void findAllReturnsUsers() throws Exception {
        given(userService.findAll()).willReturn(List.of(user(1L, "John Doe"), user(2L, "Jane Doe")));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[1].name").value("Jane Doe"));
    }

    @Test
    void findByIdReturns404WhenUserDoesNotExist() throws Exception {
        given(userService.findById(99L)).willThrow(new ResourceNotFoundException("User not found with id: 99"));

        mockMvc.perform(get("/api/users/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("User not found with id: 99"))
                .andExpect(jsonPath("$.path").value("/api/users/99"));
    }

    @Test
    void findByIdReturns400WhenIdIsNotANumber() throws Exception {
        mockMvc.perform(get("/api/users/abc"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateReturns404WhenUserDoesNotExist() throws Exception {
        given(userService.update(eq(99L), any(UserRequest.class)))
                .willThrow(new ResourceNotFoundException("User not found with id: 99"));

        mockMvc.perform(put("/api/users/99").contentType(MediaType.APPLICATION_JSON).content(VALID_BODY))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteReturns204() throws Exception {
        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isNoContent());

        verify(userService).delete(1L);
    }

    @Test
    void deleteReturns404WhenUserDoesNotExist() throws Exception {
        willThrow(new ResourceNotFoundException("User not found with id: 99")).given(userService).delete(99L);

        mockMvc.perform(delete("/api/users/99"))
                .andExpect(status().isNotFound());
    }

    private static User user(Long id, String name) {
        User user = new User(name, 30, 80.0, 1.80);
        ReflectionTestUtils.setField(user, "id", id);
        return user;
    }
}
