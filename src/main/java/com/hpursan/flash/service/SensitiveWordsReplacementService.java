package com.hpursan.flash.service;

import com.hpursan.flash.model.SensitiveWord;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class SensitiveWordsReplacementService {
    private final SensitiveWordsMaintenanceService sensitiveWordsMaintenanceService;

    public String redactSensitiveWords(String message){

        // get a list of the sensitive words
        List<SensitiveWord> sensitiveWords = sensitiveWordsMaintenanceService.listAllSensitiveWordsOrderByLengthWord(false);

        if (sensitiveWords.isEmpty()) {
            return message;
        }

        for (SensitiveWord sensitiveWord : sensitiveWords) {

            // Going to need the wrap the phrase with escape character
            String phrase = sensitiveWord.getWord();

            // Escape special characters in the phrase
            String escapedPhrase = Pattern.quote(phrase);

            // Now, we can compile the pattern with the entire phrase
            Pattern pattern = Pattern.compile("\\b" + escapedPhrase + "\\b", Pattern.CASE_INSENSITIVE);

            // Replace occurrences of the sensitive phrase with redaction character
            Matcher matcher = pattern.matcher(message);

            StringBuilder sb = new StringBuilder();
            while (matcher.find()) {
                matcher.appendReplacement(sb, "*".repeat(phrase.length()));
            }
            matcher.appendTail(sb);
            message = sb.toString();
        }

        return message;
    }

}
