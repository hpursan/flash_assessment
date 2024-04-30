package com.hpursan.flash.exception;

public class SensitiveWordNotFoundException extends RuntimeException{
    public SensitiveWordNotFoundException(String message) {
        super(message);
    }
}
