package com.hpursan.flash.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @InjectMocks
    private GlobalExceptionHandler exceptionHandler;

    @Test
    void handleSensitiveWordNotFoundException() {
        SensitiveWordNotFoundException exception = new SensitiveWordNotFoundException(1L);
        ResponseEntity<String> responseEntity = exceptionHandler.handleSensitiveWordNotFoundException(exception);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    void handleSensitiveWordAlreadyExistsException() {
        SensitiveWordAlreadyExistsException exception = new SensitiveWordAlreadyExistsException("word");
        ResponseEntity<String> responseEntity = exceptionHandler.handleSensitiveWordAlreadyExistsException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

}
