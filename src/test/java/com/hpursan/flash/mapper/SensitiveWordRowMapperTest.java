package com.hpursan.flash.mapper;

import com.hpursan.flash.model.SensitiveWord;
import org.junit.jupiter.api.Test;
import java.sql.ResultSet;
import java.sql.SQLException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SensitiveWordRowMapperTest {

    @Test
    void mapRow_ValidResultSet_ReturnsCorrectSensitiveWord() throws SQLException {
        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.getLong("id")).thenReturn(1L);
        when(resultSet.getString("word")).thenReturn("test");

        SensitiveWordRowMapper mapper = new SensitiveWordRowMapper();

        SensitiveWord sensitiveWord = mapper.mapRow(resultSet, 1);

        assertEquals(1L, sensitiveWord.getId());
        assertEquals("test", sensitiveWord.getWord());
    }
}

