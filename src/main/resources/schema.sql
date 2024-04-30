-- MSSQL
--CREATE TABLE sensitive_words (
--    id INT IDENTITY(1,1) PRIMARY KEY,
--    word NVARCHAR(255) NOT NULL
--);
--
---- enforce uniqueness on the words irrespective of case
--CREATE UNIQUE INDEX UIX_sensitive_words_word ON sensitive_words (LOWER(word) COLLATE SQL_Latin1_General_CP1_CI_AS);

-- H2
CREATE TABLE sensitive_words (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    word NVARCHAR(255) NOT NULL,
    CONSTRAINT unique_word_case_insensitive UNIQUE (word)
);