package com.hpursan.flash.mapper;

import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.hpursan.flash.model.SensitiveWord;

public class SensitiveWordRowMapper implements RowMapper<SensitiveWord> {
    @Override
    public SensitiveWord mapRow(ResultSet rs, int rowNum) throws SQLException {
        SensitiveWord sensitiveWord = new SensitiveWord();
        sensitiveWord.setId(rs.getLong("id"));
        sensitiveWord.setWord(rs.getString("word"));
        return sensitiveWord;
    }
}

