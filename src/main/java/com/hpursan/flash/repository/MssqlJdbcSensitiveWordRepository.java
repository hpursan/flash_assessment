package com.hpursan.flash.repository;

import com.hpursan.flash.model.SensitiveWord;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.math.BigDecimal;

import com.hpursan.flash.mapper.SensitiveWordRowMapper;

@Slf4j
@Repository
@AllArgsConstructor
public class MssqlJdbcSensitiveWordRepository implements SensitiveWordRepository {
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<SensitiveWord> findAll() {
        return jdbcTemplate.query("SELECT * FROM sensitive_words", new SensitiveWordRowMapper());
    }

    @Override
    public List<SensitiveWord> findAllOrderByLengthWord(Boolean asc) {
        String sql = "SELECT * FROM sensitive_words order by len(word)";
        if (!asc) {
            sql += " DESC";
        }
        return jdbcTemplate.query(sql, new SensitiveWordRowMapper());
    }

    @Override
    public SensitiveWord findById(Long id) {
        return jdbcTemplate.queryForObject("SELECT * FROM sensitive_words WHERE id = ?", new SensitiveWordRowMapper(), id);
    }

    @Override
    public SensitiveWord findByWord(String word) {
        List<SensitiveWord> results = jdbcTemplate.query("SELECT * FROM sensitive_words WHERE word = ?", new SensitiveWordRowMapper(), word);
        return results.isEmpty() ? null : results.get(0);
    }

    private long insertSensitiveWord(SensitiveWord sensitiveWord) {
        try {
            // In order to get the newly generated id back after insert, we need to use the KeyHolder class
            KeyHolder keyHolder = new GeneratedKeyHolder();

            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection
                        .prepareStatement("INSERT INTO sensitive_words(word) VALUES(?)", Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, sensitiveWord.getWord());
                return ps;
            }, keyHolder);

            Map<String, Object> keys = keyHolder.getKeys();

            if (keys != null && keys.containsKey("GENERATED_KEYS")) {
                BigDecimal generatedId = (BigDecimal) keys.get("GENERATED_KEYS");
                return generatedId.longValue();
            } else {
                throw new IllegalStateException("Generated ID is null or not found.");
            }
        } catch (Exception e) {
            // Log any exceptions that occur during the database operation
            log.error("Error occurred while inserting sensitive word: {}", e.getMessage());
            throw new RuntimeException("Error occurred while inserting sensitive word", e);
        }
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
