package com.hpursan.flash.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="sensitive_words")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SensitiveWord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "The sensitive word is required")
    private String word;

    public SensitiveWord(String word) {
        this.word = word;
    }
}
