package com.hpursan.flash.service;

import com.hpursan.flash.exception.SensitiveWordAlreadyExistsException;
import com.hpursan.flash.exception.SensitiveWordNotFoundException;
import com.hpursan.flash.repository.SensitiveWordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import com.hpursan.flash.model.SensitiveWord;

import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class SensitiveWordsMaintenanceServiceImpl implements SensitiveWordsMaintenanceService {
    private final SensitiveWordRepository sensitiveWordRepository;

    @Override
    public List<SensitiveWord> listAllSensitiveWords() {
        log.info("Retrieving all sensitive words");
        return sensitiveWordRepository.findAll();
    }

    @Override
    public List<SensitiveWord> listAllSensitiveWordsOrderByLengthWord(Boolean asc) {
        log.info("Retrieving all sensitive words ordered by length (asc: {})", asc);
        return sensitiveWordRepository.findAllOrderByLengthWord(asc);
    }

    @Override
    public SensitiveWord getSensitiveWordById(Long id) {
        log.info("Retrieving sensitive word by ID: {}", id);
        SensitiveWord sensitiveWord = sensitiveWordRepository.findById(id);
        if (sensitiveWord == null) {
            log.warn("Sensitive word not found for ID: {}", id);
            throw new SensitiveWordNotFoundException(id);
        }
        log.info("Sensitive word found for ID {}: {}", id, sensitiveWord);
        return sensitiveWord;
    }

    @Override
    public SensitiveWord createSensitiveWord(String word) {
        log.info("Creating new sensitive word: {}", word);
        // Check if the word already exists in the database
        if (sensitiveWordRepository.findByWord(word) != null) {
            log.warn("Sensitive word already exists: {}", word);
            throw new SensitiveWordAlreadyExistsException(word);
        }

        SensitiveWord sensitiveWord = new SensitiveWord(word);
        SensitiveWord createdSensitiveWord = sensitiveWordRepository.save(sensitiveWord);
        log.info("New sensitive word created: {}", createdSensitiveWord);
        return createdSensitiveWord;
    }

    @Override
    public SensitiveWord updateSensitiveWord(Long id, SensitiveWord updateWord) {
        log.info("Updating sensitive word with ID: {}", id);
        // Check if the word exists in the db
        SensitiveWord sensitiveWord = sensitiveWordRepository.findById(id);
        if (sensitiveWord != null) {
            sensitiveWord.setWord(updateWord.getWord());
            SensitiveWord updatedSensitiveWord = sensitiveWordRepository.save(sensitiveWord);
            log.info("Sensitive word updated: {}", updatedSensitiveWord);
            return updatedSensitiveWord;
        } else {
            log.warn("Sensitive word not found for ID: {}", id);
            throw new SensitiveWordNotFoundException(id);
        }
    }

    @Override
    public void deleteSensitiveWord(Long id) {
        log.info("Deleting sensitive word with ID: {}", id);
        SensitiveWord sensitiveWord = sensitiveWordRepository.findById(id);
        if (sensitiveWord != null) {
            sensitiveWordRepository.deleteById(id);
            log.info("Sensitive word deleted successfully");
        } else {
            log.warn("Sensitive word not found for ID: {}", id);
            throw new SensitiveWordNotFoundException(id);
        }
    }
}
