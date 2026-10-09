package com.bmiservice.controller;

import com.bmiservice.dto.BmiResponse;
import com.bmiservice.service.BmiService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BmiController.class)
class BmiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BmiService bmiService;

    @Test
    void returnsBmiAndClassification() throws Exception {
        given(bmiService.calculate(80.0, 1.80)).willReturn(new BmiResponse(24.69, "Normal weight"));

        mockMvc.perform(post("/api/bmi").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"weight\": 80.0, \"height\": 1.80}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bmi").value(24.69))
                .andExpect(jsonPath("$.classification").value("Normal weight"));
    }

    @Test
    void returns400WhenHeightIsZero() throws Exception {
        mockMvc.perform(post("/api/bmi").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"weight\": 80.0, \"height\": 0}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors.height").exists());

        verify(bmiService, never()).calculate(anyDouble(), anyDouble());
    }
}
