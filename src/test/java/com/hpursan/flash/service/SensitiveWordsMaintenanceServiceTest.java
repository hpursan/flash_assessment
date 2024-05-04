package com.hpursan.flash.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.hpursan.flash.repository.SensitiveWordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.hpursan.flash.model.SensitiveWord;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

public class SensitiveWordsMaintenanceServiceTest {

    @Mock
    private SensitiveWordRepository sensitiveWordRepository;

    @InjectMocks
    private SensitiveWordsMaintenanceService maintenanceService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateSensitiveWord_Success() {
        String word = "newWord";
        SensitiveWord sensitiveWord = new SensitiveWord(1L, word);
        when(sensitiveWordRepository.findByWord(word)).thenReturn(null);
        when(sensitiveWordRepository.save(any())).thenReturn(sensitiveWord);

        SensitiveWord createdWord = maintenanceService.createSensitiveWord(word);

        assertNotNull(createdWord);
        assertEquals(word, createdWord.getWord());
    }

    @Test
    void testCreateSensitiveWord_andAlreadyExists_ShouldThrowIllegalArgumentException() {
        String word = "existingWord";
        when(sensitiveWordRepository.findByWord(word)).thenReturn(new SensitiveWord(1L, word));
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> maintenanceService.createSensitiveWord(word));
        assertTrue(ex.getMessage().contains("Sensitive word already exists"));
    }

    @Test
    void testListAllSensitiveWords_Success() {
        when(sensitiveWordRepository.findAll()).thenReturn(List.of(new SensitiveWord(1L, "word1"), new SensitiveWord(2L, "word2")));
        List<SensitiveWord> words = maintenanceService.listAllSensitiveWords();
        assertEquals(words.size(), 2);
    }

    @Test
    void testGetSensitiveWordById_Success() {
        when(sensitiveWordRepository.findById(anyLong())).thenReturn(new SensitiveWord(1L, "word1"));
        SensitiveWord word = maintenanceService.getSensitiveWordById(1L);
        assertEquals("word1", word.getWord());
    }

    @Test
    void testUpdateSensitiveWordById_Success() {
        SensitiveWord original = new SensitiveWord(1L, "Existing word");
        SensitiveWord updates = new SensitiveWord(1L, "Update word");

        when(sensitiveWordRepository.findById(1L)).thenReturn(original);
        when(sensitiveWordRepository.save(any(SensitiveWord.class))).thenReturn(original);

        SensitiveWord updatedSensitiveWord = maintenanceService.updateSensitiveWord(1L, updates);

        assertEquals("Update word", updatedSensitiveWord.getWord());
    }

    @Test
    void testUpdateSensitiveWordById_andDoesNotExist_ShouldThrowIllegalArgumentException() {
        SensitiveWord updates = new SensitiveWord(1L, "Update word");
        when(sensitiveWordRepository.findById(1L)).thenReturn(null);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> maintenanceService.updateSensitiveWord(1L, updates));
        assertTrue(ex.getMessage().contains("Sensitive word with id 1 does not exist"));
    }

    @Test
    void testDeleteSensitiveWordById_Success() {
        SensitiveWord sensitiveWord = new SensitiveWord(1L, "Delete");
        when(sensitiveWordRepository.findById(1L)).thenReturn(sensitiveWord);
        maintenanceService.deleteSensitiveWord(1L);
        verify(sensitiveWordRepository).deleteById(1L);
    }

    @Test
    void testDeleteSensitiveWordById_andDoesNotExist_ShouldThrowIllegalArgumentException() {
        when(sensitiveWordRepository.findById(1L)).thenReturn(null);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> maintenanceService.deleteSensitiveWord(1L));
        assertTrue(ex.getMessage().contains("Sensitive word with id 1 does not exist"));
    }
    // More tests to follow
}
