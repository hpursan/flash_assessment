package com.hpursan.flash.repository;

import com.hpursan.flash.model.SensitiveWord;

import java.util.List;

public interface SensitiveWordRepository {
    List<SensitiveWord> findAll();

    SensitiveWord findById(Long id);

    SensitiveWord findByWord(String word);

    SensitiveWord save(SensitiveWord sensitiveWord);

    void deleteById(Long id);
}
