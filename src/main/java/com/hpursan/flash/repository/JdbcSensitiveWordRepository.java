package com.hpursan.flash.repository;

import com.hpursan.flash.model.SensitiveWord;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import com.hpursan.flash.mapper.SensitiveWordRowMapper;

@Repository
@AllArgsConstructor
public class JdbcSensitiveWordRepository implements SensitiveWordRepository {
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<SensitiveWord> findAll() {
        return jdbcTemplate.query("SELECT * FROM sensitive_words", new SensitiveWordRowMapper());
    }

    @Override
    public SensitiveWord findById(Long id) {
        List<SensitiveWord> results = jdbcTemplate.query("SELECT * FROM sensitive_words WHERE id = ?", new Object[]{id}, new SensitiveWordRowMapper());
        return results.isEmpty() ? null : results.get(0);
    }

    @Override
    public SensitiveWord findByWord(String word) {
        List<SensitiveWord> results = jdbcTemplate.query("SELECT * FROM sensitive_words WHERE word = ?", new Object[]{word}, new SensitiveWordRowMapper());
        return results.isEmpty() ? null : results.get(0);
    }

    private long insertSensitiveWord(SensitiveWord sensitiveWord) {
        // in order to get the newly generated id back after insert, we need to use the KeyHolder class
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement("INSERT INTO sensitive_words(word) VALUES(?)", Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, sensitiveWord.getWord());
            return ps;
        }, keyHolder);

        return (long) keyHolder.getKey();

    }

    @Override
    public SensitiveWord save(SensitiveWord sensitiveWord) {
        if (sensitiveWord.getId() == null) {
            Long id = insertSensitiveWord(sensitiveWord);
            sensitiveWord.setId(id);
        } else {
            jdbcTemplate.update("UPDATE sensitive_words SET word = ? WHERE id = ?", sensitiveWord.getWord(), sensitiveWord.getId());
        }
        return sensitiveWord;
    }

    @Override
    public void deleteById(Long id) {
        jdbcTemplate.update("DELETE FROM sensitive_words WHERE id = ?", id);
    }
}
