package com.hpursan.flash.controller;

import com.hpursan.flash.model.dto.RedactedMessageDTO;
import com.hpursan.flash.service.SensitiveWordsReplacementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/external")
public class SensitiveWordsController {

    private final SensitiveWordsReplacementService sensitiveWordsReplacementService;

    @PostMapping
    @Operation(summary = "Redact sensitive words from the given input", description = "Replace sensitive words and phrases with an asterisk", responses = {
            @ApiResponse(description = "Sensitive words have been successfully redacted", responseCode = "200"),
            @ApiResponse(description = "An unexpected error occurred", responseCode = "400")
    })
    public ResponseEntity<RedactedMessageDTO> redactSensitiveWords(@Valid @RequestBody String message) {
        try {
            String redactedMessage = sensitiveWordsReplacementService.redactSensitiveWords(message);
            return new ResponseEntity<>(new RedactedMessageDTO(redactedMessage), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

    }
}