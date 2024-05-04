package com.hpursan.flash.controller;


import com.hpursan.flash.exception.SensitiveWordAlreadyExistsException;
import com.hpursan.flash.exception.SensitiveWordNotFoundException;
import com.hpursan.flash.model.SensitiveWord;
import com.hpursan.flash.service.SensitiveWordsMaintenanceService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(SensitiveWordsMaintenanceController.class)
public class SensitiveWordsMaintenanceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SensitiveWordsMaintenanceService maintenanceService;

    @Test
    void shouldReturnNoContentWhenNoSensitiveWordsExist() throws Exception {
        when(maintenanceService.listAllSensitiveWords()).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/api/v1/internal"))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturnListOfSensitiveWordsWhenExists() throws Exception {
        List<SensitiveWord> sensitiveWords = Arrays.asList(
                new SensitiveWord(1L, "word1"),
                new SensitiveWord(2L, "word2"));

        when(maintenanceService.listAllSensitiveWords())
                .thenReturn(sensitiveWords);

        mockMvc.perform(get("/api/v1/internal"))
                .andExpect(jsonPath("$.size()", is(sensitiveWords.size())))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].word", is("word1")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].word", is("word2")))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnSensitiveWordByIdWhenExists() throws Exception {

        SensitiveWord sensitiveWord = new SensitiveWord(1L, "word1");

        when(maintenanceService.getSensitiveWordById(anyLong()))
                .thenReturn(sensitiveWord);

        mockMvc.perform(get("/api/v1/internal/{id}",anyLong()))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.word", is("word1")))
                .andExpect(status().isOk());
    }

    @Test
    void shouldThrowSensitiveWordNotFoundExceptionWhenSensitiveWordDoesNotExist() throws Exception {

        when(maintenanceService.getSensitiveWordById(anyLong()))
                .thenThrow(new SensitiveWordNotFoundException(1L));

        mockMvc.perform(get("/api/v1/internal/{id}",anyLong())).andExpect(status().isNotFound());
    }

    @Test
    void shouldCreateSensitiveWordSuccessfully() throws Exception {
        SensitiveWord sensitiveWord = new SensitiveWord(1L, "word1");

        when(maintenanceService.createSensitiveWord("word1")).thenReturn(sensitiveWord);

        mockMvc.perform(post("/api/v1/internal")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("word1"))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldThrowSensitiveWordAlreadyExistsExceptionWhenWordExists() throws Exception {
        SensitiveWord sensitiveWord = new SensitiveWord(1L, "word1");

        when(maintenanceService.createSensitiveWord("word1"))
                .thenThrow(new SensitiveWordAlreadyExistsException("word1"));

        mockMvc.perform(post("/api/v1/internal")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("word1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldUpdateSensitiveWordSuccessfullyWhenWordExists() throws Exception {
        SensitiveWord sensitiveWord = new SensitiveWord(1L, "word1");

        when(maintenanceService.updateSensitiveWord(anyLong(), any(SensitiveWord.class))).thenReturn(sensitiveWord);

        mockMvc.perform(put("/api/v1/internal/{id}",1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"word\":\"word1\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.word").value("word1"));
    }

    @Test
    void shouldThrowSensitiveWordNotFoundExceptionWhenUpdatingNonExistentWord() throws Exception {
        SensitiveWord sensitiveWord = new SensitiveWord(1L, "word1");

        when(maintenanceService.updateSensitiveWord(anyLong(), any(SensitiveWord.class)))
                .thenThrow(new SensitiveWordNotFoundException(1L));

        mockMvc.perform(put("/api/v1/internal/{id}",1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"word\":\"word1\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnNoContentWhenDeletingExistingSensitiveWord() throws Exception {

        doNothing().when(maintenanceService).deleteSensitiveWord(anyLong());

        mockMvc.perform(delete("/api/v1/internal/{id}",1L))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturnNotFoundWhenDeletingNonExistentSensitiveWord() throws Exception {
        doThrow(new SensitiveWordNotFoundException(1L)).when(maintenanceService).deleteSensitiveWord(anyLong());

        mockMvc.perform(delete("/api/v1/internal/{id}",1L))
                .andExpect(status().isNotFound());
    }

    // More tests to follow
}
