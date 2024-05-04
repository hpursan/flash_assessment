package com.hpursan.flash.repository;

import com.hpursan.flash.model.SensitiveWord;

import java.util.List;
import java.util.Optional;

public interface SensitiveWordRepository {

    List<SensitiveWord> findAll();

    List<SensitiveWord> findAllOrderByLengthWord(Boolean asc);

    SensitiveWord findById(Long id);

    SensitiveWord findByWord(String word);

    SensitiveWord save(SensitiveWord sensitiveWord);

    void deleteById(Long id);
}
