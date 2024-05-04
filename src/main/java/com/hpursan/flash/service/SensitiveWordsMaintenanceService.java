package com.hpursan.flash.service;

import com.hpursan.flash.model.SensitiveWord;

import java.util.List;

public interface SensitiveWordsMaintenanceService {
    List<SensitiveWord> listAllSensitiveWords();

    List<SensitiveWord> listAllSensitiveWordsOrderByLengthWord(Boolean asc);

    SensitiveWord getSensitiveWordById(Long id);

    SensitiveWord createSensitiveWord(String word);

    SensitiveWord updateSensitiveWord(Long id, SensitiveWord updateWord);

    void deleteSensitiveWord(Long id);
}
