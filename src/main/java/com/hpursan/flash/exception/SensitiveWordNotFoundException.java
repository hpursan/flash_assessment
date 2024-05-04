package com.hpursan.flash.exception;

public class SensitiveWordNotFoundException extends RuntimeException{
//    public SensitiveWordNotFoundException(String word) {
//        super("Sensitive word " + word + " not found");
//    }

    public SensitiveWordNotFoundException(Long id) {
        super("Sensitive word with id " + id + " not found");
    }
}
