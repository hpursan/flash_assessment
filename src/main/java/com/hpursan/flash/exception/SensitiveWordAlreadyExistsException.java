package com.hpursan.flash.exception;

public class SensitiveWordAlreadyExistsException extends RuntimeException{
    public SensitiveWordAlreadyExistsException(String word) {
        super("Sensitive word " + word + " already exists");
    }
}
