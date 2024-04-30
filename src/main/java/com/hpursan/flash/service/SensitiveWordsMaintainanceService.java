package com.hpursan.flash.service;

import com.hpursan.flash.repository.SensitiveWordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import com.hpursan.flash.model.SensitiveWord;

@Service
@RequiredArgsConstructor
public class SensitiveWordsMaintainanceService {
    private final SensitiveWordRepository sensitiveWordRepository;

    public List<SensitiveWord> listAllSensitiveWords() {
        return sensitiveWordRepository.findAll();
    }

    public SensitiveWord getSensitiveWordById(Long id) {
        return sensitiveWordRepository.findById(id);
    }

    public SensitiveWord createSensitiveWord(String word) {
        // Check if the word already exists in the database
        if (sensitiveWordRepository.findByWord(word) != null) {
            throw new IllegalArgumentException("Sensitive word already exists");
        }

        SensitiveWord sensitiveWord = new SensitiveWord(word);
        return sensitiveWordRepository.save(sensitiveWord);
    }

    public SensitiveWord updateSensitiveWord(Long id, SensitiveWord updateWord) {
        // Check if the word exists in the db
        SensitiveWord sensitiveWord = sensitiveWordRepository.findById(id);
        if (sensitiveWord != null) {
            sensitiveWord.setWord(updateWord.getWord());
            return sensitiveWordRepository.save(sensitiveWord);
        } else {
            throw new IllegalArgumentException("Sensitive word with id " + id + " does not exist");
        }
    }

    public void deleteSensitiveWord(Long id) {
        SensitiveWord sensitiveWord = sensitiveWordRepository.findById(id);
        if (sensitiveWord != null) {
            sensitiveWordRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("Sensitive word with id " + id + " does not exist");
        }
    }
}
