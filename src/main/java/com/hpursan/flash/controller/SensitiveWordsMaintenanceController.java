package com.hpursan.flash.controller;

import com.hpursan.flash.exception.SensitiveWordAlreadyExistsException;
import com.hpursan.flash.exception.SensitiveWordNotFoundException;
import com.hpursan.flash.model.SensitiveWord;
import com.hpursan.flash.service.SensitiveWordsMaintenanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/internal")
@AllArgsConstructor
public class SensitiveWordsMaintenanceController {
    private final SensitiveWordsMaintenanceService maintenanceService;

    @GetMapping
    @Operation(summary = "List sensitive words", description = "A simple endpoint to list all the current sensitive words", responses = {
            @ApiResponse(description = "Success", responseCode = "200"),
            @ApiResponse(description = "No content", responseCode = "204")
    })
    public ResponseEntity<List<SensitiveWord>> getAllSensitiveWords() {
        log.info("Received request to list all sensitive words");
        List<SensitiveWord> sensitiveWordList = maintenanceService.listAllSensitiveWords();

        if (sensitiveWordList.isEmpty()) {
            log.info("No sensitive words found");
            return ResponseEntity.noContent().build();
        } else {
            log.info("Returning {} sensitive words", sensitiveWordList.size());
            return ResponseEntity.ok(sensitiveWordList);
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a sensitive word", description = "An endpoint to get a sensitive word by its id", responses = {
            @ApiResponse(description = "A word with the given id was found", responseCode = "200"),
            @ApiResponse(description = "A word with the given id was not found", responseCode = "404") // not yet implemented
    })
    public ResponseEntity<SensitiveWord> getSensitiveWordById(@PathVariable("id") Long id) {
        try {
            log.info("Received request to get sensitive word by ID: {}", id);
            SensitiveWord word = maintenanceService.getSensitiveWordById(id);
            log.info("Sensitive word found for ID {}: {}", id, word);
            return ResponseEntity.ok(word);
        } catch (SensitiveWordNotFoundException e) {
            log.warn("Sensitive word not found for ID: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping
    @Operation(summary = "Create a new sensitive word", description = "Add a new sensitive word", responses = {
            @ApiResponse(description = "A new sensitive word was successfully added", responseCode = "201"),
            @ApiResponse(description = "Unable to add a word that already exists", responseCode = "400"),
            @ApiResponse(description = "Unable to add a word due to an unexpected error", responseCode = "500")
    })
    public ResponseEntity<SensitiveWord> createSensitiveWord(@Valid @RequestBody String word) {
        try {
            log.info("Received request to create a new sensitive word: {}", word);
            SensitiveWord createdSensitiveWord = maintenanceService.createSensitiveWord(word);
            log.info("New sensitive word created: {}", createdSensitiveWord);
            return new ResponseEntity<>(createdSensitiveWord, HttpStatus.CREATED);
        } catch (SensitiveWordAlreadyExistsException e) {
            log.warn("Sensitive word already exists: {}", word);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            log.warn("An unexpected error occurred when attempting to add {}", word);
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a sensitive word", description = "Update a sensitive word", responses = {
            @ApiResponse(description = "Sensitive word was successfully updated", responseCode = "200"),
            @ApiResponse(description = "Word does not exist", responseCode = "400")
    })
    public ResponseEntity<SensitiveWord> updateSensitiveWord(@PathVariable("id") Long id, @Valid @RequestBody SensitiveWord sensitiveWord) {
        try {
            log.info("Received request to update sensitive word with ID: {}", id);
            return ResponseEntity.ok(maintenanceService.updateSensitiveWord(id, sensitiveWord));
        } catch (SensitiveWordNotFoundException e) {
            log.warn("Sensitive word not found for ID: {}", id);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a sensitive word", description = "Delete a sensitive word", responses = {
            @ApiResponse(description = "Sensitive word was successfully deleted", responseCode = "204"),
            @ApiResponse(description = "Sensitive word with given id was not found", responseCode = "404")
    })
    public ResponseEntity<SensitiveWord> deleteSensitiveWord(@PathVariable("id") Long id) {
        try {
            log.info("Received request to delete sensitive word with ID: {}", id);
            maintenanceService.deleteSensitiveWord(id);
            log.info("Sensitive word deleted successfully");
            return ResponseEntity.noContent().build();
        } catch (SensitiveWordNotFoundException e) {
            log.warn("Sensitive word not found for ID: {}", id);
            return ResponseEntity.notFound().build();
        }
    }
}