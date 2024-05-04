package com.hpursan.flash.service;

import com.hpursan.flash.model.SensitiveWord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class SensitiveWordsReplacementServiceTest {

    @Mock
    SensitiveWordsMaintenanceServiceImpl sensitiveWordsMaintenanceService;

    @InjectMocks
    SensitiveWordsReplacementServiceImpl sensitiveWordsReplacementService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRedactSensitiveWords_NoSensitiveWords() {
        String message = "This is a test message.";
        when(sensitiveWordsMaintenanceService.listAllSensitiveWordsOrderByLengthWord(false)).thenReturn(emptyList());
        String redactedMessage = sensitiveWordsReplacementService.redactSensitiveWords(message);
        assertEquals(message, redactedMessage);
    }

    @Test
    void testRedactSensitiveWords_WithPhraseInSensitiveWords() {

        List<SensitiveWord> orderedList = List.of(new SensitiveWord(1L, "SELECT * FROM"));

        String message = "SELECT * FROM table";
        when(sensitiveWordsMaintenanceService.listAllSensitiveWordsOrderByLengthWord(false)).thenReturn(orderedList);
        String redactedMessage = sensitiveWordsReplacementService.redactSensitiveWords(message);
        assertEquals("************* table", redactedMessage);
    }

    @Test
    void testRedactSensitiveWords_WithWordInSensitiveWords() {

        List<SensitiveWord> orderedList = List.of(new SensitiveWord(1L, "SELECT"));

        String message = "SELECT * FROM table";
        when(sensitiveWordsMaintenanceService.listAllSensitiveWordsOrderByLengthWord(false)).thenReturn(orderedList);
        String redactedMessage = sensitiveWordsReplacementService.redactSensitiveWords(message);
        assertEquals("****** * FROM table", redactedMessage);
    }

    @Test
    void testRedactSensitiveWords_WithWordInSensitiveWords_caseSensitivity() {

        List<SensitiveWord> orderedList = List.of(new SensitiveWord(1L, "SELECT"));

        String message = "select * from table";
        when(sensitiveWordsMaintenanceService.listAllSensitiveWordsOrderByLengthWord(false)).thenReturn(orderedList);
        String redactedMessage = sensitiveWordsReplacementService.redactSensitiveWords(message);
        assertEquals("****** * from table", redactedMessage);
    }

    @Test
    void testRedactSensitiveWords_WithWordInSensitiveWords_partialMatch() {

        List<SensitiveWord> orderedList = List.of(new SensitiveWord(1L, "SELECT"));

        String message = "selection * from table";
        when(sensitiveWordsMaintenanceService.listAllSensitiveWordsOrderByLengthWord(false)).thenReturn(orderedList);
        String redactedMessage = sensitiveWordsReplacementService.redactSensitiveWords(message);
        assertEquals("selection * from table", redactedMessage);
    }

    @Test
    void testRedactSensitiveWords_EmptyInput() {
        List<SensitiveWord> orderedList = List.of(new SensitiveWord(1L, "SELECT"));

        when(sensitiveWordsMaintenanceService.listAllSensitiveWordsOrderByLengthWord(false)).thenReturn(orderedList);
        String message = "";
        String redactedMessage = sensitiveWordsReplacementService.redactSensitiveWords(message);
        assertEquals("", redactedMessage);
    }
}

