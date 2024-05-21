package com.danseg.crudusuarios.middleware;

import com.danseg.crudusuarios.exception.DataValidationException;
import com.danseg.crudusuarios.exception.UserNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GlobalExceptionHandlerTest {

    @Test
    public void handleUserNotFoundExceptionTest() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        UserNotFoundException ex = new UserNotFoundException("User not found");

        ResponseEntity<String> responseEntity = handler.handleUserNotFoundException(ex);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("User not found", responseEntity.getBody());
    }

    @Test
    public void handleDataValidationExceptionTest() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        DataValidationException ex = new DataValidationException("Data validation error");

        ResponseEntity<String> responseEntity = handler.handleDataValidationException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals("Data validation error", responseEntity.getBody());
    }

    @Test
    public void handleAllExceptionsTest() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        Exception ex = new Exception("Internal Server Error");

        ResponseEntity<String> responseEntity = handler.handleAllExceptions(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertEquals("Ha ocurrido un error: Internal Server Error", responseEntity.getBody());
    }
}

