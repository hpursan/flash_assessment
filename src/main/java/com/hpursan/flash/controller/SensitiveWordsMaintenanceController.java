package com.hpursan.flash.controller;

import com.hpursan.flash.model.SensitiveWord;
import com.hpursan.flash.service.SensitiveWordsMaintenanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/internal")
@AllArgsConstructor
public class SensitiveWordsMaintenanceController {
    private final SensitiveWordsMaintenanceService maintenanceService;

    @GetMapping
    @Operation(summary = "List sensitive words", description = "A simple endpoint to list all the current sensitive words", responses = {
        @ApiResponse(description = "Success", responseCode = "200"),
        @ApiResponse(description = "No content", responseCode = "204")
    })
    public ResponseEntity<List<SensitiveWord>> getAllSensitiveWords() {
        List<SensitiveWord> sensitiveWordList = maintenanceService.listAllSensitiveWords();

        return sensitiveWordList.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(sensitiveWordList);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a sensitive word", description = "An endpoint to get a sensitive word by it's id", responses = {
            @ApiResponse(description = "A word with the given id was found", responseCode = "200"),
            @ApiResponse(description = "A word with the given id was not found", responseCode = "404") // not yet implemented
    })
    public ResponseEntity<SensitiveWord> getSensitiveWordById(@PathVariable("id") Long id) {
        SensitiveWord word = maintenanceService.getSensitiveWordById(id);
        return ResponseEntity.ok(word);
    }

    @PostMapping
    @Operation(summary = "Create a new sensitive word", description = "Add a new sensitive word", responses = {
            @ApiResponse(description = "A new sensitive word was successfully added", responseCode = "201"),
            @ApiResponse(description = "Unable to add a word that already exists", responseCode = "400")
    })
    public ResponseEntity<SensitiveWord> createSensitiveWord(@Valid @RequestBody String word) {
        try {
            SensitiveWord createdSensitiveWord = maintenanceService.createSensitiveWord(word);
            return new ResponseEntity<>(createdSensitiveWord, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
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
            return ResponseEntity.ok(maintenanceService.updateSensitiveWord(id, sensitiveWord));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a sensitive word", description = "Delete a sensitive word", responses = {
            @ApiResponse(description = "Sensitive word was successfully deleted", responseCode = "204"),
            @ApiResponse(description = "Sensitive word with given id was not found", responseCode = "404"),
            @ApiResponse(description = "Unexpected error occurred", responseCode = "500")
    })
    public ResponseEntity<SensitiveWord> deleteSensitiveWord(@PathVariable("id") Long id) {
        try {
            maintenanceService.deleteSensitiveWord(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

}
