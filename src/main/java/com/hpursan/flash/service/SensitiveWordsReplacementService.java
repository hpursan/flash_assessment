package com.hpursan.flash.service;

import com.hpursan.flash.model.SensitiveWord;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class SensitiveWordsReplacementService {
    private final SensitiveWordsMaintenanceService sensitiveWordsMaintenanceService;

    public String redactSensitiveWords(String message) {
        log.info("Redacting sensitive words from message: {}", message);

        // Get a list of sensitive words
        List<SensitiveWord> sensitiveWords = sensitiveWordsMaintenanceService.listAllSensitiveWordsOrderByLengthWord(false);

        if (sensitiveWords.isEmpty()) {
            log.info("No sensitive words found. Skipping redaction.");
            return message;
        }

        for (SensitiveWord sensitiveWord : sensitiveWords) {
            String phrase = sensitiveWord.getWord();

            // Escape special characters in the phrase
            String escapedPhrase = Pattern.quote(phrase);

            // Compile the pattern with the entire phrase
            Pattern pattern = Pattern.compile("\\b" + escapedPhrase + "\\b", Pattern.CASE_INSENSITIVE);

            // Replace occurrences of the sensitive phrase with redaction character
            Matcher matcher = pattern.matcher(message);

            StringBuilder sb = new StringBuilder();
            while (matcher.find()) {
                matcher.appendReplacement(sb, "*".repeat(phrase.length()));
            }
            matcher.appendTail(sb);
            message = sb.toString();

            log.debug("Sensitive word '{}' redacted from message", phrase);
        }

        log.info("Sensitive words redacted. Final message: {}", message);
        return message;
    }
}

