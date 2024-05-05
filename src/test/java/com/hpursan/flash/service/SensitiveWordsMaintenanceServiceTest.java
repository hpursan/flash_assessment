package com.hpursan.flash.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.hpursan.flash.exception.SensitiveWordAlreadyExistsException;
import com.hpursan.flash.exception.SensitiveWordNotFoundException;
import com.hpursan.flash.repository.SensitiveWordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.hpursan.flash.model.SensitiveWord;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

public class SensitiveWordsMaintenanceServiceTest {

    @Mock
    private SensitiveWordRepository sensitiveWordRepository;

    @InjectMocks
    private SensitiveWordsMaintenanceServiceImpl maintenanceService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCreateSensitiveWordSuccessfully() {
        String word = "newWord";
        SensitiveWord sensitiveWord = new SensitiveWord(1L, word);
        when(sensitiveWordRepository.findByWord(word)).thenReturn(null);
        when(sensitiveWordRepository.save(any())).thenReturn(sensitiveWord);

        SensitiveWord createdWord = maintenanceService.createSensitiveWord(word);

        assertNotNull(createdWord);
        assertEquals(word, createdWord.getWord());
    }

    @Test
    void shouldThrowSensitiveWordAlreadyExistsExceptionWhenCreatingDuplicateWord() {
        String word = "existingWord";
        when(sensitiveWordRepository.findByWord(word)).thenReturn(new SensitiveWord(1L, word));
        SensitiveWordAlreadyExistsException ex = assertThrows(SensitiveWordAlreadyExistsException.class, () -> maintenanceService.createSensitiveWord(word));
        assertTrue(ex.getMessage().contains("Sensitive word " + word + " already exists"));
    }

    @Test
    void shouldListAllSensitiveWordsSuccessfully() {
        when(sensitiveWordRepository.findAll()).thenReturn(List.of(new SensitiveWord(1L, "word1"), new SensitiveWord(2L, "word2")));
        List<SensitiveWord> words = maintenanceService.listAllSensitiveWords();
        assertEquals(words.size(), 2);
    }

    @Test
    void shouldGetSensitiveWordByIdSuccessfully() {
        when(sensitiveWordRepository.findById(anyLong())).thenReturn(new SensitiveWord(1L, "word1"));
        SensitiveWord word = maintenanceService.getSensitiveWordById(1L);
        assertEquals("word1", word.getWord());
    }

    @Test
    void shouldThrowSensitiveWordNotFoundExceptionWhenGettingNonExistentWordById() {
        when(sensitiveWordRepository.findById(anyLong())).thenReturn(null);
        SensitiveWordNotFoundException ex = assertThrows(SensitiveWordNotFoundException.class, () -> maintenanceService.getSensitiveWordById(1L));
        assertTrue(ex.getMessage().contains("Sensitive word with id 1 not found"));
    }

    @Test
    void shouldUpdateSensitiveWordByIdSuccessfully() {
        SensitiveWord original = new SensitiveWord(1L, "Existing word");
        SensitiveWord updates = new SensitiveWord(1L, "Update word");

        when(sensitiveWordRepository.findById(1L)).thenReturn(original);
        when(sensitiveWordRepository.save(any(SensitiveWord.class))).thenReturn(original);

        SensitiveWord updatedSensitiveWord = maintenanceService.updateSensitiveWord(1L, updates);

        assertEquals("Update word", updatedSensitiveWord.getWord());
    }

    @Test
    void shouldThrowSensitiveWordNotFoundExceptionWhenUpdatingNonExistentWordById() {
        SensitiveWord updates = new SensitiveWord(1L, "Update word");
        when(sensitiveWordRepository.findById(1L)).thenReturn(null);
        SensitiveWordNotFoundException ex = assertThrows(SensitiveWordNotFoundException.class, () -> maintenanceService.updateSensitiveWord(1L, updates));
        assertTrue(ex.getMessage().contains("Sensitive word with id 1 not found"));
    }

    @Test
    void shouldDeleteSensitiveWordByIdSuccessfully() {
        SensitiveWord sensitiveWord = new SensitiveWord(1L, "Delete");
        when(sensitiveWordRepository.findById(1L)).thenReturn(sensitiveWord);
        maintenanceService.deleteSensitiveWord(1L);
        verify(sensitiveWordRepository).deleteById(1L);
    }

    @Test
    void shouldThrowSensitiveWordNotFoundExceptionWhenDeletingNonExistentWordById() {
        when(sensitiveWordRepository.findById(1L)).thenReturn(null);
        SensitiveWordNotFoundException ex = assertThrows(SensitiveWordNotFoundException.class, () -> maintenanceService.deleteSensitiveWord(1L));
        assertTrue(ex.getMessage().contains("Sensitive word with id 1 not found"));
    }

    @Test
    void shouldListAllSensitiveWordsOrderByLengthWordDescendingSuccessfully() {
        SensitiveWord long_word = new SensitiveWord(1L, "long word");
        SensitiveWord longer_word = new SensitiveWord(2L, "longer word");
        SensitiveWord longest_word = new SensitiveWord(3L, "longest word");

        List<SensitiveWord> expectedList = Arrays.asList(longest_word, longer_word, long_word);

        when(sensitiveWordRepository.findAllOrderByLengthWord(false)).thenReturn(expectedList);

        List<SensitiveWord> resultList = maintenanceService.listAllSensitiveWordsOrderByLengthWord(false);

        assertEquals(resultList.size(), 3);
        assertEquals(expectedList, resultList);
    }

    @Test
    void shouldListAllSensitiveWordsOrderByLengthWordAscendingSuccessfully() {
        SensitiveWord longWord = new SensitiveWord(1L, "long word");
        SensitiveWord longerWord = new SensitiveWord(2L, "longer word");
        SensitiveWord longestWord = new SensitiveWord(3L, "longest word");

        List<SensitiveWord> expectedList = Arrays.asList(longWord, longerWord, longestWord);

        when(sensitiveWordRepository.findAllOrderByLengthWord(eq(true))).thenReturn(expectedList);

        List<SensitiveWord> resultList = maintenanceService.listAllSensitiveWordsOrderByLengthWord(true);

        assertEquals(3, resultList.size());
        assertEquals(expectedList, resultList);
    }
}
